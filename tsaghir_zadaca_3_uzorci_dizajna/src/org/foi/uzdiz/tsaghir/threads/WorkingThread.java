/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.threads;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.tsaghir.data.SystemDatabase;
import org.foi.uzdiz.tsaghir.helpers.Generator;
import org.foi.uzdiz.tsaghir.helpers.Statistics;
import org.foi.uzdiz.tsaghir.models.Actuator;
import org.foi.uzdiz.tsaghir.models.Devices;
import org.foi.uzdiz.tsaghir.models.Sensor;
import org.foi.uzdiz.tsaghir.views.ConsoleView;

/**
 *
 * @author tsaghir
 */
public class WorkingThread extends Thread {

    private final int cycleNumber;
    private ConsoleView view;
    private int errorCount;
    private int actuatorOperationRandom;
    private int actuatorMin;
    private int actuatorMax;
    private int start;

    public WorkingThread(int cycleNumber, ConsoleView view) {
        this.cycleNumber = cycleNumber;
        this.view = view;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void run() {
        long timeStart = System.currentTimeMillis();
        start = 1;
        actuatorOperationRandom = Generator.getInstance().dajSlucajniBroj(actuatorMin, actuatorMax);
        while (start <= cycleNumber) {
            List<Sensor> sensorList = new ArrayList<>();
            view.printMessage("[+] Info: Izvršavam " + start + ". ciklus");
            for (Devices device : SystemDatabase.getInstance().getDevicesList()) {
                checkForErrors(device);
                if(!device.getSensorsList().isEmpty()){
                    for (Sensor sensor : device.getSensorsList()) {
                        sensorList.add(sensor);
                    }
                }
                if(!device.getActuatorsList().isEmpty()){
                    for (Actuator actuator : device.getActuatorsList()) {
                        doActuator(actuator);
                    }
                }
            }
            printSensorValues(sensorList);
            start++;
            Statistics.threadWorkingTimeSum = System.currentTimeMillis() - timeStart;
            try {
                sleep(Integer.parseInt(SystemDatabase.getInstance().getCommandLineArguments().getThreadCycleDuration()));
            } catch (InterruptedException ex) {
                Logger.getLogger(WorkingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void checkForErrors(Devices device) {
        if (errorCount >= 3) {
            view.printMessage("[+] Error: Uređaj [" + device.getDeviceId() + "] u 3 ciklusa javlja grešku. Označujem ga pogrešnim.");
            device.setError(true);
            Devices newDevice = makeNewDeviceObject(device);
            SystemDatabase.getInstance().getDevicesList().add(newDevice);
        } else {
            for (Actuator actuator : device.getActuatorsList()) {
                if (actuator.isError()) {
                    errorCount++;
                    view.printMessage("[+] Error: Aktuator [" + actuator.getId() + "] javlja grešku.");
                }
            }
            for (Sensor sensor : device.getSensorsList()) {
                if (sensor.isError()) {
                    errorCount++;
                    view.printMessage("[+] Error: Senzor [" + sensor.getId() + "] javlja grešku.");
                }
            }
        }
    }

    private Devices makeNewDeviceObject(Devices oldDevice) {
        Devices device = new Devices();
        String newDeviceId = String.valueOf(SystemDatabase.getInstance().getLastDeviceId() + 1);
        view.printMessage("[+] Info: Iz starog uređaja [" + oldDevice.getDeviceId() + "] radim novi uređaj [" + newDeviceId + "]");
        device.setDeviceId(newDeviceId);
        device.setActuatorsList(oldDevice.getActuatorsList());
        device.setSensorsList(oldDevice.getSensorsList());
        return device;
    }
    
    private void printSensorValues(List<Sensor> sensor){
        view.printMessage("[+] Info: Ispisujem podatke senzora:");
        view.printSensorValues(sensor);
    }
    
    private void doActuator(Actuator actuator){
        String[] binary1 = actuator.getName().split("/");
        if(binary1.length > 1){
            String[] binary2 = binary1[1].split(" ");
            view.printMessage("[+] Aktuator ["+ actuator.getId()+"] vrši " + (start % 2 == 0 ? binary1[0] + " " + binary2[1] : binary1[1]));
        }else{
            actuatorMin = Integer.parseInt(actuator.getMinValue());
            actuatorMax = Integer.parseInt(actuator.getMaxValue());
            
            if(actuatorOperationRandom >= actuatorMax){
                view.printMessage("[+] Aktuator ["+ actuator.getId()+"] smanjuje operaciju za " + --actuatorOperationRandom);
            }else{
                view.printMessage("[+] Aktuator ["+ actuator.getId()+"] povećava operaciju za " + ++actuatorOperationRandom);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }
}
