package com.platformlib.ktscript.application;

import com.platformlib.ktscript.application.exception.KtScriptApplicationException;
import com.platformlib.ktscript.application.exception.KtScriptApplicationExecutionException;
import com.platformlib.ktscript.application.exception.KtScriptApplicationStartupException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KtScriptApplication {
    private static final String CLI_RUNNER_CLASS = "com.platformlib.ktscript.core.runner.CliKtScriptRunner";
    private static final String LOGGER_FACTORY_CLASS = "com.platformlib.ktscript.core.logger.KtScriptLoggerFactory";
    private static final String DEFAULT_LOGGER = "logback";
    private static final String LIBRARIES_RESOURCE_NAME = "/ktscript-libraries.txt";
    private static final Collection<String> CLI_VERBOSE_MODE_OPTIONS = Arrays.asList("-v", "--verbose", "-d", "--debug");
    private static final String LEVEL_ERROR   = "ERROR";
    private static final String LEVEL_WARNING = "WARNING";
    private static final String LEVEL_INFO    = "INFO";
    private static final String LEVEL_DEBUG   = "DEBUG";
    private static final String LEVEL_TRACE   = "TRACE";

    private final String[] cliArguments;
    private final boolean verboseMode;
    private final Collection<String> logRecords = new ArrayList<>();

    KtScriptApplication(final String... cliArguments) {
        this.cliArguments = Objects.requireNonNull(cliArguments);
        this.verboseMode = isVerboseMode(cliArguments);
    }

    public Collection<String> getLogRecords() {
        return logRecords;
    }

    private boolean isVerboseMode() {
        return verboseMode;
    }

    void error(final String message, final Object... arguments) {
        log(LEVEL_ERROR, message, arguments);
    }

    void warning(final String message, final Object... arguments) {
        log(LEVEL_WARNING, message, arguments);
    }

    void info(final String message, final Object... arguments) {
        log(LEVEL_INFO, message, arguments);
    }

    void debug(final String message, final Object... arguments) {
        log(LEVEL_DEBUG, message, arguments);
    }

    void trace(final String message, final Object... arguments) {
        log(LEVEL_TRACE, message);
    }

    void log(final String level, final String message, final Object... arguments) {
        String formattedMessage = message;
        for (final Object argument: arguments) {
            formattedMessage = formattedMessage.replaceFirst("\\{}", argument.toString());
        }
        logRecords.add(String.format("%s:%s", level, formattedMessage));
    }

    void dumpLogRecords() {
        logRecords.forEach(System.err::println);
    }

    @SuppressWarnings("unchecked")
    void run() {
        final AtomicReference<URLClassLoader> urlClassLoader = new AtomicReference<>();
        Path libraryPath = null;
        final String loggerName = getLogger();
        debug("Use {} logger", loggerName);
        try {
            final Collection<String> loadedLibraries = new ArrayList<>();
            if (isBootApplication()) {
                debug("Is boot application");
                libraryPath = startupTryCatch(() -> Files.createTempDirectory("ktscript-lib-"), "Unable to create temporary directory");
                final KtScriptLibrary ktScriptLibrary = getKtScriptLibraries();
                for (final String libraryName: ktScriptLibrary.getLibraries(loggerName)) {
                    try (InputStream is = new BufferedInputStream(KtScriptApplication.class.getResourceAsStream("/ktscript-libs/" + libraryName))) {
                        debug("Extract {} to {}", libraryName, libraryPath);
                        final String libraryFileName = Paths.get(libraryName).getFileName().toString();
                        loadedLibraries.add(libraryFileName);
                        final Path extractedLibraryPath = libraryPath.resolve(libraryFileName);
                        Files.copy(is, extractedLibraryPath);
                    } catch (final IOException ioException) {
                        throw new KtScriptApplicationStartupException("Unable to extract library " + libraryName, ioException);
                    }
                    try {
                        urlClassLoader.set(
                                new URLClassLoader(
                                        Files.walk(libraryPath).map(path -> startupTryCatch(() -> path.toUri().toURL(), "Unable to convert path to URL")).toArray(URL[]::new),
                                        KtScriptApplication.class.getClassLoader()
                                )
                        );
                    } catch (final IOException ioException) {
                        throw new KtScriptApplicationStartupException("Unable to collect libraries", ioException);
                    }
                }
            } else {
                throw new IllegalStateException("Not implemented");
            }
            Thread.currentThread().setContextClassLoader(urlClassLoader.get());
            //
            final Collection<String> commandLineArguments = Arrays.asList(cliArguments);
            final Class<?> loggerFactorClass = startupTryCatch(() -> urlClassLoader.get().loadClass(LOGGER_FACTORY_CLASS), "There is a problem with getting logger factory class");
            final Method loggerConfigureMethod = startupTryCatch(() -> loggerFactorClass.getDeclaredMethod("configure", String.class, Collection.class, Collection.class), "Unable to get ktscript constructor in " + CLI_RUNNER_CLASS);
            try {
                loggerConfigureMethod.invoke(null, loggerName, commandLineArguments, logRecords);
            } catch (final InvocationTargetException | IllegalAccessException initializationException) {
                throw new KtScriptApplicationStartupException("Unable to invoke ktscript runner method", initializationException);
            } catch (final Throwable throwable) {
                throw new KtScriptApplicationExecutionException(throwable.getMessage(), throwable);
            }

            final Class<?> ktScriptCliRunnerClass = startupTryCatch(() -> urlClassLoader.get().loadClass(CLI_RUNNER_CLASS), "There is a problem with initializing ktscript runner");
            final Constructor<?> ktScriptCliRunnerConstructor = startupTryCatch(() -> ktScriptCliRunnerClass.getConstructor(
                    Path.class,
                    Collection.class,
                    ClassLoader.class,
                    Collection.class), "Unable to get ktscript constructor in " + CLI_RUNNER_CLASS);
            final Path baseLibPath = libraryPath;
            final Object ktScriptCliRunnerInstance = startupTryCatch(() -> ktScriptCliRunnerConstructor.newInstance(
                    baseLibPath,
                    loadedLibraries,
                    urlClassLoader.get(),
                    commandLineArguments), "Unable to initialize ktscript cli runner");
            final Method ktScriptCliRunnerRunMethod = startupTryCatch(() -> ktScriptCliRunnerClass.getMethod("run"), "Unable to get run method");
            final Map<String, Object> result;
            try {
                result = (Map<String, Object>) ktScriptCliRunnerRunMethod.invoke(ktScriptCliRunnerInstance);
            } catch (final IllegalAccessException | InvocationTargetException exception) {
                throw new KtScriptApplicationStartupException("Unable to invoke ktscript runner method", exception);
            } catch (final Throwable throwable) {
                throw new KtScriptApplicationExecutionException(throwable.getMessage(), throwable);
            }
            final String failureMessage = (String) result.get("failureMessage");
            final Exception exception = (Exception) result.get("exception");
            if (failureMessage != null) {
                throw new KtScriptApplicationExecutionException(failureMessage, exception);
            } else if (exception != null) {
                throw new KtScriptApplicationExecutionException(exception.getMessage(), exception);
            }
        } finally {
            if (libraryPath != null) {
                try {
                    Files.walk(libraryPath).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                } catch (final IOException ioException) {
                    System.err.println("Unable to delete directory " + libraryPath);
                    if (isVerboseMode()) {
                        ioException.printStackTrace(System.err);
                    }
                }
            }
        }
    }

    String getLogger() {
        //TODO Implement
        return DEFAULT_LOGGER;
    }

    @FunctionalInterface
    interface StartupSupplier<T> {
        /**
         * Gets a result.
         *
         * @return Returns a result
         * @throws Exception in case of failure
         */
        T get() throws Exception;
    }

    static <T> T startupTryCatch(final StartupSupplier<T> supplier, final String exceptionMessage) throws KtScriptApplicationStartupException {
        try {
            return supplier.get();
        } catch (final Exception exception) {
            throw new KtScriptApplicationStartupException(exceptionMessage, exception);
        }
    }

    /**
     * Check if application is spring or ktscript boot application.
     * @return Returns true if boot application, false otherwise
     */
    static boolean isBootApplication() {
        //TODO Implement logic
        return true;
    }

    static boolean isVerboseMode(String... cliArguments) {
        return Arrays.stream(cliArguments).anyMatch(CLI_VERBOSE_MODE_OPTIONS::contains);
    }

    static KtScriptLibrary getKtScriptLibraries() {
        final Properties properties = new Properties();
        try (InputStream is = KtScriptApplication.class.getResourceAsStream(LIBRARIES_RESOURCE_NAME)) {
            properties.load(is);
        } catch (final IOException ioException) {
            throw new KtScriptApplicationStartupException("Unable to load " + LIBRARIES_RESOURCE_NAME, ioException);
        }
        return new KtScriptLibrary(properties);
    }

    public static void main(final String... args) {
        final KtScriptApplication ktScriptApplication = new KtScriptApplication(args);
        try {
            ktScriptApplication.run();
        } catch (final KtScriptApplicationException ktScriptApplicationException) {
            ktScriptApplication.dumpLogRecords();
            if (isVerboseMode(args)) {
                ktScriptApplicationException.printStackTrace(System.err);
            } else {
                System.err.println(ktScriptApplicationException.getMessage());
            }
            System.exit(ktScriptApplicationException instanceof KtScriptApplicationStartupException ? 1 : 2);
        }
    }

    static class KtScriptLibrary {
        private final Map<String, Collection<String>> libraryMap = new HashMap<>();
        KtScriptLibrary(final Properties properties) {
            properties.forEach((k, v) -> {
                final Collection<String> librarySet = libraryMap.computeIfAbsent((String) k, key -> new ArrayList<>());
                Collections.addAll(librarySet, ((String) v).split("\\s*,\\s*"));
            });
        }

        Collection<String> getLibraries(final String logger) {
            return Stream.concat(
                    libraryMap.get("ktscript").stream(),
                    libraryMap.get(logger).stream().map(libFileName -> "loggers/" + logger + "/" + libFileName)
            ).collect(Collectors.toSet());
        }
    }

    public static void main1(String... args) {
        KtScriptApplication.getKtScriptLibraries().getLibraries("logback").forEach(System.out::println);
    }
}
