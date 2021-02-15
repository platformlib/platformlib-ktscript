package com.platformlib.ktscript.core.cli;

/**
 * Kotlin script command line options.
 */
public enum KtScriptCliOption {
    //Command line argument after that all arguments will be interpreted as kotlin script arguments
    SCRIPT_ARGUMENTS_DELIMITER("--", null, false, "Script arguments delimiter. After this all arguments are interpreted as script arguments"),
    FROM_STDIN("-", null, false, "Read script from stdin"),
    SCRIPT("-s", "--script", true, "Script to run"),
    LOG_DESTINATION(null, "--log-destination", true, "Log destination"),
    WORK_PATH(null, "--work-path", true, "Work path"),
    VERBOSE_MODE("-v", "--verbose", false, "Verbose mode"),
    DEBUG_MODE("-d", "--debug", false, "Debug mode"),
    //TODO Check if the provided logger is necessary
    //PROVIDED_LOGGER(null, "--provided-logger", "Use provided logger (assume that logger is provided with script)"),
    TEMP_DIRECTORY_ARGUMENT(null, "--temp-directory", true, "Temp directory"), //Temp directory to use. Settings java.io.tmpdir in runtime are not guaranteed to have any effect
    LOGGER("-l", "--logger", true, "Logger to use. One of [logback, log4j]");

    private final String longArgument;
    private final String shortArgument;
    private final String description;
    private final boolean optionRequired;

    KtScriptCliOption(final String shortArgument, final String longArgument, final boolean optionRequired, final String description) {
        this.longArgument = longArgument;
        this.shortArgument = shortArgument;
        this.optionRequired = optionRequired;
        this.description = description;
    }

    public String getLongArgument() {
        return longArgument;
    }

    public String getShortArgument() {
        return shortArgument;
    }

    public boolean isOptionRequired() {
        return optionRequired;
    }
}
