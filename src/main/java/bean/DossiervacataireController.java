package bean;

import model.Dossiervacataire;
import bean.util.JsfUtil;
import bean.util.JsfUtil.PersistAction;
import java.io.IOException;
import session.DossiervacataireFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.*;
import model.Anneebudgetaire;
import model.Bordereauautorisation;
import model.Bordereaucomptable;
import model.Comptebc;
import model.Dossierhsupp;
import model.Dotationsecteur;
import model.Graddiplome;
import model.Intervenant;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FlowEvent;
import session.ComptebcFacade;
import session.GraddiplomeFacade;

@Named("dossiervacataireController")
@SessionScoped
public class DossiervacataireController implements Serializable {

    @EJB
    private session.DossiervacataireFacade ejbFacade;
    @EJB
    private session.ComptebcFacade ejbCompteFacade;
    @EJB
    private session.GraddiplomeFacade ejbGP;

    public GraddiplomeFacade getEjbGP() {
        return ejbGP;
    }

    public void setEjbGP(GraddiplomeFacade ejbGP) {
        this.ejbGP = ejbGP;
    }
    public ComptebcFacade getEjbCompteFacade() {
        return ejbCompteFacade;
    }

    public void setEjbCompteFacade(ComptebcFacade ejbCompteFacade) {
        this.ejbCompteFacade = ejbCompteFacade;
    }
    @EJB
    private session.BordereauautorisationFacade ejbBordereauAuto;
    @EJB
    private session.IntervenantFacade ejbIntervenant;
    @EJB
    private session.DotationsecteurFacade ejbDotation;

    private List<Dossiervacataire> items = null;
    private Dossiervacataire selected;
    private Comptebc compte;
    
    private List<Dossierhsupp> droppedHsupp = new ArrayList<Dossierhsupp>();
    private List<Dossiervacataire> droppedVacat = new ArrayList<Dossiervacataire>();
    private List<Dossierhsupp> hsupps = null;
    private List<Dossiervacataire> vacats = null;

    public DossiervacataireController() {
    }

    public Comptebc getCompte() {
        return compte;
    }

    public void setCompte(Comptebc compte) {
        this.compte = compte;
    }

    public List<Dossierhsupp> getDroppedHsupp() {
        return droppedHsupp;
    }

    public void setDroppedHsupp(List<Dossierhsupp> droppedHsupp) {
        this.droppedHsupp = droppedHsupp;
    }

    public List<Dossiervacataire> getDroppedVacat() {
        return droppedVacat;
    }

    public void setDroppedVacat(List<Dossiervacataire> droppedVacat) {
        this.droppedVacat = droppedVacat;
    }

    public List<Dossierhsupp> getHsupps() {
        if (hsupps == null) {
            hsupps = getFacade().hsuppbordauto();
        }
        return hsupps;
    }

    public void setHsupps(List<Dossierhsupp> hsupps) {
        this.hsupps = hsupps;
    }

    public List<Dossiervacataire> getVacats() {
        if (vacats == null) {
            vacats = getFacade().vactbordauto();
        }
        return vacats;
    }

    public void setVacats(List<Dossiervacataire> vacats) {
        this.vacats = vacats;
    }

    public Dossiervacataire getSelected() {
        return selected;
    }

