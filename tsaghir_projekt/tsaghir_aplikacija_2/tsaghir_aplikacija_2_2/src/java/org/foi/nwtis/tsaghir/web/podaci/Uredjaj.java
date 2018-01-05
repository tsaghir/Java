package org.foi.nwtis.tsaghir.web.podaci;

import java.sql.Timestamp;

/**
 * Klasa koja reprezentira postavke za Uređaj
 * @author tsaghir
 */
public class Uredjaj {
    private int id;
    private String naziv;
    private Lokacija geoloc;
    private String adresa;
    private int status;
    private Timestamp vrijeme_promjene;
    private Timestamp vrijeme_kreiranja;

    public Uredjaj() {
    }

    public Uredjaj(int id, String naziv, Lokacija geoloc) {
        this.id = id;
        this.naziv = naziv;
        this.geoloc = geoloc;
    }

    public Lokacija getGeoloc() {
        return geoloc;
    }

    public void setGeoloc(Lokacija geoloc) {
        this.geoloc = geoloc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getVrijeme_promjene() {
        return vrijeme_promjene;
    }

    public void setVrijeme_promjene(Timestamp vrijeme_promjene) {
        this.vrijeme_promjene = vrijeme_promjene;
    }

    public Timestamp getVrijeme_kreiranja() {
        return vrijeme_kreiranja;
    }

    public void setVrijeme_kreiranja(Timestamp vrijeme_kreiranja) {
        this.vrijeme_kreiranja = vrijeme_kreiranja;
    }
    
    
    
}
