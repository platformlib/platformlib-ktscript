package com.platformlib.ktscript.core.runner;

import com.platformlib.ktscript.api.configuration.KtScriptEngineConfiguration;
import com.platformlib.ktscript.api.KtScriptRunner;
import com.platformlib.ktscript.api.configuration.KtScriptRunConfiguration;
import com.platformlib.ktscript.api.exception.KtScriptException;
import com.platformlib.ktscript.core.configuration.DefaultKtScriptRunConfiguration;
import com.platformlib.ktscript.dsl.KtScriptDslKt;
import org.slf4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DefaultKtScriptRunner implements KtScriptRunner {
    private static final DefaultKtScriptRunConfiguration DEFAULT_RUN_CONFIGURATION = new DefaultKtScriptRunConfiguration();
    private final KtScriptEngineConfiguration ktScriptEngineConfiguration;
    private final ScriptEngine scriptEngine;

    public DefaultKtScriptRunner(final KtScriptEngineConfiguration ktScriptEngineConfiguration) {
        this.ktScriptEngineConfiguration = ktScriptEngineConfiguration;
        scriptEngine = initialize();
    }

    private ScriptEngine initialize() {
        ///
         return new ScriptEngineManager().getEngineByName("kotlin");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T runScript(final KtScriptRunConfiguration runConfiguration, final String script) {
        final Object scriptEvalResult;
        final Logger defaultLogger = KtScriptDslKt.getLogger();
        runConfiguration.getLoggerConfiguration().getLogger().ifPresent(KtScriptDslKt::setLogger);
        try {
            try (StreamWrapper stdOutWrapper = new StreamWrapper(runConfiguration.getOutputConfiguration().getStdOutConsumer().isPresent(), System::setOut, () -> System.out)) {
                try (StreamWrapper stdErrWrapper = new StreamWrapper(runConfiguration.getOutputConfiguration().getStdErrConsumer().isPresent(), System::setErr, () -> System.err)) {
                    try {
                        scriptEvalResult = scriptEngine.eval(script);
                    } catch (final ScriptException scriptException) {
                        throw new KtScriptException(scriptException);
                    }
                    runConfiguration.getOutputConfiguration().getStdOutConsumer().ifPresent(consumer -> stdOutWrapper.getOutput().forEach(consumer));
                    runConfiguration.getOutputConfiguration().getStdErrConsumer().ifPresent(consumer -> stdErrWrapper.getOutput().forEach(consumer));
                }
            }
        } finally {
            KtScriptDslKt.setLogger(defaultLogger);
        }
        try {
            return (T) scriptEvalResult;
        } catch (final ClassCastException classCastException) {
            defaultLogger.error("unable to cast script returned value to runScript returned value " + scriptEvalResult, classCastException);
            //TODO Check if KtScript based exception should be used
            throw classCastException;
        }
    }

    @Override
    public <T> T runScript(final String script) {
        return runScript(DEFAULT_RUN_CONFIGURATION, script);
    }

    @Override
    public void close() {
    }

    private static final class StreamWrapper implements AutoCloseable {
        private final boolean wrap;
        private final Consumer<PrintStream> streamSetter;
        private final ByteArrayOutputStream outputStream;
        private final PrintStream wrapperPs;
        private final PrintStream originalPs;

        private StreamWrapper(final boolean wrap,
                              final Consumer<PrintStream> streamSetter,
                              final Supplier<PrintStream> streamGetter) {
            this.wrap = wrap;
            this.streamSetter = streamSetter;
            if (wrap) {
                this.outputStream = new ByteArrayOutputStream();
                this.originalPs = streamGetter.get();
                try {
                    this.wrapperPs = new PrintStream(outputStream, true, Charset.defaultCharset().name());
                } catch (final UnsupportedEncodingException unsupportedEncodingException) {
                    throw new IllegalStateException(unsupportedEncodingException);
                }
                streamSetter.accept(wrapperPs);
            } else {
                this.outputStream = null;
                this.originalPs = null;
                this.wrapperPs = null;
            }
        }

        public Collection<String> getOutput() {
            if (wrap) {
                wrapperPs.flush();
                try {
                    return outputStream.size() > 0 ? Arrays.asList(outputStream.toString(Charset.defaultCharset().name()).split(System.getProperty("line.separator"))) : Collections.emptyList();
                } catch (final UnsupportedEncodingException unsupportedEncodingException) {
                    throw new IllegalStateException(unsupportedEncodingException);
                }
            } else {
                return Collections.emptyList();
            }
        }

        @Override
        public void close() {
            if (wrap) {
                wrapperPs.close();
                streamSetter.accept(originalPs);
            }
        }
    }
}