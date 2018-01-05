package org.foi.nwtis.tsaghir.ejb.sb;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.foi.nwtis.tsaghir.ejb.eb.Dnevnik;
import org.foi.nwtis.tsaghir.ejb.eb.Dnevnik_;

/**
 *
 * @author tsaghir
 */
@Stateless
public class DnevnikFacade extends AbstractFacade<Dnevnik> {

    @PersistenceContext(unitName = "zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DnevnikFacade() {
        super(Dnevnik.class);
    }
    /**
     * Metoda koja traži IP adresu u bazi podataka
     * @param ipAdresa
     * @return 
     */
    public List<Dnevnik> traziIpAdresu(String ipAdresa) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Dnevnik> query = cb.createQuery(Dnevnik.class);
        Root<Dnevnik> dnevnikRoot = query.from(Dnevnik.class);
        Predicate predicate = cb.like(dnevnikRoot.get(Dnevnik_.ipadresa).as(String.class), "%"+ipAdresa+"%");
        query.where(predicate);
        TypedQuery<Dnevnik> tquery = em.createQuery(query);
        List<Dnevnik> dnevnikLista = tquery.getResultList();
        return dnevnikLista;
    }
    
    /**
     * Metoda koja traži status u adresi
     * @param status
     * @return 
     */
    public List<Dnevnik> traziStatus(String status) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Dnevnik> query = cb.createQuery(Dnevnik.class);
        Root<Dnevnik> promjeneRoot = query.from(Dnevnik.class);
        query.where(cb.equal(promjeneRoot.get(Dnevnik_.status), Integer.parseInt(status)));
        List<Dnevnik> dnevnikLista = em.createQuery(query).getResultList();
        return dnevnikLista;
    }
    
    /**
     * Metoda koja traži trajanje u bazi podataka
     * @param trajanje
     * @return 
     */
    public List<Dnevnik> traziTrajanje(String trajanje) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Dnevnik> query = cb.createQuery(Dnevnik.class);
        Root<Dnevnik> dnevnikRoot = query.from(Dnevnik.class);
        query.where(cb.equal(dnevnikRoot.get(Dnevnik_.status), Integer.parseInt(trajanje)));
        List<Dnevnik> dnevnikLista = em.createQuery(query).getResultList();
        return dnevnikLista;
    }
    
    /**
     * Metoda koja traži vrijeme u bazi podataka
     * @param vrijemeOd početak
     * @param vrijemeDo kraj
     * @return 
     */
    public List<Dnevnik> traziVrijeme(Date vrijemeOd, Date vrijemeDo) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Dnevnik> query = cb.createQuery(Dnevnik.class);
        Root<Dnevnik> dnevnikRoot = query.from(Dnevnik.class);
        query.where(cb.between(dnevnikRoot.get(Dnevnik_.vrijeme),vrijemeOd,vrijemeDo));
        List<Dnevnik> dnevnikLista = em.createQuery(query).getResultList();
        return dnevnikLista;
    }
    
}
