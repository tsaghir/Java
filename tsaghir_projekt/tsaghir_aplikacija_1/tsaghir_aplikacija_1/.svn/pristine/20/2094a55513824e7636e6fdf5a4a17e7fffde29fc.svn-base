package org.foi.nwtis.tsaghir.servisi.soap.serveri;

import java.sql.Timestamp;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.foi.nwtis.tsaghir.pomagaci.DataBaseHelper;
import org.foi.nwtis.tsaghir.web.podaci.Dnevnik;
import org.foi.nwtis.tsaghir.web.podaci.MeteoPodaci;

/**
 * Web servis klasa s određenim metodama
 * @author tsaghir
 */
@WebService(serviceName = "MeteoWebService")
public class MeteoWebService {

    /**
     * Web servis operacija koja preuzima zadnje meteorološke podatke za odabrani IoT uređaj
     * @param korisnik
     * @param lozinka
     * @param idUredaj
     * @return 
     */
    @WebMethod(operationName = "preuzmiZadnjeMeteoPodatke")
    public List<MeteoPodaci> preuzmiZadnjeMeteoPodatke(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "idUredaj") int idUredaj) {
        long pocetak = System.currentTimeMillis();
        if(DataBaseHelper.provjeriKorinika(korisnik, lozinka)){
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("MeteoWS", "/preuzmiZadnjeMeteoPodatke();", korisnik, 31, System.currentTimeMillis() - pocetak));
            return DataBaseHelper.dohvatiZadnjeMeteoPodatke(idUredaj);
        }else{
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("MeteoWS", "/preuzmiZadnjeMeteoPodatke();", korisnik, 31, System.currentTimeMillis() - pocetak));
            return null;
        }
    }

    /**
     * Web service operacija koja preuzima određen broj meteo podataka
     * @param korisnik
     * @param lozinka
     * @param broj
     * @param idUredaj
     * @return 
     */
    @WebMethod(operationName = "preuzmi_N_MeteoPodataka")
    public List<MeteoPodaci> preuzmi_N_MeteoPodataka(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "broj") int broj, @WebParam(name = "idUredaj") int idUredaj) {
        long pocetak = System.currentTimeMillis();
        if(DataBaseHelper.provjeriKorinika(korisnik, lozinka)){
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("MeteoWS", "/preuzmi_N_MeteoPodataka();", korisnik, 32, System.currentTimeMillis() - pocetak));
            return DataBaseHelper.dohvatiZadnje_N_MeteoPodatke(idUredaj, broj);
        }else{
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("MeteoWS", "/preuzmi_N_MeteoPodataka();", korisnik, 32, System.currentTimeMillis() - pocetak));
            return null;
        }
    }

    /**
     * Web service operacija koja vraća Meteo podatke IoT uređaja u vremenskom intervalu
     * @param korisnik
     * @param lozinka
     * @param idUredaj
     * @param vrijemeOd
     * @param vrijemeDo
     * @return 
     */
    @WebMethod(operationName = "preuzmiMeteoUIntervalu")
    public List<MeteoPodaci> preuzmiMeteoUIntervalu(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "idUredaj") int idUredaj, @WebParam(name = "vrijemeOd") String vrijemeOd, @WebParam(name = "vrijemeDo") String vrijemeDo) {
        long pocetak = System.currentTimeMillis();
        if(DataBaseHelper.provjeriKorinika(korisnik, lozinka)){
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("MeteoWS", "/preuzmiMeteoUIntervalu();", korisnik, 33, System.currentTimeMillis() - pocetak));
            return DataBaseHelper.dajSveMeteoPodatkeZaUredajOdDo(idUredaj, lozinka, korisnik);
        }else{
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("MeteoWS", "/preuzmiMeteoUIntervalu();", korisnik, 33, System.currentTimeMillis() - pocetak));
            return null;
        }
    }

    /**
     * Web service operacija koja vraća važeće meteo podatke za određeni uređaj
     * @param korisnik
     * @param lozinka
     * @param uredajId
     * @return 
     */
    @WebMethod(operationName = "preuzmiVazeceMeteoPodatke")
    public MeteoPodaci preuzmiVazeceMeteoPodatke(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "idUredaj") int uredajId) {
        long pocetak = System.currentTimeMillis();
        if(DataBaseHelper.provjeriKorinika(korisnik, lozinka)){
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("MeteoWS", "/preuzmiVazeceMeteoPodatke();", korisnik, 34, System.currentTimeMillis() - pocetak));
            return DataBaseHelper.dohvatiVazeceMeteoPodatke(uredajId);
        }else{
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("MeteoWS", "/preuzmiVazeceMeteoPodatke();", korisnik, 34, System.currentTimeMillis() - pocetak));
            return null;
        }
    }

   /**
    * Web service operacija koja prezima adresu odabranog uređaja
    * @param korisnik
    * @param lozinka
    * @param uredajId
    * @return 
    */
    @WebMethod(operationName = "preuzmiAdresuUredaja")
    public String preuzmiAdresuUredaja(@WebParam(name = "korisnik") String korisnik, @WebParam(name = "lozinka") String lozinka, @WebParam(name = "uredajId") int uredajId) {
        long pocetak = System.currentTimeMillis();
        if(DataBaseHelper.provjeriKorinika(korisnik, lozinka)){
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("MeteoWS", "/preuzmiAdresuUredaja();", korisnik, 35, System.currentTimeMillis() - pocetak));
            return DataBaseHelper.dohvatiUredajAdresu(uredajId);
        }else{
            DataBaseHelper.zapisiDnevnikRada(napraviDnevnikObjekt("MeteoWS", "/preuzmiAdresuUredaja();", korisnik, 35, System.currentTimeMillis() - pocetak));
            return null;
        }
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
    
}
