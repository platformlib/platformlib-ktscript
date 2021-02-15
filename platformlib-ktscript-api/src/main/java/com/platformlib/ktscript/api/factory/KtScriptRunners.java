package com.platformlib.ktscript.api.factory;

import com.platformlib.ktscript.api.KtScriptRunner;
import com.platformlib.ktscript.api.configuration.KtScriptRunnerConfiguration;
import com.platformlib.ktscript.api.enums.RunnerType;
import com.platformlib.ktscript.api.provider.KtScriptProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Script runners factory.
 */
public final class KtScriptRunners {
    private KtScriptRunners() {
    }

    public static KtScriptRunner embeddedRunner() {
        return newRunner(RunnerType.EMBEDDED, null);
    }

    public static KtScriptRunner forkRunner(final KtScriptRunnerConfiguration ktScriptRunnerConfiguration) {
        return newRunner(RunnerType.FORK, ktScriptRunnerConfiguration);
    }

    public static KtScriptRunner newRunner(final RunnerType type, final KtScriptRunnerConfiguration ktScriptRunnerConfiguration) {
        final ServiceLoader<KtScriptProvider> providers = ServiceLoader.load(KtScriptProvider.class);
        final List<KtScriptProvider> ktScriptProviders = new ArrayList<>();
        providers.forEach(provider -> {
            if (provider.getType() == type) {
                ktScriptProviders.add(provider);
            }
        });
        if (ktScriptProviders.isEmpty()) {
            throw new IllegalStateException("No ktScript provider has been found for " + type);
        }
        if (ktScriptProviders.size() > 1) {
            throw new IllegalStateException("More than one providers have been found for " + type + ": " + ktScriptProviders);
        }
        return ktScriptProviders.get(0).newScriptRunner(ktScriptRunnerConfiguration);
    }
}
