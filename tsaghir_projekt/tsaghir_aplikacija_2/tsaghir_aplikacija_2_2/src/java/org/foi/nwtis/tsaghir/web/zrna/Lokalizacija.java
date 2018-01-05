package org.foi.nwtis.tsaghir.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.foi.nwtis.tsaghir.web.podaci.Izbornik;

/**
 * Klasa koja je zrno lokalizacije odnosno odabira jezika
 * @author tsaghir
 */
@Named(value = "lokalizator")
@SessionScoped
public class Lokalizacija implements Serializable {

    private static final ArrayList<Izbornik> izbornikJezika = new ArrayList<Izbornik>();
    private String odabraniJezik;

    static {
        izbornikJezika.add(new Izbornik("hrvatski", "hr"));
        izbornikJezika.add(new Izbornik("engleski", "en"));
    }

    /**
     * Konstruktor 
     */
    public Lokalizacija() {

    }

    /**
     * getteri i setteri koji se dohvaćaju preko xhtml-a
     * @return 
     */
    public String getOdabraniJezik() {
        UIViewRoot uvir = FacesContext.getCurrentInstance().getViewRoot();
        if (uvir != null) {
            Locale lokalniJezik = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            odabraniJezik = lokalniJezik.getLanguage();

        }
        return odabraniJezik;
    }

    public void setOdabraniJezik(String odabraniJezik) {
        this.odabraniJezik = odabraniJezik;
        Locale lokalniJezik = new Locale(odabraniJezik);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(lokalniJezik);
    }

    public ArrayList<Izbornik> getIzbornikJezika() {
        return izbornikJezika;
    }

    public Object odaberiJezik() {
        setOdabraniJezik(odabraniJezik);
        return "PromjenaJezika";
    }
    
    public Object saljiPoruku() {
        return "saljiPoruku";
    }
    
    public Object pregledPoruka() {
        return "pregledPoruka";
    }
}
