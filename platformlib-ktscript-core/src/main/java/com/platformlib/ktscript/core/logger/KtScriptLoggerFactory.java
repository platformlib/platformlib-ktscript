package com.platformlib.ktscript.core.logger;

import com.platformlib.ktscript.api.enums.LoggerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public final class KtScriptLoggerFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(KtScriptLoggerFactory.class);

    private KtScriptLoggerFactory() {
    }

    //TODO Implement
    public static void configure(final String logger, final Collection<String> commandLineArguments, final Collection<String> alreadyLoggedMessages) {
        if (!"logback".equals(logger)) {
            throw new IllegalStateException("Not implemented");
        }
        KtScriptLogbackLogger.configure(parse(commandLineArguments));
        alreadyLoggedMessages.forEach(logMessage -> {
            final String[] parts = logMessage.split(":", 2);
            switch (parts[0]) {
                case "ERROR":
                    LOGGER.error(parts[1]);
                    break;
                case "WARN":
                    LOGGER.warn(parts[1]);
                    break;
                case "INFO":
                    LOGGER.info(parts[1]);
                    break;
                case "DEBUG":
                    LOGGER.debug(parts[1]);
                    break;
                case "TRACE":
                    LOGGER.trace(parts[1]);
                    break;
                default:
                    LOGGER.error("Unsupported log message {}", logMessage);
                    throw new IllegalStateException("Unmapped log level " + parts[0]);
            }
        });
    }

    //TODO Implement
    static KtScriptEngineLoggerConfiguration parse(final Collection<String> commandLineArguments) {
        final KtScriptEngineLoggerConfiguration configuration = new KtScriptEngineLoggerConfiguration();
        configuration.setLogLevel(LoggerLevel.TRACE);
        return configuration;
    }
}
