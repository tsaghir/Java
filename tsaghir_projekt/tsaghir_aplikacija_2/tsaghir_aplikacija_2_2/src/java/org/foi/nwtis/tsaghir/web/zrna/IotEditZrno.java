/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.web.zrna;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.servisi.soap.serveri.MeteoPodaci;
import org.foi.nwtis.tsaghir.web.podaci.Lokacija;
import org.foi.nwtis.tsaghir.web.podaci.Uredjaj;
import org.foi.nwtis.tsaghir.web.servisi.klijenti.MeteoWSKlijent;
import org.foi.nwtis.tsaghir.web.servisi.klijenti.RESTKlijent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.primefaces.component.datatable.DataTable;

/**
 * zrno koje reprezentira iotEdit.xhtml
 *
 * @author tsaghir
 */
@Named(value = "iotEditZrno")
@SessionScoped
public class IotEditZrno implements Serializable {

    private final RESTKlijent.UredajiREST uredajiREST;
    private final Konfiguracija konf;
    private final int limitTabliceUredaji;
    private final String korisnikWs;
    private final String lozinkaWs;
    private List<Uredjaj> listaIotUredaja;
    private List<MeteoPodaci> meteoPodaciLista;
    private String uredajId;
    private String uredajNaziv;
    private String uredajStatus;
    private String uredajLongitude;
    private String uredajLatitude;
    private Date vrijemeKreiranja;
    private Date vrijemePromjene;
    private boolean prikaziAzuriranje = false;
    private boolean prikaziAzuriranjeUspjeh = false;
    private boolean onemoguciId = true;
    private boolean prikaziDodajIotNeuspjeh = false;
    private MeteoPodaci meteoPodaciUredaj;
    private String adresaUredaja;
    private final int limitTabliceMeteo;
    private Uredjaj odabraniUredaj;
    private boolean prikaziMeteo = false;
    private boolean prikaziVazeciMeteo = false;
    private boolean prikaziAdresa = false;

    /**
     * konstruktor
     */
    public IotEditZrno() {
        uredajiREST = new RESTKlijent.UredajiREST();
        konf = (Konfiguracija) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("Konfiguracija");
        limitTabliceUredaji = Integer.parseInt(konf.dajPostavku("limitTabliceUredaji"));
        limitTabliceMeteo = Integer.parseInt(konf.dajPostavku("limitTabliceMeteo"));
        korisnikWs = konf.dajPostavku("ws.username");
        lozinkaWs = konf.dajPostavku("ws.password");
    }

    public String prikaziDodajNoviUredaj() {
        pocistiVarijable();
        onemoguciId = false;
        prikaziAzuriranje = true;
        return "";
    }

    public String azurirajUredaj() {
        prikaziAzuriranje = true;
        Uredjaj uredaj = odabraniUredaj();
        postaviVarijable(uredaj);
        return "";
    }
    
    public String zatvoriMeteo(){
        prikaziMeteo=false;
        prikaziVazeciMeteo=false;
        return "";
    }

    private Uredjaj odabraniUredaj() {
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("j_idt5:uredaji");
        Uredjaj uredaj = (Uredjaj) dataTable.getRowData();
        return uredaj;
    }

    private void postaviVarijable(Uredjaj uredaj) {
        uredajId = String.valueOf(uredaj.getId());
        uredajNaziv = uredaj.getNaziv();
        uredajStatus = String.valueOf(uredaj.getStatus());
        uredajLongitude = uredaj.getGeoloc().getLongitude();
        uredajLatitude = uredaj.getGeoloc().getLatitude();
        vrijemeKreiranja = new Date(uredaj.getVrijeme_kreiranja().getTime());
        vrijemePromjene = new Date(uredaj.getVrijeme_promjene().getTime());
    }

