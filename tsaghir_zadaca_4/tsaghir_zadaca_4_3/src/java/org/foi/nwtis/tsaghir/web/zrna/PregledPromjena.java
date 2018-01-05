package org.foi.nwtis.tsaghir.web.zrna;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import org.foi.nwtis.tsaghir.ejb.eb.Promjene;
import org.foi.nwtis.tsaghir.ejb.sb.PromjeneFacade;

/**
 *
 * @author tsaghir
 */
@Named(value = "pregledPromjena")
@SessionScoped
public class PregledPromjena implements Serializable {

    @EJB
    private PromjeneFacade promjeneFacade;

    private List<Promjene> promjeneList = null;
    private String id = null;
    private String naziv = null;

    /**
     * konstruktor
     */
    public PregledPromjena() {
    }

    /**
     * Metoda pomoću koje se vrši filtriranje podataka
     */
    public void trazi() {
        promjeneList.clear();
        if (id != null && !id.isEmpty()) {
            promjeneList.addAll(promjeneFacade.traziId(id));
        }
        if (naziv != null && !naziv.isEmpty()) {
            promjeneList.addAll(promjeneFacade.traziNaziv(naziv));
        }
        if (naziv.isEmpty()
                && id.isEmpty()) {
            promjeneList = promjeneFacade.findAll();
        }
    }

    public List<Promjene> getPromjeneList() {
        if (promjeneList == null) {
            promjeneList = new ArrayList<>();
            promjeneList = promjeneFacade.findAll();
        }
        return promjeneList;
    }

    public void setPromjeneList(List<Promjene> promjeneList) {
        this.promjeneList = promjeneList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

}
