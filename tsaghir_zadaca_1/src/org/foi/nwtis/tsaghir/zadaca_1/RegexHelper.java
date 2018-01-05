/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Singleton klasa koja služi za baratanje regexima
 * @author tsaghir
 */
public class RegexHelper {
    
    public static final String ADMIN_SINTAKSA = "^-admin -server (([A-Za-z]+)|([0-9_.]{15})) -port ([0-9]{4}) -u ([A-Za-z0-9_-]+) -p ([A-Za-z0-9_#!-]+) (-pause|-start|-stop|-stat)$";
    public static final String KORISNIK_SINTAKSA = "^-korisnik -s (([A-Za-z]+)|([0-9_.]{15})) -port ([0-9]{4}) -u ([A-Za-z0-9_#!-]+) (((-a|-t) ([a-z0-9_.:\\/-]+))|(-w) ([0-9]{1,3}))$";
    public static final String PRIKAZ_SINTAKSA = "^-prikaz -s ([a-zA-Z0-9_.:\\\\-]+)$";

    private static final String naredbaAdmin = "^USER ([A-Za-z0-9_!#-]+); PASSWD ([A-Za-z0-9_!#-]+); (PAUSE|STOP|START|STAT);$";
    private static final String naredbaKorisnik_1 = "^USER ([^\\s]+); ADD ([^\\s]+);$";
    private static final String naredbaKorisnik_2 = "^USER ([^\\s]+); TEST ([^\\s]+);$";
    private static final String naredbaKorisnik_3 = "^USER ([^\\s]+); WAIT ([^\\s]+);$";

    private static Pattern pattern = null;
    private static Matcher m = null;

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
     * Određuje da li naredba dolazi od strane admina
     * @param naredba
     * @return 
     */
    public static synchronized boolean odrediAdmina(String naredba) {
        return (provjeraRegexa(naredbaAdmin, naredba));
            
    }
    
    /**
     * Određuje da li naredba dolazi od strane korisnika
     * @param naredba
     * @return 
     */
    public static synchronized boolean odrediKorisnika(String naredba){
        if (provjeraRegexa(naredbaKorisnik_1, naredba)) {
            return true;
        } else if (provjeraRegexa(naredbaKorisnik_2, naredba)) {
            return true;
        } else if (provjeraRegexa(naredbaKorisnik_3, naredba)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Provjera regexa preko matchera te vraća rezultat provjere
     * @param sintaksa
     * @param naredba
     * @return 
     */
    private static synchronized boolean provjeraRegexa(String sintaksa, String naredba) {
        pattern = Pattern.compile(sintaksa);
        m = pattern.matcher(naredba);
        boolean status = m.matches();
        return status;
    }
    
    /**
     * Određuje unos korisnika da li paše sa zadanim regexom
     * @param unos
     * @return 
     */
    public static synchronized boolean odrediUnosKorisnika(String unos){
        if(provjeraRegexa(ADMIN_SINTAKSA, unos)){
            return true;
        }else if(provjeraRegexa(KORISNIK_SINTAKSA, unos)){
            return true;
        }else if(provjeraRegexa(PRIKAZ_SINTAKSA, unos)){
            return true;
        }
        return false;
    }
    
    /**
     * Određuje naredbu korisnika da li je ADD | TEST |WAIT
     * @param naredba
     * @return 
     */
    public static synchronized String odrediNaredbuKorisnika(String naredba) {
        String odabranaNaredba = null;
        if (naredba.contains("ADD")) {
            odabranaNaredba = "ADD";
        } else if (naredba.contains("TEST")) {
            odabranaNaredba = "TEST";
        } else if (naredba.contains("WAIT")) {
            odabranaNaredba = "WAIT";
        }
        return odabranaNaredba;

    }
    
    /**
     * Vraća matcher
     * @return 
     */
     public static synchronized Matcher getMatcher() {
        return m;
    }
}
