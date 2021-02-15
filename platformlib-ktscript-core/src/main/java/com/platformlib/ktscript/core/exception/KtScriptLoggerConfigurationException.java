package com.platformlib.ktscript.core.exception;

import com.platformlib.ktscript.api.exception.KtScriptException;

public class KtScriptLoggerConfigurationException extends KtScriptException {
    public KtScriptLoggerConfigurationException(final String message) {
        super(message);
    }

    public KtScriptLoggerConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public KtScriptLoggerConfigurationException(final Throwable cause) {
        super(cause);
    }
}
