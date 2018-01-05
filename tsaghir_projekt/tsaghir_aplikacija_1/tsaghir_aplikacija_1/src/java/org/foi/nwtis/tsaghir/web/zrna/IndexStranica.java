package org.foi.nwtis.tsaghir.web.zrna;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.tsaghir.pomagaci.DataBaseHelper;

/**
 * zrno koje predstavlja index.xhtml stranicu
 *
 * @author tsaghir
 */
@ManagedBean
@SessionScoped
public class IndexStranica implements Serializable {

    private String korisnik;
    private String lozinka;

    /**
     * konstruktor
     */
    public IndexStranica() {
    }

    /**
     * Metoda koja provjerava korisnika u bazi podataka i vrši se prijava
     * korisnika
     */
    public void prijavaKorisnika() {
        if (DataBaseHelper.provjeriKorinika(korisnik, lozinka)) {
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "pregledKorisnika.xhtml");
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.setAttribute("korisnik", korisnik);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Neispravna lozinka ili nepostojeći korisnik",
                    "Molim ponovite unos!"));
        }
    }

    /**
     * Metoda kojom se odjavljuje korisnik
     *
     * @return
     */
    public String odjavaKorisnika() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        session.removeAttribute("korisnik");
        korisnik = "";
        lozinka = "";
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml");
        return "logout";
    }

    /**
     * GETTERS AND SETTERS
     *
     * @return
     */
    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

}
