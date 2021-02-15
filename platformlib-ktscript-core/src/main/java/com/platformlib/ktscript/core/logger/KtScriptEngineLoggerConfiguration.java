package com.platformlib.ktscript.core.logger;

import com.platformlib.ktscript.api.enums.LoggerLevel;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

class KtScriptEngineLoggerConfiguration {
    private String scriptName;
    private String logPackage;
    private Path logDirectoryPath;
    private LoggerLevel logLevel;

    LoggerLevel getLogLevel() {
        return logLevel;
    }

    Optional<String> getScriptName() {
        return Optional.ofNullable(scriptName);
    }

    Optional<String> getLogPackage() {
        return Optional.of(logPackage);
    }

    Optional<Path> getLogDirectoryPath() {
        return Optional.ofNullable(logDirectoryPath);
    }

    public void setScriptName(final String scriptName) {
        this.scriptName = Objects.requireNonNull(scriptName);
    }

    public void setLogPackage(final String logPackage) {
        this.logPackage = Objects.requireNonNull(logPackage);
    }

    public void setLogDirectoryPath(final Path logDirectoryPath) {
        this.logDirectoryPath = Objects.requireNonNull(logDirectoryPath);
    }

    public void setLogLevel(final LoggerLevel logLevel) {
        this.logLevel = Objects.requireNonNull(logLevel);
    }
}