    public void setSelected(Dossiervacataire selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DossiervacataireFacade getFacade() {
        return ejbFacade;
    }

    public Dossiervacataire prepareCreate() {
        selected = new Dossiervacataire();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DossiervacataireCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public String prepareUpdate(Dossiervacataire dv){
        selected = dv;
        Query q =  ejbCompteFacade.getEntityManager().createQuery("select c from Comptebc c where c.cinPpr = :cin").setParameter("cin", dv.getCinPpr());
        compte = (Comptebc) q.getSingleResult();
        return "ModifierVacataire.xhtml";
    }
    
     public void calculnet(ValueChangeEvent event) throws RollbackException, NotSupportedException, SystemException, HeuristicMixedException {
       this.selected.setNet(getTaux().getTaux()*selected.getNbrHeures()); 
       
    }
     
    public Graddiplome getTaux(){
      String sql = "SELECT d from Graddiplome d where d.intituleGrade = '"+selected.getIdGrade().getIntituleGrade()+"' ";
        Query q = ejbGP.getEntityManager().createQuery(sql);
        return (Graddiplome) q.getSingleResult();
    }
    
    public void wizardUpdate(){
        
        try {
            ejbIntervenant.edit(this.selected.getCinPpr());
            ejbFacade.edit(this.selected);
            
            FacesContext.getCurrentInstance().getExternalContext().redirect("ListVacataire.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(DossiervacataireController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DossiervacataireUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DossiervacataireDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Dossiervacataire> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Dossiervacataire getDossiervacataire(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Dossiervacataire> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Dossiervacataire> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Dossiervacataire.class)
    public static class DossiervacataireControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DossiervacataireController controller = (DossiervacataireController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "dossiervacataireController");
            return controller.getDossiervacataire(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Dossiervacataire) {
                Dossiervacataire o = (Dossiervacataire) object;
                return getStringKey(o.getIdDossier());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Dossiervacataire.class.getName()});
                return null;
            }
        }

    }

    //  partie Admin ********
    
    public void validation(Dossiervacataire dv){
        dv.setStatutDossier("V");
        Dotationsecteur ds = dv.getIdDotation();
        double reliqua = ds.getReliquat();
        int m = dv.getNbrHeures() * dv.getIdGrade().getTaux();
        if(reliqua > m){
            ds.setReliquat(reliqua - m);
            ds.getDossiervacataireList().add(dv);
            ejbDotation.edit(ds);
            JsfUtil.addSuccessMessage("Vacataire est bien valider");
        }else{
            JsfUtil.addErrorMessage("Le montant est insuffisant !!!");
        }
        
    }
    
    // ******************
    
    public String onFlowProcess(FlowEvent event) {

        return event.getNewStep();

    }

    public void onHsuppDrop(DragDropEvent ddEvent) {
        try {
            Dossierhsupp hsupp = ((Dossierhsupp) ddEvent.getData());

            droppedHsupp.add(hsupp);
            hsupps.remove(hsupp);
        } catch (Exception e) {

        }

    }

    public void onVacatDrop(DragDropEvent ddEvent) {
        Dossiervacataire vacat = ((Dossiervacataire) ddEvent.getData());
        droppedVacat.add(vacat);
        vacats.remove(vacat);
    }

    public void genererBordereau() {
        try{
            Bordereauautorisation ba = new Bordereauautorisation();
            int year = Calendar.getInstance().get(Calendar.YEAR);
            ba.setAnneeUniversitaire(year);

            if (droppedHsupp.size() > 0) {
                for (Dossierhsupp dhs : droppedHsupp) {
                    dhs.setIdBordAut(ba);
                    ba.getDossierhsuppList().add(dhs);
                    ejbBordereauAuto.edit(ba);
                }
            }
            if (droppedVacat.size() > 0) {
                for (Dossiervacataire dv : droppedVacat) {
                    dv.setIdBordAut(ba);
                    ba.getDossiervacataireList().add(dv);
                    ejbBordereauAuto.edit(ba);
                }

            }

            JsfUtil.addSuccessMessage("Bordereau d'autorisation a été bien ajouter");
            
            FacesContext.getCurrentInstance().getExternalContext().redirect("bordereauAutorisation.xhtml?faces-redirect=true");
        }catch (IOException ex) {
            Logger.getLogger(DossiervacataireController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void imprimerBordereauAuto(Bordereauautorisation ba){
        try {
            List<Object> liste = new ArrayList<Object>();
            
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("annee", ba.getAnneeUniversitaire());
            
            for(Dossierhsupp dhs : ba.getDossierhsuppList()){
                Intervenant i = dhs.getCinPpr();
                System.out.println("***************");
                System.out.println("Nom Complet : " + i.getNomComplet());
                
                liste.add(i);
            }
            for(Dossiervacataire dv : ba.getDossiervacataireList()){
                Intervenant i = dv.getCinPpr();
                System.out.println("***************");
                System.out.println("Nom Complet : " + i.getNomComplet());
                liste.add(i);
            }
            
            reports.ReportUtils.exportarDOC("bordereauAutorisation", param, liste);
            
        } catch (JRException ex) {
            Logger.getLogger(DossierhsuppController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DossierhsuppController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void imprimerOP(Dossiervacataire dv, int a){
        try {
            System.out.println("test");
            double montant = dv.getIdGrade().getTaux() * dv.getNbrHeures();
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("montant", montant);
            param.put("mois", dv.getMois());
            param.put("compte", 68888000);
            param.put("nomComplet", dv.getCinPpr().getNomComplet());
            param.put("rip", dv.getCinPpr().getComptebcList().get(0).getRib());
            param.put("bc", dv.getCinPpr().getComptebcList().get(0).getBc());
            
            switch (a) {
                case 1:  // imprimer PDF
                    System.out.println("bean.DossiervacataireController.imprimerOP()");
                    reports.ReportUtils.exportarPDF("OP_vacation", param, new ArrayList<>());
                    break;
                case 2: // imprimer DOC
                    reports.ReportUtils.exportarDOC("OP_vacation", param, new ArrayList<Object>());
                    break;
                default: // imprimer Excel
                    reports.ReportUtils.exportarExcel("OP_vacation", param, new ArrayList<Object>());
                    break;
            }
            
        } catch (JRException ex) {
            Logger.getLogger(DossierhsuppController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DossierhsuppController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        public void imprimerOV(Dossiervacataire dv, int a){
            try {
                double montant = dv.getIdGrade().getTaux() * dv.getNbrHeures();
                HashMap<String, Object> param = new HashMap<String, Object>();
                param.put("montant", montant);
                param.put("nomComplet", dv.getCinPpr().getNomComplet());
                param.put("rip", dv.getCinPpr().getComptebcList().get(0).getRib());
                param.put("banque", dv.getCinPpr().getComptebcList().get(0).getBc());

                switch (a) {
                    case 1:  // imprimer PDF
                        reports.ReportUtils.exportarPDF("OV_Vacation", param, new ArrayList<Object>());
                        break;
                    case 2: // imprimer DOC
                        reports.ReportUtils.exportarDOC("OV_Vacation", param, new ArrayList<Object>());
                        break;
                    default: // imprimer Excel
                        reports.ReportUtils.exportarExcel("OV_Vacation", param, new ArrayList<Object>());
                        break;
                }

            } catch (JRException ex) {
                Logger.getLogger(DossierhsuppController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DossierhsuppController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

}
