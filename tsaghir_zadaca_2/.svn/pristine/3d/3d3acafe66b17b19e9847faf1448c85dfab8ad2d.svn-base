package org.foi.nwtis.tsaghir.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.tsaghir.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.tsaghir.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.tsaghir.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.tsaghir.web.dretve.ObradaPoruka;
import org.foi.nwtis.tsaghir.web.pomagaci.DataBaseHelper;
import org.foi.nwtis.tsaghir.web.pomagaci.RegexHelper;

/**
 * Slušač životnog ciklusa web aplikacija
 *
 * @author tsaghir
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private ObradaPoruka op = null;
    private static Konfiguracija konf = null;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String datoteka = context.getRealPath("/WEB-INF") + File.separator + context.getInitParameter("konfiguracija");
        RegexHelper.getInstance();
        
        BP_Konfiguracija bp_konf = new BP_Konfiguracija(datoteka);
        context.setAttribute("BP_Konfig", bp_konf);
        System.out.println("Učitana konfiguracija");
        
        
        try {
            konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            context.setAttribute("Mail_Konfig", konf);
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        op = new ObradaPoruka();
        op.setSc(context);
        op.start();
        DataBaseHelper.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(op != null){
            op.interrupt();
        }
    }
    
    public static Konfiguracija getKonfig(){
        return konf;
    }
}
