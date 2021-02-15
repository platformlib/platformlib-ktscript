package com.platformlib.ktscript.api.configurator.impl;

import com.platformlib.ktscript.api.configuration.OutputConfiguration;
import com.platformlib.ktscript.api.configurator.OutputConfigurator;

import java.util.Optional;
import java.util.function.Consumer;

public class DefaultOutputConfigurator implements OutputConfigurator, OutputConfiguration {
    private Consumer<String> stdOutConsumer;
    private Consumer<String> stdErrConsumer;

    @Override
    public Optional<Consumer<String>> getStdOutConsumer() {
        return Optional.ofNullable(stdOutConsumer);
    }

    @Override
    public Optional<Consumer<String>> getStdErrConsumer() {
        return Optional.ofNullable(stdErrConsumer);
    }

    @Override
    public void setStdOutConsumer(final Consumer<String> stdOutConsumer) {
        this.stdOutConsumer = stdOutConsumer;
    }

    @Override
    public void setStdErrConsumer(final Consumer<String> stdErrConsumer) {
        this.stdErrConsumer = stdErrConsumer;
    }
}
