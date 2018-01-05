package org.foi.nwtis.tsaghir.rest.klijenti;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.tsaghir.web.podaci.MeteoPodaci;
import org.foi.nwtis.tsaghir.web.podaci.MeteoPrognoza;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author tsaghir
 */
public class OWMKlijent {

    String apiKey;
    OWMRESTHelper helper;
    Client client;
    List<MeteoPrognoza> meteoPrognozaList = null;

    public OWMKlijent(String apiKey) {
        this.apiKey = apiKey;
        helper = new OWMRESTHelper(apiKey);
        client = ClientBuilder.newClient();
        meteoPrognozaList = new ArrayList<>();
    }

    /**
     * Metoda za dohvaćanje vremena za određnu poziciju
     * @param latitude
     * @param longitude
     * @return 
     */
    public MeteoPodaci getRealTimeWeather(String latitude, String longitude) {

        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Current_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        try {
            JsonReader reader = Json.createReader(new StringReader(odgovor));

            JsonObject jo = reader.readObject();

            MeteoPodaci mp = new MeteoPodaci();
            mp.setSunRise(new Date(jo.getJsonObject("sys").getJsonNumber("sunrise").bigDecimalValue().longValue() * 1000));
            mp.setSunSet(new Date(jo.getJsonObject("sys").getJsonNumber("sunset").bigDecimalValue().longValue() * 1000));

            mp.setTemperatureValue(new Double(jo.getJsonObject("main").getJsonNumber("temp").doubleValue()).floatValue());
            mp.setTemperatureMin(new Double(jo.getJsonObject("main").getJsonNumber("temp_min").doubleValue()).floatValue());
            mp.setTemperatureMax(new Double(jo.getJsonObject("main").getJsonNumber("temp_max").doubleValue()).floatValue());
            mp.setTemperatureUnit("celsius");

            mp.setHumidityValue(new Double(jo.getJsonObject("main").getJsonNumber("humidity").doubleValue()).floatValue());
            mp.setHumidityUnit("%");

            mp.setPressureValue(new Double(jo.getJsonObject("main").getJsonNumber("pressure").doubleValue()).floatValue());
            mp.setPressureUnit("hPa");

            mp.setWindSpeedValue(new Double(jo.getJsonObject("wind").getJsonNumber("speed").doubleValue()).floatValue());
            mp.setWindSpeedName("");

            mp.setWindDirectionValue(new Double(jo.getJsonObject("wind").getJsonNumber("deg").doubleValue()).floatValue());
            mp.setWindDirectionCode("");
            mp.setWindDirectionName("");

            mp.setCloudsValue(jo.getJsonObject("clouds").getInt("all"));
            mp.setCloudsName(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setPrecipitationMode("");

            mp.setWeatherNumber(jo.getJsonArray("weather").getJsonObject(0).getInt("id"));
            mp.setWeatherValue(jo.getJsonArray("weather").getJsonObject(0).getString("description"));
            mp.setWeatherIcon(jo.getJsonArray("weather").getJsonObject(0).getString("icon"));

            mp.setLastUpdate(new Date(jo.getJsonNumber("dt").bigDecimalValue().longValue() * 1000));
            return mp;

        } catch (Exception ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda za dohvaćanje vremena 5 dana u tjednu svaka 3 sata za određenu poziciju
     * @param latitude
     * @param longitude
     * @param brojac
     * @return 
     */
    public List<MeteoPrognoza> getWeatherForecast(String latitude, String longitude, int brojac) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Forecast_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        try {
            MeteoPodaci mp = new MeteoPodaci();
            JSONObject jo = new JSONObject(odgovor);
            JSONArray jaList = jo.getJSONArray("list");
            String grad = jo.getJSONObject("city").getString("name");
            for (int i = 0; i < jaList.length(); i++) {
                JSONObject jsonListObject = jaList.getJSONObject(i);
                JSONObject jsonMainObject = jsonListObject.getJSONObject("main");
                mp.setTemperatureValue(new Double(jsonMainObject.getDouble("temp")).floatValue());
                mp.setTemperatureMin(new Double(jsonMainObject.getDouble("temp_min")).floatValue());
                mp.setTemperatureMax(new Double(jsonMainObject.getDouble("temp_max")).floatValue());
                mp.setTemperatureUnit("celsius");

                mp.setPressureValue(new Double(jsonMainObject.getDouble("pressure")).floatValue());
                mp.setPressureUnit("hPa");

                mp.setHumidityValue(new Double(jsonMainObject.getDouble("humidity")).floatValue());
                mp.setHumidityUnit("%");

                JSONObject jsonWeatherObject = (JSONObject) jsonListObject.getJSONArray("weather").get(0);
                mp.setWeatherValue(jsonWeatherObject.getString("description"));

                mp.setCloudsValue(jsonListObject.getJSONObject("clouds").getInt("all"));
                mp.setWindSpeedValue(new Double(jsonListObject.getJSONObject("wind").getDouble("speed")).floatValue());
                mp.setWindDirectionValue(new Double(jsonListObject.getJSONObject("wind").getDouble("deg")).floatValue());
                String id = String.valueOf(brojac)+"."+String.valueOf(i);
                meteoPrognozaList.add(new MeteoPrognoza(id, jsonListObject.getString("dt_txt"), grad, mp));
            }
        } catch (JSONException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(odgovor);
        return meteoPrognozaList;
    }
}
