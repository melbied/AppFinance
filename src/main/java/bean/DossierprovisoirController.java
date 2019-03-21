package bean;

import model.Dossierprovisoir;
import bean.util.JsfUtil;
import bean.util.JsfUtil.PersistAction;
import java.io.IOException;
import session.DossierprovisoirFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.Query;
import model.Dossierhsupp;
import model.Dossiervacataire;
import model.Intervenant;

import model.Comptebc;
import model.Dotationsecteur;
import model.Graddiplome;
import model.Releve;
import model.Users;
import org.primefaces.event.FlowEvent;


@Named("dossierprovisoirController")
@SessionScoped
public class DossierprovisoirController implements Serializable {
    
    @EJB
    private session.IntervenantFacade ejbIntervenant;
     @EJB
    private session.ComptebcFacade ejbcomptebc;
    @EJB
    private session.DossiervacataireFacade ejbVacataire;
    
    @EJB
    private session.DossierhsuppFacade ejbHSupp;
    @EJB
    private session.DotationsecteurFacade ejbDotation;

    @EJB
    private session.DossierprovisoirFacade ejbFacade;
    
    @EJB
    private session.GraddiplomeFacade ejbGP;
    private List<Dossierprovisoir> items = null;
    private Dossierprovisoir selected;
    
    private Intervenant intervenant = new Intervenant();
    private Dossierhsupp dossierhsupp = new Dossierhsupp();
    private Dossiervacataire dossiervacataire = new Dossiervacataire();
    private Comptebc comptebc = new Comptebc();
    private String rib;
    private String bc;
    private int net;

    public DossierprovisoirController() {
    }
    
    public Comptebc getComptebc() {
        return comptebc;
    }

    public String getRib() {
        return rib;
    }
    

    public String getBc() {
        return bc;
    }
    
    public Graddiplome getTaux(){
      String sql = "SELECT d from Graddiplome d where d.intituleGrade = '"+selected.getIdGrade().getIntituleGrade()+"' ";
        Query q = ejbGP.getEntityManager().createQuery(sql);
        return (Graddiplome) q.getSingleResult();
    }

    public int getNet() {
       return getTaux().getTaux()*selected.getNbrHeures();
    }
    

    
    public void setNet(int net) {
        this.net = net;
    }
    

    public void setBc(String bc) {
        this.bc = bc;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }
    
    public void setComptebc(Comptebc comptebc) {
        this.comptebc = comptebc;
    }
    public void setIntervenant(Intervenant intervenant) {
        this.intervenant = intervenant;
    }

    public void setDossierhsupp(Dossierhsupp dossierhsupp) {
        this.dossierhsupp = dossierhsupp;
    }

    public void setDossiervacataire(Dossiervacataire dossiervacataire) {
        this.dossiervacataire = dossiervacataire;
    }


    public Intervenant getIntervenant() {
        return intervenant;
    }

    public Dossierhsupp getDossierhsupp() {
        return dossierhsupp;
    }

    public Dossiervacataire getDossiervacataire() {
        return dossiervacataire;
    }

    //Fonctions personalisées 
    
      public List<Dossierprovisoir> dos_prov_false(){
        String sql = "SELECT d from Dossierprovisoir d WHERE d.etat = 0";
        Query q = getFacade().getEntityManager().createQuery(sql);
        return q.getResultList();
    }
      
