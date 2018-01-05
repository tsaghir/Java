/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.models;

/**
 *
 * @author tsaghir
 */
public class Place {
    private String id;
    private String name;
    private String type;
    private String sensorsNumber;
    private String actuatorsNumber;
    private boolean error;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSensorsNumber() {
        return sensorsNumber;
    }

    public void setSensorsNumber(String sensorsNumber) {
        this.sensorsNumber = sensorsNumber;
    }

    public String getActuatorsNumber() {
        return actuatorsNumber;
    }

    public void setActuatorsNumber(String actuatorsNumber) {
        this.actuatorsNumber = actuatorsNumber;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
    
    
}
