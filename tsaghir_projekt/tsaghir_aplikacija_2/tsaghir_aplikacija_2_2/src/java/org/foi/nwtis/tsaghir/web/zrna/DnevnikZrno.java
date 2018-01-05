/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import org.foi.nwtis.tsaghir.ejb.Dnevnik;
import org.foi.nwtis.tsaghir.ejb.sb.DnevnikFacade;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 *
 * @author tsaghir
 */
@Named(value = "dnevnikZrno")
@SessionScoped
public class DnevnikZrno implements Serializable{

    @EJB
    private DnevnikFacade dnevnikFacade;
    
    private final Konfiguracija konf;
    private final int limitTabliceDnevnika;
    private List<Dnevnik> dnevnikLista;
    
    /**
     * konstruktor
     */
    public DnevnikZrno() {
        konf = (Konfiguracija) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("Konfiguracija");
        limitTabliceDnevnika = Integer.parseInt(konf.dajPostavku("limitTabliceDnevnika"));
    }

    public int getLimitTabliceDnevnika() {
        return limitTabliceDnevnika;
    }

    public List<Dnevnik> getDnevnikLista() {
        if(dnevnikLista == null){
            dnevnikLista = new ArrayList<>();
            dnevnikLista = dnevnikFacade.findAll();
        }
        return dnevnikLista;
    }

    public void setDnevnikLista(List<Dnevnik> dnevnikLista) {
        this.dnevnikLista = dnevnikLista;
    }
}
