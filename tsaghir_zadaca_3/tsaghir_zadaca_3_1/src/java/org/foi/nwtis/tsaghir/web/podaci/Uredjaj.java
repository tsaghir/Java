package org.foi.nwtis.tsaghir.web.podaci;

/**
 * Klasa koja reprezentira postavke za Uređaj
 * @author tsaghir
 */
public class Uredjaj {
    private int id;
    private String naziv;
    private Lokacija geoloc;
    private String adresa;

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
    
}
