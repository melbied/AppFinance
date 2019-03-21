/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Dossierhsupp;
import model.Dossiervacataire;

/**
 *
 * @author Marouane
 */
@Stateless
public class DossiervacataireFacade extends AbstractFacade<Dossiervacataire> {

    @PersistenceContext(unitName = "FSSM_AppFinanciereFssm_war_2.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DossiervacataireFacade() {
        super(Dossiervacataire.class);
    }
    public List<Dossierhsupp> hsuppbordauto(){
        String sql = "SELECT d from Dossierhsupp d WHERE d.idBordAut is null";
        Query q = getEntityManager().createQuery(sql);
        return q.getResultList();
   }
    public List<Dossiervacataire> vactbordauto(){
       String sql = "SELECT d from Dossiervacataire d WHERE d.idBordAut is null";
        Query q = getEntityManager().createQuery(sql);
        return q.getResultList();
   }
}
