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
public class Order {

    private String type;
    private String placeId;
    private String actuatorId;
    private List<String> sensorId;
    private String kind;
    private String deviceModelId;
    private String deviceId;
    private boolean error;

    public Order() {
        sensorId = new ArrayList<>();
    }
    
    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getDeviceModelId() {
        return deviceModelId;
    }

    public void setDeviceModelId(String deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getActuatorId() {
        return actuatorId;
    }

    public void setActuatorId(String actuatorId) {
        this.actuatorId = actuatorId;
    }

    public List<String> getSensorId() {
        return sensorId;
    }

    public void setSensorId(List<String> sensorId) {
        this.sensorId = sensorId;
    }

}
