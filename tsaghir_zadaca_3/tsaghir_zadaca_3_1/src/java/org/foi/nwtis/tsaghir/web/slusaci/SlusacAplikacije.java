package org.foi.nwtis.tsaghir.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.tsaghir.baza.DataBaseHelper;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.tsaghir.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.tsaghir.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.tsaghir.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.tsaghir.web.dretve.ObradaMeteoPodataka;

/**
 *  Slušač životnog ciklusa web aplikacije
 *
 * @author tsaghir
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private Konfiguracija konf = null;
    private static Konfiguracija KONF = null;
    ServletContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        String datoteka = context.getRealPath("/WEB-INF") + File.separator + context.getInitParameter("konfiguracija");

        BP_Konfiguracija bp_konf = new BP_Konfiguracija(datoteka);
        context.setAttribute("BP_Konfig", bp_konf);
        System.out.println("Učitana konfiguracija");

        try {
            konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            KONF = konf;
            context.setAttribute("Meteo_Konfig", konf);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataBaseHelper.getInstance(konf);
        ObradaMeteoPodataka omp = new ObradaMeteoPodataka(konf);
        omp.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ServletContext getContext() {
        return context;
    }

    public static Konfiguracija getKONF() {
        return KONF;
    }
}
