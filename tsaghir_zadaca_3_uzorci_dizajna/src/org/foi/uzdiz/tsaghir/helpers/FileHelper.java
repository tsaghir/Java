/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.helpers;

import org.foi.uzdiz.tsaghir.views.ConsoleView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.tsaghir.data.SystemDatabase;
import org.foi.uzdiz.tsaghir.models.Actuator;
import org.foi.uzdiz.tsaghir.models.Order;
import org.foi.uzdiz.tsaghir.models.Place;
import org.foi.uzdiz.tsaghir.models.Sensor;

/**
 *
 * @author tsaghir
 */
public class FileHelper {

    private static int index;
    private static ConsoleView consoleView;
    public static List<String[]> loadFile(String fileName) {

        List<String[]> loadedFile = new ArrayList<>();
        String line = "";
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            index = 0;
            while ((line = br.readLine()) != null) {
                if (!fileName.equals("Raspored.txt")) {
                    if (index != 0) {
                        String[] splitted = line.split(cvsSplitBy);
                        loadedFile.add(splitted);
                    }
                }
                index++;
            }
        } catch (IOException e) {
            String message = "[+] Error: Greška kod učitavanja datoteke mjesta: " + e.getMessage();
            consoleView.printMessage(message);
        }
        return loadedFile;
    }

    public static void validateFileLines(String fileName, List<String[]> lines) {

        switch (fileName) {
            case "DZ_3_mjesta.txt":
                validatePlaces(lines, fileName);
                break;
            case "DZ_3_senzori.txt":
                validateSensors(lines, fileName);
                break;
            case "DZ_3_aktuatori.txt":
                validateActuators(lines, fileName);
                break;
            case "DZ_3_raspored.txt":
                validateOrder(lines, fileName);
                break;
            default:
                break;
        }
    }

    private static void validatePlaces(List<String[]> placesLines, String fileName) {
        boolean error = false;

        for (int i = 0; i < placesLines.size(); i++) {
            String[] place = placesLines.get(i);
            if (place.length == 5) {
                if (RegexHelper.getInstance().checkNumberRegex(place[0])) {
                    if (RegexHelper.getInstance().checkNameRegex(place[1])) {
                        if (RegexHelper.getInstance().checkPlacesTypeRegex(place[2])) {
                            if (RegexHelper.getInstance().checkNumberRegex(place[3])) {
                                if (!RegexHelper.getInstance().checkNumberRegex(place[4])) {
                                    consoleView.printMessage("[+] Error: [" + place[0] + "] " + place[1] + " ima grešku u -broju aktuatora- mjesta.");
                                    error = true;
                                    Statistics.placesErrors++;
                                }
                            } else {
                                consoleView.printMessage("[+] Error: [" + place[0] + "] " + place[1] + " ima grešku u -broju senzora- mjesta.");
                                error = true;
                                Statistics.placesErrors++;
                            }
                        } else {
                            consoleView.printMessage("[+] Error: [" + place[0] + "] " + place[1] + " ima grešku u -tipu- mjesta.");
                            error = true;
                            Statistics.placesErrors++;
                        }
                    } else {
                        consoleView.printMessage("[+] Error: [" + place[0] + "] " + place[1] + " ima grešku u -nazivu- mjesta.");
                        error = true;
                        Statistics.placesErrors++;
                    }
                } else {
                    consoleView.printMessage("[+] Error: [" + place[0] + "] " + place[1] + " ima grešku u -ID-.");
                    error = true;
                    Statistics.placesErrors++;
                }
            } else {
                consoleView.printMessage("[+] Error: [" + place[0] + "] " + place[1] + " ima previše podataka.");
                error = true;
                Statistics.placesErrors++;
            }

            Place placeObj = makePlaceObject(place, error);
            if (!containsPlaceId(placeObj)) {
                SystemDatabase.getInstance().getPlacesList().add(placeObj);
            }
        }
        if (!error) {
            consoleView.printMessage("[+] Info: U datoteci " + fileName + " nema grešaka");
        }
    }

    private static Place makePlaceObject(String[] placeArray, boolean error) {
        Place place = new Place();
        place.setId(placeArray[0]);
        place.setName(placeArray[1]);
        place.setType(placeArray[2]);
        place.setSensorsNumber(placeArray[3]);
        place.setActuatorsNumber(placeArray[4]);
        place.setError(error);
        return place;
    }

    private static boolean containsPlaceId(Place placeObj) {
        boolean idExists = false;
        if (SystemDatabase.getInstance().getPlacesList() != null
                && SystemDatabase.getInstance().getPlacesList().size() > 0) {

            for (Place item : SystemDatabase.getInstance().getPlacesList()) {
                if (item.getId().equals(placeObj.getId())) {
                    idExists = true;
                }
            }
        }
        return idExists;
    }

    private static void validateSensors(List<String[]> sensorsLines, String fileName) {
        boolean error = false;
        for (int i = 0; i < sensorsLines.size(); i++) {
            String[] sensor = sensorsLines.get(i);
            if (sensor.length <= 7) {
                if (RegexHelper.getInstance().checkNumberRegex(sensor[0])) {
                    if (RegexHelper.getInstance().checkNameRegex(sensor[1])) {
                        if (RegexHelper.getInstance().checkTypeRegex(sensor[2])) {
                            if (RegexHelper.getInstance().checkKindRegex(sensor[3])) {
                                if (!RegexHelper.getInstance().checkMinMaxRegex(sensor[4])) {
                                    consoleView.printMessage("[+] Error: [" + sensor[0] + "] " + sensor[1] + " ima grešku u -min vrijednosti- senzora.");
                                    error = true;
                                    Statistics.sensorsErrors++;
                                }
                                if (!RegexHelper.getInstance().checkMinMaxRegex(sensor[5])) {
                                    consoleView.printMessage("[+] Error: [" + sensor[0] + "] " + sensor[1] + " ima grešku u -max vrijednosti- senzora.");
                                    error = true;
                                    Statistics.sensorsErrors++;
                                }
                            } else {
                                consoleView.printMessage("[+] Error: [" + sensor[0] + "] " + sensor[1] + " ima grešku u -vrsti- senzora.");
                                error = true;
                                Statistics.sensorsErrors++;
                            }
                        } else {
                            consoleView.printMessage("[+] Error: [" + sensor[0] + "] " + sensor[1] + " ima grešku u -tipu- senzora.");
                            error = true;
                            Statistics.sensorsErrors++;
                        }
                    } else {
                        consoleView.printMessage("[+] Error: [" + sensor[0] + "] " + sensor[1] + " ima grešku u -nazivu- senzora.");
                        error = true;
                        Statistics.sensorsErrors++;
                    }
                } else {
                    consoleView.printMessage("[+] Error: [" + sensor[0] + "] " + sensor[1] + " ima grešku u -ID-.");
                    error = true;
                    Statistics.sensorsErrors++;
                }
            } else {
                consoleView.printMessage("[+] Error: [" + sensor[0] + "] " + sensor[1] + " ima previše podataka.");
                error = true;
                Statistics.sensorsErrors++;
            }
            Sensor sensorObj = createSensorObject(sensor, error);
            if (!containsSensorId(sensorObj)) {
                SystemDatabase.getInstance().getSensorsList().add(sensorObj);
            }
        }
        if (!error) {
            consoleView.printMessage("[+] Info: U datoteci " + fileName + " nema grešaka");
        }
    }

    private static Sensor createSensorObject(String[] sensorArray, boolean error) {
        Sensor sensor = new Sensor();
        sensor.setId(sensorArray[0]);
        sensor.setName(sensorArray[1]);
        sensor.setType(sensorArray[2]);
        sensor.setKind(sensorArray[3]);
        sensor.setMinValue(sensorArray[4]);
        sensor.setMaxValue(sensorArray[5]);
        if (sensorArray.length > 6) {
            sensor.setComment(sensorArray[6]);
        }
        sensor.setError(error);
        return sensor;
    }

    private static boolean containsSensorId(Sensor sensorObj) {
        boolean idExists = false;
        if (SystemDatabase.getInstance().getSensorsList() != null
                && SystemDatabase.getInstance().getSensorsList().size() > 0) {

            for (Sensor item : SystemDatabase.getInstance().getSensorsList()) {
                if (item.getId().equals(sensorObj.getId())) {
                    idExists = true;
                }
            }
        }
        return idExists;
    }

    private static void validateActuators(List<String[]> actuatorsLines, String fileName) {
        boolean error = false;
        for (int i = 0; i < actuatorsLines.size(); i++) {
            String[] actuator = actuatorsLines.get(i);
            if (actuator.length <= 7) {
                if (RegexHelper.getInstance().checkNumberRegex(actuator[0])) {
                    if (RegexHelper.getInstance().checkNameRegex(actuator[1])) {
                        if (RegexHelper.getInstance().checkTypeRegex(actuator[2])) {
                            if (RegexHelper.getInstance().checkKindRegex(actuator[3])) {
                                if (!RegexHelper.getInstance().checkMinMaxRegex(actuator[4])) {
                                    consoleView.printMessage("[+] Error: [" + actuator[0] + "] " + actuator[1] + " ima grešku u -min vrijednosti- aktuatora.");
                                    error = true;
                                    Statistics.actuatorsErrors++;
                                    
                                }
                                if (!RegexHelper.getInstance().checkMinMaxRegex(actuator[5])) {
                                    consoleView.printMessage("[+] Error: [" + actuator[0] + "] " + actuator[1] + " ima grešku u -max vrijednosti- aktuatora.");
                                    error = true;
                                    Statistics.actuatorsErrors++;
                                }
                            } else {
                                consoleView.printMessage("[+] Error: [" + actuator[0] + "] " + actuator[1] + " ima grešku u -vrsti- aktuatora.");
                                error = true;
                                Statistics.actuatorsErrors++;
                            }
                        } else {
                            consoleView.printMessage("[+] Error: [" + actuator[0] + "] " + actuator[1] + " ima grešku u -tipu- aktuatora.");
                            error = true;
                            Statistics.actuatorsErrors++;
                        }
                    } else {
                        consoleView.printMessage("[+] Error: [" + actuator[0] + "] " + actuator[1] + " ima grešku u -nazivu- aktuatora.");
                        error = true;
                        Statistics.actuatorsErrors++;
                    }
                } else {
                    consoleView.printMessage("[+] Error: [" + actuator[0] + "] " + actuator[1] + " ima grešku u -ID-.");
                    error = true;
                    Statistics.actuatorsErrors++;
                }
            } else {
                consoleView.printMessage("[+] Error: [" + actuator[0] + "] " + actuator[1] + " ima previše podataka.");
                error = true;
                Statistics.actuatorsErrors++;
            }
            Actuator actuatorObj = createActuatorObject(actuator, error);
            if (!containsActuatorId(actuatorObj)) {
                SystemDatabase.getInstance().getActuatorsList().add(actuatorObj);
            }
        }
        if (!error) {
            consoleView.printMessage("[+] Info: U datoteci " + fileName + " nema grešaka");
        }
    }

    private static Actuator createActuatorObject(String[] actuatorArray, boolean error) {
        Actuator actuator = new Actuator();
        actuator.setId(actuatorArray[0]);
        actuator.setName(actuatorArray[1]);
        actuator.setType(actuatorArray[2]);
        actuator.setKind(actuatorArray[3]);
        actuator.setMinValue(actuatorArray[4]);
        actuator.setMaxValue(actuatorArray[5]);
        if (actuatorArray.length > 6) {
            actuator.setComment(actuatorArray[6]);
        }
        actuator.setError(error);
        return actuator;
    }

    private static boolean containsActuatorId(Actuator actuatorObj) {
        if (SystemDatabase.getInstance().getActuatorsList()!= null
                && SystemDatabase.getInstance().getActuatorsList().size() > 0) {

            for (Actuator item : SystemDatabase.getInstance().getActuatorsList()) {
                if (item.getId().equals(actuatorObj.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void validateOrder(List<String[]> orderLines, String fileName) {
        boolean error = false;
        for (int i = 0; i < orderLines.size(); i++) {
            if (i != 0 && i != 1 && i != 2) {
                String[] order = orderLines.get(i);
                if (order[0].equals("0")) {
                    if (order.length <= 5) {
                        if (RegexHelper.getInstance().checkNumberRegex(order[1])) {
                            if (RegexHelper.getInstance().checkOrderType(order[2])) {
                                if (RegexHelper.getInstance().checkNumberRegex(order[3])) {
                                    if (!RegexHelper.getInstance().checkNumberRegex(order[4])) {
                                        consoleView.printMessage("[+] Error: [" + order[1] + "] ima grešku u -ID- uredaja.");
                                        error = true;
                                    }
                                } else {
                                    consoleView.printMessage("[+] Error: [" + order[1] + "] ima grešku u -ID- modela uredaja.");
                                    error = true;
                                }
                            } else {
                                consoleView.printMessage("[+] Error: [" + order[1] + "] ima grešku u -vrsti- zapisa.");
                                error = true;
                            }
                        } else {
                            consoleView.printMessage("[+] Error: [" + order[1] + "] ima grešku u -ID- mjesta.");
                            error = true;
                        }
                        Order orderObj = makeOrderObject(order, error, null);
                        SystemDatabase.getInstance().getOrderList().add(orderObj);

                    } else {
                        consoleView.printMessage("[+] Error: [" + order[1] + "] nije zapis za mjesta.");
                        error = true;
                    }
                } else if (order[0].equals("1")) {
                    String[] sensorIds = null;
                    if (order.length <= 3) {
                        if (RegexHelper.getInstance().checkNumberRegex(order[1])) {
                            sensorIds = order[2].split(",");
                            for (String sensorId : sensorIds) {
                                if (!RegexHelper.getInstance().checkNumberRegex(sensorId)) {
                                    consoleView.printMessage("[+] Error: [" + order[1] + "] ima grešku u -ID- senzora.");
                                    error = true;
                                }
                            }
                        } else {
                            consoleView.printMessage("[+] Error: [" + order[1] + "] ima grešku u -ID- aktuatora.");
                            error = true;
                        }
                    } else {
                        consoleView.printMessage("[+] Error: [" + order[1] + "] aktuator ima premalo podataka.");
                        error = true;
                    }
                    Order orderObj = makeOrderObject(order, error, sensorIds);
                    SystemDatabase.getInstance().getOrderList().add(orderObj);
                } else {
                    consoleView.printMessage("[+] Error: [" + order[1] + "] nije zapis za mjesta ili aktuatore");
                    error = true;
                }
            }
        }
        if (!error) {
            consoleView.printMessage("[+] Info: U datoteci " + fileName + " nema grešaka");
        }
    }

    private static Order makeOrderObject(String[] orderArray, boolean error, String[] sensorIds) {
        Order order = new Order();

        if (sensorIds != null) {
            List<String> sensorIdList = new ArrayList<>();
            order.setType(orderArray[0]);
            order.setActuatorId(orderArray[1]);
            for (String sensorId : sensorIds) {
                sensorIdList.add(sensorId);
            }
            order.setSensorId(sensorIdList);
        } else {
            order.setType(orderArray[0]);
            order.setPlaceId(orderArray[1]);
            order.setKind(orderArray[2]);
            order.setDeviceModelId(orderArray[3]);
            order.setDeviceId(orderArray[4]);
        }
        order.setError(error);
        return order;
    }

    public static ConsoleView getConsoleView() {
        return consoleView;
    }

    public static void setConsoleView(ConsoleView aConsoleView) {
        consoleView = aConsoleView;
    }
    
    
}
