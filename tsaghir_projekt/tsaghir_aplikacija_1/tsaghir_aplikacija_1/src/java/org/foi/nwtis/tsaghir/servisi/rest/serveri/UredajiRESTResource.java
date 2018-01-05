/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.servisi.rest.serveri;

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.tsaghir.pomagaci.DataBaseHelper;
import org.foi.nwtis.tsaghir.web.podaci.Dnevnik;
import org.foi.nwtis.tsaghir.web.podaci.Uredjaj;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 * REST Web Service
 *
 * @author tsaghir
 */
public class UredajiRESTResource {

    private String id;

    /**
     * Creates a new instance of UredajiRESTResource
     */
    private UredajiRESTResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the UredajiRESTResource
     *
     * @param id
     * @return
     */
    public static UredajiRESTResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of UredajiRESTResource class.
        return new UredajiRESTResource(id);
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.tsaghir.servisi.rest.serveri.UredajiRESTResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String preuzmiUredaj() {
        long pocetak = System.currentTimeMillis();
        String odgovor = null;

        Uredjaj uredaj = DataBaseHelper.preuzmiIotUredaj(Integer.parseInt(id));
        if (uredaj != null) {
            JSONObject uredajJSON = new JSONObject();
            try {
                uredajJSON.put("id", uredaj.getId());
                uredajJSON.put("naziv", uredaj.getNaziv());
                uredajJSON.put("adresa", uredaj.getAdresa());
                uredajJSON.put("latitude", uredaj.getGeoloc().getLatitude());
                uredajJSON.put("longitude", uredaj.getGeoloc().getLongitude());
                uredajJSON.put("status", uredaj.getStatus());
                uredajJSON.put("vrijemeKreiranja", uredaj.getVrijeme_kreiranja());
                uredajJSON.put("vrijemePromjene", uredaj.getVrijeme_promjene());
                odgovor = uredajJSON.toString();
            } catch (JSONException ex) {
                Logger.getLogger(KorisniciRESTResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            long kraj = System.currentTimeMillis() - pocetak;
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("uredajiREST/{id}", "preuzmiUredaj()", "tsaghir", 222, kraj));
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
     * PUT method for updating or creating an instance of UredajiRESTResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    /**
     * DELETE method for resource UredajiRESTResource
     */
    @DELETE
    public void delete() {
    }
}
