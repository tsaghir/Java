/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.servisi.rest.serveri;

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.tsaghir.pomagaci.DataBaseHelper;
import org.foi.nwtis.tsaghir.web.podaci.Dnevnik;
import org.foi.nwtis.tsaghir.web.podaci.Korisnik;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Generalko
 */
public class KorisniciRESTResource {

    private final String korisnickoIme;

    /**
     * Creates a new instance of KorisniciResource
     */
    private KorisniciRESTResource(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    /**
     * Get instance of the KorisniciResource
     * @param korisnickoIme
     * @return 
     */
    public static KorisniciRESTResource getInstance(String korisnickoIme) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of KorisniciResource class.
        return new KorisniciRESTResource(korisnickoIme);
    }

    /**
     * Preuzima jednog korisnika iz baze podataka 
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String preuzmiKorisnika() {
        long pocetak = System.currentTimeMillis();
        String odgovor=null;
        
        Korisnik korisnik = DataBaseHelper.preuzmiKorisnika(korisnickoIme);
        if(korisnik != null){
            JSONObject korisnikJSON = new JSONObject();
            try {
                korisnikJSON.put("id", korisnik.getId());
                korisnikJSON.put("naziv", korisnik.getNaziv());
                korisnikJSON.put("lozinka", korisnik.getLozinka());
                odgovor = korisnikJSON.toString();
            } catch (JSONException ex) {
                Logger.getLogger(KorisniciRESTResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            long kraj = System.currentTimeMillis() - pocetak;
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("korisniciREST/{korisnickoIme}", "preuzmiKornika()", "tsaghir", 111, kraj));
            return odgovor;
        }
        odgovor = "";
        return odgovor;
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
     * PUT method for updating or creating an instance of KorisniciResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    /**
     * DELETE method for resource KorisniciResource
     */
    @DELETE
    public void delete() {
    }
}
