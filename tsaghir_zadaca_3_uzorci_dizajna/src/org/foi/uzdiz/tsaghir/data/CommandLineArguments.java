/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.data;

import org.foi.uzdiz.tsaghir.helpers.Generator;

/**
 *
 * @author tsaghir
 */
public class CommandLineArguments {

    private final String seedGenerator;
    private final String placesFile;
    private final String sensorsFile;
    private final String actuatorsFile;
    private final String layoutFile;
    private final String algorithmName;
    private final String threadCycleDuration;
    private final String threadCycleNumber;
    private final String printFile;
    private final String containerLimit;
    private final String lineNumber;
    private final String columnNumber;
    private final String commandLineNumber;
    private final String averagePi;

    public CommandLineArguments(
            final String newSeedGenerator,
            final String newPlacesFile,
            final String newSensorsFile,
            final String newActuatorsFile,
            final String newLayoutFile,
            final String newAlgorithmName,
            final String newThreadCycleDuration,
            final String newThreadCycleNumber,
            final String newPrintFile,
            final String newContainerLimit,
            final String newLineNumber,
            final String newColumnNumber,
            final String newCommandLineNumber,
            final String newAveragePi
    ) {
        seedGenerator = newSeedGenerator;
        placesFile = newPlacesFile;
        sensorsFile = newSensorsFile;
        actuatorsFile = newActuatorsFile;
        layoutFile = newLayoutFile;
        algorithmName = newAlgorithmName;
        threadCycleDuration = newThreadCycleDuration;
        threadCycleNumber = newThreadCycleNumber;
        printFile = newPrintFile;
        containerLimit = newContainerLimit;
        lineNumber = newLineNumber;
        columnNumber = newColumnNumber;
        commandLineNumber = newCommandLineNumber;
        averagePi = newAveragePi;
    }

    public String getSeedGenerator() {
        return seedGenerator;
    }

    public String getPlacesFile() {
        return placesFile;
    }

    public String getSensorsFile() {
        return sensorsFile;
    }

    public String getActuatorsFile() {
        return actuatorsFile;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getThreadCycleDuration() {
        return threadCycleDuration;
    }

    public String getThreadCycleNumber() {
        return threadCycleNumber;
    }

    public String getPrintFile() {
        return printFile;
    }

    public String getContainerLimit() {
        return containerLimit;
    }

    public String getLayoutFile() {
        return layoutFile;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public String getColumnNumber() {
        return columnNumber;
    }

    public String getCommandLineNumber() {
        return commandLineNumber;
    }

    public String getAveragePi() {
        return averagePi;
    }

    public static class CommandLineArgumentsBuilder {

        private String nestedSeedGenerator;
        private String nestedPlacesFile;
        private String nestedSensorsFile;
        private String nestedActuatorsFile;
        private String nestedLayoutFile;
        private String nestedAlgorithmName;
        private String nestedThreadCycleDuration;
        private String nestedThreadCycleNumber;
        private String nestedPrintFile;
        private String nestedContainerLimit;
        private String nestedLineNumber;
        private String nestedColumnNumber;
        private String nestedCommandLineNumber;
        private String nestedAveragePi;

        public CommandLineArgumentsBuilder() {
        }
        
        public CommandLineArgumentsBuilder averagePi(String newAveragePi) {
            nestedAveragePi = !newAveragePi.equals("") ? newAveragePi : "50";
            return this;
        }
        
        public CommandLineArgumentsBuilder lineNumber(String newLineNumber) {
            nestedLineNumber = !newLineNumber.equals("") ? newLineNumber : "24";
            return this;
        }
        
        public CommandLineArgumentsBuilder columnNumber(String newColumnNumber) {
            nestedColumnNumber = !newColumnNumber.equals("") ?  newColumnNumber : "80";
            return this;
        }
        
        public CommandLineArgumentsBuilder commandLineNumber(String newCommandLineNumber) {
            nestedCommandLineNumber = !newCommandLineNumber.equals("") ? newCommandLineNumber : "2";
            return this;
        }

        public CommandLineArgumentsBuilder seedGenerator(String newSeedGenerator) {
            nestedSeedGenerator = newSeedGenerator;
            return this;
        }

        public CommandLineArgumentsBuilder placesFile(String newPlacesFile) {
            nestedPlacesFile = newPlacesFile;
            return this;
        }

        public CommandLineArgumentsBuilder sensorsFile(String newSensorsFile) {
            nestedSensorsFile = newSensorsFile;
            return this;
        }

        public CommandLineArgumentsBuilder actuatorsFile(String newActuatorsFile) {
            nestedActuatorsFile = newActuatorsFile;
            return this;
        }

        public CommandLineArgumentsBuilder layoutFile(String newLayoutFile) {
            nestedLayoutFile = newLayoutFile;
            return this;
        }

        public CommandLineArgumentsBuilder algorithmName(String newAlgorithmName) {
            nestedAlgorithmName = newAlgorithmName;
            return this;
        }

        public CommandLineArgumentsBuilder threadCycleDuration(String newThreadCycleDuration) {
            nestedThreadCycleDuration = !newThreadCycleDuration.equals("") ? newThreadCycleDuration : String.valueOf(Generator.getInstance().dajSlucajniBroj(1, 17));
            return this;
        }

        public CommandLineArgumentsBuilder threadCycleNumber(String newThreadCycleNumber) {
            nestedThreadCycleNumber = newThreadCycleNumber;
            return this;
        }

        public CommandLineArgumentsBuilder printFile(String newPrintFile) {
            nestedPrintFile = newPrintFile;
            return this;
        }

        public CommandLineArgumentsBuilder containerLimit(String newContainerLimit) {
            nestedContainerLimit = newContainerLimit;
            return this;
        }

        public CommandLineArguments build() {
            return new CommandLineArguments(
                    nestedSeedGenerator,
                    nestedPlacesFile,
                    nestedSensorsFile,
                    nestedActuatorsFile,
                    nestedLayoutFile,
                    nestedAlgorithmName,
                    nestedThreadCycleDuration,
                    nestedThreadCycleNumber,
                    nestedPrintFile,
                    nestedContainerLimit,
                    nestedLineNumber,
                    nestedColumnNumber,
                    nestedCommandLineNumber,
                    nestedAveragePi
            );
        }
    }
}