    public String potvrdi() {
        if (onemoguciId) {
            String odgovor = null;
            try {
                odgovor = uredajiREST.azurirajUredaj(napraviUredajJSON());
            } catch (JSONException ex) {
                Logger.getLogger(IotEditZrno.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (Integer.parseInt(odgovor) == 1) {
                prikaziAzuriranjeUspjeh = true;
                listaIotUredaja = dohvatiUredaje();
            }
        } else {
            String odgovor = null;
            try {
                odgovor = uredajiREST.dodajUredaj(napraviUredajJSON());
            } catch (JSONException ex) {
                Logger.getLogger(IotEditZrno.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (Integer.parseInt(odgovor) == 1) {
                prikaziAzuriranjeUspjeh = true;
                listaIotUredaja = dohvatiUredaje();
            } else {
                prikaziDodajIotNeuspjeh = true;
            }
        }

        return "";
    }

    private String napraviUredajJSON() throws JSONException {
        JSONObject uredajJSON = new JSONObject();
        uredajJSON.put("id", uredajId);
        uredajJSON.put("naziv", uredajNaziv);
        uredajJSON.put("status", uredajStatus);
        uredajJSON.put("longitude", uredajLongitude);
        uredajJSON.put("latitude", uredajLatitude);
        uredajJSON.put("vrijemeKreiranja", vrijemeKreiranja.getTime());
        uredajJSON.put("vrijemePromjene", vrijemePromjene.getTime());
        System.out.println(uredajJSON.toString());
        return uredajJSON.toString();
    }

    public String odustaniAzuriranje() {
        prikaziAzuriranje = false;
        prikaziAzuriranjeUspjeh = false;
        return "";
    }

    private List<Uredjaj> dohvatiUredaje() {
        List<Uredjaj> listaUredaja = new ArrayList<>();
        try {
            String odgovor = uredajiREST.preuzmiUredaje();
            JSONObject uredajiJSON = new JSONObject(odgovor);
            JSONArray uredajiArray = uredajiJSON.getJSONArray("UredajiLista");
            for (int i = 0; i < uredajiArray.length(); i++) {
                JSONObject uredajJSON = uredajiArray.getJSONObject(i);
                Uredjaj uredajObjekt = new Uredjaj();
                uredajObjekt.setId(uredajJSON.getInt("id"));
                uredajObjekt.setNaziv(uredajJSON.getString("naziv"));
                uredajObjekt.setStatus(uredajJSON.getInt("status"));
                uredajObjekt.setGeoloc(new Lokacija(uredajJSON.getString("latitude"), uredajJSON.getString("longitude")));
                uredajObjekt.setVrijeme_kreiranja(Timestamp.valueOf(uredajJSON.getString("vrijemeKreiranja")));
                uredajObjekt.setVrijeme_promjene(Timestamp.valueOf(uredajJSON.getString("vrijemePromjene")));
                listaUredaja.add(uredajObjekt);
            }
        } catch (JSONException ex) {
            Logger.getLogger(IotEditZrno.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaUredaja;
    }
    
    public String zatvoriAdresa(){
        prikaziAdresa = false;
        return "";
    }

    private void pocistiVarijable() {
        uredajId = null;
        uredajNaziv = null;
        uredajStatus = null;
        uredajLongitude = null;
        uredajLatitude = null;
        vrijemeKreiranja = null;
        vrijemePromjene = null;
    }

    public String dohvatiZadnjeMeteoPodatke() {
        prikaziMeteo = true;
        odabraniUredaj = odabraniUredaj();
        meteoPodaciLista = MeteoWSKlijent.preuzmiZadnjeMeteoPodatke(korisnikWs, lozinkaWs, odabraniUredaj.getId());
        return "";
    }

    public String dohvatiVazeceMeteoPodatke() {
        prikaziMeteo = false;
        prikaziVazeciMeteo=true;
        odabraniUredaj = odabraniUredaj();
        meteoPodaciUredaj = MeteoWSKlijent.preuzmiVazeceMeteoPodatke(korisnikWs, lozinkaWs, odabraniUredaj.getId());
        return "";
    }
    
    public String dohvatiAdresuUredaja(){
        prikaziAdresa = true;
        Uredjaj uredaj = odabraniUredaj();
        adresaUredaja = MeteoWSKlijent.preuzmiAdresuUredaja(korisnikWs, lozinkaWs, uredaj.getId());
        return "";
    }

    public List<Uredjaj> getListaIotUredaja() {
        if (listaIotUredaja == null) {
            listaIotUredaja = new ArrayList<>();
            listaIotUredaja = dohvatiUredaje();
        }
        return listaIotUredaja;
    }

    public void setListaIotUredaja(List<Uredjaj> listaIotUredaja) {
        this.listaIotUredaja = listaIotUredaja;
    }

    public boolean isPrikaziAzuriranje() {
        return prikaziAzuriranje;
    }

    public void setPrikaziAzuriranje(boolean prikaziAzuriranje) {
        this.prikaziAzuriranje = prikaziAzuriranje;
    }

    public String getUredajId() {
        return uredajId;
    }

    public void setUredajId(String uredajId) {
        this.uredajId = uredajId;
    }

    public String getUredajNaziv() {
        return uredajNaziv;
    }

    public void setUredajNaziv(String uredajNaziv) {
        this.uredajNaziv = uredajNaziv;
    }

    public String getUredajStatus() {
        return uredajStatus;
    }

    public void setUredajStatus(String uredajStatus) {
        this.uredajStatus = uredajStatus;
    }

    public String getUredajLongitude() {
        return uredajLongitude;
    }

    public void setUredajLongitude(String uredajLongitude) {
        this.uredajLongitude = uredajLongitude;
    }

    public String getUredajLatitude() {
        return uredajLatitude;
    }

    public void setUredajLatitude(String uredajLatitude) {
        this.uredajLatitude = uredajLatitude;
    }

    public boolean isPrikaziAzuriranjeUspjeh() {
        return prikaziAzuriranjeUspjeh;
    }

    public void setPrikaziAzuriranjeUspjeh(boolean prikaziAzuriranjeUspjeh) {
        this.prikaziAzuriranjeUspjeh = prikaziAzuriranjeUspjeh;
    }

    public int getLimitTabliceUredaji() {
        return limitTabliceUredaji;
    }

    public Date getVrijemeKreiranja() {
        return vrijemeKreiranja;
    }

    public void setVrijemeKreiranja(Date vrijemeKreiranja) {
        this.vrijemeKreiranja = vrijemeKreiranja;
    }

    public Date getVrijemePromjene() {
        return vrijemePromjene;
    }

    public void setVrijemePromjene(Date vrijemePromjene) {
        this.vrijemePromjene = vrijemePromjene;
    }

    public boolean isOnemoguciId() {
        return onemoguciId;
    }

    public void setOnemoguciId(boolean onemoguciId) {
        this.onemoguciId = onemoguciId;
    }

    public boolean isPrikaziDodajIotNeuspjeh() {
        return prikaziDodajIotNeuspjeh;
    }

    public void setPrikaziDodajIotNeuspjeh(boolean prikaziDodajIotNeuspjeh) {
        this.prikaziDodajIotNeuspjeh = prikaziDodajIotNeuspjeh;
    }

    public List<MeteoPodaci> getMeteoPodaciLista() {
        return meteoPodaciLista;
    }

    public void setMeteoPodaciLista(List<MeteoPodaci> meteoPodaciLista) {
        this.meteoPodaciLista = meteoPodaciLista;
    }

    public MeteoPodaci getMeteoPodaciUredaj() {
        return meteoPodaciUredaj;
    }

    public void setMeteoPodaciUredaj(MeteoPodaci meteoPodaciUredaj) {
        this.meteoPodaciUredaj = meteoPodaciUredaj;
    }

    public String getAdresaUredaja() {
        return adresaUredaja;
    }

    public void setAdresaUredaja(String adresaUredaja) {
        this.adresaUredaja = adresaUredaja;
    }

    public int getLimitTabliceMeteo() {
        return limitTabliceMeteo;
    }

    public Uredjaj getOdabraniUredaj() {
        return odabraniUredaj;
    }

    public void setOdabraniUredaj(Uredjaj odabraniUredaj) {
        this.odabraniUredaj = odabraniUredaj;
    }

    public boolean isPrikaziMeteo() {
        return prikaziMeteo;
    }

    public void setPrikaziMeteo(boolean prikaziMeteo) {
        this.prikaziMeteo = prikaziMeteo;
    }

    public boolean isPrikaziVazeciMeteo() {
        return prikaziVazeciMeteo;
    }

    public void setPrikaziVazeciMeteo(boolean prikaziVazeciMeteo) {
        this.prikaziVazeciMeteo = prikaziVazeciMeteo;
    }

    public String getKorisnikWs() {
        return korisnikWs;
    }

    public String getLozinkaWs() {
        return lozinkaWs;
    }

    public boolean isPrikaziAdresa() {
        return prikaziAdresa;
    }

    public void setPrikaziAdresa(boolean prikaziAdresa) {
        this.prikaziAdresa = prikaziAdresa;
    }
    
    
}
