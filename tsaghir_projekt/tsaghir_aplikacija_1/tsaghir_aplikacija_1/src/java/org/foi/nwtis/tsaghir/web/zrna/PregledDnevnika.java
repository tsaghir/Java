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
import org.foi.nwtis.tsaghir.web.podaci.Dnevnik;

/**
 * zrno koje reprezentira pregledDnevnika.xhtml
 *
 * @author tsaghir
 */
@ManagedBean
@SessionScoped
public class PregledDnevnika {

    private List<Dnevnik> listaDnevnika;
    private final Konfiguracija konf;
    private final int limitTabliceDnevnika;

    /**
     * konstruktor
     */
    public PregledDnevnika() {
        listaDnevnika = DataBaseHelper.dajSveZapiseDnevnika();
        konf = (Konfiguracija) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("Konfiguracija");
        limitTabliceDnevnika = Integer.parseInt(konf.dajPostavku("limitTabliceDnevnika"));
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

    public List<Dnevnik> getListaDnevnika() {
        return listaDnevnika;
    }

    public void setListaDnevnika(List<Dnevnik> listaDnevnika) {
        this.listaDnevnika = listaDnevnika;
    }

    public int getLimitTabliceDnevnika() {
        return limitTabliceDnevnika;
    }
    
    

}
