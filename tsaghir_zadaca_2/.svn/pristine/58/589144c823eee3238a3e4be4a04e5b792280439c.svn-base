package org.foi.nwtis.tsaghir.web.zrna;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.web.slusaci.SlusacAplikacije;

/**
 * Zrno koje je povezano sa slanjePoruke.xhtml
 * @author tsaghir
 */
@Named(value = "slanjePoruke")
@RequestScoped
public class SlanjePoruke {

    private Konfiguracija konf = null;
    private String server;
    private String salje;
    private String prima;
    private String predmet;
    private String sadrzaj;

    private String saljeStatistika;
    private String primaStatistika;
    private String predmetStatistika;
    private String sadrzajStatistika;
    private int redniBrojStatistika = 0;

    /**
     * konstruktor
     */
    public SlanjePoruke() {
        konf = SlusacAplikacije.getKonfig();
        server = konf.dajPostavku("mail.server");
        salje = konf.dajPostavku("mail.usernameThread");
        prima = konf.dajPostavku("mail.usernameView");
        predmet = konf.dajPostavku("mail.subject");
    }

    public String getSalje() {
        return salje;
    }

    public void setSalje(String salje) {
        this.salje = salje;
    }

    public String getPrima() {
        return prima;
    }

    public void setPrima(String prima) {
        this.prima = prima;
    }

    public String getPredmet() {
        return predmet;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    /**
     * Metoda za slanje poruke na java mail server
     * @return 
     */
    public String saljiPoruku() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", server);
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        
        if (napraviPoruku(message, prima, salje, predmet, sadrzaj)) {
            try {
                Transport.send(message);
            } catch (MessagingException ex) {
                Logger.getLogger(SlanjePoruke.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("[+] Poruka uspjesno poslana!");
            obrisiVarijable();
            return "PoslanaPoruka";
        } else {
            System.out.println("[+] Greška prilikom slanja poruke!");
            return "PorukaNijePoslana";
        }
    }

    /**
     * Metoda koja služi za izradu poruke koja će se slati
     * @param message sama poruka
     * @param prima poruku
     * @param salje poruku
     * @param predmet poruke
     * @param sadrzaj poruke
     * @return 
     */
    private boolean napraviPoruku(Message message, String prima, String salje, String predmet, String sadrzaj) {
        try {
            message.setFrom(new InternetAddress(salje));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(prima));
            message.setSubject(predmet);
            message.setSentDate(new Date());
            message.setText(sadrzaj);
            return true;
        } catch (AddressException ex) {
            Logger.getLogger(SlanjePoruke.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (MessagingException ex) {
            Logger.getLogger(SlanjePoruke.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Brise varijable kako bi forma na webu bila prazna
     */
    private void obrisiVarijable() {
        salje = "";
        prima = "";
        predmet = "";
        sadrzaj ="";
    }

    /**
     * Vraća "pocetna"
     * @return 
     */
    public String pocetna() {
        return "Pocetna";
    }

    /**
     * Pregled poruka
     * @return 
     */
    public String pregledPoruka() {
        return "PregledPoruka";
    }

    /**
     * Metoda za izradu poruke statistike
     * @param vrijemePocetak
     * @param vrijemeZavrsetak
     * @param trajanjeObrade
     * @param brojPoruka
     * @param dodaniIOT
     * @param mjereniTEMP
     * @param izvrseniEVENT
     * @param greske
     * @return 
     */
    public boolean saljiStatistiku(String vrijemePocetak, String vrijemeZavrsetak, long trajanjeObrade, int brojPoruka, int dodaniIOT, int mjereniTEMP, int izvrseniEVENT, int greske) {
        popuniVarijableZaStatistiku(vrijemePocetak, vrijemeZavrsetak, trajanjeObrade, brojPoruka, dodaniIOT, mjereniTEMP, izvrseniEVENT, greske);
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", server);
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        if (napraviPoruku(message, primaStatistika, saljeStatistika, predmetStatistika, sadrzajStatistika)) {
            try {
                Transport.send(message);
            } catch (MessagingException ex) {
                Logger.getLogger(SlanjePoruke.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("[+] Poruka uspjesno poslana!");
            redniBrojStatistika++;
            return true;
        } else {
            System.out.println("[+] Greška prilikom slanja poruke");
            return false;
        }
    }

    /**
     * Metoda za popunjenje varijabli koje su potrebne za poruku statistike
     * @param vrijemePocetak
     * @param vrijemeZavrsetak
     * @param trajanjeObrade
     * @param brojPoruka
     * @param dodaniIOT
     * @param mjereniTEMP
     * @param izvrseniEVENT
     * @param greske 
     */
    private void popuniVarijableZaStatistiku(String vrijemePocetak, String vrijemeZavrsetak, long trajanjeObrade, int brojPoruka, int dodaniIOT, int mjereniTEMP, int izvrseniEVENT, int greske) {
        saljeStatistika = konf.dajPostavku("mail.usernameStatistics");
        primaStatistika = konf.dajPostavku("mail.usernameView");
        if(brojPoruke(redniBrojStatistika).contains("_")){
            predmetStatistika = konf.dajPostavku("mail.subjectStatistics")+brojPoruke(redniBrojStatistika);
        }else{
            predmetStatistika = konf.dajPostavku("mail.subjectStatistics")+" "+brojPoruke(redniBrojStatistika);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Obrada zapocela u: " + vrijemePocetak+"\n");
        sb.append("Obrada zavrsila u: "+ vrijemeZavrsetak+"\n\n");
        sb.append("Trajanje obrade u ms: "+trajanjeObrade+"\n");
        sb.append("Broj poruka: "+brojPoruka+"\n");
        sb.append("Broj dodanih IOT: "+dodaniIOT+"\n");
        sb.append("Broj mjerenih TEMP: "+mjereniTEMP+"\n");
        sb.append("Broj izvrsenih EVENT: "+izvrseniEVENT+"\n");
        sb.append("Broj pogresaka: "+greske);
        String poruka = sb.toString();
        sadrzajStatistika = poruka.trim(); 
        
    }
    
    /**
     * Metoda koja izrađuje broj poruke koji se pridružuje u naslovu poruke
     * @param broj
     * @return 
     */
    private String brojPoruke(int broj){
        String brojString = Integer.toString(broj);
        String rezultat = "";
        int index = 0;
        char arr[] = null;
        char charBroj[] = null;
        
        if (brojString.length() == 4) {
            arr = new char[brojString.length() + 1];
            for (int i = 0; i < arr.length; i++) {
                if (i == 1) {
                    arr[i] = ".".charAt(0);
                    i++;
                }
                arr[i] = brojString.charAt(index);
                index++;
            }
        } else if (brojString.length() < 4) {
            arr = new char[brojString.length() + 5 - brojString.length()];
            charBroj = new char[brojString.length()];
            for (int i = 0; i < 5 - brojString.length(); i++) {
                arr[i] = "_".charAt(0);
                index++;
            }
            for (int i = 0; i < brojString.length(); i++) {
                charBroj[i] = brojString.charAt(i);
            }
        }else{
            rezultat = String.valueOf(broj);
        }
        if (charBroj != null) {
            rezultat = String.valueOf(arr) + String.valueOf(charBroj);
        } else if(arr != null) {
            rezultat = String.valueOf(arr);
        }
        return rezultat;
    }
}
