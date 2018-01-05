/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.io.IOException;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 * Rezervna dretva koja se pali kada nema slobodnih radnih dretvi te javlja korisniku
 * tu informaciju
 * @author tsaghir
 */
class RezervnaDretva extends Thread {

    Konfiguracija konf;
    private static Socket socket;

    RezervnaDretva(Konfiguracija konf) {
        this.konf = konf;
        super.setName("rezervna-dretva");
    }

    @Override
    public void interrupt() {
        super.interrupt();
        EvidencijaHelper.postaviBrojPrekinutihZahtjeva();
    }

    @Override
    public synchronized void run() {
        int trajanjeSpavanja = Integer.parseInt(konf.dajPostavku("intervalNadzorneDretve"));
        while (true) {
            try {
                wait();
                long trenutnoVrijeme = System.currentTimeMillis();
                
                OutputStream os = socket.getOutputStream();
                String odgovor = "ERROR 20; Slobodna radna dretva ne postoji zatvaram konekciju";
                os.write(odgovor.getBytes());
                os.flush();

                socket.close();
                long vrijemeZavrsetka = System.currentTimeMillis();
                long trajanje = vrijemeZavrsetka - trenutnoVrijeme;
                EvidencijaHelper.postaviUkupnoVrijemeRadaRadnihDretvi(trajanje);

                sleep(trajanjeSpavanja - (vrijemeZavrsetka - trenutnoVrijeme));
                notify();
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(RezervnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    public static void postaviSocket(Socket socket) {
        RezervnaDretva.socket = socket;
    }

}
