/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Imane Rafiq
 */
@Entity
@Table(name = "budget")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Budget.findAll", query = "SELECT b FROM Budget b")
    , @NamedQuery(name = "Budget.findByIdCompte", query = "SELECT b FROM Budget b WHERE b.budgetPK.idCompte = :idCompte")
    , @NamedQuery(name = "Budget.findByAnnee", query = "SELECT b FROM Budget b WHERE b.budgetPK.annee = :annee")
    , @NamedQuery(name = "Budget.findByBudgetAnnuel", query = "SELECT b FROM Budget b WHERE b.budgetAnnuel = :budgetAnnuel")
    , @NamedQuery(name = "Budget.findByReliquat", query = "SELECT b FROM Budget b WHERE b.reliquat = :reliquat")})
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BudgetPK budgetPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "budgetAnnuel")
    private BigDecimal budgetAnnuel;
    @Column(name = "reliquat")
    private BigDecimal reliquat;
    @JoinColumn(name = "annee", referencedColumnName = "annee", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Anneebudgetaire anneebudgetaire;
    @JoinColumn(name = "idCompte", referencedColumnName = "idCompte", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Compte compte;

    public Budget() {
    }

    public Budget(BudgetPK budgetPK) {
        this.budgetPK = budgetPK;
    }

    public Budget(int idCompte, int annee) {
        this.budgetPK = new BudgetPK(idCompte, annee);
    }

    public BudgetPK getBudgetPK() {
        return budgetPK;
    }

    public void setBudgetPK(BudgetPK budgetPK) {
        this.budgetPK = budgetPK;
    }

    public BigDecimal getBudgetAnnuel() {
        return budgetAnnuel;
    }

    public void setBudgetAnnuel(BigDecimal budgetAnnuel) {
        this.budgetAnnuel = budgetAnnuel;
    }

    public BigDecimal getReliquat() {
        return reliquat;
    }

    public void setReliquat(BigDecimal reliquat) {
        this.reliquat = reliquat;
    }

    public Anneebudgetaire getAnneebudgetaire() {
        return anneebudgetaire;
    }

    public void setAnneebudgetaire(Anneebudgetaire anneebudgetaire) {
        this.anneebudgetaire = anneebudgetaire;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (budgetPK != null ? budgetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Budget)) {
            return false;
        }
        Budget other = (Budget) object;
        if ((this.budgetPK == null && other.budgetPK != null) || (this.budgetPK != null && !this.budgetPK.equals(other.budgetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Budget[ budgetPK=" + budgetPK + " ]";
    }
    
}
