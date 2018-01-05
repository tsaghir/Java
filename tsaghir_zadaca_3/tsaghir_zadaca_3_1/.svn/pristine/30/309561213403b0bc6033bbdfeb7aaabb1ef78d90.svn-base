package org.foi.nwtis.tsaghir.rest.serveri;

import java.io.StringReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.foi.nwtis.tsaghir.baza.DataBaseHelper;
import org.foi.nwtis.tsaghir.rest.klijenti.GMKlijent;
import org.foi.nwtis.tsaghir.web.podaci.Lokacija;
import org.foi.nwtis.tsaghir.web.podaci.Uredjaj;

/**
 * REST Web Service
 *
 * @author tsaghir
 */
@Path("/meteoREST")
public class MeteoRESTResourceContainer {

    @Context
    private UriInfo context;

    /**
     * Konstruktor
     */
    public MeteoRESTResourceContainer() {
    }

    /**
     * Preuzima reprezentanciju instance 
     * org.foi.nwtis.tsaghir.rest.serveri.MeteoRESTResourceContainer
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        List<Uredjaj> uredjaji = DataBaseHelper.dajSveUredaje();

        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Uredjaj uredjaj : uredjaji) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("uid", uredjaj.getId());
            job.add("naziv", uredjaj.getNaziv());
            job.add("lat", uredjaj.getGeoloc().getLatitude());
            job.add("lon", uredjaj.getGeoloc().getLongitude());

            jab.add(job);
        }
        return jab.build().toString();
    }
    

    /**
     * POST metoda za kreiranje instance MeteoRESTResource 
     *
     * @param content reprezentant za novi resurs 
     * @return an HTTP response sa sadržajem kreiranog resursa
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postJson(String content) {
        JsonReader reader = Json.createReader(new StringReader(content));
        JsonObject jo = reader.readObject();

        Lokacija lokacija = null;
        Uredjaj ured = new Uredjaj();
        ured.setNaziv(jo.getString("naziv"));
        ured.setAdresa(jo.getString("adresa"));
        if (jo.getString("lat").isEmpty() && jo.getString("lon").isEmpty()) {
            GMKlijent gmk = new GMKlijent();
            lokacija = gmk.getGeoLocation(ured.getAdresa());
        }
        ured.setGeoloc(lokacija);

        DataBaseHelper.spremiUredaj(ured);
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource lokator metoda za {id}
     */
    @Path("{id}")
    public MeteoRESTResource getMeteoRESTResource(@PathParam("id") String id) {
        return MeteoRESTResource.getInstance(id);
    }
}
