package org.foi.nwtis.tsaghir.web.zrna;

import javax.inject.Named;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.tsaghir.ejb.eb.Dnevnik;
import org.foi.nwtis.tsaghir.ejb.eb.Promjene;
import org.foi.nwtis.tsaghir.ejb.eb.Uredaji;
import org.foi.nwtis.tsaghir.ejb.sb.DnevnikFacade;
import org.foi.nwtis.tsaghir.ejb.sb.MeteoIoTKlijent;
import org.foi.nwtis.tsaghir.ejb.sb.PromjeneFacade;
import org.foi.nwtis.tsaghir.ejb.sb.UredajiFacade;
import org.foi.nwtis.tsaghir.web.kontrole.Izbornik;
import org.foi.nwtis.tsaghir.web.podaci.Lokacija;
import org.foi.nwtis.tsaghir.web.podaci.MeteoPrognoza;

/**
 *
 * @author tsaghir
 */
@Named(value = "odabirIotPrognoza")
@SessionScoped
public class OdabirIotPrognoza implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;

    @EJB
    private PromjeneFacade promjeneFacade;

    @EJB
    private UredajiFacade uredajiFacade;

    @EJB
    private MeteoIoTKlijent meteoIoTKlijent;

    private final String apiKey = "f0304dda4cb4c276ec76a6dea3d6251c";
    private String noviId;
    private String noviNaziv;
    private String noviAdresa;
    private List<Izbornik> raspoloziviIot;
    private List<Izbornik> odabraniIot;
    private List<String> popisRaspoloziviIot;
    private List<String> popisOdabraniIot;
    private String azurirajId;
    private String azurirajNaziv;
    private String azurirajAdresa;
    private List<MeteoPrognoza> meteoPrognoze = new ArrayList<>();
    private boolean azuriranje = false;
    private boolean prognoze = false;
    private String gumbPregledPrognoza = "Pregled prognoza";

    private long pocetak;
    private int brojac = 0;

    /*
     * konstruktor
     */
    public OdabirIotPrognoza() {
        pocetak = System.currentTimeMillis() / 1000;
    }

    /**
     * Metoda za dodavanje novog IOT uređaja u bazu podataka
     *
     * @return
     */
    public String dodajIotUredaj() {
        try {
            noviAdresa = new String(noviAdresa.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OdabirIotPrognoza.class.getName()).log(Level.SEVERE, null, ex);
        }
        Lokacija l = meteoIoTKlijent.dajLokaciju(noviAdresa);
        Uredaji uredaji = new Uredaji(Integer.parseInt(noviId), noviNaziv, Float.parseFloat(l.getLatitude()), Float.parseFloat(l.getLongitude()), 0, new Date(), new Date());
        uredajiFacade.create(uredaji);
        azurirajPromjene(uredaji);
        azurirajDnevnik(1);
        raspoloziviIot.clear();
        noviId = null;
        noviNaziv = null;
        noviAdresa = null;
        return "";
    }

    /**
     * Metoda za azuriranje promjene
     *
     * @param uredaj
     */
    private void azurirajPromjene(Uredaji uredaj) {
        Promjene promjene = new Promjene(0, uredaj.getId(), uredaj.getNaziv(), uredaj.getLatitude(), uredaj.getLongitude(), uredaj.getStatus(), new Date(), new Date());
        promjeneFacade.create(promjene);
    }

    private void azurirajDnevnik(int status) {
        long kraj = System.currentTimeMillis() / 1000 - pocetak;
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = req.getRequestURL().toString();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Dnevnik dnevnik;
        try {
            dnevnik = new Dnevnik(0, "tsaghir", url, InetAddress.getLocalHost().toString(), Integer.valueOf(String.valueOf(kraj)), status);
            dnevnik.setVrijeme(time);
            dnevnikFacade.create(dnevnik);
        } catch (UnknownHostException ex) {
            Logger.getLogger(OdabirIotPrognoza.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Metoda koja preuzima uređaje u listu razpoloživih uređaja
     */
    public void preuzmiUredaje() {
        if (popisRaspoloziviIot != null) {
            if (popisRaspoloziviIot.size() > 0) {
                for (String naziv : popisRaspoloziviIot) {
                    for (Izbornik raspolozivi : raspoloziviIot) {
                        if (naziv.equals(raspolozivi.getVrijednost())) {
                            odabraniIot.add(raspolozivi);
                        }
                    }
                }
                if (odabraniIot != null && odabraniIot.size() > 0) {
                    odabraniIot.forEach((odabrani) -> {
                        raspoloziviIot.remove(odabrani);
                    });
                }
            }
            azurirajDnevnik(2);
        }
    }
    /**
     * Metoda koja vraća odabrane uređaje u raspoložive uređaje
     */
    public void vratiUredaje() {
        if (popisOdabraniIot != null && popisOdabraniIot.size() > 0) {
            if (raspoloziviIot.isEmpty()) {
                for (Izbornik odabraniLista : odabraniIot) {
                    raspoloziviIot.add(odabraniLista);
                }
                odabraniIot.clear();
            } else {
                for (String odabrani : popisOdabraniIot) {
                    for (Iterator<Izbornik> iterator = odabraniIot.iterator(); iterator.hasNext();) {
                        Izbornik odabraniLista = iterator.next();
                        if (odabrani.equals(odabraniLista.getVrijednost())) {
                            iterator.remove();
                            raspoloziviIot.add(odabraniLista);
                        }
                    }
                }
            }
            azurirajDnevnik(3);
        }
    }

    /**
     * Metoda koja sakriva i otkriva pogled za ažuriranje
     */
    public void otkrijPogledZaAzuriranje() {
        if (popisRaspoloziviIot.size() > 0) {
            azuriranje = true;
            azurirajId = dohvatiOdabraniUredaj().getId().toString();
        }
        azurirajDnevnik(4);
    }

    /**
     * Metoda koja ažurira odabrani uređaj u bazi podataka
     */
    public void azurirajUredaj() {
        try {
            azurirajAdresa = new String(azurirajAdresa.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OdabirIotPrognoza.class.getName()).log(Level.SEVERE, null, ex);
        }
        Uredaji uredaj = dohvatiOdabraniUredaj();
        Lokacija l = meteoIoTKlijent.dajLokaciju(azurirajAdresa);
        uredaj.setNaziv(azurirajNaziv);
        uredaj.setLongitude(Float.parseFloat(l.getLongitude()));
        uredaj.setLongitude(Float.parseFloat(l.getLongitude()));
        uredaj.setStatus(0);
        uredaj.setVrijemeKreiranja(new Date());
        uredaj.setVrijemePromjene(new Date());
        uredajiFacade.edit(uredaj);
        azurirajPromjene(uredaj);
        raspoloziviIot.clear();
        azurirajAdresa = null;
        azurirajNaziv = null;
        azuriranje = false;
        azurirajDnevnik(5);
    }

    /**
     * Metoda koja dohvaća objekt odabrano uređaja iz baze podataka
     * @return 
     */
    private Uredaji dohvatiOdabraniUredaj() {
        Uredaji ured = null;
        if (popisRaspoloziviIot.size() > 0) {
            String odabrani = popisRaspoloziviIot.get(0);
            ured = uredajiFacade.find(Integer.parseInt(odabrani));
            azurirajId = ured.getId().toString();
        }
        return ured;
    }

    /**
     * Metoda koja preuzima vremensku prognozu preko OWMaps API-a
     */
    public void preuzmiPrognozuVremena() {
        meteoIoTKlijent.postaviKorisnickePodatke(apiKey);
        if (odabraniIot.size() > 0) {
            prognoze = true;
            for (Izbornik izbornik : odabraniIot) {
                brojac++;
                Uredaji ured = uredajiFacade.find(Integer.parseInt(izbornik.getVrijednost()));
                meteoPrognoze.addAll(meteoIoTKlijent.dajMeteoPrognoze(String.valueOf(ured.getLatitude()), String.valueOf(ured.getLongitude()), brojac));
            }
            promjeniImePrognozaGumba();
        } else if (odabraniIot.isEmpty()) {
            prognoze = false;
            meteoPrognoze.clear();
            brojac = 0;
        }
        azurirajDnevnik(6);
    }

    /**
     * Metoda koja mjenja naziv gumba
     */
    private void promjeniImePrognozaGumba() {
        if (gumbPregledPrognoza.equals("Pregled prognoza")) {
            gumbPregledPrognoza = "Zatvori prognoze";
        } else {
            prognoze = false;
            meteoPrognoze.clear();
            brojac = 0;
            gumbPregledPrognoza = "Pregled prognoza";
        }
    }

    /**
     * GETTERS AND SETTERS
     *
     * @return
     */
    public String getNoviId() {
        return noviId;
    }

    public void setNoviId(String noviId) {
        this.noviId = noviId;
    }

    public String getNoviNaziv() {
        return noviNaziv;
    }

    public void setNoviNaziv(String noviNaziv) {
        this.noviNaziv = noviNaziv;
    }

    public List<Izbornik> getRaspoloziviIot() {
        if (raspoloziviIot == null) {
            raspoloziviIot = new ArrayList<>();
            List<Uredaji> raspolozivi = uredajiFacade.findAll();
            for (Uredaji uredaji : raspolozivi) {
                raspoloziviIot.add(new Izbornik(uredaji.getNaziv(), uredaji.getId().toString()));
            }
        } else if (raspoloziviIot.size() == 0) {
            List<Uredaji> raspolozivi = uredajiFacade.findAll();
            List<Izbornik> priprema = new ArrayList<>();
            for (Uredaji uredaji : raspolozivi) {
                priprema.add(new Izbornik(uredaji.getNaziv(), uredaji.getId().toString()));
            }
            if (odabraniIot != null && odabraniIot.size() > 0) {
                for (Iterator<Izbornik> iter = priprema.listIterator(); iter.hasNext();) {
                    Izbornik prip = iter.next();
                    for (Izbornik odabrani : odabraniIot) {
                        if (prip.getVrijednost().equals(odabrani.getVrijednost())) {
                            iter.remove();
                        }
                    }
                }
            }
            raspoloziviIot = priprema;
        }
        return raspoloziviIot;
    }

    public void setRaspoloziviIot(List<Izbornik> raspoloziviIot) {
        this.raspoloziviIot = raspoloziviIot;
    }

    public List<Izbornik> getOdabraniIot() {
        if (odabraniIot == null) {
            odabraniIot = new ArrayList<>();
        }
        return odabraniIot;
    }

    public void setOdabraniIot(List<Izbornik> odabraniIot) {
        this.odabraniIot = odabraniIot;
    }

    public List<String> getPopisRaspoloziviIot() {
        return popisRaspoloziviIot;
    }

    public void setPopisRaspoloziviIot(List<String> popisRaspoloziviIot) {
        this.popisRaspoloziviIot = popisRaspoloziviIot;
    }

    public List<String> getPopisOdabraniIot() {
        return popisOdabraniIot;
    }

    public void setPopisOdabraniIot(List<String> popisOdabraniIot) {
        this.popisOdabraniIot = popisOdabraniIot;
    }

    public String getAzurirajId() {
        return azurirajId;
    }

    public void setAzurirajId(String azurirajId) {
        this.azurirajId = azurirajId;
    }

    public String getAzurirajNaziv() {
        return azurirajNaziv;
    }

    public void setAzurirajNaziv(String azurirajNaziv) {
        this.azurirajNaziv = azurirajNaziv;
    }

    public String getAzurirajAdresa() {
        return azurirajAdresa;
    }

    public void setAzurirajAdresa(String azurirajAdresa) {
        this.azurirajAdresa = azurirajAdresa;
    }

    public List<MeteoPrognoza> getMeteoPrognoze() {
        return meteoPrognoze;
    }

    public void setMeteoPrognoze(List<MeteoPrognoza> meteoPrognoze) {
        this.meteoPrognoze = meteoPrognoze;
    }

    public boolean isAzuriranje() {
        return azuriranje;
    }

    public void setAzuriranje(boolean azuriranje) {
        this.azuriranje = azuriranje;
    }

    public boolean isPrognoze() {
        return prognoze;
    }

    public void setPrognoze(boolean prognoze) {
        this.prognoze = prognoze;
    }

    public String getNoviAdresa() {
        return noviAdresa;
    }

    public void setNoviAdresa(String noviAdresa) {
        this.noviAdresa = noviAdresa;
    }

    public String getGumbPregledPrognoza() {
        return gumbPregledPrognoza;
    }

    public void setGumbPregledPrognoza(String gumbPregledPrognoza) {
        this.gumbPregledPrognoza = gumbPregledPrognoza;
    }

}
