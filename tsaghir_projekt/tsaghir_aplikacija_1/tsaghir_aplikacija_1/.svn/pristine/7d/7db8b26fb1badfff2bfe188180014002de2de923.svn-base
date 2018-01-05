package org.foi.nwtis.tsaghir.web.dretve;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika;
import org.foi.nwtis.dkermek.ws.serveri.StatusUredjaja;
import org.foi.nwtis.dkermek.ws.serveri.Uredjaj;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.pomagaci.DataBaseHelper;
import org.foi.nwtis.tsaghir.pomagaci.RegexHelper;
import org.foi.nwtis.tsaghir.pomagaci.ThreadHelper;
import org.foi.nwtis.tsaghir.servisi.soap.klijenti.IoT_MasterKlijent;
import org.foi.nwtis.tsaghir.web.podaci.Dnevnik;
import org.foi.nwtis.tsaghir.web.podaci.Zahtjev;

/**
 * Dretva koja služi za provjeru korisnika iz baze podataka
 *
 * @author tsaghir
 */
public class ObradaNaredbiDretva extends Thread {

    private final Socket socket;
    private String odgovor = "";
    private String server = "";
    private String salje = "";
    private String prima = "";
    private String predmet = "";
    private String primljenaNaredba = "";
    private long pocetak = 0;
    

    public ObradaNaredbiDretva(Socket socket, Konfiguracija konf) {
        super.setName("provjera-korisnika");
        this.socket = socket;
        server = konf.dajPostavku("mail.server");
        salje = konf.dajPostavku("mail.senderAddres");
        prima = konf.dajPostavku("mail.recipientAdress");
        predmet = konf.dajPostavku("mail.subject");
    }

