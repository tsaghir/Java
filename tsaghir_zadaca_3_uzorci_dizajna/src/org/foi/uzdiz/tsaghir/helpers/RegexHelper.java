/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.uzdiz.tsaghir.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tsaghir
 */
public class RegexHelper {

    private Matcher matcher;

    private final String NUMBER = "^\\d+$";
    private final String PLACES_NAME = "^([a-zA-Z0-9-\\/ \\p{L}]+)";
    private final String PLACES_TYPE = "^[0-1]{0,1}$";
    private final String ORDER_TYPE = "^[0-1]{0,1}$";
    private final String SENSORS_TYPE = "^[0-2]{0,1}$";
    private final String SENSORS_KIND = "^\\d+(\\.\\d{1})$|^\\d+(\\.\\d{5})?$";
    private final String SENSORS_MINMAX = "^-?\\d+(\\.\\d{1})$|^-?\\d+$";

    public final String CYCLE_REGEX = "^C ([0-9]|[1-9][0-9]|100)$|^C$";
    public final String PI_REGEX = "^PI ([0-9]|[1-9][0-9]|100)$|^PI$";
    public final String PLACE_PRINT = "^M [0-9]+$";
    public final String SENSOR_PRINT = "^S [0-9]+$";
    public final String ACTUATOR_PRINT = "^A [0-9]+$";

    private static RegexHelper instance = null;

    private RegexHelper() {
    }

    public static RegexHelper getInstance() {
        if (instance == null) {
            instance = new RegexHelper();
        }
        return instance;
    }

    public boolean checkNumberRegex(String text) {
        return (checkRegex(NUMBER, text));
    }

    public boolean checkNameRegex(String text) {
        return (checkRegex(PLACES_NAME, text));
    }

    public boolean checkPlacesTypeRegex(String text) {
        return (checkRegex(PLACES_TYPE, text));
    }

    public boolean checkTypeRegex(String text) {
        return (checkRegex(SENSORS_TYPE, text));
    }

    public boolean checkKindRegex(String text) {
        return (checkRegex(SENSORS_KIND, text));
    }

    public boolean checkMinMaxRegex(String text) {
        return (checkRegex(SENSORS_MINMAX, text));
    }

    public boolean checkOrderType(String text) {
        return (checkRegex(ORDER_TYPE, text));
    }

    private boolean checkRegex(String syntax, String text) {
        Pattern pattern = Pattern.compile(syntax);
        Matcher m = pattern.matcher(text);
        boolean status = m.matches();
        if (status) {
            setMatcher(m);
        } else {
            setMatcher(null);
        }
        return status;
    }

    public Matcher getMatcher() {
        return matcher;
    }

    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

}
