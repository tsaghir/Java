package org.foi.nwtis.tsaghir.web.zrna;

import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.foi.nwtis.tsaghir.ws.klijenti.MeteoPodaci;
import org.foi.nwtis.tsaghir.ws.klijenti.MeteoREST;
import org.foi.nwtis.tsaghir.ws.klijenti.MeteoWSKlijent;
import org.foi.nwtis.tsaghir.ws.klijenti.Uredjaj;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 * Zrno koje je povezano sa odabirUredaja.xhtml
 * @author tsaghir
 */
@Named(value = "odabirUredjaja")
@SessionScoped
public class OdabirUredjaja implements Serializable {
    /**
     * VARIJABLE
     */
    private Date from;
    private Date to;
    private List<Uredjaj> uredjaji;
    private List<String> id;
    private String naziv;
    private String adresa;
    private MeteoREST restServis;
    private List<MeteoPodaci> meteoPodaci = null;
    private boolean gumbPreuzmiRest = true;
    private boolean gumbPreuzmiSoap = true;
    private boolean gumbUpisi = true;

    public OdabirUredjaja() {
        restServis = new MeteoREST();
        meteoPodaci = new ArrayList<>();
    }

    /**
     * Metoda koja upisuje uredaj u bazu podataka pomoću SOAP web servisa
     * @return 
     */
    public String upisiSoap() {
        Uredjaj ured = new Uredjaj();
        ured.setNaziv(naziv);
        ured.setAdresa(adresa);
        if (!MeteoWSKlijent.dodajUredjaj(ured)) {
            return "GreskaKodDodavanja";
        }
        naziv = null;
        adresa = null;
        return null;
    }

    /**
     * Metoda koja upisuje uredaj u bazu podataka pomoću REST web servisa
     * @return 
     */
    public String upisiRest() {
        JSONObject json = new JSONObject();
        json.put("naziv", naziv);
        json.put("adresa", adresa);
        json.put("lat", "");
        json.put("lon", "");
        StringWriter out = new StringWriter();
        json.write(out);
        restServis.postJson(out.toString());
        naziv = null;
        adresa = null;
        return null;
    }

    /**
     * Metoda koja se bavi uključivanjem/isključivanjem gumbova na stranici
     */
    public void ukljuciIskljuciGumb() {
        if (naziv != null && adresa != null
                && !naziv.isEmpty() && !adresa.isEmpty()) {
            gumbUpisi = false;
        } else {
            gumbUpisi = true;
        }
        if (id != null) {
            if (id.size() == 1) {
                gumbPreuzmiRest = true;
                if (getFrom() != null && getTo() != null) {
                    gumbPreuzmiSoap = false;
                }
            } else if (id.size() >= 2) {
                gumbPreuzmiRest = false;
                gumbPreuzmiSoap = true;
            } else if (id.size() == 0) {
                gumbPreuzmiRest = true;
                gumbPreuzmiSoap = true;
            }
        }
    }

    /**
     * Metoda SOAP web servisa pomoću koje preuzimamo podatke uređaja iz baze podataka
     * @return 
     */
    public String preuzmiSoap() {
        long vrijeme1 = from.getTime() / 1000;
        long vrijeme2 = to.getTime() / 1000;
        meteoPodaci = MeteoWSKlijent.dajSveMeteoPodatkeZaUredjaj(Integer.parseInt(id.get(0)), vrijeme1, vrijeme2);
        return null;
    }

    /**
     * Metoda REST web servisa pomoću koje preuzimamo podatke za listu odabranih uređaja iz baze podataka
     * @return 
     */
    public String preuzmiRest() {
        meteoPodaci.clear();
        for (String id : id) {
            String rezultat = restServis.dajMeteoPodatke(id);
            procitajJson(rezultat);
        }
        return null;
    }

    /**
     * Metoda koja čita JSON string koji je dobiven od strane web servisa
     * @param rezultat 
     */
    private void procitajJson(String rezultat) {
        JSONArray jArray = new JSONArray(rezultat);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            MeteoPodaci mp = new MeteoPodaci();
            mp.setTemperatureMax(Float.parseFloat(obj.get("tempmax").toString()));
            mp.setTemperatureMin(Float.parseFloat(obj.get("tempmin").toString()));
            mp.setTemperatureValue(Float.parseFloat(obj.get("temp").toString()));
            mp.setHumidityValue(Float.parseFloat(obj.get("vlaga").toString()));
            mp.setPressureValue(Float.parseFloat(obj.get("tlak").toString()));
            mp.setWindSpeedValue(Float.parseFloat(obj.get("vjetar").toString()));
            mp.setWeatherValue(obj.get("vrijemeopis").toString());
            Date datum = new Date();
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(datum);
            XMLGregorianCalendar date2 = null;
            try {
                date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            } catch (DatatypeConfigurationException ex) {
                Logger.getLogger(OdabirUredjaja.class.getName()).log(Level.SEVERE, null, ex);
            }
            mp.setLastUpdate(date2);
            meteoPodaci.add(mp);
        }
    }

    /**
     * GETTERI I SETTERI
     * @return 
     */
    public List<Uredjaj> getUredjaji() {
        uredjaji = MeteoWSKlijent.dajSveUredjajeSoap();
        return uredjaji;
    }

    public void setUredjaji(List<Uredjaj> uredjaji) {
        this.uredjaji = uredjaji;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        try {
            if (adresa != null) {
                adresa = new String(adresa.getBytes("ISO-8859-1"), "UTF-8");
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OdabirUredjaja.class.getName()).log(Level.SEVERE, null, ex);
        }
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public boolean isGumbPreuzmiRest() {
        return gumbPreuzmiRest;
    }

    public void setGumbPreuzmiRest(boolean gumbPreuzmiRest) {
        this.gumbPreuzmiRest = gumbPreuzmiRest;
    }

    public boolean isGumbPreuzmiSoap() {
        return gumbPreuzmiSoap;
    }

    public void setGumbPreuzmiSoap(boolean gumbPreuzmiSoap) {
        this.gumbPreuzmiSoap = gumbPreuzmiSoap;
    }

    public List<MeteoPodaci> getMeteoPodaci() {
        return meteoPodaci;
    }

    public void setMeteoPodaci(List<MeteoPodaci> meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }

    public boolean isGumbUpisi() {
        return gumbUpisi;
    }

    public void setGumbUpisi(boolean gumbUpisi) {
        this.gumbUpisi = gumbUpisi;
    }
}
