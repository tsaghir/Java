package org.foi.nwtis.tsaghir.web.dretve;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.tsaghir.baza.DataBaseHelper;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.rest.klijenti.OWMKlijent;
import org.foi.nwtis.tsaghir.web.podaci.MeteoPodaci;
import org.foi.nwtis.tsaghir.web.podaci.Uredjaj;

/**
 * Dretva za obradu meteo podataka
 * @author tsaghir
 */
public class ObradaMeteoPodataka extends Thread {

    private boolean prekid_obrade = false;
    private int interval = 0;
    private String apikey = "";

    /**
     * Konstruktor
     * @param konf 
     */
    public ObradaMeteoPodataka(Konfiguracija konf) {
        interval = Integer.parseInt(konf.dajPostavku("intervalDretveZaMeteoPodatke"));
        apikey = konf.dajPostavku("apikey");
    }

    /**
     * Metoda koja prekida dretvu
     */
    @Override
    public void interrupt() {
        prekid_obrade = true;
        super.interrupt();
    }

    /**
     * Metoda koja izvršava kod u dretvi
     */
    @Override
    public void run() {
        List<Uredjaj> uredaji = new ArrayList<>();
        while (!prekid_obrade) {
            uredaji = DataBaseHelper.dajSveUredaje();
            if (uredaji != null) {
                for (Uredjaj uredaj : uredaji) {
                    OWMKlijent owmk = new OWMKlijent(apikey);
                    MeteoPodaci mp = owmk.getRealTimeWeather(uredaj.getGeoloc().getLatitude(), uredaj.getGeoloc().getLongitude());
                    DataBaseHelper.zapisiMeteoPodatke(uredaj.getId(), "tsaghir", Float.parseFloat(uredaj.getGeoloc().getLatitude()), Float.parseFloat(uredaj.getGeoloc().getLongitude()), mp);
                }
            }
            try {
                sleep(interval * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ObradaMeteoPodataka.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Metoda koja starta dretvu
     */
    @Override
    public synchronized void start() {
        super.start();
    }

}
