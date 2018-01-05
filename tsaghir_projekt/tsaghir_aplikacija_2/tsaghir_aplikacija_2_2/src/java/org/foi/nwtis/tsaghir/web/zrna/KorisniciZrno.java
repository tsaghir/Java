/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.foi.nwtis.tsaghir.LoginFilter;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.web.podaci.Korisnik;
import org.foi.nwtis.tsaghir.web.servisi.klijenti.RESTKlijent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * zrno koje reprezentira korisnici.xhtml
 *
 * @author tsaghir
 */
@Named(value = "korisniciZrno")
@SessionScoped
public class KorisniciZrno implements Serializable {

    private final Konfiguracija konf;
    private final RESTKlijent.KorisniciREST korisniciREST;
    private List<Korisnik> listaKorisnika;
    private final int limitTabliceKorisnici;

    private String korisnikId;
    private String lozinka;
    private String korisnik;
    private boolean prikaziTablicuKorisnika = false;
    private boolean prikaziAzurirajPodatke = false;
    private boolean azuriranjeUspjeh = false;

    /**
     * konstruktor
     */
    public KorisniciZrno() {
        konf = (Konfiguracija) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("Konfiguracija");
        limitTabliceKorisnici = Integer.parseInt(konf.dajPostavku("limitTabliceKorisnici"));
        korisniciREST = new RESTKlijent.KorisniciREST();
    }

    /**
     * Metoda koja dohvaća listu korisnika preko REST metode
     */
    public void dohvatiKorisnike() {
        List<Korisnik> korisniciLista = new ArrayList<>();
        try {
            prikaziTablicuKorisnika = true;
            prikaziAzurirajPodatke = false;
            azuriranjeUspjeh = false;
            String odgovor = korisniciREST.preuzmiKorisnike();
            JSONObject korisniciJSON = new JSONObject(odgovor);
            JSONArray korisniciArray = korisniciJSON.getJSONArray("KorisnikLista");
            for (int i = 0; i < korisniciArray.length(); i++) {
                JSONObject korisnikJSON = korisniciArray.getJSONObject(i);
                Korisnik korisnikObjekt = new Korisnik();
                korisnikObjekt.setId(korisnikJSON.getInt("id"));
                korisnikObjekt.setNaziv(korisnikJSON.getString("naziv"));
                korisnikObjekt.setLozinka(korisnikJSON.getString("lozinka"));
                korisniciLista.add(korisnikObjekt);
            }
        } catch (JSONException ex) {
            Logger.getLogger(KorisniciZrno.class.getName()).log(Level.SEVERE, null, ex);
        }
        listaKorisnika = korisniciLista;
    }

    public String azurirajPodatke() {
        prikaziTablicuKorisnika = false;
        prikaziAzurirajPodatke = true;
        azuriranjeUspjeh = false;
        Korisnik korisnik = (Korisnik) LoginFilter.sesija.getAttribute("korisnik");
        korisnikId = String.valueOf(korisnik.getId());
        this.korisnik = korisnik.getNaziv();
        this.lozinka = korisnik.getLozinka();
        return "";
    }

    public String potvrdiAzuriranje() {
        try {
            String odgovor = korisniciREST.azurirajKorisnika(napraviKorisnikObjekt());
            if (Integer.parseInt(odgovor) == 1) {
                azuriranjeUspjeh = true;
            }
        } catch (JSONException ex) {
            Logger.getLogger(KorisniciZrno.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private String napraviKorisnikObjekt() throws JSONException {
        JSONObject korisnikJSON = new JSONObject();
        korisnikJSON.put("id", korisnikId);
        korisnikJSON.put("lozinka", lozinka);
        korisnikJSON.put("naziv", korisnik);
        return korisnikJSON.toString();
    }

    public List<Korisnik> getListaKorisnika() {
        return listaKorisnika;
    }

    public void setListaKorisnika(List<Korisnik> listaKorisnika) {
        this.listaKorisnika = listaKorisnika;
    }

    public int getLimitTabliceKorisnici() {
        return limitTabliceKorisnici;
    }

    public boolean isPrikaziTablicuKorisnika() {
        return prikaziTablicuKorisnika;
    }

    public void setPrikaziTablicuKorisnika(boolean prikaziTablicuKorisnika) {
        this.prikaziTablicuKorisnika = prikaziTablicuKorisnika;
    }

    public boolean isPrikaziAzurirajPodatke() {
        return prikaziAzurirajPodatke;
    }

    public void setPrikaziAzurirajPodatke(boolean prikaziAzurirajPodatke) {
        this.prikaziAzurirajPodatke = prikaziAzurirajPodatke;
    }

    public String getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(String korisnikId) {
        this.korisnikId = korisnikId;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public boolean isAzuriranjeUspjeh() {
        return azuriranjeUspjeh;
    }

    public void setAzuriranjeUspjeh(boolean azuriranjeUspjeh) {
        this.azuriranjeUspjeh = azuriranjeUspjeh;
    }

    
}
