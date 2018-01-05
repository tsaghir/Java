/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Serijalizirana klasa koja služi za evidenciju dretvi
 * @author tsaghir
 */
public class Evidencija implements Serializable {
    private int ukupnoZahtjeva = 0;
    private int brojUspjesnihZahtjeva = 0;
    private int brojPrekinutihZahtjeva = 0;
    private ArrayList<AdresaEntitet> zahtjeviZaAdrese = new ArrayList();
    private int zadnjiBrojRadneDretve = 0;
    private long ukupnoVrijemeRadaRadnihDretvi = 0;
    
    public Evidencija(){
        
    }

    public int getUkupnoZahtjeva() {
        return ukupnoZahtjeva;
    }

    public void setUkupnoZahtjeva(int ukupnoZahtjeva) {
        this.ukupnoZahtjeva = ukupnoZahtjeva;
    }

    public int getBrojUspjesnihZahtjeva() {
        return brojUspjesnihZahtjeva;
    }

    public void setBrojUspjesnihZahtjeva(int brojUspjesnihZahtjeva) {
        this.brojUspjesnihZahtjeva = brojUspjesnihZahtjeva;
    }

    public int getBrojPrekinutihZahtjeva() {
        return brojPrekinutihZahtjeva;
    }

    public void setBrojPrekinutihZahtjeva(int brojPrekinutihZahtjeva) {
        this.brojPrekinutihZahtjeva = brojPrekinutihZahtjeva;
    }

    public ArrayList<AdresaEntitet> getZahtjeviZaAdrese() {
        return zahtjeviZaAdrese;
    }

    public void setZahtjeviZaAdrese(ArrayList<AdresaEntitet> adresa) {
        this.zahtjeviZaAdrese=adresa;
    }

    public int getZadnjiBrojRadneDretve() {
        return zadnjiBrojRadneDretve;
    }

    public void setZadnjiBrojRadneDretve(int zadnjiBrojRadneDretve) {
        this.zadnjiBrojRadneDretve = zadnjiBrojRadneDretve;
    }

    public long getUkupnoVrijemeRadaRadnihDretvi() {
        return ukupnoVrijemeRadaRadnihDretvi;
    }

    public void setUkupnoVrijemeRadaRadnihDretvi(long ukupnoVrijemeRadaRadnihDretvi) {
        this.ukupnoVrijemeRadaRadnihDretvi = ukupnoVrijemeRadaRadnihDretvi;
    }
}
