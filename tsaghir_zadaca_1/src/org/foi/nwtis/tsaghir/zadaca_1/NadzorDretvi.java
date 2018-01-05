/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 * Dretva koja služi za nadzor ostalih dretve te provjerava njihovo trajanje
 * Ako traju predugo instantno se gase
 * @author tsaghir
 */
class NadzorDretvi extends Thread {

    private final String RADNA_DRETVA = "tsaghir";
    private final String INTERVAL_NADZORNE_DRETVE = "intervalNadzorneDretve";
    private final String MAKS_VRIJEME_RADNE_DRETVE = "maksVrijemeRadneDretve";
    private final String NADZOR_DRETVI = "nadzor-dretvi";
    Konfiguracija konf;

    NadzorDretvi(Konfiguracija konf) {
        this.konf = konf;
        super.setName(NADZOR_DRETVI);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        EvidencijaHelper.postaviBrojPrekinutihZahtjeva();
    }

    @Override
    public void run() {

        int trajanjeSpavanja = Integer.parseInt(konf.dajPostavku(INTERVAL_NADZORNE_DRETVE));
        long dopustenoRadnoVrijeme = Integer.parseInt(konf.dajPostavku(MAKS_VRIJEME_RADNE_DRETVE));
        while (true) {

            long trenutnoVrijeme = System.currentTimeMillis();

            for (Thread t : Thread.getAllStackTraces().keySet()) {
                if (t.getName().contains(RADNA_DRETVA)) {
                    RadnaDretva trenutnaDretva = (RadnaDretva) t;
                    if (t.getState() != Thread.State.WAITING) {
                        long radnoVrijeme = System.currentTimeMillis() - trenutnaDretva.vrijemePocetka;
                        if (radnoVrijeme > dopustenoRadnoVrijeme) {
                            trenutnaDretva.interrupt();
                        }
                    }
                }
            }
            long vrijemeZavrsetka = System.currentTimeMillis();
            try {
                sleep(trajanjeSpavanja - (vrijemeZavrsetka - trenutnoVrijeme));
            } catch (InterruptedException ex) {
                Logger.getLogger(ProvjeraAdresa.class.getName()).log(Level.SEVERE, "Greska kod spavanja nadzorne dretve", ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

}
