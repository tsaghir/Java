/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.io.Serializable;

/**
 * Pomoćna klasa koja sprema adrese korisnika koje želi provjeriti da li su valjane
 * @author tsaghir
 */
public class AdresaEntitet implements Serializable{
    private String adresa;
    private boolean status;

    public AdresaEntitet(String adresa, boolean status) {
        this.adresa = adresa;
        this.status = status;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
