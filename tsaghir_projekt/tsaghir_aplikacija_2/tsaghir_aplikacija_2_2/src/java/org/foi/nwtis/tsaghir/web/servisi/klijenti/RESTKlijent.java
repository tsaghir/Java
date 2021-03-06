/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.web.servisi.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Klasa rest servisa korisnika
 *
 * @author tsaghir
 */
public class RESTKlijent {

    public static class KorisniciREST {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8085/tsaghir_aplikacija_1/webresources";

        public KorisniciREST() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("korisniciREST");
        }

        public String azurirajKorisnika(Object requestEntity) throws ClientErrorException {
            return webTarget.path("/azuriraj").request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }

        public String preuzmiKorisnike() throws ClientErrorException {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }

        public String dodajKorisnika(Object requestEntity) throws ClientErrorException {
            return webTarget.path("/dodaj").request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }
        
        public String dajKorisnikaPoKorImenu(String koriIme) throws ClientErrorException {
            WebTarget resource = client.target(BASE_URI).path("korisniciREST/" + koriIme);
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }

        public void close() {
            client.close();
        }
    }

    public static class UredajiREST {

        private WebTarget webTarget;
        private Client client;
        private static final String BASE_URI = "http://localhost:8085/tsaghir_aplikacija_1/webresources";

        public UredajiREST() {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            webTarget = client.target(BASE_URI).path("uredajiREST");
        }

        public String dodajUredaj(Object requestEntity) throws ClientErrorException {
            return webTarget.path("/dodaj").request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }

        public String azurirajUredaj(Object requestEntity) throws ClientErrorException {
            return webTarget.path("/azuriraj").request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
        }

        public String preuzmiUredaje() throws ClientErrorException {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }
        
         public String dajKorisnikaPoId(String id) throws ClientErrorException {
            WebTarget resource = client.target(BASE_URI).path("uredajiREST/" + id);
            return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        }

        public void close() {
            client.close();
        }
    }
}
