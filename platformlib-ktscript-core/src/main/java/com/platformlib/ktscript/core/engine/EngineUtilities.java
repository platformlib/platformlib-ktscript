package com.platformlib.ktscript.core.engine;

import com.platformlib.ktscript.api.configuration.KtScriptConfiguration;
import com.platformlib.ktscript.api.exception.KtScriptException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class EngineUtilities {
    private EngineUtilities() {
    }

    //TODO Implement
    public static String getScriptContent(final KtScriptConfiguration scriptConfiguration) {
        if (!scriptConfiguration.getScriptContent().isPresent() && !scriptConfiguration.getScriptLocation().isPresent()) {
            throw new IllegalArgumentException("No script content and no script location are set");
        }
        if (scriptConfiguration.getScriptContent().isPresent() && scriptConfiguration.getScriptLocation().isPresent()) {
            throw new IllegalArgumentException("The script content and script location are set in the same time");
        }
        if (scriptConfiguration.getScriptContent().isPresent()) {
            return scriptConfiguration.getScriptContent().get();
        }
        if (scriptConfiguration.getScriptLocation().isPresent()) {
            try {
                return new String(Files.readAllBytes(Paths.get(scriptConfiguration.getScriptLocation().get())), Charset.defaultCharset());
            } catch (final IOException ioException) {
                throw new KtScriptException(ioException);
            }
        }
        return scriptConfiguration.getScriptContent().orElseThrow(() -> new IllegalStateException("Not implemented"));
    }
}
