/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.foi.uzdiz.tsaghir.data.CommandLineArguments;
import org.foi.uzdiz.tsaghir.data.SystemDatabase;
import org.foi.uzdiz.tsaghir.helpers.FileHelper;
import org.foi.uzdiz.tsaghir.helpers.RegexHelper;
import org.foi.uzdiz.tsaghir.helpers.Statistics;
import org.foi.uzdiz.tsaghir.models.Actuator;
import org.foi.uzdiz.tsaghir.models.Place;
import org.foi.uzdiz.tsaghir.models.Sensor;
import org.foi.uzdiz.tsaghir.threads.WorkingThread;
import org.foi.uzdiz.tsaghir.views.ConsoleView;

/**
 *
 * @author tsaghir
 */
public class ConsoleController {

    private SystemDatabase model;
    private ConsoleView view;

    public ConsoleController(SystemDatabase model, ConsoleView view) {
        this.model = model;
        this.view = view;
        FileHelper.setConsoleView(view);
    }

    public void printSeparator() {
        view.printSeparator();
    }

    public boolean sortCommandLineArguments(String[] args) {
        if (args.length > 0) {
            if (args.length == 1) {
                if (args[0].equals("--help")) {
                    view.printCommandLineHelp();
                }
            } else {
                StringBuilder sb = new StringBuilder();
                CommandLineArguments.CommandLineArgumentsBuilder builder = new CommandLineArguments.CommandLineArgumentsBuilder();
                CommandLineArguments arguments = null;
                for (int i = 0; i < args.length; i++) {
                    makeCommandLineArgumentsObject(args[i], args[i += 1], builder, sb);
                }
                String command = sb.toString().trim();
                arguments = builder.build();
                model.setCommandLineArguments(arguments);
                view.printStartHeadline();
                view.printCommandLineDetails(command);
            }
            return false;
        } else {
            view.printMessage("[+] Error: Komandna linija je prazna!");
            return true;
        }
    }

    private void makeCommandLineArgumentsObject(String command, String value, CommandLineArguments.CommandLineArgumentsBuilder cmdArgumentsBuilder, StringBuilder sb) {
        switch (command) {
            case "-br":
                cmdArgumentsBuilder.commandLineNumber(value);
                sb.append(command).append(" vrijednost: ").append(value).append("\n");
                break;
            case "-bs":
                cmdArgumentsBuilder.columnNumber(value);
                sb.append(command).append(" vrijednost: ").append(value).append("\n");
                break;
            case "-brk":
                cmdArgumentsBuilder.lineNumber(value);
                sb.append(command).append(" vrijednost: ").append(value).append("\n");
                break;
            case "-pi":
                cmdArgumentsBuilder.averagePi(value);
                sb.append(command).append(" vrijednost: ").append(value).append("\n");
                break;
            case "-g":
                cmdArgumentsBuilder.seedGenerator(value);
                sb.append(command).append(" vrijednost: ").append(value).append("\n");
                break;
            case "-m":
                cmdArgumentsBuilder.placesFile(value);
                sb.append(command).append(" vrijednost: ").append(value).append("\n");
                break;
            case "-s":
                cmdArgumentsBuilder.sensorsFile(value);
                sb.append(command).append(" vrijednost: ").append(value).append("\n");
                break;
            case "-a":
                cmdArgumentsBuilder.actuatorsFile(value);
                sb.append(command).append(" vrijednost: ").append(value).append("\n");
                break;
            case "-r":
                cmdArgumentsBuilder.layoutFile(value);
                sb.append(command).append(" vrijednost: ").append(value).append("\n");
                break;
            case "-tcd":
                cmdArgumentsBuilder.threadCycleDuration(value);
                sb.append(command).append(" vrijednost: ").append(value).append("\n");
                break;
            default:
                break;
        }
    }

    public void loadFiles() {
        loadPlacesFile(model.getCommandLineArguments().getPlacesFile());
        loadSensorsFile(model.getCommandLineArguments().getSensorsFile());
        loadActuatorsFile(model.getCommandLineArguments().getActuatorsFile());
        loadOrderFile(model.getCommandLineArguments().getLayoutFile());

    }

    private void loadPlacesFile(String fileName) {
        List<String[]> places = FileHelper.loadFile(fileName);
        model.setPlaces(places);
        view.printMessage("[+] Info: Učitavanje mjesta iz datoteke");
        view.printSeparator();
        view.printMessage("[+] Info: Ispisujem mjesta:");
        for (String[] place : places) {
            if (place.length == 5) {
                view.printMessage("\t[" + place[0] + "] " + place[1] + ": " + place[2] + ", " + place[3] + ", " + place[4]);
            }
        }
        view.printSeparator();
        view.printMessage("[+]Greške mjesta:");
        FileHelper.validateFileLines(fileName, places);
        view.printSeparator();
    }

