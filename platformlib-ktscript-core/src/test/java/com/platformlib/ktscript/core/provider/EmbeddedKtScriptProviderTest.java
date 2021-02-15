package com.platformlib.ktscript.core.provider;

import com.platformlib.ktscript.api.factory.KtScriptRunners;
import com.platformlib.ktscript.core.runner.EmbeddedKtScriptRunner;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmbeddedKtScriptProviderTest {
    @Test
    void testGettingEmbeddedRunner() {
        assertThat(KtScriptRunners.embeddedRunner()).isExactlyInstanceOf(EmbeddedKtScriptRunner.class);
    }
}
