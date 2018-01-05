package org.foi.nwtis.tsaghir.web.pomagaci;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa za pomoć i usporedbu Regexa
 * @author tsaghir
 */
public class RegexHelper {

    public static final String ADD = "ADD";
    public static final String TEMP = "TEMP";
    public static final String EVENT = "EVENT";
    public static final String FAIL = "FAIL";

    private static final String add = "^ADD IoT (\\d{1,6}) \"(.{1,30})\" GPS: ((?:-?\\d{1,3})\\.(?:\\d{6})),((?:-?\\d{1,3})\\.(?:\\d{6}));$";
    private static final String temp = "^TEMP IoT (\\d{1,6}) T: ((?:\\d{4})\\.(?:0?[1-9]|1[012])\\.(?:0?[1-9]|[12][0-9]|3[01])) ((?:[0-1]?\\d|2[0-3])(?::(?:[0-5]?\\d))?(?::(?:[0-5]?\\d))?) C: (-?\\d{1,2}.\\d);$";
    private static final String event = "^EVENT IoT (\\d{1,6}) T: ((?:\\d{4})\\.(?:0?[1-9]|1[012])\\.(?:0?[1-9]|[12][0-9]|3[01])) ((?:[0-1]?\\d|2[0-3])(?::(?:[0-5]?\\d))?(?::(?:[0-5]?\\d))?) F: (\\d{1,2});$";

    private static Pattern pattern = null;
    private static Matcher matcher = null;

    private static RegexHelper instance = null;

    private RegexHelper() {
    }

    public static RegexHelper getInstance() {
        if (instance == null) {
            instance = new RegexHelper();
        }
        return instance;
    }

    /**
     * Metoda koja određuje je li naredba ispravna
     * @param poruka
     * @return 
     */
    public static String odrediNaredbu(String poruka) {
        if (provjeraRegexa(add, poruka)) {
            return ADD;
        } else if (provjeraRegexa(temp, poruka)) {
            return TEMP;
        } else if (provjeraRegexa(event, poruka)) {
            return EVENT;
        } else {
            return FAIL;
        }
    }

    /**
     * Provjera regexa preko matchera te vraća rezultat provjere
     *
     * @param sintaksa
     * @param naredba
     * @return
     */
    private static boolean provjeraRegexa(String sintaksa, String naredba) {
        pattern = Pattern.compile(sintaksa);
        matcher = pattern.matcher(naredba);
        boolean status = matcher.matches();
        return status;
    }

    /**
     * getter metoda
     * @return 
     */
    public static Matcher getMatcher() {
        return matcher;
    }
    
    

}
