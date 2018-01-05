/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.uzdiz.tsaghir.controllers.ConsoleController;
import org.foi.uzdiz.tsaghir.controllers.DeviceController;
import org.foi.uzdiz.tsaghir.data.SystemDatabase;
import org.foi.uzdiz.tsaghir.helpers.Generator;
import org.foi.uzdiz.tsaghir.views.ConsoleView;
import org.foi.uzdiz.tsaghir.views.DeviceView;

/**
 *
 * @author tsaghir
 */
public class Program {

    private static SystemDatabase database;

    /**
     * @param args the command line argumentsConsol
     */
    public static void main(String[] args) {
        database = SystemDatabase.getInstance();
        
        ConsoleView consoleView = new ConsoleView();
        ConsoleController consoleController = new ConsoleController(database, consoleView);
        
        
        if (!consoleController.sortCommandLineArguments(args)) {
            consoleController.loadFiles();
            Generator.getInstance().initGenerator();

            DeviceView deviceOrderView = new DeviceView();
            DeviceController deviceOrderController = new DeviceController(database, deviceOrderView);
            deviceOrderController.joinPlacesWithDevices();
            deviceOrderController.initSystem();

            consoleController.printSeparator();
            try {
                consoleController.doCommandInput();
            } catch (IOException ex) {
                Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
