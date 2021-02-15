package com.platformlib.ktscript.core.cli;

import com.platformlib.ktscript.core.configuration.DefaultKtScriptConfiguration;
import com.platformlib.ktscript.core.configuration.DefaultKtScriptEngineConfiguration;
import com.platformlib.ktscript.core.configuration.DefaultKtScriptRunConfiguration;
import com.platformlib.ktscript.core.exception.KtScriptDuplicateCommandLineArgumentException;
import com.platformlib.ktscript.core.exception.KtScriptIllegalArgumentOptionException;
import com.platformlib.ktscript.core.exception.KtScriptUnknownCommandLineArgumentException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Script command line arguments parser.
 */
public final class KtScriptCliParserUtils {
    private KtScriptCliParserUtils() {
    }

    public static KtScriptCliConfiguration parseConfiguration(final Collection<String> commandLineArguments) {
        final List<String> nonProcessedArguments = new ArrayList<>(commandLineArguments);
        final DefaultKtScriptConfiguration scriptConfiguration = new DefaultKtScriptConfiguration();
        final DefaultKtScriptRunConfiguration runConfiguration = new DefaultKtScriptRunConfiguration();
        final DefaultKtScriptEngineConfiguration engineConfiguration = new DefaultKtScriptEngineConfiguration();
        for (int i = 0; i < nonProcessedArguments.size(); i++) {
            final String argument = nonProcessedArguments.get(i);
            final CliOptionValuePair optionValuePair = parseArgument(argument).orElseThrow(() -> new KtScriptUnknownCommandLineArgumentException("Unknown line argument '" + argument + '"'));
            if (!optionValuePair.getOption().isOptionRequired() && optionValuePair.getValue().isPresent()) {
                throw new KtScriptIllegalArgumentOptionException("The '" + optionValuePair.argument + "' argument doesn't allow option: " + optionValuePair.getValue());
            }
            String argumentOption = null;
            if (optionValuePair.getOption().isOptionRequired()) {
                if (optionValuePair.getValue().isPresent()) {
                    argumentOption = optionValuePair.getValue().get();
                } else {
                    if (++i < nonProcessedArguments.size()) {
                        argumentOption = nonProcessedArguments.get(i);
                        if (parseArgument(argumentOption).isPresent()) {
                            throw new KtScriptIllegalArgumentOptionException("The '" + optionValuePair.argument + "' argument requires option but argument '" + argumentOption + "' is specified");
                        }
                    } else {
                        throw new KtScriptIllegalArgumentOptionException("The '" + optionValuePair.argument + "' argument requires option");
                    }
                }
            }
            switch (optionValuePair.getOption()) {
                case SCRIPT:
                    scriptConfiguration.getScriptLocation().ifPresent(location -> {
                        throw new KtScriptDuplicateCommandLineArgumentException("The script to execute is already specified: " + location);
                    });
                    scriptConfiguration.setScriptLocation(argumentOption);
                    break;
                case FROM_STDIN:
                    scriptConfiguration.getScriptLocation().ifPresent(location -> {
                        throw new KtScriptDuplicateCommandLineArgumentException("The script to execute is already specified: " + location);
                    });
                    scriptConfiguration.setScriptLocation("stdin:-");
                    break;
                case DEBUG_MODE:
                    //TODO implement
                    break;
                default:
                    throw new IllegalStateException("Not implement for " + optionValuePair.getOption());
            }
        }
        return new KtScriptCliConfiguration(scriptConfiguration, runConfiguration, engineConfiguration);
    }

    static Optional<CliOptionValuePair> parseArgument(final String argument) {
        final String[] parts = argument.split("=", 2);
        for (final KtScriptCliOption cliOption: KtScriptCliOption.values()) {
            if (parts[0].equals(cliOption.getShortArgument()) || parts[0].equals(cliOption.getLongArgument())) {
                return Optional.of(new CliOptionValuePair(parts[0], cliOption, parts.length == 2 ? parts[1] : null));
            }
        }
        return Optional.empty();
    }

     static final class CliOptionValuePair {
        private final KtScriptCliOption option;
        private final String value;
        private final String argument;

        private CliOptionValuePair(final String argument, final KtScriptCliOption option, final String value) {
            this.argument = Objects.requireNonNull(argument);
            this.option = Objects.requireNonNull(option);
            this.value = value;
        }

        public String getArgument() {
            return argument;
        }

        private KtScriptCliOption getOption() {
            return option;
        }

        private Optional<String> getValue() {
            return Optional.ofNullable(value);
        }
    }
}
