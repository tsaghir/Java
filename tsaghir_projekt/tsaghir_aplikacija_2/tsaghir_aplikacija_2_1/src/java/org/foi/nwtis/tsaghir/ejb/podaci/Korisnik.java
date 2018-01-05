package org.foi.nwtis.tsaghir.ejb.podaci;

/**
 * Klasa na koju se mapira korisnik iz baze podataka
 * @author tsaghir
 */
public class Korisnik {
   private int id;
   private String naziv;
   private String lozinka;

    public Korisnik() {
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

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
   
   
}
