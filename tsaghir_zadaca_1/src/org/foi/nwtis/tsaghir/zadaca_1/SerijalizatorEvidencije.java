/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 * Dretva koja služi za serijalizaciju evidencije
 * @author tsaghis
 */
class SerijalizatorEvidencije extends Thread {

    Konfiguracija konf;

    SerijalizatorEvidencije(Konfiguracija konf) {
        this.konf = konf;
        super.setName("serijalizator-evidencije");
    }

    @Override
    public void interrupt() {
        super.interrupt();
        EvidencijaHelper.postaviBrojPrekinutihZahtjeva();
    }

    @Override
    public synchronized void run() {
        int trajanjeSpavanja = Integer.parseInt(konf.dajPostavku("intervalNadzorneDretve"));
        int maksBrojZahtjevaZaSer = Integer.parseInt(konf.dajPostavku("brojZahtjevaZaSerijalizaciju"));
        String naziv = konf.dajPostavku("evidDatoteka");
        int zahtjeviZaSer = EvidencijaHelper.getBrojZahtjevaZaser();
        super.run();
        while (true) {
            try {
                wait();
                if (zahtjeviZaSer < maksBrojZahtjevaZaSer) {
                    long trenutnoVrijeme = System.currentTimeMillis();
                    long vrijemeZavrsetka = 0;
                    if (EvidencijaHelper.serijalizirajEvidencijuBinarnuDat(naziv)) {
                        vrijemeZavrsetka = System.currentTimeMillis();
                        long trajanje = vrijemeZavrsetka - trenutnoVrijeme;
                        EvidencijaHelper.postaviUkupnoVrijemeRadaRadnihDretvi(trajanje);
                    }

                    sleep(trajanjeSpavanja - (vrijemeZavrsetka - trenutnoVrijeme));
                }
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(SerijalizatorEvidencije.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    synchronized void obavijesti() {
        notify();
    }

    @Override
    public synchronized void start() {
        super.start();
    }

}
