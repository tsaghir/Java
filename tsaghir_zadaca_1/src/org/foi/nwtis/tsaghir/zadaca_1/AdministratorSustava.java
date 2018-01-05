/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.util.regex.Matcher;
/**
 * Klasa za provjeru upisanih naredbi koje šalje administrator
 * @author tsaghir
 */
public class AdministratorSustava {
    private final String adresa;
    private final int port;
    private final String korisnik;
    private final String lozinka;
    private final String naredba;
    private String zahtjev;
    
    public AdministratorSustava(Matcher matcher){
        adresa = matcher.group(1);
        port =  Integer.parseInt(matcher.group(4));
        korisnik = matcher.group(5);
        lozinka = matcher.group(6);
        naredba = matcher.group(7);
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

    public String getLozinka() {
        return lozinka;
    }

    public String getNaredba() {
        return naredba.substring(1).toUpperCase();
    }

    public String getZahtjev() {
        zahtjev = "USER "+korisnik+"; PASSWD "+lozinka+"; "+getNaredba()+";";
        return zahtjev;
    }
    
    
    
}
