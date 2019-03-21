package bean;

import model.Lignecommande;
import bean.util.JsfUtil;
import bean.util.JsfUtil.PersistAction;
import java.io.IOException;
import session.LignecommandeFacade;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Article;
import model.Boncommande;
import model.Compte;
import model.Dotationsecteur;
import model.Fournisseur;
import model.Secteur;
import model.Secteurprincipal;
import model.Users;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import session.BoncommandeFacade;


@Named("lignecommandeController")
@SessionScoped
public class LignecommandeController implements Serializable {

    @PersistenceContext(unitName = "FSSM_AppFinanciereFssm_war_2.0PU")
    private EntityManager em;
     @Inject
    UserTransaction ut;
    @EJB
    private session.LignecommandeFacade ejbFacade;
    @EJB
    private session.BoncommandeFacade ejbFacadeBC;

    public BoncommandeFacade getEjbFacadeBC() {
        return ejbFacadeBC;
    }

    public void setEjbFacadeBC(BoncommandeFacade ejbFacadeBC) {
        this.ejbFacadeBC = ejbFacadeBC;
    }
    
    private List<Lignecommande> items = null;
    private Lignecommande selected = new Lignecommande();
    private String description;
    private Lignecommande current = new Lignecommande();
    private int qte =1 ;
    private int bc;
    private double prixnegocie=0.0;
    private double HTtotal;
    private HtmlInputText inputComponent = new HtmlInputText();

    public HtmlInputText getInputComponent() {
        return inputComponent;
    }

    public void setInputComponent(HtmlInputText inputComponent) {
        this.inputComponent = inputComponent;
    }

    public double getHTtotal() {
        return HTtotal;
    }

