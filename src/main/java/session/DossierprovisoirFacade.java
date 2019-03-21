/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Dossierprovisoir;

/**
 *
 * @author Marouane
 */
@Stateless
public class DossierprovisoirFacade extends AbstractFacade<Dossierprovisoir> {

    @PersistenceContext(unitName = "FSSM_AppFinanciereFssm_war_2.0PU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public DossierprovisoirFacade() {
        super(Dossierprovisoir.class);
    }
    
}
