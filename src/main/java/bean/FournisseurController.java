package bean;

import model.Fournisseur;
import bean.util.JsfUtil;
import bean.util.JsfUtil.PersistAction;
import bean.util.PaginationHelper;
import session.FournisseurFacade;

import java.io.Serializable;    
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Named("fournisseurController")
@SessionScoped
public class FournisseurController implements Serializable {
    @PersistenceContext(unitName = "FSSM_AppFinanciereFssm_war_2.0PU")
    private EntityManager em;
    private Fournisseur current;
    private List<Fournisseur> items = null;
    private List<Fournisseur> filteredItems = null;
    private boolean disablCreate = false;
    private boolean disablUpdate = true;
    private boolean disablDelete = true;
    private String newName=null;
    @EJB
    private session.FournisseurFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Fournisseur selected;

    public FournisseurController() {
    }
    
    @PostConstruct
    public void init() {
        //setItems(getItemes());
    }
    public List<Fournisseur> getItemes() {
        try {
            this.items = new ArrayList<Fournisseur>();
            Query req = em.createQuery("SELECT o FROM Fournisseur o");
            List<Fournisseur> l = (List<Fournisseur>) req.getResultList();
            items = l;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aucun fournisseur n'est trouvé !", "Information"));
        }
        return items;
    }

    public void subjectSelectionChanged() {
        if (current instanceof Fournisseur && current != null) {
            try {
                Query req = em.createQuery("SELECT o FROM Fournisseur o WHERE o.nom = :nom").setParameter("nom", current.getNom());
                Fournisseur c = (Fournisseur) req.getSingleResult();
                if (c != null && c instanceof Fournisseur) {
                    current.setNom(c.getNom());
                    current.setAdresse(c.getAdresse());
                    current.setEmail(c.getEmail());
                    current.setTel(c.getTel());
                    current.setIdFournisseur(c.getIdFournisseur());
                    current.setBanque(c.getBanque());
                    current.setRib(c.getRib());
                    disablCreate = true;
                    disablUpdate = false;
                    disablDelete = false;
                } else {
                    current.setIdFournisseur(null);
                    disablCreate = false;
                    disablUpdate = true;
                    disablDelete = true;
                }
            } catch (Exception e) {
                current.setIdFournisseur(null);
                disablCreate = false;
                disablUpdate = true;
                disablDelete = true;
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    public List<String> completeText(String id) {
        List<String> FiltredF = new ArrayList<String>();
        try {
            List<Fournisseur> AllF = getItemes();
            for (Fournisseur c : AllF) {
                if (c.getNom().startsWith(id)) {
                    FiltredF.add(c.getNom());
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur de Convertion !", "Erreur"));
        }
        return FiltredF;
    }

    private FournisseurFacade getFacade() {
        return ejbFacade;
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

    /* public String prepareView() {
        current = (Fournisseur) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }*/
    public String prepareCreate() {
        current = new Fournisseur();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            newName=current.getNom();
            current.setIdFournisseur(null);
            getFacade().edit(current);
            this.items = getItemes();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FournisseurCreated"));
            return prepareCreate();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Echec d'insertion !", "Erreur"));
            return null;
        }
    }

    /*  public String prepareEdit() {
        current = (Fournisseur) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }*/
    public String update() {
        try {
            Query req = em.createQuery("SELECT a FROM Fournisseur a WHERE a.nom =:nom").setParameter("nom", current.getNom());
            Fournisseur f=(Fournisseur) req.getSingleResult();
            current.setIdFournisseur(f.getIdFournisseur());
            if(newName!=null)
            {
                    current.setNom(newName);
            }
            else
            {
                    current.setNom(f.getNom());
            }
            getFacade().edit(current);
            this.items = getItemes();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FournisseurUpdated"));
            return "GererFournisseurs.xhtml";
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Echec de modification !", "Erreur"));
            return null;
        }
    }

    /* public String destroy() {
        current = (Fournisseur) getItems().getRowData();
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

    public void performDestroy() {
        try {
            Query req = em.createQuery("SELECT a FROM Fournisseur a WHERE a.nom = :nom").setParameter("nom", current.getNom());
            Fournisseur f=(Fournisseur) req.getSingleResult();
            current.setIdFournisseur(f.getIdFournisseur());
            getFacade().remove(current);
            this.items = getItemes();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FournisseurDeleted"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Echec de suppression !", "Erreur"));
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
    
        
            public String Rafraichir()
        {
   
                    current.setNom(null);
                    current.setAdresse(null);
                    current.setEmail(null);
                    current.setTel(null);
                    current.setIdFournisseur(null);
                    current.setBanque(null);
                    current.setRib(null);
                    disablCreate = true;
                    disablUpdate = false;
                    disablDelete = false;
                    
                    return "GererFournisseurs.xhtml";
             }

    /* public DataModel getItems() {
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

    public Fournisseur getFournisseur(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public Fournisseur getCurrent() {
        if (current == null) {
            current = new Fournisseur();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    

    public void setCurrent(Fournisseur current) {
        this.current = current;
    }
    public Fournisseur getSelected() {
        if (selected == null) {
            selected = new Fournisseur();
            selectedItemIndex = -1;
        }
        return selected;
    }
    
    

    public void setSelected(Fournisseur selected) {
        this.selected = selected;
    }

    public List<Fournisseur> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public void setItems(List<Fournisseur> items) {
        this.items = items;
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

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
    
    public List<Fournisseur> getFilteredItems() {
        return filteredItems;
    }
 
    public void setFilteredItems(List<Fournisseur> filteredItems) {
        this.filteredItems = filteredItems;
    }
    public void setDisablDelete(boolean disablDelete) {
        this.disablDelete = disablDelete;
    }

    public FournisseurFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(FournisseurFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }


    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    @FacesConverter(forClass = Fournisseur.class)
    public static class FournisseurControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FournisseurController controller = (FournisseurController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "fournisseurController");
            return controller.getFournisseur(getKey(value));
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
            if (object instanceof Fournisseur) {
                Fournisseur o = (Fournisseur) object;
                return getStringKey(o.getIdFournisseur());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Fournisseur.class.getName());
            }
        }
        

        

    }

}
