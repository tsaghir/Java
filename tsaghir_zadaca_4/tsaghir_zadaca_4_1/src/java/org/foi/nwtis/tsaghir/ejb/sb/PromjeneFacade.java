package org.foi.nwtis.tsaghir.ejb.sb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.foi.nwtis.tsaghir.ejb.eb.Promjene;
import org.foi.nwtis.tsaghir.ejb.eb.Promjene_;

/**
 *
 * @author tsaghir
 */
@Stateless
public class PromjeneFacade extends AbstractFacade<Promjene> {

    @PersistenceContext(unitName = "zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PromjeneFacade() {
        super(Promjene.class);
    }

    /**
     * Metoda preko koje tražimo ID u bazi podataka
     * @param id uređaja
     * @return 
     */
    public List<Promjene> traziId(String id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Promjene> query = cb.createQuery(Promjene.class);
        Root<Promjene> promjeneRoot = query.from(Promjene.class);
        query.where(cb.equal(promjeneRoot.get(Promjene_.id), Integer.parseInt(id)));
        List<Promjene> promjeneLista = em.createQuery(query).getResultList();
        return promjeneLista;
    }

    /**
     * Metoda s kojom tražimo naziv u bazi podataka
     * @param naziv
     * @return 
     */
    public List<Promjene> traziNaziv(String naziv) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Promjene> cq = cb.createQuery(Promjene.class);
        Root<Promjene> promjeneRoot = cq.from(Promjene.class);
        Predicate predicate = cb.like(promjeneRoot.get(Promjene_.naziv).as(String.class), "%"+naziv+"%");
        cq.where(predicate);
        TypedQuery<Promjene> tquery = em.createQuery(cq);
        List<Promjene> promjeneLista = tquery.getResultList();
        return promjeneLista;
    }
}
