package com.platformlib.ktscript.api.configuration;


import java.util.Optional;

/**
 * Script configuration.
 */
public interface KtScriptConfiguration {

    /**
     * Get kotlin script location.
     * @return Return kotlin script location
     */

    Optional<String> getScriptLocation();

    /**
     * Get kotlin script content.
     * @return Returns script content
     */
    Optional<String> getScriptContent();
}
