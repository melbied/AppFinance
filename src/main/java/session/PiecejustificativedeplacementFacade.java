/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Piecejustificativedeplacement;

/**
 *
 * @author Marouane
 */
@Stateless
public class PiecejustificativedeplacementFacade extends AbstractFacade<Piecejustificativedeplacement> {

    @PersistenceContext(unitName = "FSSM_AppFinanciereFssm_war_2.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PiecejustificativedeplacementFacade() {
        super(Piecejustificativedeplacement.class);
    }
    
}
