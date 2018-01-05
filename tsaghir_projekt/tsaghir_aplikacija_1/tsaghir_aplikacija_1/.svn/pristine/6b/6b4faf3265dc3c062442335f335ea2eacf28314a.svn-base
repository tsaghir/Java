/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.servisi.rest.serveri;

import com.google.gson.Gson;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.tsaghir.pomagaci.DataBaseHelper;
import org.foi.nwtis.tsaghir.web.podaci.Dnevnik;
import org.foi.nwtis.tsaghir.web.podaci.Korisnik;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 * REST Web Service
 *
 * @author tsaghir
 */
@Path("/korisniciREST")
public class KorisniciRESTContainerResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of KorisniciContainerResource
     */
    public KorisniciRESTContainerResource() {
    }

    /**
     * Preuzima sve korisnike iza baze podataka
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String preuzmiKorisnike() {
        long pocetak = System.currentTimeMillis();
        List<Korisnik> listaKorisnika = DataBaseHelper.dajSveKorisnike();
        JSONObject jObject = null;
        try {
            jObject = new JSONObject();
            JSONArray jArray = new JSONArray();
            for (Korisnik korisnik : listaKorisnika) {

                JSONObject korisnikJSON = new JSONObject();
                korisnikJSON.put("id", korisnik.getId());
                korisnikJSON.put("naziv", korisnik.getNaziv());
                korisnikJSON.put("lozinka", korisnik.getLozinka());
                jArray.put(korisnikJSON);
            }
            jObject.put("KorisnikLista", jArray);
        } catch (JSONException ex) {
            Logger.getLogger(KorisniciRESTContainerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        long kraj = System.currentTimeMillis() - pocetak;
        DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("REST", "korisniciREST/preuzmiKorisnike();", "tsaghir", 11, kraj));
        return jObject.toString();
    }

    private Dnevnik napraviDnevnikObjekt(String ipAdresa, String komanda, String korisnik, int status, long trajanje) {
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setIpadresa(ipAdresa);
        dnevnik.setKomanda(komanda);
        dnevnik.setKorisnik(korisnik);
        dnevnik.setStatus(status);
        dnevnik.setTrajanje(trajanje);
        dnevnik.setVrijeme(new Timestamp(System.currentTimeMillis()));
        return dnevnik;
    }

    /**
     * Dodaje novog korisnika u bazu podataka
     *
     * @param sadrzaj
     * @return an instance of java.lang.String
     */
    @POST
    @Path("/dodaj")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String dodajKorisnika(String sadrzaj) {
        long pocetak = System.currentTimeMillis();
        String rezultat;
        Korisnik korisnik = null;
        try {
            JSONObject korisnikJSON = new JSONObject(sadrzaj);
            korisnik = dohvatiJSON(korisnikJSON);
        } catch (JSONException ex) {
            Logger.getLogger(KorisniciRESTContainerResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (DataBaseHelper.dodajNovogKorisnika(korisnik)) {
            rezultat = "1";
            long kraj = System.currentTimeMillis() - pocetak;
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("korisniciREST", "/dodajKorisnika(String sadrzaj);", "tsaghir", 12, kraj));
            return rezultat;

        }
        rezultat = "0";
        long kraj = System.currentTimeMillis() - pocetak;
        DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("korisniciREST", "/dodajKorisnika(String sadrzaj);", "tsaghir", 12, kraj));
        return rezultat;
    }

    private Korisnik dohvatiJSON(JSONObject korisnikJSON) {
        Korisnik korisnik = new Korisnik();
        try {
            korisnik.setNaziv(korisnikJSON.getString("naziv"));
            korisnik.setLozinka(korisnikJSON.getString("lozinka"));
        } catch (JSONException ex) {
            Logger.getLogger(KorisniciRESTContainerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return korisnik;
    }

    /**
     * Ažurira postojećeg korisnika u bazi podataka
     *
     * @param sadrzaj
     * @return an instance of java.lang.String
     */
    @POST
    @Path("/azuriraj")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String azurirajKorisnika(String sadrzaj) {
        long pocetak = System.currentTimeMillis();
        String rezultat;
        Korisnik korisnik = null;
        try {
            JSONObject korisnikJSON = new JSONObject(sadrzaj);
            korisnik = napraviKorisnikObjekt(korisnikJSON);
        } catch (JSONException ex) {
            Logger.getLogger(KorisniciRESTContainerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (korisnik != null) {
            if (DataBaseHelper.azurirajKorisnika(korisnik)) {
                rezultat = "1";
                long kraj = System.currentTimeMillis() - pocetak;
                DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("korisniciREST", "/azurirajKorisnika(String sadrzaj);", "tsaghir", 13, kraj));
                return rezultat;
            }
            rezultat = "0";
            long kraj = System.currentTimeMillis() - pocetak;
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("korisniciREST", "/azurirajKorisnika(String sadrzaj);", "tsaghir", 13, kraj));
            return rezultat;
        }
        rezultat = "0";
        return rezultat;
    }

    private Korisnik napraviKorisnikObjekt(JSONObject korisnikJSON) {
        Korisnik korisnik = new Korisnik();
        try {
            korisnik.setId(korisnikJSON.getInt("id"));
            korisnik.setNaziv(korisnikJSON.getString("naziv"));
            korisnik.setLozinka(korisnikJSON.getString("lozinka"));
        } catch (JSONException ex) {
            Logger.getLogger(KorisniciRESTContainerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return korisnik;
    }

    /**
     * Sub-resource locator method for {name}
     *
     * @param name
     * @return
     */
    @Path("{korisnickoIme}")
    public KorisniciRESTResource getKorisniciResource(@PathParam("korisnickoIme") String name) {
        return KorisniciRESTResource.getInstance(name);
    }
}
