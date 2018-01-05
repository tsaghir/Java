package org.foi.nwtis.tsaghir.web.zrna;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.web.kontrole.Izbornik;
import org.foi.nwtis.tsaghir.web.kontrole.Poruka;
import org.foi.nwtis.tsaghir.web.slusaci.SlusacAplikacije;

/**
 * Zrno koje je povezano sa pregledom poruka
 * @author tsaghir
 */
@Named(value = "pregledPoruka")
@SessionScoped
public class PregledPoruka implements Serializable {

    private Session session;
    private Store store;
    private Folder folder;
    private Message[] messages;
    private String posluzitelj;
    private String korisnik;
    private String lozinka;
    private int prikazaniBrojPoruka;

    private ArrayList<Izbornik> mape = new ArrayList<>();
    private String odabranaMapa = "INBOX";
    private ArrayList<Poruka> poruke = new ArrayList<Poruka>();
    private int ukupnoPorukaMapa = 0;
    int brojPrikazanihPoruka = 0;
    int pozicijaOdPoruke = 0;
    int pozicijaDoPoruke = 0;
    private String traziPoruke;
    private int pocetak = 0;
    private int kraj = 0;
    private int ukupnoPoruka;
    private boolean sljedecaGumb = false;
    private boolean prethodnaGumb = true;
    private boolean sljedeca = false;
    private boolean prethodna = false;
    private boolean trazi = false;
    private int trenutnoPoruka = 0;

    /**
     * Konstruktor
     */
    public PregledPoruka() {
        dohvatiPodatkeZaMailServer();
        spojiSeNaMailServer();
        preuzmiMape();
        preuzmiPoruke();

    }

    /**
     * Metoda koja dohvaća podatke za mail server
     */
    private void dohvatiPodatkeZaMailServer() {
        Konfiguracija konf = SlusacAplikacije.getKonfig();
        this.korisnik = konf.dajPostavku("mail.usernameView");
        this.lozinka = konf.dajPostavku("mail.passwordView");
        this.posluzitelj = konf.dajPostavku("mail.server");
        this.prikazaniBrojPoruka = Integer.parseInt(konf.dajPostavku("mail.numMessages"));
    }

