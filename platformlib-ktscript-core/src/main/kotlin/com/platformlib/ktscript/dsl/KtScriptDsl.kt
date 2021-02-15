package com.platformlib.ktscript.dsl

import org.slf4j.Logger
import org.slf4j.LoggerFactory

// Default logger, shouldn't be used in case of parallel scripts run.
var logger: Logger = LoggerFactory.getLogger("com.platformlib.ktscript.dsl.KtScriptDsl")