    private void loadSensorsFile(String fileName) {
        List<String[]> sensors = FileHelper.loadFile(fileName);
        model.setSensors(sensors);
        view.printMessage("[+] Info: Učitavanje senzora iz datoteke");
        view.printSeparator();
        view.printMessage("[+] Info: Ispisujem senzore:");
        for (String[] sensor : sensors) {
            if (sensor.length <= 7) {
                view.printMessage("\t[" + sensor[0] + "] " + sensor[1] + ": " + sensor[2] + ", " + sensor[3] + ", " + sensor[4] + ", " + sensor[5] + ", " + (sensor.length == 7 ? sensor[6] : ""));
            }
        }
        view.printSeparator();
        view.printMessage("[+]Greške senzora:");
        FileHelper.validateFileLines(fileName, sensors);
        view.printSeparator();
    }

    private void loadActuatorsFile(String fileName) {
        List<String[]> actuators = FileHelper.loadFile(fileName);
        model.setActuators(actuators);
        view.printMessage("[+] Info: Učitavanje aktuatora iz datoteke");
        view.printSeparator();
        view.printMessage("[+] Info: Ispisujem aktuatore:");
        for (String[] actuator : actuators) {
            if (actuator.length <= 7) {
                view.printMessage("\t[" + actuator[0] + "] " + actuator[1] + ": " + actuator[2] + ", " + actuator[3] + ", " + actuator[4] + ", " + actuator[5] + ", " + (actuator.length == 7 ? actuator[6] : ""));
            }
        }
        view.printSeparator();
        view.printMessage("Greške aktuatora:");
        FileHelper.validateFileLines(fileName, actuators);
        view.printSeparator();
    }

    private void loadOrderFile(String fileName) {
        List<String[]> orders = FileHelper.loadFile(fileName);
        model.setOrder(orders);
        view.printMessage("[+] Info: Učitavanje redoslijeda iz datoteke");
        view.printSeparator();
        view.printMessage("[+] Info: Ispisujem redoslijed:");
        for (String[] order : orders) {
            if (order.length <= 3) {
                view.printMessage("\t[" + order[0] + "] - [" + order[1] + "], " + order[2]);
            } else {
                view.printMessage("\t[" + order[0] + "] - [" + order[1] + "], " + order[2] + ", " + order[3] + ", " + order[4]);
            }
        }
        view.printSeparator();
        view.printMessage("Greške redoslijeda:");
        FileHelper.validateFileLines(fileName, orders);
        view.printSeparator();
    }

    public void doCommandInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command = "";
        do {
            view.printMessage("Unesite naredbu: ");
            command = reader.readLine();
            doCommand(command);
        } while (!command.equals("I"));
    }

    private void doCommand(String command) {
        switch (command) {
            case "H":
                view.printCommandsHelp();
                view.printSeparator();
                break;
            case "M":
                Statistics.printPlacesCount++;
                view.printPlacesIds(model.getPlacesList());
                view.printSeparator();
                break;
            case "S ":
                Statistics.printSensorsCount++;
                view.printSensorsIds(model.getSensorsList());
                view.printSeparator();
                break;
            case "A":
                view.printActuatorsIds(model.getActuatorsList());
                view.printSeparator();
                break;
            case "S":
                view.printStatistics();
                view.printSeparator();
                break;
            case "SP":
                //TODO spremi podatke
                Statistics.saveDataCount++;
                view.printSeparator();
                break;
            case "VP":
                //TODO vrati spremljene podatke
                Statistics.retrieveDataCount++;
                view.printSeparator();
                break;
            case "VF":
                // TODO vlastita funkcionalnost
                Statistics.myFunctionCall++;
                view.printSeparator();
                break;
            default:
                if (command.matches(RegexHelper.getInstance().CYCLE_REGEX)) {
                    view.printMessage("[+] Info: Pokrećem ciklus dretvi");
                    String[] input = command.split(" ");
                    WorkingThread workingThread = new WorkingThread(command.equals("C") ? Integer.parseInt(model.getCommandLineArguments().getThreadCycleDuration())
                            : Integer.parseInt(input[1]), view);
                    workingThread.start();
                    view.printSeparator();
                } else if (command.matches(RegexHelper.getInstance().PI_REGEX)) {
                    String[] input = command.split(" ");
                    model.setNewPi(Integer.parseInt(input[1]));
                    Statistics.piCall++;
                    view.printSeparator();
                } else if (command.matches(RegexHelper.getInstance().PLACE_PRINT)) {
                    Statistics.printPlacesCount++;
                    String[] input = command.split(" ");
                    Place place = model.getPlaceById(input[1]);
                    view.printPlaceById(place);
                    view.printSeparator();
                } else if (command.matches(RegexHelper.getInstance().SENSOR_PRINT)) {
                    Statistics.printSensorsCount++;
                    String[] input = command.split(" ");
                    Sensor sensor = model.getSensorById(input[1]);
                    view.printSensorById(sensor);
                    view.printSeparator();
                } else if (command.matches(RegexHelper.getInstance().ACTUATOR_PRINT)) {
                    Statistics.printActuatorCount++;
                    String[] input = command.split(" ");
                    Actuator actuator = model.getActuatorById(input[1]);
                    view.printActuatorById(actuator);
                    view.printSeparator();
                }
                break;
        }
    }
}
