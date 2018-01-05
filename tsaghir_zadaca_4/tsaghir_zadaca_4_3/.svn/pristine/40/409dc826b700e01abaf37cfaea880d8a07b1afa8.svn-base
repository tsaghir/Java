package org.foi.nwtis.tsaghir.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import org.foi.nwtis.tsaghir.ejb.eb.Dnevnik;
import org.foi.nwtis.tsaghir.ejb.sb.DnevnikFacade;

/**
 *
 * @author tsaghir
 */
@Named(value = "pregledDnevnika")
@SessionScoped
public class PregledDnevnika implements Serializable{

    @EJB
    private DnevnikFacade dnevnikFacade;

    private List<Dnevnik> dnevnikLista = null;
    private Date vrijemeOd;
    private Date vrijemeDo;
    private String ipAdresa;
    private String trajanje;
    private String status;

    /**
     * konstruktor
     */
    public PregledDnevnika() {
    }

    /**
     * Metoda pomoću koje se vrši filtriranje podataka
     */
    public void trazi() {
        dnevnikLista.clear();
        if (vrijemeOd != null
                && vrijemeDo != null) {
            dnevnikLista.addAll(dnevnikFacade.traziVrijeme(vrijemeOd, vrijemeDo));
        }
        if(ipAdresa != null && !ipAdresa.isEmpty()){
            dnevnikLista.addAll(dnevnikFacade.traziIpAdresu(ipAdresa));
        }
        if(trajanje != null && !trajanje.isEmpty()){
            dnevnikLista.addAll(dnevnikFacade.traziTrajanje(trajanje));
        }
        if(status != null && !status.isEmpty()){
            dnevnikLista.addAll(dnevnikFacade.traziStatus(status));
        }
        if(vrijemeOd == null
                && vrijemeDo == null
                && ipAdresa.isEmpty()
                && trajanje.isEmpty()
                && status.isEmpty()){
            dnevnikLista = dnevnikFacade.findAll();
        }
    }

    /**
     * GETTERS AND SETTERS
     * @return 
     */
    public List<Dnevnik> getDnevnikList() {
        if (dnevnikLista == null) {
            dnevnikLista = new ArrayList<>();
            dnevnikLista = dnevnikFacade.findAll();
        }
        return dnevnikLista;
    }

    public void setDnevnikList(List<Dnevnik> dnevnikList) {
        this.dnevnikLista = dnevnikList;
    }

   
    public Date getVrijemeOd() {
        return vrijemeOd;
    }

    public void setVrijemeOd(Date vrijemeOd) {
        this.vrijemeOd = vrijemeOd;
    }

    public Date getVrijemeDo() {
        return vrijemeDo;
    }

    public void setVrijemeDo(Date vrijemeDo) {
        this.vrijemeDo = vrijemeDo;
    }

    public String getIpAdresa() {
        return ipAdresa;
    }

    public void setIpAdresa(String ipAdresa) {
        this.ipAdresa = ipAdresa;
    }

    public String getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
