package com.platformlib.ktscript.core.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

public class KtScriptLogbackLoggingPatternLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {
    private KtScriptLogbackLoggingPatternLayout patternLayout = null;

    @Override
    public void start() {
        patternLayout = new KtScriptLogbackLoggingPatternLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern(getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        layout = patternLayout;
        super.start();
    }

    KtScriptLogbackLoggingPatternLayout getPatternLayout() {
        return patternLayout;
    }
}