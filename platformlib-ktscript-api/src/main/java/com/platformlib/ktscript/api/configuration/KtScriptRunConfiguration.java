package com.platformlib.ktscript.api.configuration;


/**
 * Script runner configuration.
 */
public interface KtScriptRunConfiguration {
    /**
     * Get script output configuration.
     * @return Returns script output configuration
     */
    OutputConfiguration getOutputConfiguration();

    /**
     * Get script logger configuration.
     * @return Returns script logger configuration
     */
    LoggerConfiguration getLoggerConfiguration();
}
