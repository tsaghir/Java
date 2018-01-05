package org.foi.nwtis.tsaghir.web.dretve;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.ServletContext;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.web.pomagaci.DataBaseHelper;
import org.foi.nwtis.tsaghir.web.pomagaci.RegexHelper;
import org.foi.nwtis.tsaghir.web.zrna.SlanjePoruke;

/**
 * Dretva koja obrađuje poslane poruke na java mail serveru
 * @author tsaghir
 */
public class ObradaPoruka extends Thread {

    private boolean prekid_obrade = false;
    private ServletContext sc = null;
    private Session session;

    private String server;
    private String korisnik;
    private String lozinka;
    private String folderOstalo;
    private String folderPoruka;
    private int trajanjeCiklusa;
    private SlanjePoruke sp;

    /**
     * Konstruktor dretve
     */
    public ObradaPoruka() {
        sp = new SlanjePoruke();
        super.setName("obrada-poruka");
    }

    /**
     * Metoda koja se pokreće ukoliko se naglo prekine dretva
     */
    @Override
    public void interrupt() {
        prekid_obrade = true;
        super.interrupt();
    }

    /**
     * Metoda koja se poziva nakon instance dretve
     */
    @Override
    public void run() {
        Konfiguracija konf = (Konfiguracija) sc.getAttribute("Mail_Konfig");
        server = konf.dajPostavku("mail.server");
        korisnik = konf.dajPostavku("mail.usernameThread");
        lozinka = konf.dajPostavku("mail.passwordThread");
        trajanjeCiklusa = Integer.parseInt(konf.dajPostavku("mail.timeSecThread"));
        folderOstalo = konf.dajPostavku("mail.folderOther");
        folderPoruka = konf.dajPostavku("mail.folderNWTiS");
        long trajanjeObrade = 0;
        long pocetakObrade =0;
        String vrijemeZapocela = null;
        String vrijemeZavrsila = null;
        int brojPorukaCiklus = 0;
        int redniBrojCiklusa = 0;
        int dodaniIOT=0;
        int mjereniTEMP=0;
        int izvrseniEVENT=0;
        int greska=0;
        

        while (!prekid_obrade) {
            brojPorukaCiklus = 0;
            pocetakObrade = System.currentTimeMillis();
            vrijemeZapocela = dohvatiVrijemeObrade();
            // Start the session
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", server);
            session = Session.getInstance(properties, null);

            // Connect to the store
            try {
                Store store = session.getStore("imap");
                store.connect(server, korisnik, lozinka);
                // Open the INBOX folder
                Folder folderInbox = store.getFolder("INBOX");
                folderInbox.open(Folder.READ_ONLY);

                Message[] messages = folderInbox.getMessages();
                if (messages.length > 0) {
                    for (int i = 0; i < messages.length; i++) {
                        Message m = messages[i];
                        if (m.getContent() instanceof String) {
                            String svjezaPoruka = m.getContent().toString();
                            String sadrzajPoruke = svjezaPoruka.trim();
                            switch (obradiPoruku(sadrzajPoruke)) {
                                case RegexHelper.ADD:
                                    Matcher matcherAdd = RegexHelper.getMatcher();
                                    if (dodajPodatkeUredaji(matcherAdd)) {
                                        System.out.println("[+] " + sadrzajPoruke + " (Dodano u tablicu uredaji) [Premještam poruku]");
                                        dodaniIOT++;
                                        brojPorukaCiklus++;
                                        premjestiPoruku(store, folderInbox, m, folderPoruka);
                                    } else {
                                        greska++;
                                        System.out.println("[+] Uređaj već postoji, [Premještam poruku]");
                                        premjestiPoruku(store, folderInbox, m, folderPoruka);
                                    }
                                    break;
                                case RegexHelper.TEMP:
                                    Matcher matcherTemp = RegexHelper.getMatcher();
                                    if (dodajPodatkeTemperatura(matcherTemp)) {
                                        System.out.println("[+] "+sadrzajPoruke + " (Dodano u tablicu temperature)");
                                        mjereniTEMP++;
                                        brojPorukaCiklus++;
                                        premjestiPoruku(store, folderInbox, m, folderPoruka);
                                    } else {
                                        greska++;
                                        System.out.println("[+] Greška, ne postoji taj uredaj u bazi");
                                        premjestiPoruku(store, folderInbox, m, folderPoruka);
                                    }
                                    break;
                                case RegexHelper.EVENT:
                                    Matcher matcherEvent = RegexHelper.getMatcher();
                                    if (dodajPodatkeDogadaj(matcherEvent)) {
                                        System.out.println("[+] "+sadrzajPoruke + " (Dodano u tablicu dogadaji)");
                                        izvrseniEVENT++;
                                        brojPorukaCiklus++;
                                        premjestiPoruku(store, folderInbox, m, folderPoruka);
                                    } else {
                                        greska++;
                                        System.out.println("[+] Greška, ne postoji taj uredaj u bazi");
                                        premjestiPoruku(store, folderInbox, m, folderPoruka);
                                    }
                                    break;
                                case RegexHelper.FAIL:
                                    brojPorukaCiklus++;
                                    premjestiPoruku(store, folderInbox, m, folderOstalo);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    folderInbox.close(true);
                    store.close();
                }
                trajanjeObrade = System.currentTimeMillis() - pocetakObrade;
                vrijemeZavrsila = dohvatiVrijemeObrade();
                redniBrojCiklusa++;
                System.out.println("[+] Obrada poruka u ciklusu: " + redniBrojCiklusa);
                sp.saljiStatistiku(vrijemeZapocela, vrijemeZavrsila, trajanjeObrade, brojPorukaCiklus, dodaniIOT, mjereniTEMP, izvrseniEVENT, greska);
                sleep(trajanjeCiklusa * 1000 - trajanjeObrade);
            } catch (MessagingException | InterruptedException | IOException ex) {
                Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    /**
     * Metoda koja dohvaća trenutno vrijeme obrade i vraća ga u odgovarajućem formatu
     * @return 
     */
    private String dohvatiVrijemeObrade(){
        java.util.Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss.zzzz");
        String format = formatter.format(date);
        return format;
    }

    /**
     * Metoda koja određe naredbu pomoću regex patterna
     * @param sadrzajPoruke
     * @return 
     */
    private String obradiPoruku(String sadrzajPoruke) {
        return RegexHelper.odrediNaredbu(sadrzajPoruke);
    }

    /**
     * Metoda koja od matchera uzima odgovarajuće grupe i popunjuje varijable za uređaje
     * @param matcher
     * @return 
     */
    private boolean dodajPodatkeUredaji(Matcher matcher) {
        int id = Integer.parseInt(matcher.group(1));
        String naziv = matcher.group(2);
        float latitude = Float.parseFloat(matcher.group(3));
        float longitude = Float.parseFloat(matcher.group(3));
        try {
            DataBaseHelper.dodajUredaj(id, naziv, latitude, longitude);
        } catch (SQLException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    /**
     * Metoda koja od matchera uzima odgovarajuće grupe i popunjuje varijable za temperature
     * @param matcher
     * @return 
     */
    private boolean dodajPodatkeTemperatura(Matcher matcher) {
        int id = Integer.parseInt(matcher.group(1));
        float temp = Float.parseFloat(matcher.group(4));
        String raw_vrijeme_mjerenja = matcher.group(2) + " " + matcher.group(3);
        String vrijeme_mjerenja = raw_vrijeme_mjerenja.replace(".", "-");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sada = new Date();
        String vrijeme_kreiranja = sdf.format(sada);
        try {
            DataBaseHelper.dodajTemperature(id, temp, vrijeme_mjerenja, vrijeme_kreiranja);
        } catch (SQLException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Metoda koja od matchera uzima odgovarajuće grupe i popunjuje varijable za događaje
     * @param matcher
     * @return 
     */
    private boolean dodajPodatkeDogadaj(Matcher matcher) {
        int id = Integer.parseInt(matcher.group(1));
        int vrsta = Integer.parseInt(matcher.group(4));
        String raw_vrijeme_izvrsavanja = matcher.group(2) + " " + matcher.group(3);
        String vrijeme_izvrsavanja = raw_vrijeme_izvrsavanja.replace(".", "-");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sada = new Date();
        String vrijeme_kreiranja = sdf.format(sada);
        try {
            DataBaseHelper.dodajDogadaje(id, vrsta, vrijeme_izvrsavanja, vrijeme_kreiranja);
        } catch (SQLException ex) {
            Logger.getLogger(ObradaPoruka.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    /**
     * Metoda koja premješta poruku u drugi folder
     * @param store trenutni server
     * @param folder trenutni folder
     * @param poruka trenutna poruka
     * @param naziv foldera
     * @throws MessagingException 
     */
    private void premjestiPoruku(Store store, Folder folder, Message poruka, String naziv) throws MessagingException {
        if(naziv == folderOstalo){
            System.out.println("[+] Prebacujem poruke u "+folderOstalo);
        }else{
            System.out.println("[+] Prebacujem poruke u "+folderPoruka);
        }
        Folder noviFolder = store.getFolder(naziv);
        if (!noviFolder.exists()) {
            noviFolder.create(Folder.HOLDS_MESSAGES);
        }
        noviFolder.open(Folder.READ_WRITE);
        Message[] poruke = new Message[1];
        poruke[0] = poruka;
        if (folder != null) {
            folder.copyMessages(poruke, noviFolder);
        } else {
            noviFolder.appendMessages(poruke);
        }
        noviFolder.close(false);
        poruka.setFlag(Flags.Flag.DELETED, true);
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * Setter za kontekst
     * @param sc 
     */
    public void setSc(ServletContext sc) {
        this.sc = sc;
    }

}
