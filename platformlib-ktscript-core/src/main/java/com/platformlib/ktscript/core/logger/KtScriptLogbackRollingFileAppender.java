package com.platformlib.ktscript.core.logger;

import ch.qos.logback.core.rolling.RollingFileAppender;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class KtScriptLogbackRollingFileAppender<E> extends RollingFileAppender<E> {
    private static final Map<String, AtomicBoolean> INITIALIZED_APPENDERS = new ConcurrentHashMap<>();
    /**
     * Roll over log files on startup.
     */
    @Override
    public void start() {
        AtomicBoolean appender = INITIALIZED_APPENDERS.get(getName());
        if (appender == null) {
            appender = new AtomicBoolean(false);
            INITIALIZED_APPENDERS.put(getName(), appender);
        }
        if (!appender.get()) {
            ((KtScriptLogbackLoggingPatternLayoutEncoder) getEncoder()).getPatternLayout().enableHeader();
            if (Files.exists(Paths.get(fileName))) {
                rollover();
                ((KtScriptLogbackLoggingPatternLayoutEncoder) getEncoder()).getPatternLayout().disableHeader();
            }
        }
        super.start();
        appender.set(true);
        ((KtScriptLogbackLoggingPatternLayoutEncoder) this.getEncoder()).getPatternLayout().enableHeader();
    }
}
