/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.web.zrna;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 *
 * @author tsaghir
 */
@Named(value = "iotPregledZrno")
@SessionScoped
public class IotPregledZrno implements Serializable{

    private final Konfiguracija konf;
    private final int port;
    private final String adresa;
    private boolean odgovorIoT = false;
    private boolean odgovorIotMaster = false;
    private boolean odgovorIoT2 = false;
    private String pristigliOdgovorIoT;
    private String pristigliOdgovorIoT2;
    private String naredbaZaPoslatiIoT;
    private String naredbaZaPoslatiIoT2;
    private String pristigliOdgovorIoTMaster;
    private String naredbaZaPoslatiIoTMaster;
    
    /**
     * konstruktor
     */
    public IotPregledZrno() {
         konf = (Konfiguracija) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("Konfiguracija");
        port = Integer.parseInt(konf.dajPostavku("listener.port"));
        adresa = konf.dajPostavku("listener.adresa");
    }
    
     public void posaljiNaredbuIot() {
        pristigliOdgovorIoT= posaljiNaredbu(naredbaZaPoslatiIoT);
        odgovorIoT = true;
    }
     
      public void dodajIot() {
        pristigliOdgovorIoT2= posaljiNaredbu(naredbaZaPoslatiIoT2);
        odgovorIoT2 = true;
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

    public boolean isOdgovorIoT() {
        return odgovorIoT;
    }

    public void setOdgovorIoT(boolean odgovorIoT) {
        this.odgovorIoT = odgovorIoT;
    }

    public boolean isOdgovorIotMaster() {
        return odgovorIotMaster;
    }

    public void setOdgovorIotMaster(boolean odgovorIotMaster) {
        this.odgovorIotMaster = odgovorIotMaster;
    }

    public String getPristigliOdgovorIoT() {
        return pristigliOdgovorIoT;
    }

    public void setPristigliOdgovorIoT(String pristigliOdgovorIoT) {
        this.pristigliOdgovorIoT = pristigliOdgovorIoT;
    }

    public String getNaredbaZaPoslatiIoT() {
        return naredbaZaPoslatiIoT;
    }

    public void setNaredbaZaPoslatiIoT(String naredbaZaPoslatiIoT) {
        this.naredbaZaPoslatiIoT = naredbaZaPoslatiIoT;
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

    public boolean isOdgovorIoT2() {
        return odgovorIoT2;
    }

    public void setOdgovorIoT2(boolean odgovorIoT2) {
        this.odgovorIoT2 = odgovorIoT2;
    }

    public String getPristigliOdgovorIoT2() {
        return pristigliOdgovorIoT2;
    }

    public void setPristigliOdgovorIoT2(String pristigliOdgovorIoT2) {
        this.pristigliOdgovorIoT2 = pristigliOdgovorIoT2;
    }

    public String getNaredbaZaPoslatiIoT2() {
        return naredbaZaPoslatiIoT2;
    }

    public void setNaredbaZaPoslatiIoT2(String naredbaZaPoslatiIoT2) {
        this.naredbaZaPoslatiIoT2 = naredbaZaPoslatiIoT2;
    }
    
    
    
}
