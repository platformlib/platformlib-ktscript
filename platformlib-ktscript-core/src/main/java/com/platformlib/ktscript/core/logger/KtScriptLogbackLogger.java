package com.platformlib.ktscript.core.logger;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.platformlib.ktscript.api.enums.LoggerLevel;
import com.platformlib.ktscript.core.exception.KtScriptLoggerConfigurationException;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class KtScriptLogbackLogger {
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private KtScriptLogbackLogger() {
    }

    public static void configure(final KtScriptEngineLoggerConfiguration configuration) {
        final String logLevel = configuration.getLogLevel().name();
        //Setup logger
        final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        final JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(context);
        context.reset(); // Override default configuration
        final boolean logToFile = configuration.getLogDirectoryPath().isPresent();
        if (logToFile) {
            context.putProperty("log-type", "file");
        }
        final String scriptLog = configuration.getScriptName().orElseGet(() -> "unnamed-" + DEFAULT_DATE_FORMAT.format(new Date()));
        context.putProperty("log-file-name", scriptLog);
        context.putProperty("log-level", logLevel);
        try {
            jc.doConfigure(KtScriptLogbackLogger.class.getResourceAsStream("/ktscript-logback-standalone.xml"));
        } catch (final JoranException joranException) {
            throw new KtScriptLoggerConfigurationException(joranException);
        }
        if (logToFile && (configuration.getLogLevel() == LoggerLevel.TRACE || configuration.getLogLevel() == LoggerLevel.DEBUG || configuration.getLogLevel() == LoggerLevel.INFO)) {
            KtScriptConsoleLogger.CONSOLE_LOGGER.info("The script log is {}.log", scriptLog);
        }
    }
}
