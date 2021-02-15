package com.platformlib.ktscript.core.cli;

import com.platformlib.ktscript.api.configuration.KtScriptConfiguration;
import com.platformlib.ktscript.api.configuration.KtScriptEngineConfiguration;
import com.platformlib.ktscript.api.configuration.KtScriptRunConfiguration;

public class KtScriptCliConfiguration {
    private final KtScriptConfiguration scriptConfiguration;
    private final KtScriptRunConfiguration scriptRunConfiguration;
    private final KtScriptEngineConfiguration scriptEngineConfiguration;

    public KtScriptCliConfiguration(final KtScriptConfiguration scriptConfiguration,
                                    final KtScriptRunConfiguration scriptRunConfiguration,
                                    final KtScriptEngineConfiguration scriptEngineConfiguration) {
        this.scriptConfiguration = scriptConfiguration;
        this.scriptRunConfiguration = scriptRunConfiguration;
        this.scriptEngineConfiguration = scriptEngineConfiguration;
    }

    public KtScriptConfiguration getScriptConfiguration() {
        return scriptConfiguration;
    }

    public KtScriptRunConfiguration getScriptRunConfiguration() {
        return scriptRunConfiguration;
    }

    public KtScriptEngineConfiguration getScriptEngineConfiguration() {
        return scriptEngineConfiguration;
    }
}
