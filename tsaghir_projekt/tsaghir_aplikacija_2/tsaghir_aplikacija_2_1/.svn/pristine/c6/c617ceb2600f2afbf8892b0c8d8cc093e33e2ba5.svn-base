package org.foi.nwtis.tsaghir.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.xml.bind.Unmarshaller.Listener;
import org.foi.nwtis.tsaghir.ejb.podaci.Korisnik;
import org.foi.nwtis.tsaghir.ejb.rest.klijeti.KorisniciREST;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

/**
 * Stateful session bean koji služi za autentikaciju korisnika
 * @author tsaghir
 */
@Stateful
@LocalBean
public class AutenticiranjeKorisnika {

    private String korisnik;
    private String lozinka;
    
    /**
     * Metoda preko koje se autenticira korisnik
     * @param korisnik
     * @return 
     */
    public boolean autenticirajKorisnika(String korisnik) {
        KorisniciREST korisniciREST = new KorisniciREST(korisnik);
        String odgovorKorisnik = korisniciREST.preuzmiKorisnika();
        Korisnik kor = null;
        if(kor != null){
            this.korisnik = kor.getNaziv();
            this.lozinka = kor.getLozinka();
            return true;
        }
        return false;
    }
    
    /**
     * Metoda kojom se registrira na MQTT poruke
     * @throws Exception 
     */
    public void registrirajMQTT() throws Exception{
        
        int port = 61613;
        final String host = "nwtis.foi.hr";
        final String destination = "/NWTiS/tsaghir";

        MQTT mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setUserName(this.korisnik);
        mqtt.setPassword(this.lozinka);

        final CallbackConnection connection = mqtt.callbackConnection();
        connection.listener(new org.fusesource.mqtt.client.Listener() {
            long count = 0;

            @Override
            public void onConnected() {
                System.out.println("Otvorena veza na MQTT");
            }

            @Override
            public void onDisconnected() {
                System.out.println("Prekinuta veza na MQTT");
                System.exit(0);
            }

            @Override
            public void onFailure(Throwable value) {
                System.out.println("Problem u vezi na MQTT");
                System.exit(-2);
            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
                String body = msg.utf8().toString();
                System.out.println("Stigla poruka br: " + count);
                count++;
            }
        });
        connection.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                Topic[] topics = {new Topic(destination, QoS.AT_LEAST_ONCE)};
                connection.subscribe(topics, new Callback<byte[]>() {
                    @Override
                    public void onSuccess(byte[] qoses) {
                        System.out.println("Pretplata na: " + destination);
                    }

                    @Override
                    public void onFailure(Throwable value) {
                        System.out.println("Problem kod pretplate na: " + destination);
                        System.exit(-2);
                    }
                });
            }

            @Override
            public void onFailure(Throwable value) {
                System.out.println("Neuspjela pretplata na: " + destination);
                System.exit(-2);
            }
        });

        // Wait forever..
        synchronized (Listener.class) {
            while (true) {
                Listener.class.wait();
            }
        }
    }
}