    public void initredirection(Dossierprovisoir dp,int a){
        selected = dp;
        intervenant = new Intervenant();
        intervenant.setNomComplet(dp.getNomComplet());
        try {
            if(a == 1){
                dossiervacataire = new Dossiervacataire();
                dossiervacataire.setIdDossierProv(selected);
                dossiervacataire.setSemestre(dp.getIdRelever().getSemestre());
                dossiervacataire.setNbrHeures(dp.getNbrHeures());
                FacesContext.getCurrentInstance().getExternalContext().redirect("creerVacation.xhtml?faces-redirect=true");
            }else{
                dossierhsupp = new Dossierhsupp();
                dossierhsupp.setIdDossierProv(selected);
                dossierhsupp.setSemestre(dp.getIdRelever().getSemestre());
                dossierhsupp.setNbrHeures(dp.getNbrHeures());
                FacesContext.getCurrentInstance().getExternalContext().redirect("creerHSupp.xhtml?faces-redirect=true");
            }
        } catch (IOException ex) {
                Logger.getLogger(DossierprovisoirController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    


    public String onFlowProcess(FlowEvent event) {
        
        return event.getNewStep();
        
    }
    public void EditDossierProv(Dossierprovisoir dp){
        selected = dp;
    }
    public void handleBlurEvent(){
        FacesMessage msg ;
        if(ejbIntervenant.find(intervenant.getCinPpr()) != null){
            intervenant = ejbIntervenant.find(intervenant.getCinPpr());
            
            msg = new FacesMessage("Intervenant Déja Existe", "CIN PPR : " + intervenant.getCinPpr() + ", Nom : " + intervenant.getNomComplet());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    /*public List<Comptebc> getCompteBC()
    {
        try {
            Query req = em.createQuery("SELECT o FROM Comptebc o where o.idCptBc :us").setParameter("us",1);
            items = (List<Comptebc>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! Aucune Commande de ce type n'est en cours de traitement !", "Secteur Innexistant"));
        }
        return items;
    }*/
      
     public void wizaedfinish(){
        try {
            Users u = new Users();
            u.setIdUser(2);
            this.dossiervacataire.setDateCreance(new Date());
            this.dossiervacataire.setStatutDossier("N");
            this.dossiervacataire.setIdDotation(ejbDotation.find(1));
            Comptebc comptebc = new Comptebc();
          //  cpt
          //comptebc = ejbcomptebc.find(2);
           // this.comptebc.setIdCptBc(null);
             comptebc.setCinPpr(this.intervenant);
             comptebc.setIntitule(null);
            comptebc.setRib(rib);
            comptebc.setBc(bc);
           
           
            this.intervenant.setCinPpr(intervenant.getCinPpr());
            this.intervenant.setIdUser(u);
            this.intervenant.setMail(intervenant.getMail());
            
           ejbcomptebc.edit(comptebc);
           
           ejbIntervenant.edit(this.intervenant);
           
           intervenant = ejbIntervenant.find(intervenant.getCinPpr());
            List<Comptebc> cb = this.intervenant.getComptebcList();
                    cb.add(comptebc);
            
            dossiervacataire.setCinPpr(this.intervenant);
            dossiervacataire.setIdGrade(selected.getIdGrade());
            dossiervacataire.setNet(getNet());
            ejbVacataire.edit(this.dossiervacataire);
            //selected.setEtat(Boolean.TRUE);
            
            DossiervacataireController dv = new DossiervacataireController();
            dv.update();
            dv.create();
            
            JsfUtil.addSuccessMessage("Dossier vacataire bien ajouté");
            FacesContext.getCurrentInstance().getExternalContext().redirect("dossierIntervenant.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(DossierprovisoirController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
      public void wizaedfinish2(){
        try {
            Users u = new Users();
            u.setIdUser(2);
            double mont = reports.ReportUtils.calculerMontant(this.dossierhsupp);
            this.dossierhsupp.setMontantHsupp((int)mont);
            this.dossierhsupp.setStatutDossier("N");
            this.dossierhsupp.setDateCreance(new Date());
            this.dossierhsupp.setIdDotation(ejbDotation.find(1));
            this.comptebc.setCinPpr(this.intervenant);
            this.intervenant.setIdUser(u);
            this.intervenant.getComptebcList().add(this.comptebc);
            ejbIntervenant.edit(this.intervenant);
            dossierhsupp.setCinPpr(this.intervenant);

            ejbHSupp.edit(this.dossierhsupp);
            
            selected.setEtat(Boolean.TRUE);
            update();
            JsfUtil.addSuccessMessage("Dossier d'heure supplémentaire bien ajouter");
            FacesContext.getCurrentInstance().getExternalContext().redirect("dossierIntervenant.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            JsfUtil.addSuccessMessage("Error au cour de traitement");
            Logger.getLogger(DossierprovisoirController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Dossierprovisoir getSelected() {
        return selected;
    }

    public void setSelected(Dossierprovisoir selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DossierprovisoirFacade getFacade() {
        return ejbFacade;
    }

    public Dossierprovisoir prepareCreate() {
        selected = new Dossierprovisoir();
        initializeEmbeddableKey();
        return selected;
    }
    
    public Releve getreleve()
    {
        String sql = "SELECT d from Releve d WHERE d.idRelever = 1";
        Query q = getFacade().getEntityManager().createQuery(sql);
        return (Releve)q.getSingleResult();
    }
    

    public void create() {
        selected.setEtat(Boolean.FALSE);
        selected.setIdRelever(getreleve());
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DossierprovisoirCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DossierprovisoirUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DossierprovisoirDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Dossierprovisoir> getItems() {
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

    public Dossierprovisoir getDossierprovisoir(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Dossierprovisoir> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Dossierprovisoir> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Dossierprovisoir.class)
    public static class DossierprovisoirControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DossierprovisoirController controller = (DossierprovisoirController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "dossierprovisoirController");
            return controller.getDossierprovisoir(getKey(value));
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
            if (object instanceof Dossierprovisoir) {
                Dossierprovisoir o = (Dossierprovisoir) object;
                return getStringKey(o.getIdDossierProv());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Dossierprovisoir.class.getName()});
                return null;
            }
        }

    }

}
