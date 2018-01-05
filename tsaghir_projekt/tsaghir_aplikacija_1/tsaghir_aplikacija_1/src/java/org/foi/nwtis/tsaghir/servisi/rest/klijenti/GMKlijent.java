/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.servisi.rest.klijenti;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.tsaghir.web.podaci.Lokacija;

/**
 *
 * @author nwtis_1
 */
public class GMKlijent {

    GMRESTHelper helper;
    Client client;

    public GMKlijent() {
        client = ClientBuilder.newClient();
    }

    public Lokacija getGeoLocation(String adresa) {
        try {
            WebTarget webResource = client.target(GMRESTHelper.getGM_BASE_URI())
                    .path("maps/api/geocode/json");
            webResource = webResource.queryParam("address",
                    URLEncoder.encode(adresa, "UTF-8"));
            webResource = webResource.queryParam("sensor", "false");

            String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

            JsonReader reader = Json.createReader(new StringReader(odgovor));

            JsonObject jo = reader.readObject();

            JsonObject obj = jo.getJsonArray("results")
                    .getJsonObject(0)
                    .getJsonObject("geometry")
                    .getJsonObject("location");

            Lokacija loc = new Lokacija(obj.getJsonNumber("lat").toString(), obj.getJsonNumber("lng").toString());

            return loc;

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getAdress(String latitude, String longitude) {
        String parametri = String.format("%s,%s",latitude,longitude);
        WebTarget webResource = client.target(GMRESTHelper.getGM_BASE_URI())
                .path("maps/api/geocode/json");
        try {
            webResource = webResource.queryParam("latlng",
                    URLEncoder.encode(parametri, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        webResource = webResource.queryParam("sensor", "false");

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);

        JsonReader reader = Json.createReader(new StringReader(odgovor));

        JsonObject jo = reader.readObject();

        JsonObject obj = jo.getJsonArray("results").getJsonObject(0);
                
        return obj.getString("formatted_address");
    }
}