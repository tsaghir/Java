package org.foi.nwtis.tsaghir.ws.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Klasa koja služi kao klijent za REST web servis
 * @author tsaghir
 */
public class MeteoREST {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8084/tsaghir_zadaca_3_1/webresources";

    /**
     * Konstruktor
     */
    public MeteoREST() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("meteoREST");
    }

    /**
     * Metoda koja šalje JSON string  na servis te se sprema u bazu podataka
     * @param requestEntity
     * @return
     * @throws ClientErrorException 
     */
    public Response postJson(Object requestEntity) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    /**
     * Metoda koja dohvaća JSON string odabranog uređaja u listi
     * @return
     * @throws ClientErrorException 
     */
    public String getJson() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }
    
    /**
     * Metoda koja vraća svježe meteo podatke za odabrano više uređaja u listi
     * @param id
     * @return
     * @throws ClientErrorException 
     */
     public String dajMeteoPodatke(String id) throws ClientErrorException {
        WebTarget resource = client.target(BASE_URI).path("meteoREST/"+id);
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public void close() {
        client.close();
    }
    
    
}
