package com.platformlib.ktscript.core.exception;

import com.platformlib.ktscript.api.exception.KtScriptException;

/**
 * Thrown when unknown command line argument is duplicated.
 */
public class KtScriptDuplicateCommandLineArgumentException extends KtScriptException {
    public KtScriptDuplicateCommandLineArgumentException(final String message) {
        super(message);
    }
}
