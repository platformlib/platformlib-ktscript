package com.platformlib.ktscript.api.configuration;

import org.slf4j.Logger;

import java.util.Optional;

public interface LoggerConfiguration {
    Optional<Logger> getLogger();
    Optional<String> getName();
}
