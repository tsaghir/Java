package org.foi.nwtis.tsaghir.ws.klijenti;


/**
 *
 * @author tsaghir
 * Klasa koja služi kao klijent SOAP web servisa sa projekta 3_1
 */
public class MeteoWSKlijent {

    /**
     * Metoda koja vraća sve uređaje iz baze podataka
     * @return 
     */
    public static java.util.List<org.foi.nwtis.tsaghir.ws.klijenti.Uredjaj> dajSveUredjajeSoap() {
        org.foi.nwtis.tsaghir.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.tsaghir.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.tsaghir.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveUredjaje();
    }
    
    /**
     * Mteoda koja dodaje uređaj u bazu podataka
     * @param uredjaj
     * @return 
     */
    public static Boolean dodajUredjaj(org.foi.nwtis.tsaghir.ws.klijenti.Uredjaj uredjaj) {
        org.foi.nwtis.tsaghir.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.tsaghir.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.tsaghir.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dodajUredjaj(uredjaj);
    }

    /**
     * Metoda koja daje sve meteo podatke za uređaj
     * @param id uređaja
     * @param from početak datuma
     * @param to kraj datuma
     * @return 
     */
    public static java.util.List<org.foi.nwtis.tsaghir.ws.klijenti.MeteoPodaci> dajSveMeteoPodatkeZaUredjaj(int id, long from, long to) {
        org.foi.nwtis.tsaghir.ws.klijenti.GeoMeteoWS_Service service = new org.foi.nwtis.tsaghir.ws.klijenti.GeoMeteoWS_Service();
        org.foi.nwtis.tsaghir.ws.klijenti.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveMeteoPodatkeZaUredjaj(id, from, to);
    }
}
