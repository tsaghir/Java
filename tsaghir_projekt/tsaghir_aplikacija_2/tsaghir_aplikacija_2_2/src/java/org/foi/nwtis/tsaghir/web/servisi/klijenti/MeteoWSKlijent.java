/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.web.servisi.klijenti;

import org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoPodaci;

/**
 *
 * @author tsaghir
 */
public class MeteoWSKlijent {

    public static java.util.List<org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoPodaci> preuzmiZadnjeMeteoPodatke(java.lang.String korisnik, java.lang.String lozinka, int idUredaj) {
        org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoWebService_Service service = new org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoWebService_Service();
        org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoWebService port = service.getMeteoWebServicePort();
        return port.preuzmiZadnjeMeteoPodatke(korisnik, lozinka, idUredaj);
    }

    public static MeteoPodaci preuzmiVazeceMeteoPodatke(java.lang.String korisnik, java.lang.String lozinka, int idUredaj) {
        org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoWebService_Service service = new org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoWebService_Service();
        org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoWebService port = service.getMeteoWebServicePort();
        return port.preuzmiVazeceMeteoPodatke(korisnik, lozinka, idUredaj);
    }

    public static String preuzmiAdresuUredaja(java.lang.String korisnik, java.lang.String lozinka, int uredajId) {
        org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoWebService_Service service = new org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoWebService_Service();
        org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoWebService port = service.getMeteoWebServicePort();
        return port.preuzmiAdresuUredaja(korisnik, lozinka, uredajId);
    }
}
