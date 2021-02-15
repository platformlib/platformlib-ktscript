package com.platformlib.ktscript.application

import com.platformlib.process.local.factory.LocalProcessBuilderFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.nio.file.Paths

class KtScriptBootApplicationTest {
    companion object {
        val logger = LoggerFactory .getLogger(KtScriptBootApplicationTest::class.java)!!
        val bootAppPath = Paths.get(KtScriptBootApplicationTest::class.java.getResource("/distribution/platformlib-ktscript-boot-app.jar")!!.toURI())!!
    }

    @Test
    fun testKtScriptBootApplication() {
        val scriptPath = Paths.get(javaClass.getResource("/scripts/check-work-directory.kts").toURI()).toAbsolutePath()
        val workPath = scriptPath.parent
        val processInstance = LocalProcessBuilderFactory
            .newLocalProcessBuilder()
            .useCurrentJava()
            .processInstance { it.unlimited() }
            .logger { it.logger(logger) }
            .workDirectory(workPath)
            .build()
            .execute("--illegal-access=warn", "-jar", bootAppPath, "--debug", "--script", scriptPath).toCompletableFuture().join()
        assertEquals(0, processInstance.exitCode)
        assertThat(processInstance.stdOut).contains("Current directory is $workPath")
    }
}