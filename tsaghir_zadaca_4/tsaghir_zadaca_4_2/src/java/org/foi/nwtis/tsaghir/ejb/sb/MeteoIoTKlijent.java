package org.foi.nwtis.tsaghir.ejb.sb;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.tsaghir.rest.klijenti.GMKlijent;
import org.foi.nwtis.tsaghir.rest.klijenti.OWMKlijent;
import org.foi.nwtis.tsaghir.web.podaci.Lokacija;
import org.foi.nwtis.tsaghir.web.podaci.MeteoPrognoza;

/**
 *
 * @author tsaghir
 */
@Stateless
@LocalBean
public class MeteoIoTKlijent {

    private String apiKey;
    private OWMKlijent klijent;    
    /**
     * Metoda koja postavlja API key od OWMaps api-a
     * @param apiKey 
     */
    public void postaviKorisnickePodatke(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Metoda koja daje lokaciju uz pomoć google-ovog API-a
     * @param adresa
     * @return 
     */
    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmKlijent = new GMKlijent();
        return gmKlijent.getGeoLocation(adresa);
    }

    /**
     * Metoda koja vraća meteo prognozu u narednih 5 dana svaka 3 sata
     * @param latitude
     * @param longitude
     * @return 
     */
    public List<MeteoPrognoza> dajMeteoPrognoze(String latitude, String longitude, int brojac) {
        klijent = new OWMKlijent(apiKey);
        return klijent.getWeatherForecast(latitude, longitude, brojac);
    }
}
