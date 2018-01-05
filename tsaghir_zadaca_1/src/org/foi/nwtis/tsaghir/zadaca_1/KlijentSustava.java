/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.util.regex.Matcher;

/**
 * Klasa koja provjerava unos naredbi klijenta te ih pretvara u zahtjeve
 * @author tsaghis
 */
public class KlijentSustava {
    private String adresa;
    private int port;
    private String korisnik;
    private String opcijaAddIliTest;
    private String naredbaAddIliTest;
    private String opcijaWait;
    private String naredbaWait;
    private String zahtjev;

    public KlijentSustava(Matcher matcher) {
        adresa = matcher.group(1);
        port = Integer.parseInt(matcher.group(4));
        korisnik = matcher.group(5);
        opcijaAddIliTest = matcher.group(8);
        naredbaAddIliTest = matcher.group(9);
        opcijaWait = matcher.group(10);
        naredbaWait = matcher.group(11);
    }

    public String getAdresa() {
        return adresa;
    }

    public int getPort() {
        return port;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public String getNaredba() {
        return opcijaAddIliTest;
    }

    public String getOpcijaAddIliTest() {
        return opcijaAddIliTest;
    }

    public String getNaredbaAddIliTest() {
        return naredbaAddIliTest;
    }

    public String getOpcijaWait() {
        return opcijaWait;
    }

    public String getNaredbaWait() {
        return naredbaWait;
    }

    public String getZahtjev() {
        zahtjev = "USER "+korisnik+"; "+odrediNaredbu();
        return zahtjev;
    }
    
    private String odrediNaredbu(){
        String opcija = "";
        if(opcijaAddIliTest != null){
            if(opcijaAddIliTest.contains("-a")){
                opcija = "ADD "+naredbaAddIliTest+";";
            }
            else{
                opcija = "TEST "+naredbaAddIliTest+";";
            }
        }else if(opcijaWait != null
                && naredbaWait != null){
            opcija = "WAIT "+naredbaWait+";";
        }
        return opcija;
    }
}
