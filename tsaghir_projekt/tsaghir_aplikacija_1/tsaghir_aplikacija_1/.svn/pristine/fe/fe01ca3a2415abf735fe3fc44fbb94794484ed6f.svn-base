package org.foi.nwtis.tsaghir.web.dretve;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 * Dretva koja služi kao primitivni poslužitelj i ima ulogu socket servera na
 * određenom portu
 *
 * @author tsaghir
 */
public class SocketServerDretva extends Thread {

    private final Konfiguracija konf;
    private final int port;
    public static boolean PRIMANJE_NAREDBI = true;

    public SocketServerDretva() {
        super.setName("socket-server");
        konf = (Konfiguracija) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("Konfiguracija");
        port = Integer.parseInt(konf.dajPostavku("listener.port"));
    }

    @Override
    public synchronized void interrupt() {
        System.out.println("Prekinuta obrada: " + super.getName());
        PRIMANJE_NAREDBI = false;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(port);
            while (PRIMANJE_NAREDBI) {
                Socket socket = ss.accept();
                ObradaNaredbiDretva obradaNaredbi = new ObradaNaredbiDretva(socket, konf);
                obradaNaredbi.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketServerDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

}
