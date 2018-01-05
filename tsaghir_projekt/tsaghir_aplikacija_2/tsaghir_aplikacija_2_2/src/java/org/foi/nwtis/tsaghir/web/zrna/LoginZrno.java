/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.foi.nwtis.tsaghir.web.podaci.Korisnik;
import org.foi.nwtis.tsaghir.web.servisi.klijenti.RESTKlijent;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Zrno koje reprezentira login.xhtml
 *
 * @author tsaghir
 */
@Named(value = "loginStranica")
@SessionScoped
public class LoginZrno implements Serializable {

    private String korisnik;
    private String lozinka;
    private String lozinkaPonoviTekst;
    private String registrirajSe;
    private RESTKlijent.KorisniciREST korisniciREST;

    private boolean registracijaGumb = false;
    private boolean lozinkaPonovi = false;
    private boolean upozorenjeKorisnik = false;
    private boolean upozorenjeLozinka1 = false;
    private boolean upozorenjeLozinka2 = false;
    private boolean korisnikNePostoji = false;
    private boolean krivaLozinka = false;
    private boolean prekiniRegistracijuLink = false;
    private boolean uspjehLogin = false;

    /**
     * konstruktor
     */
    public LoginZrno() {
        korisniciREST = new RESTKlijent.KorisniciREST();
    }

