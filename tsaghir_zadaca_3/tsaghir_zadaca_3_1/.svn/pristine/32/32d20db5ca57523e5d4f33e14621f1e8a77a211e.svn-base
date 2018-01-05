package org.foi.nwtis.tsaghir.rest.serveri;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.tsaghir.baza.DataBaseHelper;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.rest.klijenti.OWMKlijent;
import org.foi.nwtis.tsaghir.web.podaci.MeteoPodaci;
import org.foi.nwtis.tsaghir.web.podaci.Uredjaj;
import org.foi.nwtis.tsaghir.web.slusaci.SlusacAplikacije;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author tsaghir
 */
public class MeteoRESTResource {

    private String id;

    /**
     * Konstruktor
     */
    private MeteoRESTResource(String id) {
        this.id = id;
    }

    /**
     * Daj instancu MeteoRESTResource
     */
    public static MeteoRESTResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of MeteoRESTResource class.
        return new MeteoRESTResource(id);
    }

    /**
     * Preuzima reprezentaciju
     * org.foi.nwtis.tsaghir.rest.serveri.MeteoRESTResource
     *
     * @return an instanca of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        
        Konfiguracija konf = SlusacAplikacije.getKONF();
        List<Uredjaj> uredjaji = DataBaseHelper.dajSveUredaje();
        OWMKlijent owmk = new OWMKlijent(konf.dajPostavku("apikey"));
        JSONArray jArray = new JSONArray();

        for (Uredjaj uredjaj : uredjaji) {
            if (uredjaj.getId() == Integer.parseInt(this.id)) {
                MeteoPodaci mp = owmk.getRealTimeWeather(uredjaj.getGeoloc().getLatitude(), uredjaj.getGeoloc().getLongitude());
                try {
                    JSONObject job = new JSONObject();
                    job.put("vrijemeopis", mp.getWeatherValue());
                    job.put("temp", mp.getTemperatureValue());
                    job.put("tempmin", mp.getTemperatureMin());
                    job.put("tempmax", mp.getTemperatureMax());
                    job.put("vlaga", mp.getHumidityValue());
                    job.put("tlak", mp.getPressureValue());
                    job.put("vjetar", mp.getWindSpeedValue());
                    job.put("preuzeto", mp.getLastUpdate().toString());
                    jArray.put(job);
                } catch (JSONException ex) {
                    Logger.getLogger(MeteoRESTResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return jArray.toString();
    }

    /**
     * PUT metoda za ažuriranje ili kreiranje instance MeteoRESTResource
     *
     * @param content reprezentacija resursa
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    /**
     * DELETE metoda za resurs MeteoRESTResource
     */
    @DELETE
    public void delete() {
    }
}
