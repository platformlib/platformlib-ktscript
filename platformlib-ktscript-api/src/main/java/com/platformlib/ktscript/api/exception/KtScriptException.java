package com.platformlib.ktscript.api.exception;

/**
 * Base script exception.
 */
public class KtScriptException extends RuntimeException {
    public KtScriptException(String message) {
        super(message);
    }

    public KtScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    public KtScriptException(Throwable cause) {
        super(cause);
    }
}