    /**
     * Metoda pomoću koje se korisnik prijavljuje
     *
     * @return
     */
    public String prijavaKorisnika() {
        String korisnikRestOdgovor = korisniciREST.dajKorisnikaPoKorImenu(korisnik);
        Korisnik korisnik = null;
        if (!korisnikRestOdgovor.isEmpty()) {
            korisnik = napraviKorisnikaIzJSON(korisnikRestOdgovor);
            if (!korisnik.getNaziv().isEmpty()
                    && korisnik.getNaziv().equals(this.korisnik)
                    && !korisnik.getLozinka().isEmpty()
                    && korisnik.getLozinka().equals(lozinka)) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getSessionMap().remove("korisnik");
                context.getExternalContext().getSessionMap().put("korisnik", korisnik);
                context.getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml");

                postaviRenderNaPocetno();
            } else if (!korisnik.getLozinka().isEmpty()
                    && !korisnik.getLozinka().equals(lozinka)) {
                krivaLozinka = true;
            }
        } else {
            korisnikNePostoji = true;
        }
        return "";
    }

    /**
     * Metoda koja čita JSON sadržaj i radi korisnik objekt
     *
     * @param jsonSadrzaj
     * @return
     */
    private Korisnik napraviKorisnikaIzJSON(String jsonSadrzaj) {
        Korisnik korisnik = new Korisnik();
        try {
            JSONObject korisnikJSON = new JSONObject(jsonSadrzaj);
            korisnik.setId(korisnikJSON.getInt("id"));
            korisnik.setLozinka(korisnikJSON.getString("lozinka"));
            korisnik.setNaziv(korisnikJSON.getString("naziv"));
        } catch (JSONException ex) {
            Logger.getLogger(LoginZrno.class.getName()).log(Level.SEVERE, null, ex);
        }
        return korisnik;
    }

    /**
     * Metoda koja služi za registraciju korisnika kada se stisne na gumb
     *
     * @return
     */
    public String registracijaKorisnika() {
        String korisnikRestOdgovor = korisniciREST.dajKorisnikaPoKorImenu(korisnik);
        if (korisnikRestOdgovor.isEmpty()) {
            if (!lozinka.equals(lozinkaPonoviTekst)) {
                upozorenjeLozinka1 = true;
                upozorenjeLozinka2 = true;
                return "";
            }
            try {
                String odgovor = korisniciREST.dodajKorisnika(napraviKorisnikJSON());
                if (Integer.parseInt(odgovor) == 1) {
                    postaviRenderNaPocetno();
                    uspjehLogin = true;
                    korisnik = null;
                    lozinka = null;
                    lozinkaPonoviTekst = null;
                }

            } catch (JSONException ex) {
                Logger.getLogger(LoginZrno.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            upozorenjeKorisnik = true;
        }

        return "";
    }

    /**
     * Metoda koja radi JSON iz podataka korisnika
     *
     * @return
     * @throws JSONException
     */
    private String napraviKorisnikJSON() throws JSONException {
        JSONObject korisnikJSON = new JSONObject();
        korisnikJSON.put("naziv", korisnik);
        korisnikJSON.put("lozinka", lozinka);
        return korisnikJSON.toString();
    }

    /**
     * Metoda preko koje se otkrivaju elementi na stranici
     */
    public void aktivirajRegistraciju() {
        korisnikNePostoji = false;
        krivaLozinka = false;
        registracijaGumb = true;
        lozinkaPonovi = true;
        prekiniRegistracijuLink = true;
    }

    /**
     * Metoda preko koje se vraćaju svi elementi na početak
     */
    public void postaviRenderNaPocetno() {
        registracijaGumb = false;
        lozinkaPonovi = false;
        upozorenjeKorisnik = false;
        upozorenjeLozinka1 = false;
        upozorenjeLozinka2 = false;
        korisnikNePostoji = false;
        krivaLozinka = false;
        prekiniRegistracijuLink = false;
        uspjehLogin = false;
    }

    /**
     * GETTERS AND SETTERS
     *
     * @return
     */
    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getRegistrirajSe() {
        return registrirajSe;
    }

    public void setRegistrirajSe(String registrirajSe) {
        this.registrirajSe = registrirajSe;
    }

    public boolean isRegistracijaGumb() {
        return registracijaGumb;
    }

    public void setRegistracijaGumb(boolean registracijaGumb) {
        this.registracijaGumb = registracijaGumb;
    }

    public boolean isLozinkaPonovi() {
        return lozinkaPonovi;
    }

    public void setLozinkaPonovi(boolean lozinkaPonovi) {
        this.lozinkaPonovi = lozinkaPonovi;
    }

    public String getLozinkaPonoviTekst() {
        return lozinkaPonoviTekst;
    }

    public void setLozinkaPonoviTekst(String lozinkaPonoviTekst) {
        this.lozinkaPonoviTekst = lozinkaPonoviTekst;
    }

    public boolean isUpozorenjeKorisnik() {
        return upozorenjeKorisnik;
    }

    public void setUpozorenjeKorisnik(boolean upozorenjeKorisnik) {
        this.upozorenjeKorisnik = upozorenjeKorisnik;
    }

    public boolean isUpozorenjeLozinka1() {
        return upozorenjeLozinka1;
    }

    public void setUpozorenjeLozinka1(boolean upozorenjeLozinka1) {
        this.upozorenjeLozinka1 = upozorenjeLozinka1;
    }

    public boolean isUpozorenjeLozinka2() {
        return upozorenjeLozinka2;
    }

    public void setUpozorenjeLozinka2(boolean upozorenjeLozinka2) {
        this.upozorenjeLozinka2 = upozorenjeLozinka2;
    }

    public boolean isKorisnikNePostoji() {
        return korisnikNePostoji;
    }

    public void setKorisnikNePostoji(boolean korisnikNePostoji) {
        this.korisnikNePostoji = korisnikNePostoji;
    }

    public boolean isKrivaLozinka() {
        return krivaLozinka;
    }

    public void setKrivaLozinka(boolean krivaLozinka) {
        this.krivaLozinka = krivaLozinka;
    }

    public boolean isPrekiniRegistracijuLink() {
        return prekiniRegistracijuLink;
    }

    public void setPrekiniRegistracijuLink(boolean prekiniRegistracijuLink) {
        this.prekiniRegistracijuLink = prekiniRegistracijuLink;
    }

    public boolean isUspjehLogin() {
        return uspjehLogin;
    }

    public void setUspjehLogin(boolean uspjehLogin) {
        this.uspjehLogin = uspjehLogin;
    }

}