    @Override
    public synchronized void run() {
        pocetak = System.currentTimeMillis() / 1000;
        primljenaNaredba = "";
        odgovor = "";
        InputStream is = null;
        OutputStream os = null;

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
            System.out.println("[+] Primljena naredba: " + naredba);

            Zahtjev zahtjev = new Zahtjev();
            primljenaNaredba = naredba.toString().trim();
            zahtjev.setKomanda(primljenaNaredba);
            Matcher naredbaUGrupama = RegexHelper.odrediKorisnika(primljenaNaredba);
            if (naredbaUGrupama != null) {
                String korisnik = naredbaUGrupama.group(1);
                String lozinka = naredbaUGrupama.group(2);
                String akcija = naredbaUGrupama.group(3);
                zahtjev.setKorisnik(korisnik);
                if (DataBaseHelper.provjeriKorinika(korisnik, lozinka)) {
                    if (akcija.equals("IoT_Master")) {
                        String akcijaIot_Master = naredbaUGrupama.group(4);
                        zahtjev.setRazinaKomande("IoT_Master");
                        zahtjev.setVrstaKomande(akcijaIot_Master);
                        zapisiDnevnikRada(korisnik, 2, primljenaNaredba);
                        switch (akcijaIot_Master) {
                            case "START":
                                odgovor = izvrsiIoT_MasterSTART(korisnik, lozinka);
                                break;
                            case "STOP":
                                odgovor = izvrsiIoT_MasterSTOP(korisnik, lozinka);
                                break;
                            case "WORK":
                                odgovor = izvrsiIoT_MasterWORK(korisnik, lozinka);
                                break;
                            case "WAIT":
                                odgovor = izvrsiIoT_MasterWAIT(korisnik, lozinka);
                                break;
                            case "LOAD":
                                odgovor = izvrsiIoT_MasterLOAD(korisnik, lozinka);
                                break;
                            case "CLEAR":
                                odgovor = izvrsiIoT_MasterCLEAR(korisnik, lozinka);
                                break;
                            case "STATUS":
                                odgovor = izvrsiIoT_MasterSTATUS(korisnik, lozinka);
                                break;
                            case "LIST":
                                odgovor = izvrsiIoT_MasterLIST(korisnik, lozinka);
                                break;
                            default:
                                break;
                        }
                    } else if (akcija.equals("IoT")) {
                        zapisiDnevnikRada(korisnik, 3, primljenaNaredba);
                        int idUredaj = Integer.parseInt(naredbaUGrupama.group(4));
                        String akcijaIot = naredbaUGrupama.group(5);
                        zahtjev.setRazinaKomande("IoT");
                        zahtjev.setVrstaKomande(akcijaIot);
                        zahtjev.setBrojIotUredaja(idUredaj);
                        switch (akcijaIot) {
                            case "ADD":
                                String naziv = naredbaUGrupama.group(6);
                                String adresa = naredbaUGrupama.group(7);
                                zahtjev.setNazivIotUredaja(naziv);
                                zahtjev.setAdresaIotUredaja(adresa);
                                odgovor = izvrsiIoTADD(korisnik, lozinka, idUredaj, naziv, adresa);
                                break;
                            case "WORK":
                                odgovor = izvrsiIoTWORK(korisnik, lozinka, idUredaj);
                                break;
                            case "WAIT":
                                odgovor = izvrsiIoTWAIT(korisnik, lozinka, idUredaj);
                                break;
                            case "REMOVE":
                                odgovor = izvrsiIoTREMOVE(korisnik, lozinka, idUredaj);
                                break;
                            case "STATUS":
                                odgovor = izvrsiIoTSTATUS(korisnik, lozinka, idUredaj);
                                break;
                            default:
                                break;
                        }
                    } else {
                        zahtjev.setRazinaKomande("Server Socket");
                        zahtjev.setVrstaKomande(akcija);
                        zapisiDnevnikRada(korisnik, 1, primljenaNaredba);
                        switch (akcija) {
                            case "PAUSE":
                                odgovor = izvrsiServerPAUSE();
                                break;
                            case "START":
                                odgovor = izvrsiServerSTART();
                                break;
                            case "STOP":
                                odgovor = izvrsiServerSTOP();
                                break;
                            case "STATUS":
                                odgovor = izvrsiServerSTATUS();
                                break;
                            default:
                                break;
                        }
                    }

                } else {
                    zapisiDnevnikRada(korisnik, 4, primljenaNaredba);
                    odgovor = "ERR 10 - korisnik ne postoji ili lozinka ne odgovara";
                }
            }
            zahtjev.setPreuzeto(new Timestamp(System.currentTimeMillis()));
            DataBaseHelper.zapisiDospjeliZahtjev(zahtjev);
            os.write(odgovor.getBytes());
            os.flush();

        } catch (IOException ex) {
            Logger.getLogger(ObradaNaredbiDretva.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ObradaNaredbiDretva.class.getName()).log(Level.SEVERE, "Problem kod zatvaranja i/o stream-a", ex);
            }
        }
    }

    /**
     * Metoda za slanje poruke na java mail server
     *
     * @return
     */
    private boolean saljiEmailPoruku() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", server);
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss.zzz");
        String formatDatum = format.format(new Date());

        StringBuilder sb = new StringBuilder();
        sb.append(primljenaNaredba).append("\r\n");
        sb.append(formatDatum);
        String poruka = sb.toString();
        String sadrzaj = poruka.trim();

        if (napraviEmailPoruku(message, prima, salje, predmet, sadrzaj)) {
            try {
                Transport.send(message);
            } catch (MessagingException ex) {
                Logger.getLogger(ObradaNaredbiDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metoda koja služi za izradu poruke koja će se slati
     *
     * @param message sama poruka
     * @param prima poruku
     * @param salje poruku
     * @param predmet poruke
     * @param sadrzaj poruke
     * @return
     */
    private boolean napraviEmailPoruku(Message message, String prima, String salje, String predmet, String sadrzaj) {
        try {
            message.setFrom(new InternetAddress(salje));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(prima));
            message.setSubject(predmet);
            message.setSentDate(new Date());
            message.setText(sadrzaj);
            return true;
        } catch (AddressException ex) {
            Logger.getLogger(ObradaNaredbiDretva.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (MessagingException ex) {
            Logger.getLogger(ObradaNaredbiDretva.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Metoda koja zapisuje evidenciju u dnevnik rada
     *
     * @param korisnik
     * @param status
     * @return
     */
    private void zapisiDnevnikRada(String korisnik, int status, String komanda) {
        long kraj = System.currentTimeMillis() - pocetak;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Dnevnik dnevnik = null;
        try {
            dnevnik = new Dnevnik(korisnik, InetAddress.getLocalHost().toString(), timestamp, kraj, status, komanda);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ObradaNaredbiDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!DataBaseHelper.zapisiDnevnikRada(dnevnik)) {
            System.out.println("[+] DNEVNIK RADA: Greška prilikom upisa");
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa za registriranje grupe
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    private String izvrsiIoT_MasterSTART(String korisnik, String lozinka) {
        if (IoT_MasterKlijent.registrirajGrupuIoT(korisnik, lozinka)) {
            return "OK 10;";
        } else {
            return "ERR 20;";
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa za deregistriranje grupe
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    private String izvrsiIoT_MasterSTOP(String korisnik, String lozinka) {
        if (IoT_MasterKlijent.deregistrirajGrupuIoT(korisnik, lozinka)) {
            return "OK 10;";
        } else {
            return "ERR 21;";
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa za aktiviranje IoT grupe
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    private String izvrsiIoT_MasterWORK(String korisnik, String lozinka) {
        if (IoT_MasterKlijent.aktivirajGrupuIoT(korisnik, lozinka)) {
            return "OK 10;";
        } else {
            return "ERR 22;";
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa za blokiranje IoT grupe
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    private String izvrsiIoT_MasterWAIT(String korisnik, String lozinka) {
        if (IoT_MasterKlijent.blokirajGrupuIoT(korisnik, lozinka)) {
            return "OK 10;";
        } else {
            return "ERR 23;";
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa za učitavanje predefinirane IoT
     * uređaje u grupu
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    private String izvrsiIoT_MasterLOAD(String korisnik, String lozinka) {
        if (IoT_MasterKlijent.ucitajSveUredjajeGrupe(korisnik, lozinka)) {
            return "OK 10;";
        } else {
            return "";
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa za brisanje svih IoT uređaja iz
     * grupe
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    private String izvrsiIoT_MasterCLEAR(String korisnik, String lozinka) {
        if (IoT_MasterKlijent.obrisiSveUredjajeGrupe(korisnik, lozinka)) {
            return "OK 10;";
        } else {
            return "";
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa koja vraća status grupe
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    private String izvrsiIoT_MasterSTATUS(String korisnik, String lozinka) {
        StatusKorisnika statusKorisnika = IoT_MasterKlijent.dajStatusGrupeIoT(korisnik, lozinka);
        switch (statusKorisnika) {
            case AKTIVAN:
                return "OK 25;";
            case BLOKIRAN:
                return "OK 24;";
            case NEAKTIVAN:
                return "OK 26;";
            case NEPOSTOJI:
                return "OK 27;";
            case PASIVAN:
                return "OK 28;";
            default:
                return "";
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa koja vraća listu svih uređaja iz
     * grupe
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    private String izvrsiIoT_MasterLIST(String korisnik, String lozinka) {
        List<Uredjaj> uredajiLista = IoT_MasterKlijent.dajSveUredjajeGrupe(korisnik, lozinka);
        StringBuilder odgovor = new StringBuilder();
        odgovor.append("{");
        int index = 1;
        for (Uredjaj uredjaj : uredajiLista) {
            odgovor.append(String.valueOf(uredjaj.getId())).append(" ").append(uredjaj.getNaziv());
            if(index != uredajiLista.size()){
                odgovor.append(", ");
            }
            index++;
        }
        odgovor.append("}");
        return "OK 10; " + odgovor.toString();
    }

    /**
     * Metoda koja poziva metodu SOAP servisa
     *
     * @param korisnik
     * @param lozinka
     * @param idUredaj
     * @param naziv
     * @param adresa
     * @return
     */
    private String izvrsiIoTADD(String korisnik, String lozinka, int idUredaj, String naziv, String adresa) {
        if (IoT_MasterKlijent.dodajNoviUredjajGrupi(korisnik, lozinka, idUredaj, naziv, adresa)) {
            return "OK 10;";
        } else {
            return "ERR 30;";
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa koja aktivira uređaj pod zadanim
     * ID-om
     *
     * @param korisnik
     * @param lozinka
     * @param idUredaj
     * @return
     */
    private String izvrsiIoTWORK(String korisnik, String lozinka, int idUredaj) {
        if (IoT_MasterKlijent.aktivirajUredjajGrupe(korisnik, lozinka, idUredaj)) {
            return "OK 10;";
        } else {
            return "ERR 31;";
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa koja blokira uređaj pod zadanim
     * ID-om
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    private String izvrsiIoTWAIT(String korisnik, String lozinka, int idUredaj) {
        if (IoT_MasterKlijent.blokirajUredjajGrupe(korisnik, lozinka, idUredaj)) {
            return "OK 10;";
        } else {
            return "ERR 32;";
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa koja briše uređaj pod zadanim
     * ID-om
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    private String izvrsiIoTREMOVE(String korisnik, String lozinka, int idUredaj) {
        if (IoT_MasterKlijent.obrisiUredjajGrupe(korisnik, lozinka, idUredaj)) {
            return "OK 10;";
        } else {
            return "ERR 33;";
        }
    }

    /**
     * Metoda koja poziva metodu SOAP servisa koja vraća status IoT uređaja
     *
     * @param korisnik
     * @param lozinka
     * @return
     */
    private String izvrsiIoTSTATUS(String korisnik, String lozinka, int idUredaj) {
        StatusUredjaja statusUredaja = IoT_MasterKlijent.dajStatusUredjajaGrupe(korisnik, lozinka, idUredaj);
        switch (statusUredaja) {
            case AKTIVAN:
                return "OK 35;";
            case BLOKIRAN:
                return "OK 34;";
            case NEPOSTOJI:
                return "OK 36;";
            case PASIVAN:
                return "OK 37;";
            default:
                return "";
        }
    }

    /**
     * Metoda koja pauzira obradu meteo podataka
     *
     * @return
     */
    private String izvrsiServerPAUSE() {
        if (!ObradaMeteoPodatakaDretva.PAUZIRAJ_OBRADU && SocketServerDretva.PRIMANJE_NAREDBI) {
            ObradaMeteoPodatakaDretva.PAUZIRAJ_OBRADU = true;
            if (saljiEmailPoruku()) {
                System.out.println("[+] Email poruka:" + salje + " uspjesno poslana na: " + prima + "!");
            } else {
                System.out.println("[+] Greska prilikom slanja email poruke korisnika: " + salje + " sadrzaja: " + predmet + "!");
            }
            return "OK 10;";
        } else {
            return "ERR 10;";
        }
    }

    /**
     * Metoda koja ponovo pokrece obradu meteo podataka
     *
     * @return
     */
    private String izvrsiServerSTART() {
        if (ObradaMeteoPodatakaDretva.PAUZIRAJ_OBRADU && SocketServerDretva.PRIMANJE_NAREDBI) {
            ObradaMeteoPodatakaDretva.PAUZIRAJ_OBRADU = false;
            if (saljiEmailPoruku()) {
                System.out.println("[+] Email poruka:" + salje + " uspjesno poslana na: " + prima + "!");
            } else {
                System.out.println("[+] Greska prilikom slanja email poruke korisnika: " + salje + " sadrzaja: " + predmet + "!");
            }
            return "OK 10;";
        } else {
            return "ERR 11;";
        }
    }

    /**
     * Metoda koja zaustavlja obradu meteo podataka i primanje naredbi
     *
     * @return
     */
    private String izvrsiServerSTOP() {
        //Thread dretvaObradaMeteoPodataka = ThreadHelper.getThreadByName("obrada-meteo-podataka");
        Thread dretvaSocketServer = ThreadHelper.getThreadByName("socket-server");
        Thread dretvaObradaMeteoPodataka = ThreadHelper.getThreadByName("obrada-meteo-podataka");
        if (SocketServerDretva.PRIMANJE_NAREDBI) {
            if (dretvaObradaMeteoPodataka != null && dretvaSocketServer != null) {
                dretvaObradaMeteoPodataka.interrupt();
                dretvaObradaMeteoPodataka.interrupt();
                dretvaSocketServer.interrupt();
                if (saljiEmailPoruku()) {
                    System.out.println("[+] Email poruka:" + salje + " uspjesno poslana na: " + prima + "!");
                } else {
                    System.out.println("[+] Greska prilikom slanja email poruke korisnika: " + salje + " sadrzaja: " + predmet + "!");
                }
                return "OK 10;";
            }
        }
        return "ERR 12;";
    }

    /**
     * Metoda koja provjerava stanje aplikacije aplikacije
     *
     * @return
     */
    private String izvrsiServerSTATUS() {
        if (!ObradaMeteoPodatakaDretva.PAUZIRAJ_OBRADU && SocketServerDretva.PRIMANJE_NAREDBI) {
            if (saljiEmailPoruku()) {
                System.out.println("[+] Email poruka:" + salje + " uspjesno poslana na: " + prima + "!");
            } else {
                System.out.println("[+] Greska prilikom slanja email poruke korisnika: " + salje + " sadrzaja: " + predmet + "!");
            }
            // preuzima podatke 
            return "OK 14;";
        } else if (!SocketServerDretva.PRIMANJE_NAREDBI) {

            if (saljiEmailPoruku()) {
                System.out.println("[+] Email poruka:" + salje + " uspjesno poslana na: " + prima + "!");
            } else {
                System.out.println("[+] Greska prilikom slanja email poruke korisnika: " + salje + " sadrzaja: " + predmet + "!");
            }
            // ne preuzima podatke i korisničke komande
            return "OK 15;";
        } else if (ObradaMeteoPodatakaDretva.PAUZIRAJ_OBRADU) {
            if (saljiEmailPoruku()) {
                System.out.println("[+] Email poruka:" + salje + " uspjesno poslana na: " + prima + "!");
            } else {
                System.out.println("[+] Greska prilikom slanja email poruke korisnika: " + salje + " sadrzaja: " + predmet + "!");
            }
            // privremeno ne preuzima podatke
            return "OK 13;";
        }
        return "";
    }

    @Override
    public void interrupt() {
        super.interrupt();
        System.out.println("Prekinuta obrada: " + super.getName());
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

}
