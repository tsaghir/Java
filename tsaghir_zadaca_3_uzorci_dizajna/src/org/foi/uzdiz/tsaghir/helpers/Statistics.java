/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.helpers;

import org.foi.uzdiz.tsaghir.data.SystemDatabase;

/**
 *
 * @author tsaghir
 */
public class Statistics {
    public static int printPlacesCount;
    public static int printSensorsCount;
    public static int printActuatorCount;
    public static int saveDataCount;
    public static int retrieveDataCount;
    public static long threadWorkingTimeSum;
    public static int myFunctionCall;
    public static int piCall;
    public static int placesErrors;
    public static int sensorsErrors;
    public static int actuatorsErrors;
    public static int placesCount = SystemDatabase.getInstance().getPlacesList().size();
    public static int sensorsCount = SystemDatabase.getInstance().getSensorsList().size();
    public static int actuatorsCount = SystemDatabase.getInstance().getActuatorsList().size();
}
