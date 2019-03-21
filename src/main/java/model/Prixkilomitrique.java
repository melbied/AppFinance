/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Marouane
 */
@Entity
@Table(name = "prixkilomitrique")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prixkilomitrique.findAll", query = "SELECT p FROM Prixkilomitrique p")
    , @NamedQuery(name = "Prixkilomitrique.findByIdPrixKilo", query = "SELECT p FROM Prixkilomitrique p WHERE p.idPrixKilo = :idPrixKilo")
    , @NamedQuery(name = "Prixkilomitrique.findByKmSup", query = "SELECT p FROM Prixkilomitrique p WHERE p.kmSup = :kmSup")
    , @NamedQuery(name = "Prixkilomitrique.findByKmInf", query = "SELECT p FROM Prixkilomitrique p WHERE p.kmInf = :kmInf")
    , @NamedQuery(name = "Prixkilomitrique.findByPrixRoute", query = "SELECT p FROM Prixkilomitrique p WHERE p.prixRoute = :prixRoute")
    , @NamedQuery(name = "Prixkilomitrique.findByPrixPiste", query = "SELECT p FROM Prixkilomitrique p WHERE p.prixPiste = :prixPiste")})
public class Prixkilomitrique implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPrixKilo")
    private Integer idPrixKilo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "kmSup")
    private BigDecimal kmSup;
    @Column(name = "kmInf")
    private BigDecimal kmInf;
    @Column(name = "prixRoute")
    private BigDecimal prixRoute;
    @Column(name = "prixPiste")
    private BigDecimal prixPiste;

    public Prixkilomitrique() {
    }

    public Prixkilomitrique(Integer idPrixKilo) {
        this.idPrixKilo = idPrixKilo;
    }

    public Integer getIdPrixKilo() {
        return idPrixKilo;
    }

    public void setIdPrixKilo(Integer idPrixKilo) {
        this.idPrixKilo = idPrixKilo;
    }

    public BigDecimal getKmSup() {
        return kmSup;
    }

    public void setKmSup(BigDecimal kmSup) {
        this.kmSup = kmSup;
    }

    public BigDecimal getKmInf() {
        return kmInf;
    }

    public void setKmInf(BigDecimal kmInf) {
        this.kmInf = kmInf;
    }

    public BigDecimal getPrixRoute() {
        return prixRoute;
    }

    public void setPrixRoute(BigDecimal prixRoute) {
        this.prixRoute = prixRoute;
    }

    public BigDecimal getPrixPiste() {
        return prixPiste;
    }

    public void setPrixPiste(BigDecimal prixPiste) {
        this.prixPiste = prixPiste;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrixKilo != null ? idPrixKilo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prixkilomitrique)) {
            return false;
        }
        Prixkilomitrique other = (Prixkilomitrique) object;
        if ((this.idPrixKilo == null && other.idPrixKilo != null) || (this.idPrixKilo != null && !this.idPrixKilo.equals(other.idPrixKilo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Prixkilomitrique[ idPrixKilo=" + idPrixKilo + " ]";
    }
    
}
