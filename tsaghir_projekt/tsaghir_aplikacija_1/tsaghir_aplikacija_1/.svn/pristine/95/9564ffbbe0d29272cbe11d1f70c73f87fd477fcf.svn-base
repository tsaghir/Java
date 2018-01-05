/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.servisi.rest.serveri;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.tsaghir.pomagaci.DataBaseHelper;
import org.foi.nwtis.tsaghir.web.podaci.Dnevnik;
import org.foi.nwtis.tsaghir.web.podaci.Lokacija;
import org.foi.nwtis.tsaghir.web.podaci.Uredjaj;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 * REST Web Service
 *
 * @author tsaghir
 */
@Path("/uredajiREST")
public class UredajiRESTsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UredajiRESTsResource
     */
    public UredajiRESTsResource() {
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.tsaghir.servisi.rest.serveri.UredajiRESTsResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String preuzmiUredaje() {
        long pocetak = System.currentTimeMillis();
        List<Uredjaj> listaUredaja = DataBaseHelper.dajSveUredaje();
        JSONObject jObject = null;
        try {
            jObject = new JSONObject();
            JSONArray jArray = new JSONArray();
            for (Uredjaj uredaj : listaUredaja) {
                JSONObject uredajJSON = new JSONObject();
                uredajJSON.put("id", uredaj.getId());
                uredajJSON.put("naziv", uredaj.getNaziv());
                uredajJSON.put("adresa", uredaj.getAdresa());
                uredajJSON.put("latitude", uredaj.getGeoloc().getLatitude());
                uredajJSON.put("longitude", uredaj.getGeoloc().getLongitude());
                uredajJSON.put("status", uredaj.getStatus());
                uredajJSON.put("vrijemeKreiranja", uredaj.getVrijeme_kreiranja());
                uredajJSON.put("vrijemePromjene", uredaj.getVrijeme_promjene());
                jArray.put(uredajJSON);
            }
            jObject.put("UredajiLista", jArray);
        } catch (JSONException ex) {
            Logger.getLogger(KorisniciRESTContainerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        long kraj = System.currentTimeMillis() - pocetak;
        DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("REST", "uredajiREST/preuzmiUredaje();", "tsaghir", 21, kraj));
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
     * Retrieves representation of an instance of
     * org.foi.nwtis.tsaghir.servisi.rest.serveri.UredajiRESTContainerResource
     *
     * @param sadrzaj
     * @return an instance of java.lang.String
     */
    @POST
    @Path("/dodaj")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String dodajUredaj(String sadrzaj) {
        long pocetak = System.currentTimeMillis();
        String rezultat;
        Uredjaj uredaj = null;
        try {
            JSONObject uredajJSON = new JSONObject(sadrzaj);
            uredaj = napraviUredajObjekt(uredajJSON);
        } catch (JSONException | ParseException ex) {
            Logger.getLogger(UredajiRESTsResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (DataBaseHelper.dodajNoviIotUredaj(uredaj)) {
            rezultat = "1";
            long kraj = System.currentTimeMillis() - pocetak;
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("uredajiREST", "/dodajUredaj(String sadrzaj);", "tsaghir", 22, kraj));
            return rezultat;

        }
        rezultat = "0";
        long kraj = System.currentTimeMillis() - pocetak;
        DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("uredajiREST", "/dodajUredaj(String sadrzaj);", "tsaghir", 22, kraj));
        return rezultat;
    }

    private Uredjaj napraviUredajObjekt(JSONObject uredajJSON) throws ParseException {
        Uredjaj uredaj = new Uredjaj();
        try {
            uredaj.setId(uredajJSON.getInt("id"));
            uredaj.setNaziv(uredajJSON.getString("naziv"));
            uredaj.setGeoloc(new Lokacija(uredajJSON.getString("latitude"), uredajJSON.getString("longitude")));
            uredaj.setStatus(uredajJSON.getInt("status"));
            uredaj.setVrijeme_kreiranja(new Timestamp(Long.parseLong(uredajJSON.getString("vrijemeKreiranja"))));
            uredaj.setVrijeme_promjene(new Timestamp(Long.parseLong(uredajJSON.getString("vrijemePromjene"))));
        } catch (JSONException ex) {
            Logger.getLogger(UredajiRESTsResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uredaj;
    }
    
    /**
     * POST method for creating an instance of UredajiRESTResource
     *
     * @param sadrzaj
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Path("/azuriraj")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String azurirajUredaj(String sadrzaj) {
        long pocetak = System.currentTimeMillis();
        String rezultat;
        Uredjaj uredaj = null;
        try {
            JSONObject uredajJSON = new JSONObject(sadrzaj);
            uredaj = napraviUredajObjekt(uredajJSON);
        } catch (JSONException | ParseException ex) {
            Logger.getLogger(UredajiRESTsResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (uredaj != null) {
            if (DataBaseHelper.azurirajIotUredaj(uredaj)) {
                rezultat = "1";
                long kraj = System.currentTimeMillis() - pocetak;
                DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("uredajiREST", "/azurirajUredaj(String sadrzaj);", "tsaghir", 23, kraj));
                return rezultat;
            }
            rezultat = "0";
            long kraj = System.currentTimeMillis() - pocetak;
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("uredajiREST", "/azurirajUredaj(String sadrzaj);", "tsaghir", 23, kraj));
            return rezultat;

        }
        rezultat = "0";
        return rezultat;
    }

    /**
     * Sub-resource locator method for {id}
     *
     * @param id
     * @return
     */
    @Path("{id}")
    public UredajiRESTResource getUredajiRESTResource(@PathParam("id") String id) {
        return UredajiRESTResource.getInstance(id);
    }
}
