package org.foi.uzdiz.tsaghir.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import org.foi.uzdiz.tsaghir.data.SystemDatabase;
import org.foi.uzdiz.tsaghir.helpers.Generator;
import org.foi.uzdiz.tsaghir.models.Actuator;
import org.foi.uzdiz.tsaghir.models.Order;
import org.foi.uzdiz.tsaghir.models.Place;
import org.foi.uzdiz.tsaghir.models.Devices;
import org.foi.uzdiz.tsaghir.models.Sensor;
import org.foi.uzdiz.tsaghir.views.DeviceView;

/**
 *
 * @author tsaghir
 */
public class DeviceController {

    private SystemDatabase model;
    private DeviceView view;
    private List<Place> places;
    private List<Order> orders;

    public DeviceController(SystemDatabase model, DeviceView view) {
        this.model = model;
        this.view = view;
        getDataFromModel();
    }

    private void getDataFromModel() {
        places = model.getPlacesList();
        orders = model.getOrderList();
    }

    public SystemDatabase getModel() {
        return model;
    }

    public void setModel(SystemDatabase model) {
        this.model = model;
    }

    public DeviceView getView() {
        return view;
    }

    public void setView(DeviceView view) {
        this.view = view;
    }

    public void joinPlacesWithDevices() {
        for (Place place : places) {
            for (Order order : orders) {
                switch (order.getType()) {
                    case "0":
                        addDevicesToPlace(order, place);
                        break;
                    case "1":
                        addSensorsToActuator(order);
                }
            }
        }
    }

    private void addDevicesToPlace(Order order, Place place) {
        Devices placeWithDevices = new Devices();
        if (place.getId().equals(order.getPlaceId())
                && place.getType().equals(order.getKind())) {
            if (order.getKind().equals("0")) {
                placeWithDevices.setDeviceId(order.getDeviceId());
                Sensor sensor = model.getSensorById(order.getDeviceModelId());
                if (sensor != null
                        && !sensor.isError()) {
                    int limit = Integer.parseInt(place.getSensorsNumber());
                    if (placeWithDevices.getSensorsList().size() <= limit) {
                        placeWithDevices.getSensorsList().add(sensor);
                        model.getDevicesList().add(placeWithDevices);
                    } else {
                        view.printError("Tom mjestu je dodan maksimalan broj senzora");
                    }
                }

            } else if (order.getKind().equals("1")) {
                placeWithDevices.setDeviceId(order.getDeviceId());
                Actuator actuator = model.getActuatorById(order.getDeviceModelId());
                if (actuator != null
                        && !actuator.isError()) {
                    int limit = Integer.parseInt(place.getActuatorsNumber());
                    if (placeWithDevices.getActuatorsList().size() <= limit) {
                        placeWithDevices.getActuatorsList().add(actuator);
                        model.getDevicesList().add(placeWithDevices);
                    } else {
                        view.printError("Tom mjestu je dodan maksimalan broj aktuatora");
                    }
                } else {
                    view.printError("Takav aktuator ne postoji.");
                }
            }
        }
    }

    private void addSensorsToActuator(Order order) {
        Actuator actuator = model.getActuatorById(order.getActuatorId());
        if (actuator != null
                && actuator.getSensorList().isEmpty()) {
            for (String string : order.getSensorId()) {
                Sensor sensor = model.getSensorById(string);
                if (sensor != null
                        && !sensor.isError()) {
                    actuator.getSensorList().add(sensor);
                }
            }
        } else if (actuator != null
                && actuator.getSensorList().size() > 0) {
            for (String sensorId : order.getSensorId()) {
                Sensor sensor = model.getSensorById(sensorId);
                if (sensor != null
                        && !sensor.isError()) {
                    if(!model.checkIfActuatorContainsSensor(actuator.getId(),sensorId))
                        actuator.getSensorList().add(sensor);
                }
            }
        } else {
            Actuator actuatorObj = new Actuator();
            actuatorObj.setId(order.getActuatorId());
            for (String string : order.getSensorId()) {
                Sensor sensor = model.getSensorById(string);
                if (sensor != null
                        && !sensor.isError()) {
                    actuatorObj.getSensorList().add(sensor);
                }
            }
            if (!model.checkIfActuatorIdExists(actuatorObj.getId())) {
                SystemDatabase.getInstance().getActuatorsList().add(actuatorObj);
            }
        }
    }

    public void initSystem() {
        view.printInitMessage();
        for (Devices device : model.getDevicesList()) {
            for (Actuator actuator : device.getActuatorsList()) {
                if (actuator.isError()) {
                    device.getErrorsLogList().add(view.printActuatorErrorMessage(actuator));
                } else {
                    view.printActuatorFeedBackMessage(device, actuator);
                }
            }

            for (Sensor sensor : device.getSensorsList()) {
                if (sensor.isError()) {
                    device.getErrorsLogList().add(view.printSensorErrorMessage(sensor));
                } else {
                    view.printSensorFeedBackMessage(device, sensor);
                }
            }
        }
    }

    public Devices getRandomPlace() {
        boolean idFound = false;
        Devices placeWithDevices = null;
        while (idFound) {
            int id = Generator.getInstance().dajSlucajniBroj(1000, 1050);
            for (Devices place : model.getDevicesList()) {
                if (Integer.parseInt(place.getDeviceId()) == id) {
                    placeWithDevices = place;
                    idFound = true;
                }
            }
        }
        return placeWithDevices;
    }
}
