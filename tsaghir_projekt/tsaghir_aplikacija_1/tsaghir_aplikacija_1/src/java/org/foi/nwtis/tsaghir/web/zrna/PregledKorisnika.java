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
import org.foi.nwtis.tsaghir.web.podaci.Korisnik;

/**
 * zrno koje reprezentira pregledKorisnika.xhtml
 * @author tsaghir
 */
@ManagedBean
@SessionScoped
public class PregledKorisnika {

    private List<Korisnik> korisniciLista;
    private final Konfiguracija konf;
    private final int limitTabliceKorisnici;
    /**
     * konstruktor
     */
    public PregledKorisnika() {
        korisniciLista = DataBaseHelper.dajSveKorisnike();
        konf = (Konfiguracija) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("Konfiguracija");
        limitTabliceKorisnici = Integer.parseInt(konf.dajPostavku("limitTabliceKorisnici"));
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

    public List<Korisnik> getKorisniciLista() {
        return korisniciLista;
    }

    public void setKorisniciLista(List<Korisnik> korisniciLista) {
        this.korisniciLista = korisniciLista;
    }

    public int getLimitTabliceKorisnici() {
        return limitTabliceKorisnici;
    }
    
    
}
