package bean;

import model.Boncommande;
import bean.util.JsfUtil;
import bean.util.PaginationHelper;
import java.io.IOException;
import session.BoncommandeFacade;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import model.Article;
import model.Compte;
import model.Deplacement;
import model.Dotationsecteur;
import model.Fournisseur;
import model.Lignecommande;
import model.Secteur;
import model.Secteurprincipal;
import model.Users;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;



@Named("boncommandeController")
@SessionScoped
@ManagedBean
public class BoncommandeController implements Serializable {

    @PersistenceContext(unitName = "FSSM_AppFinanciereFssm_war_2.0PU")
    private EntityManager em;
    private boolean disablCreate = false;
    private boolean disablUpdate = true;
    private boolean disablDelete = true;
    private String secteurP;
    private String secteur;
    private Compte cpt;
    private Dotationsecteur ds;
    private Boncommande current;
    private String type;
    private String nomFournisseur;
    private Date dateRecep;
    private Double reliquatDS;
    private int idBC;
    private Date dateRec;
    private String etat;
    private Date datecmd;
    private double TTC;
    private String datecm;
    private long TRestant;
    @Inject
    UserTransaction ut;
    private List<Boncommande> items = null;
    private List<Boncommande> filteredItems =null ;
    private boolean disableOVImpr = false;

    public List<Boncommande> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Boncommande> filteredItems) {
        this.filteredItems = filteredItems;
    }
    private List<Boncommande> itemsValides = null;
    @EJB
    private session.BoncommandeFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
     private Boncommande selected;

    public void setSelected(Boncommande selected) {
        this.selected = selected;
    }

    public Boncommande getSelected() {
        return selected;
    }

    public BoncommandeController() {

    }

    public boolean isDisableOVImpr() {
        return disableOVImpr;
    }

    public void setDisableOVImpr(boolean disableOVImpr) {
        this.disableOVImpr = disableOVImpr;
    }

    public int calcultemps(Date dr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(dr);
        cal.add(Calendar.DATE, 45); // add 45 days
        Date dernierDelais = cal.getTime();
        Date auj = cal.getTime();
        Date dateStart = simpleDateFormat.parse("2017-06-17");
         Date dateEnd = simpleDateFormat.parse(dernierDelais.toString());

    //time is always 00:00:00 so rounding should help to ignore the missing hour when going from winter to summer time as well as the extra hour in the other direction
    long diff = (dateEnd.getTime() - dateStart.getTime()) / (long) 86400000;
    //Math.round(diff)
        return Math.toIntExact(diff);
    }

    public void setTRestant(long TRestant) {
        this.TRestant = TRestant;
    }
    
    public String getDatecm() {
        return mediumDateFormat.format(new Date()).toString();
    }

    public void setDatecm(String datecm) {
        this.datecm = datecm;
    }
    
DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
DateFormat.MEDIUM,
DateFormat.MEDIUM);

    public Date getDatecmd() {
        return datecmd;
    }

    public void setDatecmd(Date datecmd) {
        this.datecmd = datecmd;
    }

    public double getTTC() {
        return TTC;
    }
    
    public double calculTTC()
    {
        Query req4 = em.createQuery("select o from Lignecommande o where o.idBC= :bc").setParameter("bc", current.getIdBC());
            List<Lignecommande> list = req4.getResultList();
            Double HT = 0.0;
            for (Lignecommande lc : list) {
                HT = HT + lc.getMontant();
            }
            Double TTC = HT + HT * current.getTva() / 100;
            return TTC;
    }
    
    public List<Lignecommande> LigneByIDBC(int bc)
    {
        try {
            Query req = em.createQuery("SELECT o FROM Lignecommande o where o.idBC = :bc").setParameter("bc", idBC);
            List<Lignecommande> items = (List<Lignecommande>) req.getResultList();
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! "+ idBC +" Aucune commande n'est enregistrer pour l'utilisateur courant!", "aucun iteme"));
             return null;
        }
       
    }
    
    //PARTIE PAYER BON COMMANDE
    
    public void RECEPTONNERbc(int idvar){
        
        try {
            Query q = getFacade().getEntityManager().createQuery("SELECT bc from Boncommande bc WHERE bc.idBC = :idbc ").setParameter("idbc", idvar);
        Boncommande bc = (Boncommande) q.getSingleResult();
        bc.setEtat("reçu");
            //System.out.println(dateRecep);
        bc.setDateReception(dateRecep);
        getFacade().edit(bc);
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Le bon de commande est passé de validé à payé avec succés !", "Payement confirmé"));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur de confirmation !", "Erreur"));
        }
        
    }
    public void PAYERbc(int idvar){
        
        try {
            Query q = getFacade().getEntityManager().createQuery("SELECT bc from Boncommande bc WHERE bc.idBC = :idbc ").setParameter("idbc", idvar);
        Boncommande bc = (Boncommande) q.getSingleResult();
        bc.setEtat("payé");
        getFacade().edit(bc);
        //PDFOP();
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Le bon de commande est passé de validé à payé avec succés !", "Payement confirmé"));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur de confirmation !", "Erreur"));
        }
        
    }
    public void NONPAYERbc(int idvar){
          try {
         Query q = getFacade().getEntityManager().createQuery("SELECT bc from Boncommande bc WHERE bc.idBC = :idbc ").setParameter("idbc", idvar);
        Boncommande bc = (Boncommande) q.getSingleResult();
        bc.setEtat("non payé");
        getFacade().edit(bc);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Le bon de commande est passé de validé à non payé avec succés !", "Non Payement confirmé"));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur de confirmation !", "Erreur"));
        }
       
    }
    ///////////////
    public void setTTC(double TTC) {
        this.TTC = TTC;
    }
    
    

    public List<Boncommande> getItemsValides() {
        return getAllItemesVALIDE();
    }

    public void setItemsValides(List<Boncommande> itemsValides) {
        this.itemsValides = itemsValides;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
    
    

    public Dotationsecteur getDS() {
        Dotationsecteur d = null;
        try {
            Secteur sect = getSect();
            System.out.println(sect.getIdSecteur());
            System.out.println(cpt.getIdCompte());
            Query req = em.createQuery("SELECT o FROM Dotationsecteur o WHERE o.idSecteur.idSecteur = :sec and o.idCompte.idCompte= :cp").setParameter("sec", sect.getIdSecteur()).setParameter("cp", cpt.getIdCompte());
            d = (Dotationsecteur) req.getSingleResult();
            System.out.println(d.getIdDotation());
            return d;
        } catch (Exception e) {
            disablCreate = true;
            disablUpdate = true;
            disablDelete = true;
            e.printStackTrace();
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Se Secteur N'existe pas!", "Secteur Innexistant"));
            return null;
        }
    }

    public Fournisseur getFournisseur() {
        Fournisseur c = null;
        try {
            Query req = em.createQuery("SELECT o FROM Fournisseur o WHERE o.nom = :nom").setParameter("nom", this.nomFournisseur);
            c = (Fournisseur) req.getSingleResult();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! Fournisseur innexistant !", "erreur"));
        }
        return c;
    }

    public int MaxFournisseur() {
        try {
            Query req = em.createQuery("select max(a.idFournisseur) from Fournisseur a");
            int max = (Integer) req.getSingleResult();
            if (max != 0) {
                return max;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void remplireFormulaire() {
        Dotationsecteur ds = null;
        Secteur s = null;
        Secteurprincipal sp = null;
        try {
            if (cpt == null) {
                cpt = new Compte();
            }
            type = current.getType();
            this.idBC=current.getIdBC();
            dateRec=current.getDateReception();
            Query req = em.createQuery("SELECT o FROM Dotationsecteur o WHERE o.idDotation= :dot").setParameter("dot", current.getIdDotation().getIdDotation());
            ds = (Dotationsecteur) req.getSingleResult();
            cpt.setIdCompte(ds.getIdCompte().getIdCompte());
            Query req2 = em.createQuery("SELECT o FROM Secteur o WHERE o.idSecteur= :sec").setParameter("sec", ds.getIdSecteur());
            s = (Secteur) req2.getSingleResult();
            this.secteur = s.getIntituleSecteur();
            Query req3 = em.createQuery("SELECT o FROM Secteurprincipal o WHERE o.idSecteurP= :sp").setParameter("sp", s.getIdSecteurP());
            sp = (Secteurprincipal) req3.getSingleResult();
            this.secteurP = sp.getDesignation();
            Query req4 = em.createQuery("select o from Lignecommande o where o.idBC= :bc").setParameter("bc", current.getIdBC());
            List<Lignecommande> list = req4.getResultList();
            Query req5 = em.createQuery("SELECT o FROM Fournisseur o WHERE o.idFournisseur= :four").setParameter("four", current.getIdFournisseur());
            Fournisseur f = (Fournisseur) req5.getSingleResult();
            this.nomFournisseur = f.getNom();
            Double HT = 0.0;
            for (Lignecommande lc : list) {
                HT = HT + lc.getMontant();
            }
            Double TTC = HT + HT * current.getTva() / 100;
            current.setMontant(TTC);
            
            subjectSelectionChangedPBC();
        } catch (Exception e) {
            // e.printStackTrace();
            // JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void subjectSelectionChangedPBC() {
        //Boolean b = current instanceof Boncommande;
        /*if (current == null) {
            current = new Boncommande();
        }*/
        ds = null;
        ds = getDS();
        if (current instanceof Boncommande && current != null) {
            type = current.getType();
            if (ds != null) {
                Boncommande bc = getbon();
                Dotationsecteur d=new Dotationsecteur();
                Compte c=new Compte();
                Secteur s=new Secteur();
                c.setIdCompte(cpt.getIdCompte());
                s.setIdSecteur(ds.getIdSecteur().getIdSecteur());
                d.setIdSecteur(s);
                d.setIdCompte(c);
                d.setIdDotation(ds.getIdDotation());
                if (bc instanceof Boncommande && bc != null) {
                    Users user = getUser();
                    dateRec=bc.getDateReception();
                    if (user.getIdUser() == bc.getIdUser().getIdUser()) {
                        current.setIdBC(bc.getIdBC());
                        this.idBC=bc.getIdBC();
                        current.setDateCommande(bc.getDateCommande());
                        Users u=new Users();
                        u.setIdUser(user.getIdUser());
                        u.setLogin(user.getLogin());
                        current.setIdUser(u);
                        current.setDateReception(bc.getDateReception());
                        type = bc.getType();
                        disablCreate = true;
                        disablUpdate = false;
                        disablDelete = false;
                    } else {
                        current.setDateReception(dateRecep);
                        disablCreate = true;
                        disablUpdate = true;
                        disablDelete = true;
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! vous n'etes pas le proprietaire de cet Engagement !", "erreur"));
                    }
                }
            } else {
                disablCreate = true;
                disablUpdate = true;
                disablDelete = true;
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! secteur innexistant dans ce secteur principal", "erreur"));
            }
            current.setDateCommande(new Date());
        }
    }

     public void remplireFormulaireGBC() {
        Dotationsecteur ds = null;
        Secteur s = null;
        Secteurprincipal sp = null;
        try {
            if (cpt == null) {
                cpt = new Compte();
            }
            type = current.getType();
            dateRecep = current.getDateReception();
            Query req = em.createQuery("SELECT o FROM Dotationsecteur o WHERE o.idDotation = :do").setParameter("do", current.getIdDotation().getIdDotation());
            ds = (Dotationsecteur) req.getSingleResult();
            reliquatDS = ds.getReliquat();
            cpt.setIdCompte(ds.getIdCompte().getIdCompte());
            Query req2 = em.createQuery("SELECT o FROM Secteur o WHERE o.idSecteur= :sec").setParameter("sec", ds.getIdSecteur().getIdSecteur());
            s = (Secteur) req2.getSingleResult();
            this.secteur = s.getIntituleSecteur();
            Query req3 = em.createQuery("SELECT o FROM Secteurprincipal o WHERE o.idSecteurP= :sp").setParameter("sp", s.getIdSecteurP().getIdSecteurP());
            sp = (Secteurprincipal) req3.getSingleResult();
            this.secteurP = sp.getDesignation();
            Query req4 = em.createQuery("select o from Lignecommande o where o.idBC = :bc").setParameter("bc", current.getIdBC());
            List<Lignecommande> list = req4.getResultList();
            Query req5 = em.createQuery("SELECT o FROM Fournisseur o WHERE o.idFournisseur= :four").setParameter("four", current.getIdFournisseur());
            Fournisseur f = (Fournisseur) req5.getSingleResult();
            this.nomFournisseur = f.getNom();
            Double HT = 0.0;
            for (Lignecommande lc : list) {
                //HT = HT + lc.getMontant();
                HT = HT + lc.getPu() * lc.getQuantite();
            }
            Double TTC = HT + HT * current.getTva() / 100;
            current.setMontant(TTC);
            this.idBC=current.getIdBC();
            subjectSelectionChanged();
            getAllItemesVALIDE();
        } catch (Exception e) {
            e.printStackTrace();
            // JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void subjectSelectionChanged() {
        //Boolean b = current instanceof Boncommande;
        if (current == null) {
            current = new Boncommande();
        }
        ds = null;
        ds = getDS();
        if (current instanceof Boncommande && current != null) {
             type = current.getType();
            if (ds != null) {
                Boncommande bc = getbon();
                Dotationsecteur d=new Dotationsecteur();
                Compte c=new Compte();
                Secteur s=new Secteur();
                c.setIdCompte(ds.getIdCompte().getIdCompte());
                s.setIdSecteur(ds.getIdSecteur().getIdSecteur());
                d.setIdSecteur(s);
                d.setIdCompte(c);
                d.setIdDotation(ds.getIdDotation());
                current.setIdDotation(d);
                if (bc instanceof Boncommande && bc != null) {
                    current.setEtat(bc.getEtat());
                    Users user = getUser();
                    if (user.getIdUser() == bc.getIdUser().getIdUser()) {
                        current.setIdBC(bc.getIdBC());
                        current.setDateCommande(bc.getDateCommande());
                        Users u=new Users();
                        u.setIdUser(user.getIdUser());
                        u.setLogin(user.getLogin());
                        current.setIdUser(u);
                        current.setDateReception(bc.getDateReception());
                        type = bc.getType();
                        //current.setMontant(current.getMontant()+current.getMontant()*current.getTva()/100);
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! vous n'etes pas le proprietaire de cet Engagement !", "erreur"));
                    }
                }
            } else {
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! secteur innexistant dans ce secteur principal", "erreur"));
            }
        }
    }

    public Boncommande getbon() {
        ds = getDS();
        Boncommande bc = null;
        try {
            Query req2 = em.createQuery("SELECT o FROM Boncommande o WHERE o.idBC = :bc").setParameter("bc", this.idBC);
            bc = (Boncommande) req2.getSingleResult();
        } catch (Exception e) {
            disablCreate = false;
            disablUpdate = true;
            disablDelete = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bon de Commande inexistante!", "Information"));
            return null;
        }
        return bc;
    }

    public List<Boncommande> completeText(String id) {
        List<Boncommande> FiltredComptes = new ArrayList<Boncommande>();
        try {
            List<Boncommande> AllComptes = getAllItemes();
            for (Boncommande bc : AllComptes) {
                if (bc.getIdBC().toString().startsWith(id)) {
                    FiltredComptes.add(bc);
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! probleme de conversion !", "Erreur"));
        }
        return FiltredComptes;
    }

    public Secteur getSect() {
        try {
            Query query = em.createQuery("SELECT o FROM Secteur o WHERE o.intituleSecteur= :sec and o.idSecteurP.idSecteurP in (select sp.idSecteurP from Secteurprincipal sp where sp.designation = :desi)").setParameter("sec", "Secteur administratif").setParameter("desi", "Unite");
            Secteur s = (Secteur) query.getSingleResult();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
           // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Se Secteur N'existe pas!", "Secteur Innexistant dans ce secteur principal"));
            return null;
        }
    }

    /* public Users getUser() {
        ShiroLoginBean slb = (ShiroLoginBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("shiroLoginBean");
        Users user = null;
        /*try {
            Query req = em.createQuery("SELECT o FROM Users o where o.login=? and o.password=?").setParameter(1, slb.getUsername()).setParameter(2, slb.getPassword());
            user = (Users) req.getResultList();
            return user;
        } catch (Exception e) {
            user = new Users();
            user.setIdUser(2);
            return user;
            //  e.printStackTrace();
            // JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        user = new Users();
        user.setIdUser(2);
        return user;
    }*/
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

    public Dotationsecteur getDs() {
        return ds;
    }

    public void setDs(Dotationsecteur ds) {
        this.ds = ds;
    }

     public List<Boncommande> getItemes() {
        Users user = getUser();
        try {
            Query req = em.createQuery("SELECT o FROM Boncommande o where o.idUser.idUser :user").setParameter("user", user.getIdUser());
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Aucune commande n'est enregistrer pour l'utilisateur courant!", "aucun iteme"));
        }
        return items;
    }

    public List<Boncommande> getItemesCours() {
        Users user = getUser();
        try {
            items = new ArrayList<Boncommande>();
            Query req = em.createQuery("SELECT o FROM Boncommande o where o.idUser.idUser= :user and (o.etat='enCours' or o.etat='invalide')").setParameter("user", user.getIdUser());
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Aucune commande n'est enregistrer pour l'utilisateur courant!", "aucun iteme"));
        }
        return items;
    }

    public List<Boncommande> getItemesEtatBC() {
        Users user = getUser();
        try {
            Query req = em.createQuery("SELECT o FROM Boncommande o where o.idUser.idUser :user and (o.etat='enTraitement' or o.etat='valide' or o.etat='invalide' or o.etat='paye')").setParameter("user", user.getIdUser());
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Aucune commande n'est enregistrer pour l'utilisateur courant!", "aucun iteme"));
        }
        return items;
    }

    public List<Boncommande> getAllItemes() {
        Users user = getUser();
        try {
            Query req = em.createQuery("SELECT o FROM Boncommande o where o.idUser.idUser= :user").setParameter("user", user.getIdUser());
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Aucune commande n'est enregistrer pour l'utilisateur courant!", "aucun iteme"));
        }
        return items;
    }

    public List<Boncommande> getAllItemesValides() {
        try {
            Query req = em.createQuery("SELECT o FROM Boncommande o where o.etat='valide' or o.etat='non validé' or o.etat='non payé' ");
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur!Aucune Commande n'est validés !", "Secteur Innexistant"));
        }
        return items;
    }
     public List<Boncommande> getAllItemesValideDem() {
        try {
            Users user=getUser();
            Query req = em.createQuery("SELECT o FROM Boncommande o where (o.etat='valide' or o.etat='paye') and o.idUser.idUser= :us").setParameter("us",user.getIdUser());
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur!Aucune Commande n'est validés !", "Secteur Innexistant"));
        }
        return items;
    }
 public List<Boncommande> getTypeItemesRechValidesDem() {
        try {
             Users user=getUser();
            Query req = em.createQuery("SELECT o FROM Boncommande o where (o.etat='valide' or o.etat='paye') and o.type= :type and o.idUser.idUser= :us").setParameter("us",user.getIdUser()).setParameter("type", this.type);
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! Aucune Commande de ce type n'est en cours de traitement !", "Secteur Innexistant"));
        }
        return items;
    }

    public List<Boncommande> getAllItemesBCG() {
        try {
            Query req = em.createQuery("SELECT o FROM Boncommande o where o.etat='enCours'");
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur!Aucune Commande n'est en cours de traitement !", "Secteur Innexistant"));
        }
        return items;
    }
    
    
    public List<Boncommande> getAllItemesVALIDE() {
        try {
            Query req = em.createQuery("SELECT o FROM Boncommande o where o.etat='validé' ");
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur!Aucune Commande n'est en cours de traitement !", "Secteur Innexistant"));
        }
        return items;
    }
    
        
    public List<Boncommande> getAllBCrecuETnonpaye() {
        try {
            Query req = em.createQuery("SELECT o FROM Boncommande o where o.etat='reçu' or o.etat='non payé' ");
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur!Aucune Commande n'est en cours de traitement !", "Secteur Innexistant"));
        }
        return items;
    }
    
    public List<Boncommande> getAllBCpayes() {
        try {
            Query req = em.createQuery("SELECT o FROM Boncommande o where o.etat='payé'");
           List<Boncommande> items = (List<Boncommande>) req.getResultList();
            return items;
        } catch (Exception e) {
            e.printStackTrace();
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! !", "BC PAYE Innexistant"));
        return null;}
        
    }

    

            public List<Boncommande> getAllItemesRechValides() {
        try {
            Query req = em.createQuery("SELECT o FROM Boncommande o where (o.etat='valide' or o.etat='invalide') and o.type= :type").setParameter("type", this.type);
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
           // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! Aucune Commande de ce type n'est en cours de traitement !", "Secteur Innexistant"));
        }
        return items;
    }

    public List<Boncommande> getAllItemesRechValidesTraiteDem() {
        try {
            Users user=getUser();
            Query req = em.createQuery("SELECT o FROM Boncommande o where (o.etat='valide' or o.etat='invalide' or o.etat='enTraitement' or o.etat='paye') and o.type= :type and o.idUser.idUser = :us").setParameter("type", this.type).setParameter("us",user.getIdUser() );
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! Aucune Commande de ce type n'est en cours de traitement !", "Secteur Innexistant"));
        }
        return items;
    }
   public List<Boncommande> getAllItemesRechValidesTraite() {
        try {
            Query req = em.createQuery("SELECT o FROM Boncommande o where (o.etat='valide' or o.etat='invalide' or o.etat='enTraitement' or o.etat='paye') and o.type= :type").setParameter("type", this.type);
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            ////FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! Aucune Commande de ce type n'est en cours de traitement !", "Secteur Innexistant"));
        }
        return items;
    }
    public List<Boncommande> getAllItemesRech() {
        try {
            Query req = em.createQuery("SELECT o FROM Boncommande o where o.etat='enTraitement' and o.type= :type").setParameter("type", this.type);
            items = (List<Boncommande>) req.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! Aucune Commande de ce type n'est en cours de traitement !", "Secteur Innexistant"));
        }
        return items;
    }

    public Boncommande getCurrent() {
        if (current == null) {
            current = new Boncommande();
            selectedItemIndex = -1;
        }
        return current;
    }

    public void setCurrent(Boncommande current) {
        this.current = current;
    }

    public String getNomFournisseur() {
        return nomFournisseur;
    }

    public void setNomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
    }

    private BoncommandeFacade getFacade() {
        return ejbFacade;
    }

    public List<Boncommande> getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(List<Boncommande> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public ServletOutputStream getServletOutputStream() {
        return servletOutputStream;
    }

    public void setServletOutputStream(ServletOutputStream servletOutputStream) {
        this.servletOutputStream = servletOutputStream;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    /*public String prepareView() {
        current = (Boncommande) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }*/
    public String prepareCreate() {
        current = new Boncommande();
        selectedItemIndex = -1;
        return "ChoixArticles.xhtml";
    }

    public String create() {
        try {
            ds = null;
            ds = getDS();
            System.out.println(ds.getIdDotation());
            //boolean b=current instanceof Boncommande;
            if (current == null) {
                current = new Boncommande();
            }
            //current.setIdBC(null);
            current.setIdBC(null);
            this.current.setEtat("enCours");
            this.current.setDateCommande(new Date());
            current.setMontant(0.0);
            current.setTva(20);
            current.setDateReception(dateRecep);
            current.setType(type);
            current.setIdDotation(ds);
            Users user = getUser();
            Users u=new Users();
                        u.setIdUser(user.getIdUser());
                        u.setLogin(user.getLogin());
                        current.setIdUser(u);
            /*if (current instanceof Boncommande && current != null) {
                if (ds != null) {
                    Boncommande bc = getbon();
                Dotationsecteur d=new Dotationsecteur();
                Compte c=new Compte();
                Secteur s=new Secteur();
                c.setIdCompte(ds.getIdCompte().getIdCompte());
                s.setIdSecteur(ds.getIdSecteur().getIdSecteur());
                d.setIdSecteur(s);
                d.setIdCompte(c);
                d.setIdDotation(ds.getIdDotation());
                current.setIdDotation(d);
                    if (bc instanceof Boncommande && bc != null) {
                        current.setIdBC(bc.getIdBC());
                        if (user.getIdUser() == bc.getIdUser().getIdUser()) {
                            current.setIdBC(bc.getIdBC());
                            current.setDateCommande(bc.getDateCommande());
                            current.setDateReception(bc.getDateReception());
                        }
                    }
                }
            }*/
            if(current.getDateReception()!=null){
            //current.setEtat("paye");
            current.setDateReception(null);
            }
            current.setIdFournisseur(5);
           /* Boncommande b=new Boncommande();
            b.setIdDotation(current.getIdDotation());
            b.setDateCommande(current.getDateCommande());
            b.setDateReception(current.getDateReception());
            b.setEtat(current.getEtat());
            b.setIdFournisseur(current.getIdFournisseur());
            b.setIdUser(current.getIdUser());
            b.setMontant(current.getMontant());
            b.setTva(current.getTva());
            b.setType(current.getType());
            getFacade().edit(b);*/
           // subjectSelectionChangedPBC();
            getFacade().edit(current);
            /*Query req = em.createQuery("select max(a.idBC) from Boncommande a");
            int max = (Integer) req.getSingleResult();
            */
            idBC=(getMaxBC()+1);
            
            current.setIdBC(getMaxBC());
            //getItemesCours();
 
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Boncommande Number:+'" + current.getIdBC() + "' Created !", "Succes"));
            return prepareCreate();
        } catch (Exception e) {
            e.printStackTrace();
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! '" + current.getIdBC() + "'", "Erreur"));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Ajout non effectué !" + e.getMessage(), "Erreur"));
            return null;
        }
    }
    public int getMaxBC(){
            Query req = em.createQuery("select max(a.idBC) from Boncommande a");
            int max = (Integer) req.getSingleResult();
            return max;
    }
    /* public String prepareEdit() {
        current = (Boncommande) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }*/
    public String update() {
        try {
            ds = null;
            ds = getDS();
            if(current!=null && current instanceof Boncommande){
            if (current.getEtat().equals("valide")) {
                if (current.getDateReception() != null) {
                    this.current.setEtat("paye");
                }
            } else {
                current.setEtat("enCours");
            }
            }else{
                current = new Boncommande();
            }
            Dotationsecteur d=new Dotationsecteur();
                Compte c=new Compte();
                Secteur s=new Secteur();
                c.setIdCompte(ds.getIdCompte().getIdCompte());
                s.setIdSecteur(ds.getIdSecteur().getIdSecteur());
                d.setIdSecteur(s);
                d.setIdCompte(c);
                d.setIdDotation(ds.getIdDotation());
                current.setIdDotation(d);
            current.setMontant(0.0);
            current.setTva(0);
            current.setDateReception(dateRecep);
            current.setType(type);
            current.setIdFournisseur(5);
            current.setDateCommande(new Date());
            Users user=getUser();
            Users u=new Users();
                        u.setIdUser(user.getIdUser());
                        u.setLogin(user.getLogin());
                        current.setIdUser(u);
            if(current.getDateReception()!=null){
            current.setEtat("paye");
            }
            current.setIdBC(idBC);
            getFacade().edit(current);
            getItemesCours();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoncommandeUpdated"));
            return "View";
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Modification non effectué !", "Erreur"));
            return null;
        }
    }
    public void receptioner(){
       try {
           ut.begin();
           em.joinTransaction();
           current=em.find(Boncommande.class,current.getIdBC());
           ut.commit();
          if(current.getEtat().equals("valide")){
           current.setEtat("paye");
           current.setDateReception(dateRec);
          }
           getFacade().edit(current);
           this.getAllItemesValideDem();
           JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoncommandeUpdated"));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! modification non effectuée !", "erreur"));
        }
    }
    public String updateBCfournisseur (){
         try {
            Fournisseur a = getFournisseur();
            if (a == null) {
                ut.begin();
                em.joinTransaction();
                a = new Fournisseur();
                a.setNom(nomFournisseur);
                em.persist(a);
                ut.commit();
                a.setIdFournisseur(MaxFournisseur());
                current.setIdFournisseur(a.getIdFournisseur());
            } else {
                Fournisseur f=new Fournisseur();
                f.setIdFournisseur(a.getIdFournisseur());
                current.setIdFournisseur(f.getIdFournisseur());
               // System.out.println(current.getEtat());
               // current.setEtat(current.getEtat());
            }
           /* if (current.getEtat().equals("valide")) {
                //current.setIdBC(idBC);
                Query req = em.createQuery("select o from Dotationsecteur o where o.idDotation= :dot").setParameter("dot", current.getIdDotation().getIdDotation());
                Dotationsecteur ds = (Dotationsecteur) req.getSingleResult();
                Double d = ds.getReliquat();
                if (d >= current.getMontant()) {
                    Double ancienMontant = ds.getMontantInitial() - ds.getReliquat();
                    d = d - (ancienMontant - current.getMontant());
                    ut.begin();
                    em.joinTransaction();
                    Query req2 = em.createQuery("update Dotationsecteur o set o.reliquat = :Reliquat where o.idDotation = :ds").setParameter("Reliquat", d).setParameter("ds", current.getIdDotation().getIdDotation());
                    int n = req2.executeUpdate();
                    ut.commit();
                    if (n > 0) {
                
                //current.setEtat(current.getEtat());
                        getFacade().edit(current);
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vous avez dépassé le Reliquat de ce compte !", "Erreur"));
                }
            } else {
                System.out.println(etat);
                current.setEtat(etat);
                current.setMontant(calculTTC());
                getFacade().edit(current);
            }*/
         current.setMontant(calculTTC());
                getFacade().edit(current);
            getAllItemesBCG();
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Boncommande modifié"));
            return "ValiderBC.xhtml";
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! Modification non effectué !", "Erreur"));
            return null;
        }
    }
    public String updateBC() {
        try {
            Fournisseur a = getFournisseur();
            if (a == null) {
                ut.begin();
                em.joinTransaction();
                a = new Fournisseur();
                a.setNom(nomFournisseur);
                em.persist(a);
                ut.commit();
                a.setIdFournisseur(MaxFournisseur());
                current.setIdFournisseur(a.getIdFournisseur());
            } else {
                Fournisseur f=new Fournisseur();
                f.setIdFournisseur(a.getIdFournisseur());
                current.setIdFournisseur(f.getIdFournisseur());
               // System.out.println(current.getEtat());
               // current.setEtat(current.getEtat());
            }
            if (current.getEtat().equals("valide")) {
                if (current.getDateReception() != null) {
                    this.current.setEtat("paye");
                }
                //current.setIdBC(idBC);
                Query req = em.createQuery("select o from Dotationsecteur o where o.idDotation= :dot").setParameter("dot", current.getIdDotation().getIdDotation());
                Dotationsecteur ds = (Dotationsecteur) req.getSingleResult();
                Double d = ds.getReliquat();
                if (d >= current.getMontant()) {
                    Double ancienMontant = ds.getMontantInitial() - ds.getReliquat();
                    d = d - (ancienMontant - current.getMontant());
                    ut.begin();
                    em.joinTransaction();
                    Query req2 = em.createQuery("update Dotationsecteur o set o.reliquat = :Reliquat where o.idDotation = :ds").setParameter("Reliquat", d).setParameter("ds", current.getIdDotation().getIdDotation());
                    int n = req2.executeUpdate();
                    ut.commit();
                    if (n > 0) {
                        Dotationsecteur dot=new Dotationsecteur();
                Compte c=new Compte();
                Secteur s=new Secteur();
                c.setIdCompte(ds.getIdCompte().getIdCompte());
                s.setIdSecteur(ds.getIdSecteur().getIdSecteur());
                dot.setIdSecteur(s);
                dot.setIdCompte(c);
                dot.setIdDotation(ds.getIdDotation());
                current.setIdDotation(dot);
                current.setDateReception(null);
                current.setEtat(etat);
                
                //current.setEtat(current.getEtat());
                       System.out.println(etat);
                        getFacade().edit(current);
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vous avez dépassé le Reliquat de ce compte !", "Erreur"));
                }
            } else {
                System.out.println(etat);
                current.setEtat(etat);
                current.setMontant(calculTTC());
                getFacade().edit(current);
            }
            getAllItemesBCG();
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Boncommande modifié"));
            return "ValiderBC.xhtml";
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! Modification non effectué !", "Erreur"));
            return null;
        }
    }

    /* public String destroy() {
        current = (Boncommande) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }*/
    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    public String updateBCValide() {
        try {
            Fournisseur a = getFournisseur();
            if (a == null) {
                ut.begin();
                em.joinTransaction();
                a = new Fournisseur();
                a.setNom(nomFournisseur);
                em.persist(a);
                ut.commit();
                current.setIdFournisseur(MaxFournisseur());
            } else {
                current.setIdFournisseur(a.getIdFournisseur());
            }
            if (current.getEtat().equals("valide") || current.getEtat().equals("paye")) {
                if (current.getDateReception() != null) {
                    this.current.setEtat("paye");
                }
                Query req = em.createQuery("select o from Dotationsecteur o where o.idDotation= :dot").setParameter("dot", current.getIdDotation());
                Dotationsecteur ds = (Dotationsecteur) req.getSingleResult();
                Double d = ds.getReliquat();
                if (d >= current.getMontant()) {
                    Double ancienMontant = ds.getMontantInitial() - ds.getReliquat();
                    d = d - (ancienMontant - current.getMontant());
                    ut.begin();
                    em.joinTransaction();
                    Query req2 = em.createQuery("update Dotationsecteur o set o.reliquat = :Reliquat where o.idDotation = :ds").setParameter("Reliquat", d).setParameter("ds", current.getIdDotation());
                    int n = req2.executeUpdate();
                    ut.commit();
                    if (n > 0) {
                        getFacade().edit(current);
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vous avez dépassé le Reliquat de ce compte !", "Erreur"));
                }
            } else {
                Query req = em.createQuery("select o from Dotationsecteur o where o.idDotation= :dot").setParameter("dot", current.getIdDotation());
                Dotationsecteur ds = (Dotationsecteur) req.getSingleResult();
                Double d = ds.getReliquat();
                d=d+current.getMontant();
                ut.begin();
                em.joinTransaction();
                Query req2 = em.createQuery("update Dotationsecteur o set o.reliquat = :reliquat where o.idDotation =:dot").setParameter("reliquat",d).setParameter("dot", current.getIdDotation());
                int updateCount = req2.executeUpdate();
                ut.commit();
                if (updateCount > 0) {
                    getFacade().edit(current);
                }
            }
            getAllItemesValides();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoncommandeUpdated"));
            return "View";
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur! Modification non effectué !", "Erreur"));
            return null;
        }
    }

    public void performDestroyBCG() {
        try {
                ut.begin();
                em.joinTransaction();
                Query reqe = em.createQuery("DELETE from Lignecommande o where o.idBC= :bc").setParameter("bc", current.getIdBC());
                reqe.executeUpdate();
                ut.commit();
                 //current=em.find(Boncommande.class,idBC);
                getFacade().remove(current);
            getAllItemesBCG();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoncommandeDeleted"));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Suppression non effectué !", "Erreur"));
        }
    }
    public void performDestroyRecep() {
        try {
                ut.begin();
                em.joinTransaction();
                Query reqe = em.createQuery("DELETE from Lignecommande o where o.idBC= :bc").setParameter("bc", current.getIdBC());
                reqe.executeUpdate();
                ut.commit();
                 //current=em.find(Boncommande.class,idBC);
                getFacade().remove(current);
            getAllItemesValideDem();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoncommandeDeleted"));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Suppression non effectué !", "Erreur"));
        }
    }

    public void performDestroyBCGValide() {
        try {
            Query reqe= em.createQuery("select o from Dotationsecteur o where o.idDotation= :dot").setParameter("dot", current.getIdDotation().getIdDotation());
                Dotationsecteur ds = (Dotationsecteur) reqe.getSingleResult();
                Double d = ds.getReliquat();
                d=d+current.getMontant();
            ut.begin();
            em.joinTransaction();
            if(!(current.getEtat().equals("valide"))){
            Query req2 = em.createQuery("update Dotationsecteur o set o.reliquat = :reliquat where o.idDotation =:dot").setParameter("reliquat", d).setParameter("dot", current.getIdDotation().getIdDotation());
            int updateCount = req2.executeUpdate();
            }
            ut.commit();
                ut.begin();
                em.joinTransaction();
                Query req = em.createQuery("DELETE from Lignecommande o where o.idBC= :bc").setParameter("bc", current.getIdBC());
                req.executeUpdate();
                ut.commit();
               // current=em.find(Boncommande.class,idBC);
                getFacade().remove(current);
            getAllItemesValides();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoncommandeDeleted"));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Suppression non effectué !", "Erreur"));
        }
    }

    public void performDestroy() {
        try {
            ut.begin();
            em.joinTransaction();
            Query req = em.createQuery("DELETE  from Lignecommande o where o.idBC= :bc").setParameter("bc",this.idBC);
            req.executeUpdate();
            ut.commit();
            if(current==null){
            current=new Boncommande();
            }
            current=em.find(Boncommande.class,idBC);
            getFacade().remove(current);
            getItemesCours();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BoncommandeDeleted"));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur! Suppression non effectué !", "Erreur"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Boncommande> getItems() {
        return getAllItemesBCG();
    }

    public void setItems(List<Boncommande> items) {
        this.items = items;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public boolean isDisablCreate() {
        return disablCreate;
    }

    public void setDisablCreate(boolean disablCreate) {
        this.disablCreate = disablCreate;
    }

    public boolean isDisablUpdate() {
        return disablUpdate;
    }

    public void setDisablUpdate(boolean disablUpdate) {
        this.disablUpdate = disablUpdate;
    }

    public boolean isDisablDelete() {
        return disablDelete;
    }

    public void setDisablDelete(boolean disablDelete) {
        this.disablDelete = disablDelete;
    }

    public String getSecteurP() {
        return secteurP;
    }

    public void setSecteurP(String secteurP) {
        this.secteurP = secteurP;
    }

    public int getIdBC() {
        return idBC;
    }

    public void setIdBC(int idBC) {
        this.idBC = idBC;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public Compte getCpt() {
        return cpt;
    }

    public void setCpt(Compte cpt) {
        this.cpt = cpt;
    }
    

    public Double getReliquatDS() {
        return reliquatDS;
    }
    

    public void setReliquatDS(Double reliquatDS) {
        this.reliquatDS = reliquatDS;
    }

    public UserTransaction getUt() {
        return ut;
    }

    public void setUt(UserTransaction ut) {
        this.ut = ut;
    }

    public BoncommandeFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(BoncommandeFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public Date getDateRecep() {
        return dateRecep;
    }

    public void setDateRecep(Date dateRecep) {
        this.dateRecep = dateRecep;
    }

    /*public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }*/
    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Date getDateRec() {
        if(dateRec==null){
        dateRec=new Date();
        }
        return dateRec;
    }

    public void setDateRec(Date dateRec) {
        this.dateRec = dateRec;
    }

    public Boncommande getBoncommande(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public void setPagination(PaginationHelper pagination) {
        this.pagination = pagination;
    }

    public Article getArticle(int i){
        Article a=null;
     try {
            Query req = em.createQuery("SELECT o FROM Article o where o.idArticle= :art").setParameter("art",i);
            a = (Article) req.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur!Aucune Commande n'est en cours de traitement !", "Secteur Innexistant"));
        }
    return a;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    //////******************************************************///////*************
    /*
    Etat
     */
    private List<Boncommande> listOfUsers = new ArrayList<Boncommande>();
    HttpServletResponse httpServletResponse;
    ServletOutputStream servletOutputStream;

    JasperPrint jasperPrint;

    public void initBon() throws JRException {
        subjectSelectionChanged();
        Dotationsecteur ds = null;
        Secteur s = null;
        Secteurprincipal sp = null;
        try {
            if (cpt == null) {
                cpt = new Compte();
            }
            if(current.getType().equals("BC")){
            type ="Bon de Commande" ;
            }else{
            type ="Lettre de Commande" ;
            }
            dateRecep = current.getDateReception();
            Query req = em.createQuery("SELECT o FROM Dotationsecteur o WHERE o.idDotation= :dot").setParameter("dot", current.getIdDotation().getIdDotation());
            ds = (Dotationsecteur) req.getSingleResult();
            reliquatDS = ds.getReliquat();
            cpt.setIdCompte(ds.getIdCompte().getIdCompte());
            Query req6 = em.createQuery("SELECT o FROM Compte o WHERE o.idCompte= :cp").setParameter("cp", cpt.getIdCompte());
            cpt = (Compte) req6.getSingleResult();
            Query req2 = em.createQuery("SELECT o FROM Secteur o WHERE o.idSecteur= :sec").setParameter("sec", ds.getIdSecteur().getIdSecteur());
            s = (Secteur) req2.getSingleResult();
            this.secteur = s.getIntituleSecteur();
            Query req3 = em.createQuery("SELECT o FROM Secteurprincipal o WHERE o.idSecteurP= :sp").setParameter("sp", s.getIdSecteurP().getIdSecteurP());
            sp = (Secteurprincipal) req3.getSingleResult();
            this.secteurP = sp.getDesignation();
            Query req7 = em.createQuery("select o from Boncommande o where o.idBC = :bc").setParameter("bc", current.getIdBC());
            Boncommande b = (Boncommande)req7.getSingleResult();
            Query req4 = em.createQuery("select o from Lignecommande o where o.idBC = :bc").setParameter("bc", current.getIdBC());
            List<Lignecommande> list = req4.getResultList();
            Query req5 = em.createQuery("SELECT o FROM Fournisseur o WHERE o.idFournisseur= :four").setParameter("four", current.getIdFournisseur());
            Fournisseur f = (Fournisseur) req5.getSingleResult();
            this.nomFournisseur = f.getNom();
            if(f.getBanque()==null){
            f.setBanque("");
            }
            if(f.getAdresse()==null){
            f.setAdresse("");
            }
            if(cpt.getIntitule()==null){
            cpt.setIntitule("");
            }
            listOfUsers.add(current);
            Lignecommande l1=new Lignecommande();
        Lignecommande l2=new Lignecommande();
        Lignecommande l3=new Lignecommande();
        HashMap map = new HashMap();
            if(list.size()==1){
         l1=list.get(0);
         if(cpt.getIdCompte()!=null){
        map.put("cmp", cpt.getIdCompte()+","+cpt.getIntitule());
         }else{
         map.put("cmp"," ,"+cpt.getIntitule());
         }
        map.put("NomFourniseur", f.getNom());
        map.put("qualite", f.getAdresse());
        map.put("cmpt", cpt.getIdCompte());
        map.put("intitule",cpt.getIntitule());
        Article a1=new Article();
         a1=getArticle(l1.getIdArticle());
        map.put("art1", a1.getDescription());
        map.put("art2", "");
        map.put("art3", "");
        //map.put("totale", " DH");
       // map.put("totalettc", " DH");
        
        map.put("qte1", ""+l1.getQuantite()+"");
        map.put("qte2","");
        map.put("qte3","");
        map.put("pu1", l1.getPu()+" DH");
         map.put("pu2", "");
          map.put("pu3","");
        map.put("mnt1", list.get(0).getMontant()+" DH");
        map.put("mnt2", "");
        map.put("mnt3", "");
        map.put("depatement", s.getIntituleSecteur());
        Double ht=l1.getMontant();
        map.put("totalht",ht+" DH");
        map.put("totaltva", current.getTva()+" %");
        //Double ttc=ht+(ht*current.getTva());
        map.put("ttc",""+b.getMontant()+" DH");
        //map.put("totalttc", current.getMontant());
        map.put("titre", type+" Numéro:  "+current.getIdBC());
        
        
        }else if(list.size()==2){
        l1=list.get(0);
        l2=list.get(1);
         if(cpt.getIdCompte()!=null){
        map.put("cmp", cpt.getIdCompte()+","+cpt.getIntitule());
         }else{
         map.put("cmp"," ,"+cpt.getIntitule());
         }
        map.put("NomFourniseur", f.getNom());
        map.put("qualite", f.getAdresse());
        map.put("cmpt", cpt.getIdCompte());
        map.put("intitule",cpt.getIntitule());
        Article a1=new Article();
        Article a2=new Article();
                Article a3 = new Article();
                a1 = getArticle(list.get(0).getIdArticle());
                a2 = getArticle(list.get(1).getIdArticle());
        map.put("art1", a1.getDescription());
        map.put("art2", a2.getDescription());
        map.put("art3", "");
        map.put("qte1", ""+l1.getQuantite()+"");
        map.put("qte2",l2.getQuantite()+"");
        map.put("qte3","");
        map.put("pu1", l1.getPu()+" DH");
        map.put("pu2",l2.getPu()+" DH");
        map.put("pu3","");
        map.put("mnt1", list.get(0).getMontant()+" DH");
        map.put("mnt2", list.get(1).getMontant()+" DH");
        map.put("mnt3","");
        map.put("depatement", s.getIntituleSecteur());
        Double ht=l1.getMontant()+l2.getMontant();
        map.put("totalht",ht+" DH");
        map.put("totaltva", current.getTva()+" %");
        //Double ttc=ht+(ht*current.getTva());
        map.put("ttc",""+b.getMontant()+" DH");
        //map.put("totalttc", current.getMontant());
        map.put("titre", type+" Numéro:  "+current.getIdBC());
        
        }else if(list.size()==3){
        l1=list.get(0);
        l2=list.get(1);
        l3=list.get(2);
        if(cpt.getIdCompte()!=null){
        map.put("cmp", cpt.getIdCompte()+","+cpt.getIntitule());
         }else{
         map.put("cmp"," ,"+cpt.getIntitule());
         }
        map.put("NomFourniseur", f.getNom());
        map.put("qualite", f.getAdresse());
        map.put("cmpt", cpt.getIdCompte());
        map.put("intitule",cpt.getIntitule());
        Article a1=new Article();
        Article a2=new Article();
        Article a3=new Article();
        a1=getArticle(list.get(0).getIdArticle());
        a2=getArticle(list.get(1).getIdArticle());
        a3=getArticle(list.get(2).getIdArticle());
        
        map.put("art1", a1.getDescription());
        map.put("art2", a2.getDescription());
        map.put("art3", a3.getDescription());
        //map.put("totale", " DH");
       // map.put("totalettc", " DH");
        
        map.put("qte1", l1.getQuantite()+"");
        map.put("qte2",l2.getQuantite()+"");
        map.put("qte3", l3.getQuantite()+"");
        map.put("pu1", l1.getPu()+" DH");
        map.put("pu2",l2.getPu()+" DH");
        map.put("pu3",l3.getPu()+" DH");
        map.put("mnt1", list.get(0).getMontant()+" DH");
        map.put("mnt2", list.get(1).getMontant()+" DH");
        map.put("mnt3", list.get(2).getMontant()+" DH");
        map.put("depatement", s.getIntituleSecteur());
        Double ht=l1.getMontant()+l2.getMontant()+l3.getMontant();
        map.put("totalht",ht+" DH");
        map.put("totaltva", current.getTva()+" %");
       // Double ttc=ht+(ht*current.getTva());
        map.put("ttc",""+b.getMontant()+" DH");
        map.put("titre", type+" Numéro:  "+current.getIdBC());
        }else{
        l1=new Lignecommande();
         l2=new Lignecommande();
        l3=new Lignecommande();
        
        }
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listOfUsers, false);
        listOfUsers = new ArrayList<Boncommande>();
        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("report/report3.jasper");
        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            // JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        
    }

    public void PDBon(ActionEvent actionEvent) throws JRException, IOException {
        initBon();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.pdf");
        servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void DOCXBon(ActionEvent actionEvent) throws JRException, IOException {
        initBon();
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.docx");
        servletOutputStream = httpServletResponse.getOutputStream();
        JRDocxExporter docxExporter = new JRDocxExporter();
        docxExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING.JASPER_PRINT, jasperPrint);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.setParameter(JRDocxExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
    }

    public void XLSXBon(ActionEvent actionEvent) throws JRException, IOException {
        initBon();
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.xlsx");
        servletOutputStream = httpServletResponse.getOutputStream();
        JRXlsxExporter docxExporter = new JRXlsxExporter();
        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
    }
    
    public void initBR() throws JRException {
        subjectSelectionChanged();
        Dotationsecteur ds = null;
        Secteur s = null;
        Secteurprincipal sp = null;
        try {
            if (cpt == null) {
                cpt = new Compte();
            }
            if(current.getType().equals("BC")){
            type ="Bon de Commande" ;
            }else{
            type ="Lettre de Commande" ;
            }
            dateRecep = current.getDateReception();
            Query req = em.createQuery("SELECT o FROM Dotationsecteur o WHERE o.idDotation= :dot").setParameter("dot", current.getIdDotation().getIdDotation());
            ds = (Dotationsecteur) req.getSingleResult();
            reliquatDS = ds.getReliquat();
            cpt.setIdCompte(ds.getIdCompte().getIdCompte());
            Query req6 = em.createQuery("SELECT o FROM Compte o WHERE o.idCompte= :cp").setParameter("cp", cpt.getIdCompte());
            cpt = (Compte) req6.getSingleResult();
            Query req2 = em.createQuery("SELECT o FROM Secteur o WHERE o.idSecteur= :sec").setParameter("sec", ds.getIdSecteur().getIdSecteur());
            s = (Secteur) req2.getSingleResult();
            this.secteur = s.getIntituleSecteur();
            Query req3 = em.createQuery("SELECT o FROM Secteurprincipal o WHERE o.idSecteurP= :sp").setParameter("sp", s.getIdSecteurP().getIdSecteurP());
            sp = (Secteurprincipal) req3.getSingleResult();
            this.secteurP = sp.getDesignation();
            Query req7 = em.createQuery("select o from Boncommande o where o.idBC = :bc").setParameter("bc", current.getIdBC());
            Boncommande b = (Boncommande)req7.getSingleResult();
            Query req4 = em.createQuery("select o from Lignecommande o where o.idBC = :bc").setParameter("bc", current.getIdBC());
            List<Lignecommande> list = req4.getResultList();
            Query req5 = em.createQuery("SELECT o FROM Fournisseur o WHERE o.idFournisseur= :four").setParameter("four", current.getIdFournisseur());
            Fournisseur f = (Fournisseur) req5.getSingleResult();
            this.nomFournisseur = f.getNom();
            if(f.getBanque()==null){
            f.setBanque("");
            }
            if(f.getAdresse()==null){
            f.setAdresse("");
            }
            if(cpt.getIntitule()==null){
            cpt.setIntitule("");
            }
            listOfUsers.add(current);
            Lignecommande l1=new Lignecommande();
        Lignecommande l2=new Lignecommande();
        Lignecommande l3=new Lignecommande();
        HashMap map = new HashMap();
            if(list.size()==1){
         l1=list.get(0);
         if(cpt.getIdCompte()!=null){
        map.put("cmp", cpt.getIdCompte()+","+cpt.getIntitule());
         }else{
         map.put("cmp"," ,"+cpt.getIntitule());
         }
        map.put("NomFourniseur", f.getNom());
        map.put("qualite", f.getAdresse());
        map.put("cmpt", cpt.getIdCompte());
        map.put("intitule",cpt.getIntitule());
        Article a1=new Article();
         a1=getArticle(l1.getIdArticle());
        map.put("art1", a1.getDescription());
        map.put("art2", "");
        map.put("art3", "");
        //map.put("totale", " DH");
       // map.put("totalettc", " DH");
        
        map.put("qte1", ""+l1.getQuantite()+"");
        map.put("qte2","");
        map.put("qte3","");
        map.put("pu1", l1.getPu()+" DH");
         map.put("pu2", "");
          map.put("pu3","");
        map.put("mnt1", list.get(0).getMontant()+" DH");
        map.put("mnt2", "");
        map.put("mnt3", "");
        map.put("depatement", s.getIntituleSecteur());
        Double ht=l1.getMontant();
        map.put("totalht",ht+" DH");
        map.put("totaltva", current.getTva()+" %");
        //Double ttc=ht+(ht*current.getTva());
        map.put("ttc",""+b.getMontant()+" DH");
        //map.put("totalttc", current.getMontant());
        map.put("titre", "Bon de Reception Numéro: "+current.getIdBC());
        
        
        }else if(list.size()==2){
        l1=list.get(0);
        l2=list.get(1);
         if(cpt.getIdCompte()!=null){
        map.put("cmp", cpt.getIdCompte()+","+cpt.getIntitule());
         }else{
         map.put("cmp"," ,"+cpt.getIntitule());
         }
        map.put("NomFourniseur", f.getNom());
        map.put("qualite", f.getAdresse());
        map.put("cmpt", cpt.getIdCompte());
        map.put("intitule",cpt.getIntitule());
        Article a1=new Article();
        Article a2=new Article();
                Article a3 = new Article();
                a1 = getArticle(list.get(0).getIdArticle());
                a2 = getArticle(list.get(1).getIdArticle());
        map.put("art1", a1.getDescription());
        map.put("art2", a2.getDescription());
        map.put("art3", "");
        map.put("qte1", ""+l1.getQuantite()+"");
        map.put("qte2",l2.getQuantite()+"");
        map.put("qte3","");
        map.put("pu1", l1.getPu()+" DH");
        map.put("pu2",l2.getPu()+" DH");
        map.put("pu3","");
        map.put("mnt1", list.get(0).getMontant()+" DH");
        map.put("mnt2", list.get(1).getMontant()+" DH");
        map.put("mnt3","");
        map.put("depatement", s.getIntituleSecteur());
        Double ht=l1.getMontant()+l2.getMontant();
        map.put("totalht",ht+" DH");
        map.put("totaltva", current.getTva()+" %");
        //Double ttc=ht+(ht*current.getTva());
        map.put("ttc",""+b.getMontant()+" DH");
        //map.put("totalttc", current.getMontant());
        map.put("titre", "Bon de Reception Numéro: "+current.getIdBC());
        
        }else if(list.size()==3){
        l1=list.get(0);
        l2=list.get(1);
        l3=list.get(2);
        if(cpt.getIdCompte()!=null){
        map.put("cmp", cpt.getIdCompte()+","+cpt.getIntitule());
         }else{
         map.put("cmp"," ,"+cpt.getIntitule());
         }
        map.put("NomFourniseur", f.getNom());
        map.put("qualite", f.getAdresse());
        map.put("cmpt", cpt.getIdCompte());
        map.put("intitule",cpt.getIntitule());
        Article a1=new Article();
        Article a2=new Article();
        Article a3=new Article();
        a1=getArticle(list.get(0).getIdArticle());
        a2=getArticle(list.get(1).getIdArticle());
        a3=getArticle(list.get(2).getIdArticle());
        
        map.put("art1", a1.getDescription());
        map.put("art2", a2.getDescription());
        map.put("art3", a3.getDescription());
        //map.put("totale", " DH");
       // map.put("totalettc", " DH");
        
        map.put("qte1", l1.getQuantite()+"");
        map.put("qte2",l2.getQuantite()+"");
        map.put("qte3", l3.getQuantite()+"");
        map.put("pu1", l1.getPu()+" DH");
        map.put("pu2",l2.getPu()+" DH");
        map.put("pu3",l3.getPu()+" DH");
        map.put("mnt1", list.get(0).getMontant()+" DH");
        map.put("mnt2", list.get(1).getMontant()+" DH");
        map.put("mnt3", list.get(2).getMontant()+" DH");
        map.put("depatement", s.getIntituleSecteur());
        Double ht=l1.getMontant()+l2.getMontant()+l3.getMontant();
        map.put("totalht",ht+" DH");
        map.put("totaltva", current.getTva()+" %");
        Double ttc=ht+(ht*current.getTva());
        map.put("ttc",""+b.getMontant()+" DH");
        map.put("titre", "Bon de Reception Numéro: "+current.getIdBC());
        }else{
        l1=new Lignecommande();
         l2=new Lignecommande();
        l3=new Lignecommande();
        
        }
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listOfUsers, false);
        listOfUsers = new ArrayList<Boncommande>();
        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("report/report3.jasper");
        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            // JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        
    }
     public void PDFBR(ActionEvent actionEvent) throws JRException, IOException {
        initBR();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.pdf");
        servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void DOCXBR(ActionEvent actionEvent) throws JRException, IOException {
        initBR();
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.docx");
        servletOutputStream = httpServletResponse.getOutputStream();
        JRDocxExporter docxExporter = new JRDocxExporter();
        docxExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING.JASPER_PRINT, jasperPrint);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.setParameter(JRDocxExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
    }

    public void XLSXBR(ActionEvent actionEvent) throws JRException, IOException {
        initBR();
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.xlsx");
        servletOutputStream = httpServletResponse.getOutputStream();
        JRXlsxExporter docxExporter = new JRXlsxExporter();
        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
    }
    
        //*****************************************************************************
    /*
    OV
     */
    public void initOV() throws JRException {
        listOfUsers = new ArrayList<Boncommande>();
        listOfUsers.add(current);
        HashMap map = new HashMap();
        Query req5 = em.createQuery("SELECT o FROM Fournisseur o WHERE o.idFournisseur= :four").setParameter("four", current.getIdFournisseur());
            Fournisseur f = (Fournisseur) req5.getSingleResult();
            Query req = em.createQuery("SELECT o FROM Dotationsecteur o WHERE o.idDotation= :dot").setParameter("dot", current.getIdDotation().getIdDotation());
            ds = (Dotationsecteur) req.getSingleResult();
        map.put("nom", f.getNom());
        map.put("compteDep", "" +ds.getIdCompte().getIntitule());
        map.put("bnc",f.getBanque());
        map.put("numCmpt", f.getRib());
        map.put("somme", "" + current.getMontant());

        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listOfUsers, false);
        listOfUsers = new ArrayList<Boncommande>();
        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("report/OVBON.jasper");
        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    public void PDFOV(ActionEvent actionEvent) throws JRException, IOException {
        initOV();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.pdf");
        servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void DOCXOV(ActionEvent actionEvent) throws JRException, IOException {
        initOV();
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.docx");
        servletOutputStream = httpServletResponse.getOutputStream();
        JRDocxExporter docxExporter = new JRDocxExporter();
        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.setParameter(JRDocxExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
    }

    public void XLSXOV(ActionEvent actionEvent) throws JRException, IOException {
        initOV();
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.xlsx");
        servletOutputStream = httpServletResponse.getOutputStream();
        JRXlsxExporter docxExporter = new JRXlsxExporter();
        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
    }

    //*****************************************************************************
    /*
    OP
     */
    public void initOP() throws JRException {
        listOfUsers = new ArrayList<Boncommande>();
        listOfUsers.add(current);
        HashMap map = new HashMap();
        Query req5 = em.createQuery("SELECT o FROM Fournisseur o WHERE o.idFournisseur= :four").setParameter("four", current.getIdFournisseur());
            Fournisseur f = (Fournisseur) req5.getSingleResult();
            Query req = em.createQuery("SELECT o FROM Dotationsecteur o WHERE o.idDotation= :dot").setParameter("dot", current.getIdDotation().getIdDotation());
            ds = (Dotationsecteur) req.getSingleResult();
        map.put("nom", f.getNom());
        map.put("compteBnf", f.getRib()+" , "+f.getBanque());
        map.put("compteDep",ds.getIdCompte().getIdCompte()+","+ds.getIdCompte().getIntitule());
        map.put("pieces", "");
        map.put("natures", type);
        map.put("mnt", current.getMontant() + " DH");
        map.put("mntCH", "Montant chaine");
        map.put("dateCommande", current.getDateCommande());
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listOfUsers, false);
        listOfUsers = new ArrayList<Boncommande>();
        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("report/OPBON.jasper");
        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    public void PDFOP(ActionEvent actionEvent) throws JRException, IOException {
        initOP();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.pdf");
        servletOutputStream = httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        FacesContext.getCurrentInstance().responseComplete();
    }


    public void DOCXOP(ActionEvent actionEvent) throws JRException, IOException {
        initOP();
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.docx");
        servletOutputStream = httpServletResponse.getOutputStream();
        JRDocxExporter docxExporter = new JRDocxExporter();
        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.setParameter(JRDocxExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
    }

    public void XLSXOP(ActionEvent actionEvent) throws JRException, IOException {
        initOP();
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.xlsx");
        servletOutputStream = httpServletResponse.getOutputStream();
        JRXlsxExporter docxExporter = new JRXlsxExporter();
        docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
        docxExporter.exportReport();
    }

}