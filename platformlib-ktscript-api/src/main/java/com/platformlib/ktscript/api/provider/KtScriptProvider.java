package com.platformlib.ktscript.api.provider;

import com.platformlib.ktscript.api.KtScriptRunner;
import com.platformlib.ktscript.api.configuration.KtScriptRunnerConfiguration;
import com.platformlib.ktscript.api.enums.RunnerType;

public interface KtScriptProvider {
    RunnerType getType();
    KtScriptRunner newScriptRunner(KtScriptRunnerConfiguration ktScriptRunnerConfiguration);
}
