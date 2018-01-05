/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.web.zrna;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.pomagaci.DataBaseHelper;
import org.foi.nwtis.tsaghir.web.podaci.Zahtjev;

/**
 * zrno koje reprezentira pregledZahtjeva.xhtml
 * @author tsaghir
 */
@ManagedBean
@SessionScoped
public class PregledZahtjeva {

    private List<Zahtjev> listaZahtjeva;
    private final Konfiguracija konf;
    private final int limitTabliceZahtjevi;
    
    /**
     * konstruktor
     */
    public PregledZahtjeva() {
        listaZahtjeva = DataBaseHelper.dajSveZahtjeve();
        konf = (Konfiguracija) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("Konfiguracija");
        limitTabliceZahtjevi = Integer.parseInt(konf.dajPostavku("limitTabliceZahtjeva"));
    }
    
    /**
     * Metoda kojom se odjavljuje korisnik
     *
     * @return
     */
    public String odjavaKorisnika() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        session.removeAttribute("korisnik");
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml");
        return "logout";
    }

    public List<Zahtjev> getListaZahtjeva() {
        return listaZahtjeva;
    }

    public void setListaZahtjeva(List<Zahtjev> listaZahtjeva) {
        this.listaZahtjeva = listaZahtjeva;
    }

    public int getLimitTabliceZahtjevi() {
        return limitTabliceZahtjevi;
    }
}
