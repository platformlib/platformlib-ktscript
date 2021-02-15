package com.platformlib.ktscript.api.builder;

import com.platformlib.ktscript.api.configuration.KtScriptRunConfiguration;
import com.platformlib.ktscript.api.configurator.LoggerConfigurator;
import com.platformlib.ktscript.api.configurator.OutputConfigurator;

import java.util.function.Consumer;

public interface KtScriptRunConfigurationBuilder {
    KtScriptRunConfigurationBuilder output(Consumer<OutputConfigurator> outputConfiguratorSupplier);
    KtScriptRunConfigurationBuilder logger(Consumer<LoggerConfigurator> loggerConfiguratorSupplier);
    KtScriptRunConfiguration build();
}
