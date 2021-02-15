package com.platformlib.ktscript.api.configurator;

import java.util.function.Consumer;

public interface OutputConfigurator {
    void setStdOutConsumer(Consumer<String> stdOutConsumer);
    void setStdErrConsumer(Consumer<String> stdErrConsumer);
}
