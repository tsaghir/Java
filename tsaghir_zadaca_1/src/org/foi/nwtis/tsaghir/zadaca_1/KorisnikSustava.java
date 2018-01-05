/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 *
 * @author tsaghis
 */
public class KorisnikSustava {

    public static boolean STAT = false;

    private static final String ADMIN = "-admin";
    private static final String KORISNIK = "-korisnik";
    private static final String PRIKAZ = "-prikaz";
    private static String zahtjev = "";
    private static Matcher matcher = null;

    /**
     * Glavna metoda koja pokreće sustav korisnika
     * @param args argumenti komandne linije
     */
    public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        String p = sb.toString().trim();
        if (RegexHelper.odrediUnosKorisnika(p)) {
            if (p.contains(ADMIN)) {
                postaviAdmina();
            } else if (p.contains(KORISNIK)) {
                postaviKlijenta();
            } else if (p.contains(PRIKAZ)) {
                postaviPrikaz();
            }
        } else {
            System.out.println("ERROR 90; upisana naredba ne odgovara");
        }
    }

    /**
     * Metoda koja postavlja zahtjev admina pomoću klase administrator sustava
     */
    private static void postaviAdmina() {
        matcher = RegexHelper.getMatcher();
        AdministratorSustava admin = new AdministratorSustava(matcher);
        zahtjev = admin.getZahtjev();
        int poc = 0;
        int kraj = matcher.groupCount();
        for (int i = poc; i <= kraj; i++) {
            System.out.println(i + ". " + matcher.group(i));
        }
        if("STAT".equals(admin.getNaredba())){
            STAT=true;
        }
        KorisnikSustava korisnik = new KorisnikSustava();
        korisnik.pokreniKorisnika(admin.getAdresa(), admin.getPort());
    }
    
    /**
     * Metoda koja postavlja zahtjev klijenta pomoću klase klijent sustava
     */
    private static void postaviKlijenta() {
        matcher = RegexHelper.getMatcher();
        KlijentSustava klijent = new KlijentSustava(matcher);
        zahtjev = klijent.getZahtjev();
        int poc = 0;
        int kraj = matcher.groupCount();
        for (int i = poc; i <= kraj; i++) {
            System.out.println(i + ". " + matcher.group(i));
        }
        KorisnikSustava korisnik = new KorisnikSustava();
        korisnik.pokreniKorisnika(klijent.getAdresa(), klijent.getPort());
    }
    
    /**
     * Metoda koja postavlja prikaz evidencije učitane iz datoteke ako postoji
     */
    private static void postaviPrikaz(){
        matcher = RegexHelper.getMatcher();
        PregledSustava pregled = new PregledSustava(matcher);
        pregled.ispisiEvidenciju();
    }

    /**
     * Metoda koja pokreće spajanje na server 
     * @param nazivServera
     * @param port 
     */
    private void pokreniKorisnika(String nazivServera, int port) {
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;

        try {
            socket = new Socket(nazivServera, port);

            is = socket.getInputStream();
            os = socket.getOutputStream();

            //String zahtjev = "USER pero; PASSWD 123456; STOP;";
            //zahtjev = "USER pero; ADD www.google.com;";
            os.write(zahtjev.getBytes());
            os.flush();
            socket.shutdownOutput();

            if (STAT) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] data = new byte[16384];

                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                Evidencija evid = EvidencijaHelper.ucitajEvidencijuIzBinPolja(buffer);
                ispisiEvidenciju(evid);
            }

            if (!STAT) {
                StringBuffer sb = new StringBuffer();
                while (true) {
                    int znak = is.read();
                    if (znak == -1) {
                        break;
                    }
                    sb.append((char) znak);
                }
                System.out.println("Primljeni odgovor: " + sb);
            }
        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Metoda koja ispisuje na ekran klijenta evidenciju 
     * @param evid 
     */
    private void ispisiEvidenciju(Evidencija evid) {
        System.out.println("Broj prekinutih zahtjeva.................." + evid.getBrojPrekinutihZahtjeva());
        System.out.println("Broj uspjesnih zahtjeva..................." + evid.getBrojUspjesnihZahtjeva());
        System.out.println("Ukupno vrijeme radnih dretvi.............." + evid.getUkupnoVrijemeRadaRadnihDretvi());
        System.out.println("Broj ukupnih zahtjeva....................." + evid.getUkupnoZahtjeva());
        System.out.println("Zadnji broj radne dretve.................." + evid.getZadnjiBrojRadneDretve());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Svi zahtjevi za adrese: ");
        if (evid.getZahtjeviZaAdrese().size() > 0) {
            for (AdresaEntitet adresa : evid.getZahtjeviZaAdrese()) {
                System.out.println(adresa.getAdresa() + "\tStatus: " + (adresa.getStatus() == false ? "Neispravan" : "Ispravan"));
            }
        } else {
            System.out.println("NEMA ZAHTJEVA");
        }

    }
}