    public void setHTtotal(double HTtotal) {
        this.HTtotal = HTtotal;
    }
   public void onQuantityChange(ValueChangeEvent event) throws RollbackException, NotSupportedException, SystemException, HeuristicMixedException {
        int qtt = (int) event.getNewValue();
        Integer idligne =
        (Integer) ((UIInput) event.getSource()).getAttributes().get("idligne");
        
        System.out.println(idligne);
        Lignecommande lg = em.find(Lignecommande.class, idligne);
        lg.setQuantite(qtt);
        lg.setMontant(qtt*lg.getPu());
        getFacade().edit(lg);
        int idb = lg.getIdBC();
        Boncommande bc = em.find(Boncommande.class,idb);
        Query qr = ejbFacadeBC.getEntityManager().createQuery("select o from Lignecommande o where o.idBC = :bc").setParameter("bc", bc.getIdBC());
        List<Lignecommande> listLignecmd = qr.getResultList();
        double mHT = 0;
        for (Lignecommande lc : listLignecmd) {
                mHT = mHT + lc.getPu() * lc.getQuantite();
            }
        double tVa= mHT /100 * bc.getTva();
        bc.setMontant(mHT+tVa);
        
        try {
        ut.begin();
        ejbFacadeBC.getEntityManager().merge(bc);
        ut.commit();
        //inputComponent.setValue(lg.getMontant());
        //BoncommandeController bcc =new BoncommandeController();
        //bcc.subjectSelectionChanged();
        
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(LignecommandeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(LignecommandeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(LignecommandeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
     
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = getPrixnegocie();
         System.out.println("love you");
         System.out.println("love you");
         System.out.println(newValue +" "+ getPrixnegocie());
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    public void onValueQuantityChange(ValueChangeEvent event) throws RollbackException, NotSupportedException, SystemException, HeuristicMixedException {
        prixnegocie = (Double) event.getNewValue();
        Integer idligne =
        (Integer) ((UIInput) event.getSource()).getAttributes().get("idligne");
        
        System.out.println(idligne);
        Lignecommande lg = em.find(Lignecommande.class, idligne);
        lg.setPu(prixnegocie);
        lg.setMontant(prixnegocie*lg.getQuantite());
        getFacade().edit(lg);
        int idb = lg.getIdBC();
        Boncommande bc = em.find(Boncommande.class,idb);
        Query qr = ejbFacadeBC.getEntityManager().createQuery("select o from Lignecommande o where o.idBC = :bc").setParameter("bc", bc.getIdBC());
        List<Lignecommande> listLignecmd = qr.getResultList();
        double mHT = 0;
        for (Lignecommande lc : listLignecmd) {
                mHT = mHT + lc.getPu() * lc.getQuantite();
            }
        double tVa= mHT /100 * bc.getTva();
        bc.setMontant(mHT+tVa);
        
        try {
        ut.begin();
        ejbFacadeBC.getEntityManager().merge(bc);
        ut.commit();
        //inputComponent.setValue(lg.getMontant());
        //BoncommandeController bcc =new BoncommandeController();
        //bcc.subjectSelectionChanged();
        
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(LignecommandeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(LignecommandeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(LignecommandeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public String getDescription() {
        return description;
    }

    public double getPrixnegocie() {
        return prixnegocie;
    }

    public void setPrixnegocie(int prixnegocie) {
        this.prixnegocie = prixnegocie;
    }

    
    public void setDescription(String description) {
        this.description = description;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public LignecommandeFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(LignecommandeFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public Lignecommande getCurrent() {
        return current;
    }

    public void setCurrent(Lignecommande current) {
        this.current = current;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }
    
    public void init()
    {
        this.bc = MaxBC();
       // selected = prepareCreate();
    }
    
    public LignecommandeController() {
        
    }

    public Lignecommande getSelected() {
        return selected;
    }

    public int getBc() {
        return MaxBC();
    }

    public void setBc(int bc) {
        this.bc = bc;
    }

    public void setSelected(Lignecommande selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private LignecommandeFacade getFacade() {
        return ejbFacade;
    }

    public String prepareCreate() {
        current = new Lignecommande();
        //selectedItemIndex = -1;
        return "ChoixArticles.xhtml";
    }
    
        public String create() {
        try {
            Article a = getArticle();
            if (a == null) {
                ut.begin();
                em.joinTransaction();
                Article art = new Article();
                art.setDescription(description);
                art.setIdArticle(null);
                art.setPu(0.0);
                em.persist(art);
                ut.commit();
                //current.setIdArticle(a.getIdArticle());
                System.out.println(art.getIdArticle());
                System.out.println(art.getIdArticle());
                a=getArticle();
            }
            current.setIdLigne(null);
            current.setIdBC(MaxBC());
            current.setQuantite(qte);
            current.setMontant(a.getPu()*qte);
            current.setIdArticle(a.getIdArticle());
            current.setPu(a.getPu());
            //urrent.setIdArticle(getArticle().getIdArticle());
            
            getFacade().edit(current);
            this.items = getItemes();
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Ligne created"));
            return prepareCreate();
        } catch (Exception e) {
           // JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Ligne created"));
            return null;
        }
    }
        
        public String valider()
        {
           
            return "LancerDemande.xhtml";
        }
                
        public Article getArticle() {
        Article c = null;
        try {
            Query req = em.createQuery("SELECT o FROM Article o WHERE o.description = :des").setParameter("des", description);
            c = (Article) req.getSingleResult();
            return c;
        } catch (Exception e) {
                    
            // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Article innexistant !", "erreur"));
            return null;
        }

    }
        
    public Users getUser() {
        Users user = null;
        /*Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Object login = session.getAttribute("login");
        Object password = session.getAttribute("password"); 
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info" + login, "PrimeFaces Rocks." + password));     
        Query req = getFacade().em().createQuery("SELECT o FROM Users o where o.login= :log").setParameter("log", login.toString());
        user = (Users) req.getSingleResult();*/
        if (user == null) {
            user = new Users();
            user.setIdUser(2);
        }
        return user;
    }
            
    public int MaxBC() {
        try {
            Query req = em.createQuery("select max(a.idBC) from Boncommande a");
            int max = (Integer) req.getSingleResult();
            if (max != 0) {
                return max;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
        public Boncommande BCMAX(int max) {
        try {
            Query req = em.createQuery("select a from Boncommande a where a.idBC = :max").setParameter("max", max);
            Boncommande bc = (Boncommande) req.getSingleResult();
            if (max != 0) {
                return bc;
            }
            return bc;
        } catch (Exception e) {
            e.printStackTrace();
             return null;
        }
       
    }
        
    public List<Lignecommande> getItemes() {
        try {
            Query req2 = em.createQuery("SELECT o FROM Boncommande o where o.idBC=:bc").setParameter("bc", MaxBC());
            Boncommande ligne = (Boncommande) req2.getSingleResult();
            this.items = new ArrayList<>();
            if (ligne.getEtat().equals("enCours") || ligne.getEtat().equals("invalide")) {
                Users user = getUser();
                Query req = em.createQuery("SELECT o FROM Lignecommande o where  o.idBC in(select bc.idBC from Boncommande bc where bc.idBC= :idbc and  bc.idUser.idUser = :us)").setParameter("idbc", ligne.getIdBC()).setParameter("us", user.getIdUser());
                List<Lignecommande> l = (List<Lignecommande>) req.getResultList();
                items = l;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "cette commande est en cours de Traitement ! !", "Information"));
            }
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Veillez choisir un numero de commande !", "Information"));
        }
        return items;
    }
    
     public void subjectSelectionChangedUnite() {
         Article a = getArticle();
         
         this.selected.setIdArticle(a.getIdArticle());
         this.selected.setPu(a.getPu());
         this.selected.setMontant(selected.getPu()*selected.getQuantite());
         
         /*   if (current instanceof Lignecommande && current != null) {
            current.setQuantite(qte);
            current.setIdBC(bc);
            //Boncommande bc = getBC();
            Article a = getArticle();
            current = new Lignecommande();
             current.setPu(a.getPu());
             current.setIdArticle(a.getIdArticle());
             
        /*    Lignecommande lc = null;
            try {
                Query req = em.createQuery("SELECT o FROM Lignecommande o WHERE o.idBC = :idbc and o.idArticle in (select a.idArticle from Article a where a.description= :des)").setParameter("idbc", bc).setParameter("des", this.description);
                lc = (Lignecommande) req.getSingleResult();
            } catch (Exception e) {
                current.setIdLigne(null);
                e.printStackTrace();

            }
            if ( bc != 0) {
                if (a instanceof Article && a != null) {
                    current.setPu(a.getPu());
                    current.setIdArticle(a.getIdArticle());

                    if (lc != null && lc instanceof Lignecommande) {
                        current.setIdBC(lc.getIdBC());
                        current.setIdLigne(null);

                    } else {
                        current.setIdLigne(null);

                    }
                } else {
                    current.setPu(0.0);
                    current.setIdLigne(null);

                }
            }
        } else {
            current = new Lignecommande();
            current.setIdBC(bc);
            current.setQuantite(qte);
        }*/
       
    }



    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LignecommandeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("LignecommandeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Lignecommande> getItems() {
        if (items == null) {
            items = getItemes();
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

    public Lignecommande getLignecommande(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Lignecommande> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Lignecommande> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
     private List<Lignecommande> listOfUsers = new ArrayList<Lignecommande>();
    HttpServletResponse httpServletResponse;
    ServletOutputStream servletOutputStream;

    JasperPrint jasperPrint;

    public void initBon() throws JRException {
        //subjectSelectionChanged();
        
        try {
        HashMap map = new HashMap();
           
        map.put("BC",getBc() );
        
        //JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listOfUsers, false);
        //listOfUsers = new ArrayList<>();
        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("report/report4.jasper");
   
        String url = "jdbc:mysql://localhost/bd_finance";
        Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        Connection conn = DriverManager.getConnection (url, "root","root");
        jasperPrint = JasperFillManager.fillReport(reportPath, map,  conn);
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
           System.out.println("initbon");
            System.out.println(getBc());
        } catch (Exception e) {
            e.printStackTrace();
             JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        
    }

    public void PDBon(ActionEvent actionEvent) throws JRException, IOException {
        initBon();
            
        //System.out.println(bc);System.out.println(bc);System.out.println(bc);System.out.println(bc);System.out.println(bc);
        httpServletResponse.setHeader("Content-disposition", "attachment; filename=Expression_besoin "+getBc()+".pdf");
        servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        FacesContext.getCurrentInstance().responseComplete();
        //System.out.println("pdbon");
    }

    @FacesConverter(forClass = Lignecommande.class)
    public static class LignecommandeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LignecommandeController controller = (LignecommandeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "lignecommandeController");
            return controller.getLignecommande(getKey(value));
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
            if (object instanceof Lignecommande) {
                Lignecommande o = (Lignecommande) object;
                return getStringKey(o.getIdLigne());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Lignecommande.class.getName()});
                return null;
            }
        }

    }

}
