package com.platformlib.ktscript.api.configuration;

import java.util.Optional;
import java.util.function.Consumer;

public interface OutputConfiguration {
    Optional<Consumer<String>> getStdOutConsumer();
    Optional<Consumer<String>> getStdErrConsumer();
}
