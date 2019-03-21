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
@Table(name = "prixrepas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prixrepas.findAll", query = "SELECT p FROM Prixrepas p")
    , @NamedQuery(name = "Prixrepas.findByIdPrixRepas", query = "SELECT p FROM Prixrepas p WHERE p.idPrixRepas = :idPrixRepas")
    , @NamedQuery(name = "Prixrepas.findByIndiceSup", query = "SELECT p FROM Prixrepas p WHERE p.indiceSup = :indiceSup")
    , @NamedQuery(name = "Prixrepas.findByIndiceInf", query = "SELECT p FROM Prixrepas p WHERE p.indiceInf = :indiceInf")
    , @NamedQuery(name = "Prixrepas.findByPrixDs", query = "SELECT p FROM Prixrepas p WHERE p.prixDs = :prixDs")
    , @NamedQuery(name = "Prixrepas.findByPrixD", query = "SELECT p FROM Prixrepas p WHERE p.prixD = :prixD")
    , @NamedQuery(name = "Prixrepas.findByPrixRs", query = "SELECT p FROM Prixrepas p WHERE p.prixRs = :prixRs")})
public class Prixrepas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPrixRepas")
    private Integer idPrixRepas;
    @Column(name = "indiceSup")
    private Integer indiceSup;
    @Column(name = "indiceInf")
    private Integer indiceInf;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prixDs")
    private BigDecimal prixDs;
    @Column(name = "prixD")
    private BigDecimal prixD;
    @Column(name = "prixRs")
    private BigDecimal prixRs;

    public Prixrepas() {
    }

    public Prixrepas(Integer idPrixRepas) {
        this.idPrixRepas = idPrixRepas;
    }

    public Integer getIdPrixRepas() {
        return idPrixRepas;
    }

    public void setIdPrixRepas(Integer idPrixRepas) {
        this.idPrixRepas = idPrixRepas;
    }

    public Integer getIndiceSup() {
        return indiceSup;
    }

    public void setIndiceSup(Integer indiceSup) {
        this.indiceSup = indiceSup;
    }

    public Integer getIndiceInf() {
        return indiceInf;
    }

    public void setIndiceInf(Integer indiceInf) {
        this.indiceInf = indiceInf;
    }

    public BigDecimal getPrixDs() {
        return prixDs;
    }

    public void setPrixDs(BigDecimal prixDs) {
        this.prixDs = prixDs;
    }

    public BigDecimal getPrixD() {
        return prixD;
    }

    public void setPrixD(BigDecimal prixD) {
        this.prixD = prixD;
    }

    public BigDecimal getPrixRs() {
        return prixRs;
    }

    public void setPrixRs(BigDecimal prixRs) {
        this.prixRs = prixRs;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrixRepas != null ? idPrixRepas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prixrepas)) {
            return false;
        }
        Prixrepas other = (Prixrepas) object;
        if ((this.idPrixRepas == null && other.idPrixRepas != null) || (this.idPrixRepas != null && !this.idPrixRepas.equals(other.idPrixRepas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Prixrepas[ idPrixRepas=" + idPrixRepas + " ]";
    }
    
}
