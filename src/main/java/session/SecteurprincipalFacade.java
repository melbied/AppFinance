/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Secteurprincipal;

/**
 *
 * @author Marouane
 */
@Stateless
public class SecteurprincipalFacade extends AbstractFacade<Secteurprincipal> {

    @PersistenceContext(unitName = "FSSM_AppFinanciereFssm_war_2.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SecteurprincipalFacade() {
        super(Secteurprincipal.class);
    }
    
}
