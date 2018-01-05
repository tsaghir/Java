/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tsaghir
 */
public class Devices {

    private String deviceId;
    private List<Actuator> actuatorsList;
    private List<Sensor> sensorsList;
    private List<String> errorsLogList;
    private boolean error = false;

    public Devices() {
     actuatorsList = new ArrayList<>();
     sensorsList = new ArrayList<>();
     errorsLogList = new ArrayList<>();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<Actuator> getActuatorsList() {
        return actuatorsList;
    }

    public void setActuatorsList(List<Actuator> actuatorsList) {
        this.actuatorsList = actuatorsList;
    }

    public List<Sensor> getSensorsList() {
        return sensorsList;
    }

    public void setSensorsList(List<Sensor> sensorsList) {
        this.sensorsList = sensorsList;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<String> getErrorsLogList() {
        return errorsLogList;
    }

    public void setErrorsLogList(List<String> errorsLogList) {
        this.errorsLogList = errorsLogList;
    }
    
    
}
