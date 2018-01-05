package org.foi.nwtis.tsaghir.web.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.tsaghir.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.tsaghir.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.tsaghir.pomagaci.DataBaseHelper;
import org.foi.nwtis.tsaghir.pomagaci.RegexHelper;
import org.foi.nwtis.tsaghir.pomagaci.ThreadHelper;
import org.foi.nwtis.tsaghir.web.dretve.ObradaMeteoPodatakaDretva;
import org.foi.nwtis.tsaghir.web.dretve.SocketServerDretva;

/**
 * Slušač web aplikacije
 *
 * @author tsaghir
 */
public class SlusacAplikacije implements ServletContextListener {

    private Konfiguracija konf = null;
    ServletContext context;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        String datoteka = context.getRealPath("/WEB-INF") + "/NWTiS.projekt.config.xml";
        
        try {
            konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            context.setAttribute("Konfiguracija", konf);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataBaseHelper.getInstance();
        RegexHelper.getInstance();
        ThreadHelper.getInstance();
        SocketServerDretva serverDretva = new SocketServerDretva();
        serverDretva.start();
        ObradaMeteoPodatakaDretva meteoPodaciDretva = new ObradaMeteoPodatakaDretva();
        meteoPodaciDretva.start();
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
