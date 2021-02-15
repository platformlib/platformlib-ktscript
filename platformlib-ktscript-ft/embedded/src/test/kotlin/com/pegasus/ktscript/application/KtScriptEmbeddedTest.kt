package com.pegasus.ktscript.application

import com.platformlib.ktscript.api.factory.KtScriptRunners
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths

class KtScriptEmbeddedTest {
    @Test
    fun testReturnValue() {
        val scriptReturnedPath: Path = KtScriptRunners.embeddedRunner().use { embeddedKtScriptRunner ->
            embeddedKtScriptRunner.runScript("java.nio.file.Paths.get(\".\")")
        }
        assertEquals(Paths.get("."), scriptReturnedPath)
    }
}