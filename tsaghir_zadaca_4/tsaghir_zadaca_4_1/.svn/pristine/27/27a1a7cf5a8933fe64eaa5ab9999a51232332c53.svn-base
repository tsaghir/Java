package org.foi.nwtis.tsaghir.ejb.sb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.foi.nwtis.tsaghir.ejb.eb.Uredaji;

/**
 *
 * @author tsaghir
 */
@Stateless
public class UredajiFacade extends AbstractFacade<Uredaji> {

    @PersistenceContext(unitName = "zadaca_4_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UredajiFacade() {
        super(Uredaji.class);
    }
}
