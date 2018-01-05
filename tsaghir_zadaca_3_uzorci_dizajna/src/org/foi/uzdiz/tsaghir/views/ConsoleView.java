/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.views;

import java.util.List;
import org.foi.uzdiz.tsaghir.helpers.Statistics;
import org.foi.uzdiz.tsaghir.models.Actuator;
import org.foi.uzdiz.tsaghir.models.Place;
import org.foi.uzdiz.tsaghir.models.Sensor;

/**
 *
 * @author tsaghir
 */
public class ConsoleView {

    public void printStartHeadline() {
        printSeparator();
        System.out.println("\t\t\t\t\tNAZIV ZADAĆE: ToF 3. dio (Things of FOI - 3. dio)");
        printSeparator();
    }

    public void printCommandLineHelp() {
        System.out.println("******Pomoć******");
        System.out.println("OPCIJE:");
        System.out.println("\t-br\tBroj redaka na ekranu (24-40)");
        System.out.println("\t-bs\tBroj stupaca na ekranu (80-160)");
        System.out.println("\t-brk\tBroj redaka na ekranu za unos komandi (2-5)");
        System.out.println("\t-pi\tProsječni % ispravnosti uređaja (0-100)");
        System.out.println("\t-g\tSjeme za generator slučajnog broja (u intervalu 0-65535)");
        System.out.println("\t-m\tNaziv datoteke mjesta");
        System.out.println("\t-s\tNaziv datoteke senzora");
        System.out.println("\t-a\tNaziv datoteke aktuatora");
        System.out.println("\t-r\tNaziv datoteke rasporeda");
        System.out.println("\t-tcd\tTrajanje ciklusa dretve u sekundama");
    }

    public void printCommandsHelp() {
        System.out.println("******Pomoć******");
        System.out.println("KOMANDE:");
        System.out.println("\tM x\tIspis podataka mjesta x");
        System.out.println("\tS x\tIspis podataka senzora x");
        System.out.println("\tA x\tIspis podataka aktuatora x");
        System.out.println("\tS\tIspis statistike");
        System.out.println("\tSP\tSpremi podatke");
        System.out.println("\tVP\tVrati spremljene podatke (mjesta, uređaja)");
        System.out.println("\tC n\tizvršavanje n ciklusa dretve (1-100)");
        System.out.println("\tVF [x]\tIzvršavanje vlastite funkcionalnosti");
        System.out.println("\tH \tPomoć, ispis dopuštenih komandi");
        System.out.println("\tPI n\tProsječni % ispravnosti uređaja (0-100)");
        System.out.println("\tI\tIzlaz");
    }

    public void printCommandLineDetails(String command) {
        printSeparator();
        System.out.println("Detalji upisane naredbe:");
        System.out.println(command);
        printSeparator();
    }

    public void printSeparator() {
        System.out.println("--------------------------------------------------------------------------------------------------------------");
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printSensorValues(List<Sensor> sensors) {
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.printf("%10s %30s %10s %10s %10s %10s %10s", "SENZOR ID", "NAZIV", "TIP", "VRSTA", "MIN", "MAX", "OPIS");
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (Sensor sensor : sensors) {
            System.out.format("%10s %30s %10s %10s %10s %10s %10s",
                    sensor.getId(), sensor.getName(), sensor.getType(), sensor.getKind(), sensor.getMinValue(), sensor.getMaxValue(), sensor.getComment() != null ? sensor.getComment() : "");
            System.out.println();
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    public void printPlacesIds(List<Place> places) {
        String text = "";
        int index = 1;
        for (Place place : places) {
            text += place.getId();
            if (index != places.size()) {
                text += ", ";
            } else {
                text += ";";
            }
            index++;
        }
        System.out.println(text);
    }

    public void printPlaceById(Place place) {
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("%10s %30s %10s %10s %10s", "MJESTO ID", "NAZIV", "TIP", "BR. SENZORA", "BR. AKTUATORA");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.format("%10s %30s %10s %10s %10s",
                place.getId(), place.getName(), place.getType(), place.getSensorsNumber(), place.getActuatorsNumber());
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------");
    }

    public void printActuatorsIds(List<Actuator> actuators) {
        String text = "";
        int index = 1;
        for (Actuator actuator : actuators) {
            text += actuator.getId();
            if (index != actuators.size()) {
                text += ", ";
            } else {
                text += ";";
            }
            index++;
        }
        System.out.println(text);
    }

    public void printActuatorById(Actuator actuator) {
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.printf("%10s %30s %10s %10s %10s %10s %10s", "AKTUATOR ID", "NAZIV", "TIP", "VRSTA", "MIN", "MAX", "OPIS");
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.format("%10s %30s %10s %10s %10s %10s %10s",
                actuator.getId(), actuator.getName(), actuator.getType(), actuator.getKind(), actuator.getMinValue(), actuator.getMaxValue(), actuator.getComment() != null ? actuator.getComment() : "nema");
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------------");

    }

    public void printSensorsIds(List<Sensor> sensors) {
        String text = "";
        int index = 1;
        for (Sensor place : sensors) {
            text += place.getId();
            if (index != sensors.size()) {
                text += ", ";
            } else {
                text += ";";
            }
            index++;
        }
        System.out.println(text);
    }

    public void printSensorById(Sensor sensor) {
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("%10s %20s %10s %10s %10s %10s %10s", "SENZOR ID", "NAZIV", "TIP", "VRSTA", "MIN", "MAX", "OPIS");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.format("%10s %20s %10s %10s %10s %10s %10s",
                sensor.getId(), sensor.getName(), sensor.getType(), sensor.getKind(), sensor.getMinValue(), sensor.getMaxValue(), sensor.getComment() != null ? sensor.getComment() : "nema");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------");

    }

    public void printStatistics() {
        System.out.println("Ispis mjesta..........................................." + Statistics.printPlacesCount);
        System.out.println("Ukupno mjesta.........................................." + Statistics.placesCount);
        System.out.println("Greške mjesta.........................................." + Statistics.placesErrors);
        System.out.println("Ispis senzora.........................................." + Statistics.printSensorsCount);
        System.out.println("Ukupno senzora........................................." + Statistics.sensorsCount);
        System.out.println("Greške senzora........................................." + Statistics.sensorsErrors);
        System.out.println("Ispis Aktuatora........................................" + Statistics.printActuatorCount);
        System.out.println("Ukupno aktuatora......................................." + Statistics.actuatorsCount);
        System.out.println("Greške aktuatora......................................." + Statistics.actuatorsErrors);
        System.out.println("Spremljeno podataka...................................." + Statistics.saveDataCount);
        System.out.println("Preuzeto podataka......................................" + Statistics.retrieveDataCount);
        System.out.println("Ukupan rad dretvi......................................" + Statistics.threadWorkingTimeSum);
        System.out.println("Pozivi vlastite funkcije..............................." + Statistics.myFunctionCall);
        System.out.println("Promjena PI broja......................................" + Statistics.piCall);
    }
}
