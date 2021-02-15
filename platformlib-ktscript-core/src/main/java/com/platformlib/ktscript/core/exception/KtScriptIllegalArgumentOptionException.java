package com.platformlib.ktscript.core.exception;

import com.platformlib.ktscript.api.exception.KtScriptException;

/**
 * Thrown when command line argument is specified with option when it is not allowed.
 */
public class KtScriptIllegalArgumentOptionException extends KtScriptException {
    public KtScriptIllegalArgumentOptionException(final String message) {
        super(message);
    }
}
