/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.views;

import org.foi.uzdiz.tsaghir.models.Actuator;
import org.foi.uzdiz.tsaghir.models.Devices;
import org.foi.uzdiz.tsaghir.models.Sensor;

/**
 *
 * @author tsaghir
 */
public class DeviceView {
    public void printInfo(String text){
        System.out.println("[+] Info: " + text);
    }
    
    public void printError(String text){
        System.out.println("[+] Error: " + text);
    }
    
    public void printInitMessage(){
        System.out.println("[+] Inicijalizacija: Šaljem inicijalizacijske poruke uređajima na postavljenim mjestima");
        printSeparator();
    }
    
    public String printActuatorErrorMessage(Actuator actuator){
        String message = "[+] Error[0]: Greška Aktuatora ["+ actuator.getId() +"]\n"
                + "\t Zapis: " + actuator.getName() + ", " + actuator.getType() + ", " + actuator.getKind() + ", " + actuator.getMinValue()+ ", " 
                + actuator.getMaxValue() + ", " + actuator.getComment();
        System.out.println(message);
        return message;
    }
    
    public String printSensorErrorMessage(Sensor sensor){
        String message = "[+] Error[0]: Greška Senzora ["+ sensor.getId() +"]\n"
                + "\t Zapis: " + sensor.getName() + ", " + sensor.getType() + ", " + sensor.getKind() + ", " + sensor.getMinValue()+ ", " 
                + sensor.getMaxValue() + ", " + sensor.getComment();
        System.out.println(message);
        return message;
    }
    
    public void printActuatorFeedBackMessage(Devices device,Actuator actuator){
        System.out.println("Uređaj: ["+ device.getDeviceId() + "]");
        System.out.println("[+] Info[1]: Aktuator ["+ actuator.getId() +"]: " + actuator.getName() + ", je u redu.");
    }
    
    public void printSensorFeedBackMessage(Devices device, Sensor sensor){
        System.out.println("Uređaj: ["+ device.getDeviceId() + "]");
        System.out.println("[+] Info[1]: Senzor ["+ sensor.getId() +"]: " + sensor.getName() + ", je u redu.");
    }
    
    public void printSeparator() {
        System.out.println("--------------------------------------------------------------------------------------------------------------");
    }
}
