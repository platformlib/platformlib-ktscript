package com.platformlib.ktscript.api.builder.impl;

import com.platformlib.ktscript.api.builder.KtScriptRunConfigurationBuilder;
import com.platformlib.ktscript.api.configuration.KtScriptRunConfiguration;
import com.platformlib.ktscript.api.configuration.LoggerConfiguration;
import com.platformlib.ktscript.api.configuration.OutputConfiguration;
import com.platformlib.ktscript.api.configurator.LoggerConfigurator;
import com.platformlib.ktscript.api.configurator.OutputConfigurator;
import com.platformlib.ktscript.api.configurator.impl.DefaultLoggerConfigurator;
import com.platformlib.ktscript.api.configurator.impl.DefaultOutputConfigurator;

import java.util.function.Consumer;

public class DefaultKtScriptRunConfigurationBuilder implements KtScriptRunConfigurationBuilder, KtScriptRunConfiguration {
    private final DefaultOutputConfigurator outputConfiguration = new DefaultOutputConfigurator();
    private final DefaultLoggerConfigurator loggerConfigurator = new DefaultLoggerConfigurator();

    @Override
    public KtScriptRunConfigurationBuilder output(final Consumer<OutputConfigurator> outputConfiguratorSupplier) {
        outputConfiguratorSupplier.accept(outputConfiguration);
        return this;
    }

    @Override
    public KtScriptRunConfigurationBuilder logger(final Consumer<LoggerConfigurator> loggerConfiguratorSupplier) {
        loggerConfiguratorSupplier.accept(loggerConfigurator);
        return this;
    }

    @Override
    public OutputConfiguration getOutputConfiguration() {
        return outputConfiguration;
    }

    @Override
    public LoggerConfiguration getLoggerConfiguration() {
        return loggerConfigurator;
    }

    @Override
    public KtScriptRunConfiguration build() {
        return this;
    }
}
