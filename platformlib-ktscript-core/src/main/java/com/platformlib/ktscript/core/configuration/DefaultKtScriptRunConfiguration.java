package com.platformlib.ktscript.core.configuration;

import com.platformlib.ktscript.api.configuration.KtScriptRunConfiguration;
import com.platformlib.ktscript.api.configuration.LoggerConfiguration;
import com.platformlib.ktscript.api.configuration.OutputConfiguration;
import com.platformlib.ktscript.api.configurator.impl.DefaultLoggerConfigurator;
import com.platformlib.ktscript.api.configurator.impl.DefaultOutputConfigurator;

public class  DefaultKtScriptRunConfiguration implements KtScriptRunConfiguration {
    private OutputConfiguration outputConfiguration = new DefaultOutputConfigurator();
    private LoggerConfiguration loggerConfiguration = new DefaultLoggerConfigurator();

    @Override
    public OutputConfiguration getOutputConfiguration() {
        return outputConfiguration;
    }

    @Override
    public LoggerConfiguration getLoggerConfiguration() {
        return loggerConfiguration;
    }

}
