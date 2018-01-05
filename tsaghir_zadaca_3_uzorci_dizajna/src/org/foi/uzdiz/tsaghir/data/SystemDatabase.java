/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.data;

import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.tsaghir.models.Actuator;
import org.foi.uzdiz.tsaghir.models.Order;
import org.foi.uzdiz.tsaghir.models.Place;
import org.foi.uzdiz.tsaghir.models.Devices;
import org.foi.uzdiz.tsaghir.models.Sensor;

/**
 *
 * @author tsaghir
 */
public class SystemDatabase {

    private CommandLineArguments commandLineArguments;
    private List<String[]> places;
    private List<String[]> sensors;
    private List<String[]> actuators;
    private List<String[]> order;
    private List<Place> placesList;
    private List<Sensor> sensorsList;
    private List<Actuator> actuatorsList;
    private List<Order> orderList;
    private List<Devices> devicesList;
    private int newPi = 0;
    private static SystemDatabase instance = null;

    private SystemDatabase() {
        placesList = new ArrayList<>();
        sensorsList = new ArrayList<>();
        actuatorsList = new ArrayList<>();
        orderList = new ArrayList<>();
        devicesList = new ArrayList<>();
    }

    public static SystemDatabase getInstance() {
        if (instance == null) {
            instance = new SystemDatabase();
        }
        return instance;
    }

    public CommandLineArguments getCommandLineArguments() {
        return commandLineArguments;
    }

    public void setCommandLineArguments(CommandLineArguments commandLineArguments) {
        this.commandLineArguments = commandLineArguments;
    }

    public List<String[]> getPlaces() {
        return places;
    }

    public void setPlaces(List<String[]> places) {
        this.places = places;
    }

    public List<String[]> getSensors() {
        return sensors;
    }

    public void setSensors(List<String[]> sensors) {
        this.sensors = sensors;
    }

    public List<String[]> getActuators() {
        return actuators;
    }

    public void setActuators(List<String[]> actuators) {
        this.actuators = actuators;
    }

    public List<String[]> getOrder() {
        return order;
    }

    public void setOrder(List<String[]> order) {
        this.order = order;
    }

    public List<Place> getPlacesList() {
        return placesList;
    }

    public void setPlacesList(List<Place> placesList) {
        this.placesList = placesList;
    }

    public List<Sensor> getSensorsList() {
        return sensorsList;
    }

    public void setSensorsList(List<Sensor> sensorsList) {
        this.sensorsList = sensorsList;
    }

    public List<Actuator> getActuatorsList() {
        return actuatorsList;
    }

    public void setActuatorsList(List<Actuator> actuatorsList) {
        this.actuatorsList = actuatorsList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<Devices> getDevicesList() {
        return devicesList;
    }

    public void setDevicesList(List<Devices> devicesList) {
        this.devicesList = devicesList;
    }
    
    

    public Sensor getSensorById(String id) {
        for (Sensor sensor : sensorsList) {
            if (sensor.getId().equals(id)) {
                return sensor;
            }
        }
        return null;
    }

    public Actuator getActuatorById(String id) {
        for (Actuator actuator : actuatorsList) {
            if (actuator.getId().equals(id)) {
                return actuator;
            }
        }
        return null;
    }
    
    public Place getPlaceById(String id) {
        for (Place place : placesList) {
            if (place.getId().equals(id)) {
                return place;
            }
        }
        return null;
    }

    public int getLastDeviceId() {
        int index = devicesList.size() - 1;
        int id = Integer.parseInt(devicesList.get(index).getDeviceId());
        return id;
    }

    public boolean checkIfActuatorIdExists(String id) {
        for (Actuator actuator : actuatorsList) {
            if (actuator.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfActuatorContainsSensor(String actuatorId, String sensorId) {
        for (Actuator actuator : actuatorsList) {
            if (actuator.getId().equals(actuatorId)) {
                for (Sensor sensor : actuator.getSensorList()) {

                    if (sensor.getId().equals(sensorId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getNewPi() {
        return newPi;
    }

    public void setNewPi(int newPi) {
        this.newPi = newPi;
    }
}
