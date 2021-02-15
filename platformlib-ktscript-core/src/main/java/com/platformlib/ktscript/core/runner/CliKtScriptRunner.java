package com.platformlib.ktscript.core.runner;

import com.platformlib.ktscript.api.configuration.KtScriptEngineConfiguration;
import com.platformlib.ktscript.core.cli.KtScriptCliConfiguration;
import com.platformlib.ktscript.core.cli.KtScriptCliParserUtils;
import com.platformlib.ktscript.core.engine.EngineUtilities;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CliKtScriptRunner {
    private KtScriptCliConfiguration scriptCliConfiguration;
    private final Collection<String> cliArguments;

    public CliKtScriptRunner(final Path libPath,
                             final Collection<String> loadedLibraries,
                             final ClassLoader classLoader,
                             final Collection<String> cliArguments) {
        this.cliArguments = new ArrayList<>(cliArguments);
    }

    /**
     * This method is called from boot/standalone application.
     */
    public Map<String, Object> run() {
        final KtScriptCliConfiguration scriptCliConfiguration = KtScriptCliParserUtils.parseConfiguration(cliArguments);
        final DefaultKtScriptRunner ktScriptRunner = new DefaultKtScriptRunner(scriptCliConfiguration.getScriptEngineConfiguration());
        try {
            final Object result = ktScriptRunner.runScript(scriptCliConfiguration.getScriptRunConfiguration(), EngineUtilities.getScriptContent(scriptCliConfiguration.getScriptConfiguration()));
            return new CliRunnerResult(result).toMap();
        } catch (final Exception exception) {
            return new CliRunnerResult(exception).toMap();
        }
    }

    private KtScriptEngineConfiguration initialize(final Collection<String> cliArguments) {
        scriptCliConfiguration = KtScriptCliParserUtils.parseConfiguration(cliArguments);
        return scriptCliConfiguration.getScriptEngineConfiguration();
    }

    public static final class CliRunnerResult {
        private final Object result;
        private final Throwable exception;
        private final String failureMessage;

        private CliRunnerResult(final Object result, final String failureMessage, final Throwable exception) {
            this.result = result;
            this.failureMessage = failureMessage;
            this.exception = exception;
        }

        public CliRunnerResult(final Object result) {
            this(result, null, null);
        }

        public CliRunnerResult(final Exception exception) {
            this(null, null, exception);
        }

        public CliRunnerResult(final String failureMessage, final Exception exception) {
            this(null, failureMessage, exception);
        }

        private Map<String, Object> toMap() {
            final Map<String, Object> map = new HashMap<>();
            if (result != null) {
                map.put("result", result);
            }
            if (exception != null) {
                map.put("exception", exception);
            }
            if (failureMessage != null) {
                map.put("failureMessage", failureMessage);
            }
            return map;
        }
    }
}
