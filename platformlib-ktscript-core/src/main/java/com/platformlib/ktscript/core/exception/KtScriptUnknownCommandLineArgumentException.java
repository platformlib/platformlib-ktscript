package com.platformlib.ktscript.core.exception;

import com.platformlib.ktscript.api.exception.KtScriptException;

/**
 * Thrown when unknown command line argument is specified.
 */
public class KtScriptUnknownCommandLineArgumentException extends KtScriptException {
    public KtScriptUnknownCommandLineArgumentException(final String message) {
        super(message);
    }
}
