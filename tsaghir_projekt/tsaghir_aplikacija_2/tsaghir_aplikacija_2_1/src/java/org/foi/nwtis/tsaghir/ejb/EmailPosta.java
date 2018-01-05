/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.faces.context.FacesContext;
import org.foi.nwtis.tsaghir.ejb.dretve.ObradaPosteDretva;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;

/**
 * Singleton session bean koji služi za pokretanje dretve
 * @author tsaghir
 */
@Singleton
@LocalBean
public class EmailPosta{
    
    private ObradaPosteDretva obradaPosteDretva;
    private Konfiguracija konf;
    
    
    /**
     * Metoda koja pokreće dretvu za obradu email pošte
     * @param konfiguracija 
     */
    @PostConstruct
    public void pokreniDretvuZaObraduPoste(){
        konf = (Konfiguracija) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("Konfiguracija");
        obradaPosteDretva = new ObradaPosteDretva(konf);
        obradaPosteDretva.start();
    }
    
    /**
     * Metoda koja zaustavlja dretvu prije nego se bean obriše
     */
    @PreDestroy
    public void zaustaviDretvu(){
        obradaPosteDretva.interrupt();
    }
}
