package com.platformlib.ktscript.api;

import com.platformlib.ktscript.api.configuration.KtScriptRunConfiguration;

/**
 * Script runner.
 */
public interface KtScriptRunner extends AutoCloseable {
    /**
     * Run script.
     * @param script script to run
     * @param <T> script return type
     * @return Returns script return
     */
    <T> T runScript(String script);

    /**
     * Run script.
     * @param runConfiguration run configuration
     * @param script script to run
     * @param <T> script return type
     * @return Returns script return
     */
    <T> T runScript(KtScriptRunConfiguration runConfiguration, String script);
}
