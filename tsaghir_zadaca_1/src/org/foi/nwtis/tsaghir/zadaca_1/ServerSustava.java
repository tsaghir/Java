/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.tsaghir.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.tsaghir.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author tsaghir
 */
public class ServerSustava {
    public static short redniBrojDretve = 0;
    /**
     * Glavna metoda koja se prva poziva prilikom pokretanja programa
     *
     * @param args argumenti komandne linije
     */
    public static void main(String[] args) {

        String sintaksa = "^-konf ([^\\s]+\\.(?i))(txt|xml|bin)( +-load)?$";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        String p = sb.toString().trim();
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();

        if (status) {
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                System.out.println(i + ". " + m.group(i));
            }

            String nazivDatoteke = m.group(1) + m.group(2);
            boolean trebaUcitatiEvidenciju = false;
            if (m.group(3) != null) {
                trebaUcitatiEvidenciju = true;
            }

            ServerSustava server = new ServerSustava();
            try {
                server.pokreniServer(nazivDatoteke, trebaUcitatiEvidenciju);
            } catch (IOException ex) {
                Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("ERROR 90; krivi naziv datoteke");
        }
    }

    /**
     * Metoda koja pokrece server
     *
     * @param nazivDatoteke koji je korisnik unio pod args[]
     * @param trebaUcitatiEvidenciju opcija zeli li korisnik ucitati evidenciju
     * @throws IOException
     */
    private void pokreniServer(String nazivDatoteke, boolean trebaUcitatiEvidenciju) throws IOException {
        try {

            Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);

            int port = Integer.parseInt(konf.dajPostavku("port"));
            int maksBrojRadnihDretvi = Integer.parseInt(konf.dajPostavku("maksBrojRadnihDretvi"));
            
            List<RadnaDretva> listaRadnihDretvi = new ArrayList<>();
            EvidencijaHelper.getInstance();
            RegexHelper.getInstance();
            DretvaHelper.getInstance();

            if (trebaUcitatiEvidenciju) {
                Evidencija evid = EvidencijaHelper.ucitajEvidencijuIzBinarneDat();
                if (evid != null) {
                    EvidencijaHelper.ucitajPodatkeIzBinarneDat(evid);
                } else {
                    System.out.println("Datoteka evidencije ne postoji");
                }

            }
            pokreniDretve(konf);
            DretvaHelper.setKonf(konf);

            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket socket = ss.accept();
                if (listaRadnihDretvi.size() >= maksBrojRadnihDretvi) {
                    RezervnaDretva.postaviSocket(socket);
                    RezervnaDretva rezervnaDretva = (RezervnaDretva) DretvaHelper.pronadiDretvuPremaImenu("rezervna-dretva");
                    synchronized (rezervnaDretva) {
                        rezervnaDretva.notify();
                    }
                    redniBrojDretve = 0;
                }else{
                    redniBrojDretve++;
                    RadnaDretva radnaDretva = new RadnaDretva(socket, listaRadnihDretvi);
                    listaRadnihDretvi.add(radnaDretva);
                    radnaDretva.start();
                }

                EvidencijaHelper.postaviUkupnoZahtjeva();
            }
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Privatna metoda koja pokrece zadane dretve
     *
     * @param konf iz ucitane datoteke
     */
    private void pokreniDretve(Konfiguracija konf) {
        NadzorDretvi nd = new NadzorDretvi(konf);
        nd.start();
        ProvjeraAdresa pa = new ProvjeraAdresa(konf);
        pa.start();
        RezervnaDretva rd = new RezervnaDretva(konf);
        rd.start();

        SerijalizatorEvidencije se = new SerijalizatorEvidencije(konf);
        se.start();
    }
}
