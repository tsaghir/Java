/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 * Dretva koja služi za izvršavanje pristiglih zahtjeva korisnika i administratora
 * @author tsaghir
 */
class RadnaDretva extends Thread {

    final List<RadnaDretva> listaAktivnihRadnihDretvi;
    Socket socket;
    Matcher matcher;
    String zahtjev = "";
    String korisnik = "";
    String lozinka = "";
    String naredbaKorisnik = "";
    String pristiglaNaredbaAdmin = "";
    String pristiglaNaredbaKorisnik = "";
    String odgovor = "";
    long vrijemePocetka = 0;
    byte[] serijaliziranaEvidencija = null;

    RadnaDretva(Socket socket, List<RadnaDretva> listaRadnihDretvi) {
        this.socket = socket;
        this.listaAktivnihRadnihDretvi = listaRadnihDretvi;
        super.setName("tsaghir-" + Short.toUnsignedInt(ServerSustava.redniBrojDretve));
    }

    @Override
    public void interrupt() {
        super.interrupt();
        SerijalizatorEvidencije se = (SerijalizatorEvidencije) DretvaHelper.pronadiDretvuPremaImenu("serijalizator-evidencije");
        se.obavijesti();
        System.out.println("Prekidam '" + this.getName() + "' zbog predugog radnog trajanja");
        EvidencijaHelper.postaviBrojPrekinutihZahtjeva();
    }

    @Override
    public synchronized void run() {
        super.run();
        System.out.println(this.getName() + " se pokrece...");

        InputStream is = null;
        OutputStream os = null;
        long vrijemeZavrsetka = 0;

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();

            StringBuffer naredba = new StringBuffer();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                naredba.append((char) znak);
            }
            System.out.println("Primljena naredba: " + naredba);

            zahtjev = naredba.toString();
            vrijemePocetka = System.currentTimeMillis();
            if (RegexHelper.odrediAdmina(zahtjev)) {
                matcher = RegexHelper.getMatcher();
                odrediNaredbu(matcher, true);

                switch (pristiglaNaredbaAdmin) {
                    case "PAUSE":
                        izvrsiAdminNaredbuPause();
                        break;
                    case "START":
                        izvrsiAdminNaredbuStart();
                        break;
                    case "STOP":
                        izvrsiAdminNaredbuStop();
                        break;
                    case "STAT":
                        izvrsiAdminNaredbuStat();
                        break;
                    default:
                        break;
                }
            } else if (RegexHelper.odrediKorisnika(zahtjev)) {
                matcher = RegexHelper.getMatcher();
                odrediNaredbu(matcher, false);
                switch (pristiglaNaredbaKorisnik) {
                    case "ADD":
                        izvrsiKorisnikNaredbuAdd();
                        break;
                    case "TEST":
                        izvrsiKorisnikNaredbuTest();
                        break;
                    case "WAIT":
                        izvrsiKorisnikNaredbuWait();
                        break;
                    default:
                        break;
                }
            } else {
                odgovor = "ERROR 00; korisnik nije administrator ili lozinka ne odgovara";
                EvidencijaHelper.postaviBrojPrekinutihZahtjeva();
            }

            if (EvidencijaHelper.isPosaljiSerijalizaciju()) {
                os.write(serijaliziranaEvidencija);
                os.flush();
                KorisnikSustava.STAT = false;
                EvidencijaHelper.posaljiSerijalizaciju(false);
            }
            os.write(odgovor.getBytes());
            os.flush();

