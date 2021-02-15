package com.platformlib.ktscript.core.logger;

import ch.qos.logback.classic.PatternLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class KtScriptLogbackLoggingPatternLayout extends PatternLayout {
    private static final String HEADER_FOOTER = "----------------------------------------------------------";
    private AtomicBoolean headerEnabled = new AtomicBoolean(false);

    private void putHeader(final StringBuilder header, final String tag, final String value) {
        header.append(tag).append(": ").append(value).append(System.lineSeparator());
    }

    @Override
    public String getFileHeader() {
        if (!headerEnabled.get()) {
            return null;
        }
        final StringBuilder header = new StringBuilder();
        putHeader(header, "Startup time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)));
        putHeader(header, "Script runner version", getClass().getPackage().getImplementationVersion());
        putHeader(header, "OS", System.getProperty("os.name") + ", " + System.getProperty("os.version") + " (" + System.getProperty("os.arch") + ")");
        putHeader(header, "User", System.getProperty("user.name"));
        putHeader(header, "User home", System.getProperty("user.home"));
        putHeader(header, "Current Dir", System.getProperty("user.dir"));
        header.append(HEADER_FOOTER);
        return header.toString();
    }

    public void enableHeader() {
        headerEnabled.set(true);
    }

    public void disableHeader() {
        headerEnabled.set(false);
    }

}
