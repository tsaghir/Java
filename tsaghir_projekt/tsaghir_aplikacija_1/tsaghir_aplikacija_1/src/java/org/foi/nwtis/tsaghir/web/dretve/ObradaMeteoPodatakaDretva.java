package org.foi.nwtis.tsaghir.web.dretve;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.pomagaci.DataBaseHelper;
import org.foi.nwtis.tsaghir.servisi.rest.klijenti.GMKlijent;
import org.foi.nwtis.tsaghir.servisi.rest.klijenti.OWMKlijent;
import org.foi.nwtis.tsaghir.web.podaci.MeteoPodaci;
import org.foi.nwtis.tsaghir.web.podaci.Uredjaj;

/**
 * Dretva koja preuzima meteo podatke za svaki uređaju svakih [nnn] vremena
 * određeno intervalom
 *
 * @author tsaghir
 */
public class ObradaMeteoPodatakaDretva extends Thread {

    public static boolean PAUZIRAJ_OBRADU = false;
    private boolean PREKINI_OBRADU = false;
    private final Konfiguracija konf;
    private final String apikey;
    private int interval = 0;

    public ObradaMeteoPodatakaDretva() {
        super.setName("obrada-meteo-podataka");
        konf = (Konfiguracija) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("Konfiguracija");
        apikey = konf.dajPostavku("apikey");
        interval = Integer.parseInt(konf.dajPostavku("intervalDretveZaMeteoPodatke"));
    }

    @Override
    public void run() {
        List<Uredjaj> uredaji = new ArrayList<>();
        MeteoPodaci mp = null;
        String latitude = "";
        String longitude = "";
        String adresa="";
        while (!PREKINI_OBRADU) {
            if (!PAUZIRAJ_OBRADU) {
                uredaji = DataBaseHelper.dajSveUredaje();
                if (uredaji != null) {
                    for (Uredjaj uredaj : uredaji) {
                        OWMKlijent owmk = new OWMKlijent(apikey);
                        GMKlijent gmk = new GMKlijent();
                        if (!uredaj.getGeoloc().getLatitude().equals(latitude) || !uredaj.getGeoloc().getLongitude().equals(longitude)) {
                            mp = owmk.getRealTimeWeather(uredaj.getGeoloc().getLatitude(), uredaj.getGeoloc().getLongitude());
                        }
                        latitude = uredaj.getGeoloc().getLatitude();
                        longitude = uredaj.getGeoloc().getLongitude();
                        adresa = gmk.getAdress(latitude, longitude);
                        DataBaseHelper.zapisiMeteoPodatke(uredaj.getId(), adresa, Float.parseFloat(latitude), Float.parseFloat(longitude), mp);
                    }
                }
                try {
                    sleep(interval * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ObradaMeteoPodatakaDretva.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Override
    public synchronized void interrupt() {
        PREKINI_OBRADU = true;
        System.out.println("Prekinuta obrada: " + super.getName());
    }
    
    @Override
    public synchronized void start() {
        super.start();
    }
    

}
