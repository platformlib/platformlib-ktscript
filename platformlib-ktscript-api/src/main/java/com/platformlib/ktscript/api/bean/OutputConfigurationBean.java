package com.platformlib.ktscript.api.bean;

import com.platformlib.ktscript.api.configuration.OutputConfiguration;

import java.util.Optional;
import java.util.function.Consumer;

public class OutputConfigurationBean implements OutputConfiguration {
    private Consumer<String> stdOutConsumer;
    private Consumer<String> stdErrConsumer;

    @Override
    public Optional<Consumer<String>> getStdOutConsumer() {
        return Optional.ofNullable(stdOutConsumer);
    }

    public void setStdOutConsumer(Consumer<String> stdOutConsumer) {
        this.stdOutConsumer = stdOutConsumer;
    }

    @Override
    public Optional<Consumer<String>> getStdErrConsumer() {
        return Optional.ofNullable(stdErrConsumer);
    }

    public void setStdErrConsumer(final Consumer<String> stdErrConsumer) {
        this.stdErrConsumer = stdErrConsumer;
    }
}