    /**
     * Metoda koja stvara novu sesiju za mail server
     */
    private void spojiSeNaMailServer() {
        // Start the session
        java.util.Properties properties = System.getProperties();
        session = Session.getInstance(properties, null);

        try {
            // Connect to the store
            store = session.getStore("imap");
            store.connect(this.posluzitelj, this.korisnik, this.lozinka);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Host: " + this.posluzitelj + ", user: " + korisnik + ", pw: " + lozinka);
    }

    /**
     * Metoda koja preuzima mape sa mail servera
     */
    void preuzmiMape() {
        Folder[] folderi;
        try {
            folder = store.getDefaultFolder();
            folderi = folder.list();
            mape = new ArrayList<Izbornik>();

            for (Folder f : folderi) {
                String brojPoruka = String.valueOf(f.getMessageCount());
                mape.add(new Izbornik(f.getName() + " - " + brojPoruka, f.getName()));
            }
        } catch (MessagingException ex) {
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda koja preuzima poruke sa mail servera
     */
    void preuzmiPoruke() {
        poruke.clear();
        try {
            // Open the INBOX folder
            folder = store.getFolder(this.odabranaMapa);
            folder.open(Folder.READ_ONLY);

            //Ako nema poruka, završi
            if (folder.getMessageCount() == 0) {
                return;
            }
            if (trenutnoPoruka == 0) {
                ukupnoPoruka = folder.getMessageCount();
            }
            trenutnoPoruka = ukupnoPoruka;

            if (ukupnoPoruka > prikazaniBrojPoruka) {
                sljedecaGumb = false;
                stranicenje();

                if (!trazi) {
                    messages = folder.getMessages(pocetak, kraj);
                }

                messages = okreniRedoslijed(messages);
                for (Message message : messages) {
                    Message m = message;
                    m.getAllHeaders();
                    if (traziPoruke != null && !traziPoruke.isEmpty()) {
                        String sadrzajPoruke = dohvatiTekstualniSadrzajPoruke(m.getContent());
                        if (sadrzajPoruke.indexOf(traziPoruke) >= 0) {
                            poruke.add(new Poruka(Integer.toString(m.getMessageNumber()), m.getSentDate(), m.getReceivedDate(), m.getFrom()[0].toString(),
                                    m.getSubject(), dohvatiTekstualniSadrzajPoruke(m.getContent()), m.getContentType()));
                        }
                    } else {
                        poruke.add(new Poruka(Integer.toString(m.getMessageNumber()), m.getSentDate(), m.getReceivedDate(), m.getFrom()[0].toString(),
                                m.getSubject(), dohvatiTekstualniSadrzajPoruke(m.getContent()), m.getContentType()));
                    }
                }
            } else {
                sljedecaGumb = true;
                messages = folder.getMessages();
                messages = okreniRedoslijed(messages);
                for (int i = 0; i < messages.length; i++) {
                    Message m = messages[i];
                    m.getAllHeaders();
                    if (traziPoruke != null && !traziPoruke.isEmpty()) {
                        String sadrzajPoruke = dohvatiTekstualniSadrzajPoruke(m.getContent());
                        if (sadrzajPoruke.indexOf(traziPoruke) >= 0) {
                            poruke.add(new Poruka(Integer.toString(m.getMessageNumber()), m.getSentDate(), m.getReceivedDate(), m.getFrom()[0].toString(),
                                    m.getSubject(), dohvatiTekstualniSadrzajPoruke(m.getContent()), m.getContentType()));
                        }
                    } else {
                        poruke.add(new Poruka(Integer.toString(m.getMessageNumber()), m.getSentDate(), m.getReceivedDate(), m.getFrom()[0].toString(),
                                m.getSubject(), dohvatiTekstualniSadrzajPoruke(m.getContent()), m.getContentType()));
                    }
                }
            }

        } catch (MessagingException | IOException ex) {
            sljedecePoruke();
            prethodnaGumb = true;
            Logger.getLogger(PregledPoruka.class.getName()).log(Level.SEVERE, null, ex);
        }
        ukupnoPorukaMapa = ukupnoPoruka;
        trazi = false;
    }

    /**
     * Metoda koja okreće redoslijed dohvaćenih poruka
     * @param m
     * @return 
     */
    private Message[] okreniRedoslijed(Message[] m) {
        List<Message> pom = new ArrayList<>();

        for (int i = m.length - 1; i >= 0; i--) {
            pom.add(m[i]);
        }

        return pom.toArray(new Message[pom.size()]);
    }

    /**
     * Metoda za straničenje po ograničenom broju iz konfiguracije
     */
    private void stranicenje() {
        if (pocetak == 0 && kraj == 0) {
            kraj = ukupnoPoruka;
            pocetak = kraj - prikazaniBrojPoruka + 1;
        } else if (sljedeca) {
            kraj = pocetak;
            pocetak = kraj - prikazaniBrojPoruka + 1;
            if (pocetak <= 0) {
                pocetak = 1;
                sljedecaGumb = true;
            }
        } else if (prethodna) {
            sljedecaGumb = false;
            pocetak = kraj;
            kraj = kraj + prikazaniBrojPoruka - 1;
            if (kraj == ukupnoPoruka) {
                prethodnaGumb = true;
            }
        }
    }

    /**
     * Metoda koja vraća tekstualni sadržaj poruka
     * @param sadrzaj
     * @return 
     */
    private String dohvatiTekstualniSadrzajPoruke(Object sadrzaj) {
        if (sadrzaj instanceof String) {
            return sadrzaj.toString();
        }
        return "Ne-TekstualniSadržaj";
    }

    /**
     * Metoda koja služi za promjenu mapi
     * @return 
     */
    public String promjenaMape() {
        pocetak = 0;
        kraj = 0;
        trenutnoPoruka = 0;
        this.preuzmiMape();
        this.preuzmiPoruke();
        return "PromjenaMape";
    }

    /**
     * Metoda za pretraživanje poruka 
     * @return 
     */
    public String traziPoruke() {
        trazi = true;
        this.preuzmiPoruke();
        traziPoruke = null;
        return "FiltrirajPoruke";
    }

    /**
     * Metoda koja se poziva pritiskom na gumb prethodne poruke
     * @return 
     */
    public String prethodnePoruke() {
        sljedeca = false;
        prethodna = true;
        if (kraj >= ukupnoPoruka || kraj == ukupnoPoruka || trenutnoPoruka == ukupnoPoruka) {
            prethodnaGumb = true;
        }
        this.preuzmiPoruke();
        return "PrethodnePoruke";
    }

    /**
     *  Metoda koja se poziva pritiskom na gumb sljedeće poruke
     * @return 
     */
    public String sljedecePoruke() {
        prethodna = false;
        sljedeca = true;
        prethodnaGumb = false;
        this.preuzmiPoruke();
        return "SljedecePoruke";
    }

    /**
     * Promjena jezika
     * @return 
     */
    public String promjenaJezika() {
        return "PromjenaJezika";
    }

    /**
     * Slanje poruke
     * @return 
     */
    public String saljiPoruku() {
        return "saljiPoruku";
    }

    /**
     * Getteri i Setteri koji su u direktnoj vezi sa XHTML-om
     * @return 
     */
    public String getOdabranaMapa() {
        return odabranaMapa;
    }

    public void setOdabranaMapa(String odabranaMapa) {
        this.odabranaMapa = odabranaMapa;
    }

    public int getUkupnoPorukaMapa() {
        return ukupnoPorukaMapa;
    }

    public void setUkupnoPorukaMapa(int ukupnoPorukaMapa) {
        this.ukupnoPorukaMapa = ukupnoPorukaMapa;
    }

    public String getTraziPoruke() {
        return traziPoruke;
    }

    public void setTraziPoruke(String traziPoruke) {
        this.traziPoruke = traziPoruke;
    }

    public ArrayList<Izbornik> getMape() {
        return mape;
    }

    public ArrayList<Poruka> getPoruke() {
        return poruke;
    }

    public boolean isSljedecaGumb() {
        return sljedecaGumb;
    }

    public void setSljedecaGumb(boolean sljedecaGumb) {
        this.sljedecaGumb = sljedecaGumb;
    }

    public boolean isPrethodnaGumb() {
        return prethodnaGumb;
    }

    public void setPrethodnaGumb(boolean prethodnaGumb) {
        this.prethodnaGumb = prethodnaGumb;
    }

    public int getUkupnoPoruka() {
        return ukupnoPoruka;
    }

    public void setUkupnoPoruka(int ukupnoPoruka) {
        this.ukupnoPoruka = ukupnoPoruka;
    }

}
