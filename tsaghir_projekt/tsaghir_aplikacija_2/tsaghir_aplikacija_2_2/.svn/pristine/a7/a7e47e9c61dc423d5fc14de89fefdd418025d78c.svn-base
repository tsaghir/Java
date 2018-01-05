/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.web.zrna;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 * zrno koje reprezentira serverStatus.xhtml
 *
 * @author tsaghir
 */
@Named(value = "serverStatus")
@SessionScoped
public class ServerStatusZrno implements Serializable {

    private final Konfiguracija konf;
    private final int port;
    private final String adresa;
    private boolean odgovorServer = false;
    private boolean odgovorIotMaster = false;
    private String pristigliOdgovorServer;
    private String pristigliOdgovorIoTMaster;
    private String naredbaZaPoslatiServer;
    private String naredbaZaPoslatiIoTMaster;

    /**
     * konstruktor
     */
    public ServerStatusZrno() {
        konf = (Konfiguracija) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("Konfiguracija");
        port = Integer.parseInt(konf.dajPostavku("listener.port"));
        adresa = konf.dajPostavku("listener.adresa");
    }

    public void posaljiNaredbuServer() {
        pristigliOdgovorServer= posaljiNaredbu(naredbaZaPoslatiServer);
        odgovorServer = true;
    }

    public void posaljiNaredbuIotMaster() {
        pristigliOdgovorIoTMaster= posaljiNaredbu(naredbaZaPoslatiIoTMaster);
        odgovorIotMaster = true;
    }
    
    public String posaljiNaredbu(String zahtjev){
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;
        String odgovor = null;
        try {
            socket = new Socket(adresa,port);

            is = socket.getInputStream();
            os = socket.getOutputStream();
            
            os.write(zahtjev.getBytes());
            os.flush();
            socket.shutdownOutput();
            StringBuffer sb = new StringBuffer();
            while (true) {
                int znak = is.read();
                if (znak == -1) {
                    break;
                }
                sb.append((char) znak);
            }
            System.out.println("Primljeni odgovor: " + sb);
            odgovor = sb.toString().trim();
        } catch (IOException ex) {
            Logger.getLogger(ServerStatusZrno.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ServerStatusZrno.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return odgovor;
    }

    /**
     * GETTERS AND SETTERS
     *
     * @return
     */

    public boolean isOdgovorServer() {
        return odgovorServer;
    }

    public void setOdgovorServer(boolean odgovorServer) {
        this.odgovorServer = odgovorServer;
    }

    public String getPristigliOdgovorServer() {
        return pristigliOdgovorServer;
    }

    public void setPristigliOdgovorServer(String pristigliOdgovorServer) {
        this.pristigliOdgovorServer = pristigliOdgovorServer;
    }

   

    public boolean isOdgovorIotMaster() {
        return odgovorIotMaster;
    }

    public void setOdgovorIotMaster(boolean odgovorIotMaster) {
        this.odgovorIotMaster = odgovorIotMaster;
    }

    public String getNaredbaZaPoslatiServer() {
        return naredbaZaPoslatiServer;
    }

    public void setNaredbaZaPoslatiServer(String naredbaZaPoslatiServer) {
        this.naredbaZaPoslatiServer = naredbaZaPoslatiServer;
    }

    public String getNaredbaZaPoslatiIoTMaster() {
        return naredbaZaPoslatiIoTMaster;
    }

    public void setNaredbaZaPoslatiIoTMaster(String naredbaZaPoslatiIoTMaster) {
        this.naredbaZaPoslatiIoTMaster = naredbaZaPoslatiIoTMaster;
    }

    public String getPristigliOdgovorIoTMaster() {
        return pristigliOdgovorIoTMaster;
    }

    public void setPristigliOdgovorIoTMaster(String pristigliOdgovorIoTMaster) {
        this.pristigliOdgovorIoTMaster = pristigliOdgovorIoTMaster;
    }
    
    
    
}
