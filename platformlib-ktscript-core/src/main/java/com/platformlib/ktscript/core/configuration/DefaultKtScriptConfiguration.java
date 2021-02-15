package com.platformlib.ktscript.core.configuration;

import com.platformlib.ktscript.api.configuration.KtScriptConfiguration;

import java.util.Optional;

public class DefaultKtScriptConfiguration implements KtScriptConfiguration {
    private String scriptContent;
    private String scriptLocation;

    public void setScriptContent(final String scriptContent) {
        this.scriptContent = scriptContent;
    }

    public void setScriptLocation(final String scriptLocation) {
        this.scriptLocation = scriptLocation;
    }

    @Override
    public Optional<String> getScriptContent() {
        return Optional.ofNullable(scriptContent);
    }

    @Override
    public Optional<String> getScriptLocation() {
        return Optional.ofNullable(scriptLocation);
    }
}
