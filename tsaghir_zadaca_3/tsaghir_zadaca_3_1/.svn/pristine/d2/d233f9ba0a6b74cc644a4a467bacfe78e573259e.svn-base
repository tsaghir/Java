package org.foi.nwtis.tsaghir.ws.serveri;

import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.foi.nwtis.tsaghir.baza.DataBaseHelper;
import org.foi.nwtis.tsaghir.rest.klijenti.GMKlijent;
import org.foi.nwtis.tsaghir.web.podaci.Lokacija;
import org.foi.nwtis.tsaghir.web.podaci.MeteoPodaci;
import org.foi.nwtis.tsaghir.web.podaci.MaxMin;
import org.foi.nwtis.tsaghir.web.podaci.Uredjaj;

/**
 * SOAP web servis
 * @author tsaghir
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    /**
     * Web service operacija za dohvaćanje svih uređaja iz baze podataka
     */
    @WebMethod(operationName = "dajSveUredjaje")
    public List<Uredjaj> dajSveUredjaje() {
        List<Uredjaj> uredjaji = DataBaseHelper.dajSveUredaje();
        return uredjaji;
    }

    /**
     * Web service operacija za dodavanje uređaja u bazu podataka
     */
    @WebMethod(operationName = "dodajUredjaj")
    public Boolean dodajUredjaj(@WebParam(name = "uredjaj") Uredjaj uredaj) {
        if (uredaj.getGeoloc() == null) {
            GMKlijent gmk = new GMKlijent();
            Lokacija lokacija = gmk.getGeoLocation(uredaj.getAdresa());
            if (lokacija != null) {
                uredaj.setGeoloc(lokacija);
            } else {
                return false;
            }
        }
        return DataBaseHelper.spremiUredaj(uredaj);
    }

    /**
     * Web service operacija za dohvaćanje meteo podataka za određeni uređaj od-do
     */
    @WebMethod(operationName = "dajSveMeteoPodatkeZaUredjaj")
    public List<MeteoPodaci> dajSveMeteoPodatkeZaUredjaj(@WebParam(name = "id") int id, @WebParam(name = "from") long from, @WebParam(name = "to") long to) {
        List<MeteoPodaci> mp = DataBaseHelper.dajSveMeteoPodatkeZaUredajOdDo(id, from, to);
        return mp;
    }

    /**
     * Web service operacija za dohvaćanje zadnjih meteo podataka za određeni uređaj
     */
    @WebMethod(operationName = "dajZadnjeMeteoPodatke")
    public List<MeteoPodaci> dajZadnjeMeteoPodatke(@WebParam(name = "id") int id) {
        List<MeteoPodaci> mp = DataBaseHelper.dajZadnjeMeteoPodatke(id);
        return mp;
    }

    /**
     * Web service operacija za dohvaćanje max i min temperature za uređaj
     */
    @WebMethod(operationName = "dajMinMaxTempZaUredjaj")
    public List<MaxMin> dajMinMaxTempZaUredjaj(@WebParam(name = "id") int id, @WebParam(name = "from") long from, @WebParam(name = "to") long to) {
        return DataBaseHelper.dajMaxMinTemperaturu(id, from, to);
    }

    /**
     * Web service operacija za dohvaćanje max i min vlage za uređaj
     */
    @WebMethod(operationName = "dajMinMaxVlagaZaUredjaj")
    public List<MaxMin> dajMinMaxVlagaZaUredjaj(@WebParam(name = "id") int id, @WebParam(name = "from") long from, @WebParam(name = "to") long to) {
        return DataBaseHelper.dajMaxMinVlagu(id, from, to);
    }

    /**
     * Web service operacija za dohvaćanje max i min tlaka za uređaj
     */
    @WebMethod(operationName = "dajMinMaxTlakZaUredjaj")
    public List<MaxMin> dajMinMaxTlakZaUredjaj(@WebParam(name = "id") int id, @WebParam(name = "from") long from, @WebParam(name = "to") long to) {
        return DataBaseHelper.dajMaxMinTlak(id, from, to);
    }

}
