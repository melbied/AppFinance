/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "anneebudgetaire")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Anneebudgetaire.findAll", query = "SELECT a FROM Anneebudgetaire a")
    , @NamedQuery(name = "Anneebudgetaire.findByAnnee", query = "SELECT a FROM Anneebudgetaire a WHERE a.annee = :annee")
    , @NamedQuery(name = "Anneebudgetaire.findByMontantRap", query = "SELECT a FROM Anneebudgetaire a WHERE a.montantRap = :montantRap")
    , @NamedQuery(name = "Anneebudgetaire.findByReliquatRap", query = "SELECT a FROM Anneebudgetaire a WHERE a.reliquatRap = :reliquatRap")})
public class Anneebudgetaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "annee")
    private Integer annee;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montantRap")
    private BigDecimal montantRap;
    @Column(name = "reliquatRap")
    private BigDecimal reliquatRap;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anneebudgetaire", fetch = FetchType.EAGER)
    private List<Budget> budgetList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "annee", fetch = FetchType.EAGER)
    private List<Bordereaucomptable> bordereaucomptableList;

    public Anneebudgetaire() {
    }

    public Anneebudgetaire(Integer annee) {
        this.annee = annee;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public BigDecimal getMontantRap() {
        return montantRap;
    }

    public void setMontantRap(BigDecimal montantRap) {
        this.montantRap = montantRap;
    }

    public BigDecimal getReliquatRap() {
        return reliquatRap;
    }

    public void setReliquatRap(BigDecimal reliquatRap) {
        this.reliquatRap = reliquatRap;
    }

    @XmlTransient
    public List<Budget> getBudgetList() {
        return budgetList;
    }

    public void setBudgetList(List<Budget> budgetList) {
        this.budgetList = budgetList;
    }

    @XmlTransient
    public List<Bordereaucomptable> getBordereaucomptableList() {
        return bordereaucomptableList;
    }

    public void setBordereaucomptableList(List<Bordereaucomptable> bordereaucomptableList) {
        this.bordereaucomptableList = bordereaucomptableList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (annee != null ? annee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Anneebudgetaire)) {
            return false;
        }
        Anneebudgetaire other = (Anneebudgetaire) object;
        if ((this.annee == null && other.annee != null) || (this.annee != null && !this.annee.equals(other.annee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Anneebudgetaire[ annee=" + annee + " ]";
    }
    
}
