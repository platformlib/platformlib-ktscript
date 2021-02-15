package com.platformlib.ktscript.core.runner;

import com.platformlib.ktscript.api.builder.KtScriptRunConfigurationBuilder;
import com.platformlib.ktscript.api.builder.impl.DefaultKtScriptRunConfigurationBuilder;
import com.platformlib.ktscript.core.configuration.EmbeddedKtScriptEngineConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmbeddedKtScriptRunnerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedKtScriptRunnerTest.class);

    @ParameterizedTest
    @MethodSource("scriptCapture")
    void testScriptOutputCapture(final String script, final String stdOutExpect, final String stdErrExpect) {
        final Object out = System.out;
        final Object err = System.err;
        final List<String> stdOut = new CopyOnWriteArrayList<>();
        final List<String> stdErr = new CopyOnWriteArrayList<>();
        final KtScriptRunConfigurationBuilder builder = new DefaultKtScriptRunConfigurationBuilder();
        builder.output(outputConfigurator -> {
            outputConfigurator.setStdOutConsumer(stdOut::add);
            outputConfigurator.setStdErrConsumer(stdErr::add);
        });
        try (EmbeddedKtScriptRunner embeddedKtScriptRunner = new EmbeddedKtScriptRunner(new EmbeddedKtScriptEngineConfiguration())) {
            embeddedKtScriptRunner.runScript(builder.build(), script);
        }
        if (stdOutExpect != null) {
            assertThat(stdOut).containsExactly(stdOutExpect);
        } else {
            assertTrue(stdOut.isEmpty());
        }
        if (stdErrExpect != null) {
            assertThat(stdErr).containsExactly(stdErrExpect);
        } else {
            assertTrue(stdErr.isEmpty());
        }
        assertSame(out, System.out);
        assertSame(err, System.err);
    }

    @ParameterizedTest
    @MethodSource("scriptCapture")
    void testScriptOutputCaptureWithoutSetting(final String script, final String stdOutExpect, final String stdErrExpect) {
        final Object out = System.out;
        final Object err = System.err;
        final AtomicReference<String> stdOut = new AtomicReference<>();
        final AtomicReference<String> stdErr = new AtomicReference<>();
        final KtScriptRunConfigurationBuilder builder = new DefaultKtScriptRunConfigurationBuilder();
        if (stdOutExpect != null) {
            builder.output(outputConfigurator -> outputConfigurator.setStdOutConsumer(stdOut::set));
        }
        if (stdErrExpect != null) {
            builder.output(outputConfigurator -> outputConfigurator.setStdErrConsumer(stdErr::set));
        }
        try (EmbeddedKtScriptRunner embeddedKtScriptRunner = new EmbeddedKtScriptRunner(new EmbeddedKtScriptEngineConfiguration())) {
            embeddedKtScriptRunner.runScript(builder.build(), script);
        }
        if (stdOutExpect != null) {
            assertEquals(stdOutExpect, stdOut.get());
        }
        if (stdErrExpect != null) {
            assertEquals(stdErrExpect, stdErr.get());
        }
        assertSame(out, System.out);
        assertSame(err, System.err);
    }

    @Test
    void testLogger() {
        final List<String> stdOut = new ArrayList<>();
        final KtScriptRunConfigurationBuilder builder = new DefaultKtScriptRunConfigurationBuilder();
        builder.output(outputConfigurator -> outputConfigurator.setStdOutConsumer(stdOut::add));
        builder.logger(loggerConfigurator -> loggerConfigurator.setLogger(LOGGER));
        try (EmbeddedKtScriptRunner embeddedKtScriptRunner = new EmbeddedKtScriptRunner(new EmbeddedKtScriptEngineConfiguration())) {
            embeddedKtScriptRunner.runScript(builder.build(), getKtScript("LoggerScript.kts"));
        }
        assertThat(stdOut.get(0)).endsWith("Logger script");
        assertThat(stdOut.get(1)).endsWith("EmbeddedKtScriptRunnerTest - Error message");
        assertThat(stdOut.get(2)).endsWith("EmbeddedKtScriptRunnerTest - Info message");
        assertThat(stdOut.get(3)).endsWith("EmbeddedKtScriptRunnerTest - Debug message");
        assertThat(stdOut.get(4)).endsWith("EmbeddedKtScriptRunnerTest - Trace message");
    }

    static Stream<Arguments> scriptCapture() {
        return Stream.of(
                Arguments.of("", null, null),
                Arguments.of("System.out.print(\"One\");", "One", null),
                Arguments.of("System.err.print(\"Two\");", null, "Two"),
                Arguments.of("System.out.print(\"Three\");System.err.print(\"Four\");", "Three", "Four")
        );
    }

    private String getKtScript(final String baseScriptName) {
        try (InputStream is = new BufferedInputStream(EmbeddedKtScriptRunnerTest.class.getResourceAsStream("/scripts/" + baseScriptName))) {
            final byte[] buffer = new byte[8192];
            int len;
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            return os.toString();
        } catch (final IOException ioException) {
            throw new IllegalStateException(ioException);
        }
    }
}
