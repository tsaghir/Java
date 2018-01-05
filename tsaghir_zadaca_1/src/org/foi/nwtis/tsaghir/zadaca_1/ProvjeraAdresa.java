/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 * Dretva koja služi za provjeru adresa koje je korisnik unio
 * @author tsaghir
 */
class ProvjeraAdresa extends Thread {

    private final String PROVJERA_ADRESA = "provjera-adresa";
    private final String INTERVAL_ADRESNE_DRETVE = "intervalAdresneDretve";
    Konfiguracija konf;

    ProvjeraAdresa(Konfiguracija konf) {
        super.setName(PROVJERA_ADRESA);
        this.konf = konf;
    }

    @Override
    public void interrupt() {
        super.interrupt(); 
    }

    @Override
    public void run() {

        int trajanjeSpavanja = Integer.parseInt(konf.dajPostavku(INTERVAL_ADRESNE_DRETVE));
        while (true) {
            long trenutnoVrijeme = System.currentTimeMillis();
            if (EvidencijaHelper.dohvatiZahtjeviZaAdrese() != null) {
                for (AdresaEntitet adresa : EvidencijaHelper.dohvatiZahtjeviZaAdrese()) {
                    try {
                        URL url = new URL(adresa.getAdresa());
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.connect();
                    } catch (MalformedURLException ex) {
                        adresa.setStatus(false);
                    } catch (IOException ex) {
                        adresa.setStatus(false);
                    }
                    adresa.setStatus(true);
                }
            }
            long vrijemeZavrsetka = System.currentTimeMillis() - trenutnoVrijeme;
            EvidencijaHelper.postaviUkupnoVrijemeRadaRadnihDretvi(vrijemeZavrsetka);
            try {
                sleep(trajanjeSpavanja - (vrijemeZavrsetka - trenutnoVrijeme));
            } catch (InterruptedException ex) {
                Logger.getLogger(ProvjeraAdresa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start(); 
    }

}
