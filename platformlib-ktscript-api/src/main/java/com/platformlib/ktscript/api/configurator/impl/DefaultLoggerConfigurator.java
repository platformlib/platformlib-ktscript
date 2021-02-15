package com.platformlib.ktscript.api.configurator.impl;

import com.platformlib.ktscript.api.configuration.LoggerConfiguration;
import com.platformlib.ktscript.api.configurator.LoggerConfigurator;
import org.slf4j.Logger;

import java.util.Optional;

public class DefaultLoggerConfigurator implements LoggerConfigurator, LoggerConfiguration {
    private Logger logger;
    private String name;

    @Override
    public Optional<Logger> getLogger() {
        return Optional.ofNullable(logger);
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
