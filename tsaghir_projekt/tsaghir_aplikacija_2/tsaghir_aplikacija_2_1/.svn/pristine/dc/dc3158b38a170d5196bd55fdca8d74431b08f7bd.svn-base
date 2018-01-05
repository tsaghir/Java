/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.ejb.dretve;

import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import org.foi.nwtis.tsaghir.ejb.podaci.Poruka;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 * Dretva koja služi za obradu pošte
 *
 * @author tsaghir
 */
public class ObradaPosteDretva extends Thread {

    @Resource(mappedName = "jms/QueueFactoryNWTiS_tsaghir_1")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/QueueNWTiS_tsaghir_1")
    private Queue queue;

    private final Konfiguracija konf;
    private Session session;

    private final int trajanjeCiklusa;
    private final String server;
    private final String korisnik;
    private final String lozinka;
    private final String subject;
    private final String folderOther;
    private final String folderNwtis;

    private int brojProcitanihPoruka = 0;
    private int brojUkupnoPoruka = 0;
    private long trajanjeObrade = 0;
    private long pocetakObrade = 0;

    public ObradaPosteDretva(Konfiguracija konfiguracija) {
        super.setName("obrada-email-poste");
        this.konf = konfiguracija;
        server = konf.dajPostavku("mail.server");
        korisnik = konf.dajPostavku("mail.username");
        lozinka = konf.dajPostavku("mail.password");
        trajanjeCiklusa = Integer.parseInt(konf.dajPostavku("mail.timeSecThread"));
        subject = konf.dajPostavku("mail.subject");
        folderOther = konf.dajPostavku("mail.folderOther");
        folderNwtis = konf.dajPostavku("mail.folderNWTiS");
    }

    @Override
    public void run() {
        pocetakObrade = System.currentTimeMillis();
        while (!Thread.currentThread().isInterrupted()) {
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", server);
            session = Session.getInstance(properties, null);
            try {
                Store store = session.getStore("imap");
                store.connect(server, korisnik, lozinka);
                // Open the INBOX folder
                Folder folderInbox = store.getFolder("INBOX");
                folderInbox.open(Folder.READ_ONLY);
                Message[] messages = folderInbox.getMessages();
                brojUkupnoPoruka = messages.length;
                if (messages.length > 0) {
                    for (int i = 0; i < messages.length; i++) {
                        Message m = messages[i];
                        if (m.getContent() instanceof String
                                && m.getSubject().equals(subject)) {
                            premjestiPorukuNWTIS(store, folderInbox, m, folderNwtis);
                        }
                    }
                }
                trajanjeObrade = System.currentTimeMillis() - pocetakObrade;
                posaljiJMSPoruku();
                sleep(trajanjeCiklusa * 1000);
            } catch (InterruptedException | MessagingException | IOException ex) {
                Logger.getLogger(ObradaPosteDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Metoda koja premješta poruku u drugi folder
     *
     * @param store trenutni server
     * @param folder trenutni folder
     * @param poruka trenutna poruka
     * @param naziv foldera
     * @throws MessagingException
     */
    private void premjestiPorukuNWTIS(Store store, Folder folder, Message poruka, String folderZaPremjestaj) throws MessagingException {
        if (folderZaPremjestaj.equals(folderNwtis)) {
            Folder noviFolder = store.getFolder(folderZaPremjestaj);
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
            poruka.setFlag(Flags.Flag.SEEN, true);
            brojProcitanihPoruka++;
            System.out.println("[+] Prebacujem poruke u " + folderNwtis);
        } else {
            Folder noviFolder = store.getFolder(folderZaPremjestaj);
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
            System.out.println("[+] Prebacujem poruke u " + folderOther);
        }
    }

    private void posaljiJMSPoruku() {
        try {
            javax.jms.Connection connection = connectionFactory.createConnection();
            javax.jms.Session sessionJms = connection.createSession(false,
                    javax.jms.Session.AUTO_ACKNOWLEDGE);
            javax.jms.MessageProducer messageProducer = sessionJms.createProducer(queue);
            ObjectMessage message = sessionJms.createObjectMessage();
            Poruka poruka = new Poruka();
            poruka.setBrojNwtisPoruka(brojUkupnoPoruka);
            poruka.setBrojProcitanihPoruka(brojProcitanihPoruka);
            poruka.setVrijemePocetkaRada(pocetakObrade);
            poruka.setVrijemeZavrsetkaRada(trajanjeObrade);
            message.setObject(poruka);
            messageProducer.send(message);
            messageProducer.close();
            connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(ObradaPosteDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void interrupt() {
        super.interrupt();
        System.out.println("Prekinuta obrada: " + super.getName());
    }
}