            ukloniRadnuDretvu();

        } catch (IOException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, "Problem kod izvršavanja radne dretve", ex);

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }

                this.socket.close();
            } catch (IOException ex) {
                Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, "Problem kod zatvaranja i/o stream-a", ex);
            }
        }
        vrijemeZavrsetka = vrijemePocetka - System.currentTimeMillis();
        EvidencijaHelper.postaviUkupnoVrijemeRadaRadnihDretvi(vrijemeZavrsetka);
        System.out.println("Završavanje " + this.getName() + " ...");
    }

    /**
     * Određuje naredbu da li je korisnik ili admin kako bi lakše odredili tok programa
     * @param matcher
     * @param admin 
     */
    private void odrediNaredbu(Matcher matcher, boolean admin) {
        if (admin) {
            korisnik = matcher.group(1);
            lozinka = matcher.group(2);
            pristiglaNaredbaAdmin = matcher.group(3);
        } else {
            korisnik = matcher.group(1);
            naredbaKorisnik = matcher.group(2);
            pristiglaNaredbaKorisnik = RegexHelper.odrediNaredbuKorisnika(zahtjev);
        }

    }

    /**
     * Izvršavanje naredbe PAUSE od strane admina
     * pauzira server te prima samo naredbe administratora
     */
    private void izvrsiAdminNaredbuPause() {
        try {
            if (DretvaHelper.provjeriKorisnika(korisnik, lozinka)) {
                DretvaHelper.setPauza(true);
                odgovor = "OK; ";
                EvidencijaHelper.postaviBrojUspjesnihZahtjeva();
            } else {
                odgovor = "ERROR 00; korisnik ne postoji";
                EvidencijaHelper.postaviBrojPrekinutihZahtjeva();
            }

        } catch (IOException ex) {
            odgovor = "ERROR 00; greška prilikom čitanja datoteke";
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, "Greška prilikom izvršavanja admin naredbe PAUSE", ex);
        }
    }

    /**
     * Izvršavanje naredbe START od strane admina
     * te vraća stanje servera iz pauziranog u normalno
     */
    private void izvrsiAdminNaredbuStart() {

        try {
            if (DretvaHelper.provjeriKorisnika(korisnik, lozinka)
                    && DretvaHelper.isPauza()) {
                DretvaHelper.setPauza(false);
                odgovor = "OK; ";
                EvidencijaHelper.postaviBrojUspjesnihZahtjeva();
            } else if (!DretvaHelper.isPauza()) {
                odgovor = "ERROR 02; server nije u stanju pauze";
            } else {
                odgovor = "ERROR 00; korisnik ne postoji";
            }
        } catch (IOException ex) {
            odgovor = "ERROR 00; greška prilikom čitanja datoteke";
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Izvršavanje naredbe STOP od strane admina
     * te zaustavlja sve dretve i radi serijalizaciju
     */
    private void izvrsiAdminNaredbuStop() {
        try {
            if (DretvaHelper.provjeriKorisnika(korisnik, lozinka)) {
                try {

                    DretvaHelper.ugasiSveDretve();

                    SerijalizatorEvidencije se = (SerijalizatorEvidencije) DretvaHelper.pronadiDretvuPremaImenu("serijalizator-evidencije");
                    se.obavijesti();

                    odgovor = "OK; ";
                } catch (Exception ex) {
                    odgovor = "ERROR 03; greška tijekom serijalizacije podataka";
                    Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, "Greška tijekom serijalizacije podataka", ex);
                }
            } else {
                odgovor = "ERROR 00; korisnik ne postoji";
            }
        } catch (IOException ex) {
            odgovor = "ERROR 00; greška prilikom čitanja datoteke";
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Izvršavanje naredbe STAT od strane admina
     * te ispisuje evidenciju na ekran klijenta
     */
    private void izvrsiAdminNaredbuStat() {
        try {
            if (DretvaHelper.provjeriKorisnika(korisnik, lozinka)) {
                serijaliziranaEvidencija = EvidencijaHelper.serijalizirajEvidencijuBytePolje();
                EvidencijaHelper.posaljiSerijalizaciju(true);
                EvidencijaHelper.postaviBrojUspjesnihZahtjeva();
                odgovor = "OK; LENGTH " + (byte) serijaliziranaEvidencija.length + "<CRLF>";
            } else {
                odgovor = "ERROR 00; korisnik ne postoji";
            }
        } catch (IOException ex) {
            odgovor = "ERROR 04; greška prilikom izrade evidencije";
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Izvršavanje naredbe ADD od strane korisnika
     * te dodaje adresu u listu
     */
    private void izvrsiKorisnikNaredbuAdd() {
        if (!DretvaHelper.isPauza()) {
            Konfiguracija konf = DretvaHelper.getKonf();
            int maksAdresa = Integer.parseInt(konf.dajPostavku("maksAdresa"));
            if (EvidencijaHelper.dohvatiZahtjeviZaAdrese().size() < maksAdresa) {
                EvidencijaHelper.dodajZahtjevZaAdrese(new AdresaEntitet(naredbaKorisnik, false));
                odgovor = "OK; ";
            } else {
                odgovor = "ERROR 11; nema mejsta za vašu adresu";
            }
        } else {
            odgovor = "ERROR 01; server je u pauziranom stanju i ne prima naredbe korisnika";
        }

    }

    /**
     * Izvršavanje naredbe TEST od strane korisnika
     * testira unesenu adresu da li postoji
     */
    private void izvrsiKorisnikNaredbuTest() {
        if (!DretvaHelper.isPauza()) {
            for (AdresaEntitet adresa : EvidencijaHelper.dohvatiZahtjeviZaAdrese()) {
                if (adresa.getAdresa().equals(naredbaKorisnik)
                        && !adresa.getStatus()) {
                    odgovor = "OK; NO";
                    return;
                } else if (adresa.getAdresa().equals(naredbaKorisnik)
                        && adresa.getStatus()) {
                    odgovor = "OK; YES";
                    return;
                } else {
                    odgovor = "ERROR 12; trazena adresa ne postoji u listi";
                    return;
                }
            }
        } else {
            odgovor = "ERROR 01; server je u pauziranom stanju i ne prima naredbe korisnika";
        }
    }

    /**
     * Izvršavanje naredbe WAIT od strane klijenta
     * radna dretva čeka sa izvršavanje nnn broj sekundi
     */
    private void izvrsiKorisnikNaredbuWait() {
        if (!DretvaHelper.isPauza()) {
            try {
                int cekaj = 1000 * Integer.parseInt(naredbaKorisnik);
                sleep(cekaj);
                odgovor = "OK; ";
            } catch (InterruptedException ex) {
                odgovor = "ERROR 13; Došlo je do greške prilikom pauziranja";
            }
        } else {
            odgovor = "ERROR 01; server je u pauziranom stanju i ne prima naredbe korisnika";
        }
    }

    /**
     * Izrisanje radne dretve iz liste radnih dretvi
     */
    private void ukloniRadnuDretvu() {
        EvidencijaHelper.postaviZadnjiBrojRadneDretve(ServerSustava.redniBrojDretve);
        listaAktivnihRadnihDretvi.remove(this);
        ServerSustava.redniBrojDretve--;
    }

    synchronized void obavijesti() {
        notify();
    }

    @Override
    public synchronized void start() {
        super.start();
    }

}
