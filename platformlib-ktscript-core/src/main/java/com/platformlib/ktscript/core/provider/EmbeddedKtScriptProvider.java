package com.platformlib.ktscript.core.provider;

import com.platformlib.ktscript.api.KtScriptRunner;
import com.platformlib.ktscript.api.configuration.KtScriptRunnerConfiguration;
import com.platformlib.ktscript.api.enums.RunnerType;
import com.platformlib.ktscript.api.provider.KtScriptProvider;
import com.platformlib.ktscript.core.runner.EmbeddedKtScriptRunner;

public class EmbeddedKtScriptProvider implements KtScriptProvider {
    @Override
    public RunnerType getType() {
        return RunnerType.EMBEDDED;
    }

    @Override
    public KtScriptRunner newScriptRunner(final KtScriptRunnerConfiguration ktScriptRunnerConfiguration) {
        //TODO implement
        return new EmbeddedKtScriptRunner(null);
    }
}
