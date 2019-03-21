/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Marouane
 */
@Entity
@Table(name = "dossierhsupp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dossierhsupp.findAll", query = "SELECT d FROM Dossierhsupp d")
    , @NamedQuery(name = "Dossierhsupp.findByIdDossier", query = "SELECT d FROM Dossierhsupp d WHERE d.idDossier = :idDossier")
    , @NamedQuery(name = "Dossierhsupp.findByNbrHeures", query = "SELECT d FROM Dossierhsupp d WHERE d.nbrHeures = :nbrHeures")
    , @NamedQuery(name = "Dossierhsupp.findByMois", query = "SELECT d FROM Dossierhsupp d WHERE d.mois = :mois")
    , @NamedQuery(name = "Dossierhsupp.findBySemestre", query = "SELECT d FROM Dossierhsupp d WHERE d.semestre = :semestre")
    , @NamedQuery(name = "Dossierhsupp.findByDateCreance", query = "SELECT d FROM Dossierhsupp d WHERE d.dateCreance = :dateCreance")
    , @NamedQuery(name = "Dossierhsupp.findByMontantHsupp", query = "SELECT d FROM Dossierhsupp d WHERE d.montantHsupp = :montantHsupp")
    , @NamedQuery(name = "Dossierhsupp.findByStatutDossier", query = "SELECT d FROM Dossierhsupp d WHERE d.statutDossier = :statutDossier")
    , @NamedQuery(name = "Dossierhsupp.findBySalaireAnnuelleBrut", query = "SELECT d FROM Dossierhsupp d WHERE d.salaireAnnuelleBrut = :salaireAnnuelleBrut")
    , @NamedQuery(name = "Dossierhsupp.findByAllocationFamiliale", query = "SELECT d FROM Dossierhsupp d WHERE d.allocationFamiliale = :allocationFamiliale")
    , @NamedQuery(name = "Dossierhsupp.findByBrutAdditionner", query = "SELECT d FROM Dossierhsupp d WHERE d.brutAdditionner = :brutAdditionner")
    , @NamedQuery(name = "Dossierhsupp.findByAmo", query = "SELECT d FROM Dossierhsupp d WHERE d.amo = :amo")
    , @NamedQuery(name = "Dossierhsupp.findByRetenuCmr", query = "SELECT d FROM Dossierhsupp d WHERE d.retenuCmr = :retenuCmr")
    , @NamedQuery(name = "Dossierhsupp.findByMutuelleMutialiste", query = "SELECT d FROM Dossierhsupp d WHERE d.mutuelleMutialiste = :mutuelleMutialiste")
    , @NamedQuery(name = "Dossierhsupp.findByMutuelleCaisse", query = "SELECT d FROM Dossierhsupp d WHERE d.mutuelleCaisse = :mutuelleCaisse")
    , @NamedQuery(name = "Dossierhsupp.findByRachatCmr", query = "SELECT d FROM Dossierhsupp d WHERE d.rachatCmr = :rachatCmr")
    , @NamedQuery(name = "Dossierhsupp.findBySommeDeduire", query = "SELECT d FROM Dossierhsupp d WHERE d.sommeDeduire = :sommeDeduire")
    , @NamedQuery(name = "Dossierhsupp.findByNbrEnfant", query = "SELECT d FROM Dossierhsupp d WHERE d.nbrEnfant = :nbrEnfant")
    , @NamedQuery(name = "Dossierhsupp.findByChargeFamiliale", query = "SELECT d FROM Dossierhsupp d WHERE d.chargeFamiliale = :chargeFamiliale")
    , @NamedQuery(name = "Dossierhsupp.findByIrSource", query = "SELECT d FROM Dossierhsupp d WHERE d.irSource = :irSource")
    , @NamedQuery(name = "Dossierhsupp.findByIrComplement", query = "SELECT d FROM Dossierhsupp d WHERE d.irComplement = :irComplement")
    , @NamedQuery(name = "Dossierhsupp.findByNet", query = "SELECT d FROM Dossierhsupp d WHERE d.net = :net")
    , @NamedQuery(name = "Dossierhsupp.findByIr", query = "SELECT d FROM Dossierhsupp d WHERE d.ir = :ir")
    , @NamedQuery(name = "Dossierhsupp.findByEchelle", query = "SELECT d FROM Dossierhsupp d WHERE d.echelle = :echelle")
    , @NamedQuery(name = "Dossierhsupp.findByEchelon", query = "SELECT d FROM Dossierhsupp d WHERE d.echelon = :echelon")})
public class Dossierhsupp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDossier")
    private Integer idDossier;
    @Column(name = "nbrHeures")
    private Integer nbrHeures;
    @Size(max = 254)
    @Column(name = "mois")
    private String mois;
    @Size(max = 254)
    @Column(name = "semestre")
    private String semestre;
    @Column(name = "dateCreance")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreance;
    @Column(name = "montantHsupp")
    private Integer montantHsupp;
    @Size(max = 254)
    @Column(name = "statutDossier")
    private String statutDossier;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "salaireAnnuelleBrut")
    private Double salaireAnnuelleBrut;
    @Column(name = "allocationFamiliale")
    private Double allocationFamiliale;
    @Column(name = "brutAdditionner")
    private Double brutAdditionner;
    @Column(name = "amo")
    private Double amo;
    @Column(name = "retenuCmr")
    private Double retenuCmr;
    @Column(name = "mutuelleMutialiste")
    private Double mutuelleMutialiste;
    @Column(name = "mutuelleCaisse")
    private Double mutuelleCaisse;
    @Column(name = "rachatCmr")
    private Double rachatCmr;
    @Column(name = "sommeDeduire")
    private Double sommeDeduire;
    @Column(name = "nbrEnfant")
    private Integer nbrEnfant;
    @Column(name = "chargeFamiliale")
    private Double chargeFamiliale;
    @Column(name = "irSource")
    private Double irSource;
    @Column(name = "irComplement")
    private Double irComplement;
    @Column(name = "net")
    private Integer net;
    @Column(name = "ir")
    private Integer ir;
    @Column(name = "echelle")
    private Integer echelle;
    @Size(max = 100)
    @Column(name = "echelon")
    private String echelon;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dosidDossier", fetch = FetchType.EAGER)
    private List<Dossierrejete> dossierrejeteList;
    @JoinColumn(name = "idDotation", referencedColumnName = "idDotation")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Dotationsecteur idDotation;
    @JoinColumn(name = "idGrade", referencedColumnName = "idGrade")
    @ManyToOne(fetch = FetchType.EAGER)
    private Graddiplome idGrade;
    @JoinColumn(name = "idDossierProv", referencedColumnName = "idDossierProv")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Dossierprovisoir idDossierProv;
    @JoinColumn(name = "idBordAut", referencedColumnName = "idBordAut")
    @ManyToOne(fetch = FetchType.EAGER)
    private Bordereauautorisation idBordAut;
    @JoinColumn(name = "idBordComp", referencedColumnName = "idBordComp")
    @ManyToOne(fetch = FetchType.EAGER)
    private Bordereaucomptable idBordComp;
    @JoinColumn(name = "cinPpr", referencedColumnName = "cinPpr")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Intervenant cinPpr;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dosidDossier", fetch = FetchType.EAGER)
    private List<Piecejustificativevacation> piecejustificativevacationList;

    public Dossierhsupp() {
    }

    public Dossierhsupp(Integer idDossier) {
        this.idDossier = idDossier;
    }

    public Integer getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(Integer idDossier) {
        this.idDossier = idDossier;
    }

    public Integer getNbrHeures() {
        return nbrHeures;
    }

    public void setNbrHeures(Integer nbrHeures) {
        this.nbrHeures = nbrHeures;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public Date getDateCreance() {
        return dateCreance;
    }

    public void setDateCreance(Date dateCreance) {
        this.dateCreance = dateCreance;
    }

    public Integer getMontantHsupp() {
        return montantHsupp;
    }

    public void setMontantHsupp(Integer montantHsupp) {
        this.montantHsupp = montantHsupp;
    }

    public String getStatutDossier() {
        return statutDossier;
    }

    public void setStatutDossier(String statutDossier) {
        this.statutDossier = statutDossier;
    }

    public Double getSalaireAnnuelleBrut() {
        return salaireAnnuelleBrut;
    }

    public void setSalaireAnnuelleBrut(Double salaireAnnuelleBrut) {
        this.salaireAnnuelleBrut = salaireAnnuelleBrut;
    }

    public Double getAllocationFamiliale() {
        return allocationFamiliale;
    }

    public void setAllocationFamiliale(Double allocationFamiliale) {
        this.allocationFamiliale = allocationFamiliale;
    }

    public Double getBrutAdditionner() {
        return brutAdditionner;
    }

    public void setBrutAdditionner(Double brutAdditionner) {
        this.brutAdditionner = brutAdditionner;
    }

    public Double getAmo() {
        return amo;
    }

    public void setAmo(Double amo) {
        this.amo = amo;
    }

    public Double getRetenuCmr() {
        return retenuCmr;
    }

    public void setRetenuCmr(Double retenuCmr) {
        this.retenuCmr = retenuCmr;
    }

    public Double getMutuelleMutialiste() {
        return mutuelleMutialiste;
    }

    public void setMutuelleMutialiste(Double mutuelleMutialiste) {
        this.mutuelleMutialiste = mutuelleMutialiste;
    }

    public Double getMutuelleCaisse() {
        return mutuelleCaisse;
    }

    public void setMutuelleCaisse(Double mutuelleCaisse) {
        this.mutuelleCaisse = mutuelleCaisse;
    }

    public Double getRachatCmr() {
        return rachatCmr;
    }

    public void setRachatCmr(Double rachatCmr) {
        this.rachatCmr = rachatCmr;
    }

    public Double getSommeDeduire() {
        return sommeDeduire;
    }

    public void setSommeDeduire(Double sommeDeduire) {
        this.sommeDeduire = sommeDeduire;
    }

    public Integer getNbrEnfant() {
        return nbrEnfant;
    }

    public void setNbrEnfant(Integer nbrEnfant) {
        this.nbrEnfant = nbrEnfant;
    }

    public Double getChargeFamiliale() {
        return chargeFamiliale;
    }

    public void setChargeFamiliale(Double chargeFamiliale) {
        this.chargeFamiliale = chargeFamiliale;
    }

    public Double getIrSource() {
        return irSource;
    }

    public void setIrSource(Double irSource) {
        this.irSource = irSource;
    }

    public Double getIrComplement() {
        return irComplement;
    }

    public void setIrComplement(Double irComplement) {
        this.irComplement = irComplement;
    }

    public Integer getNet() {
        return net;
    }

    public void setNet(Integer net) {
        this.net = net;
    }

    public Integer getIr() {
        return ir;
    }

    public void setIr(Integer ir) {
        this.ir = ir;
    }

    public Integer getEchelle() {
        return echelle;
    }

    public void setEchelle(Integer echelle) {
        this.echelle = echelle;
    }

    public String getEchelon() {
        return echelon;
    }

    public void setEchelon(String echelon) {
        this.echelon = echelon;
    }

    @XmlTransient
    public List<Dossierrejete> getDossierrejeteList() {
        return dossierrejeteList;
    }

    public void setDossierrejeteList(List<Dossierrejete> dossierrejeteList) {
        this.dossierrejeteList = dossierrejeteList;
    }

    public Dotationsecteur getIdDotation() {
        return idDotation;
    }

    public void setIdDotation(Dotationsecteur idDotation) {
        this.idDotation = idDotation;
    }

    public Graddiplome getIdGrade() {
        return idGrade;
    }

    public void setIdGrade(Graddiplome idGrade) {
        this.idGrade = idGrade;
    }

    public Dossierprovisoir getIdDossierProv() {
        return idDossierProv;
    }

    public void setIdDossierProv(Dossierprovisoir idDossierProv) {
        this.idDossierProv = idDossierProv;
    }

    public Bordereauautorisation getIdBordAut() {
        return idBordAut;
    }

    public void setIdBordAut(Bordereauautorisation idBordAut) {
        this.idBordAut = idBordAut;
    }

    public Bordereaucomptable getIdBordComp() {
        return idBordComp;
    }

    public void setIdBordComp(Bordereaucomptable idBordComp) {
        this.idBordComp = idBordComp;
    }

    public Intervenant getCinPpr() {
        return cinPpr;
    }

    public void setCinPpr(Intervenant cinPpr) {
        this.cinPpr = cinPpr;
    }

    @XmlTransient
    public List<Piecejustificativevacation> getPiecejustificativevacationList() {
        return piecejustificativevacationList;
    }

    public void setPiecejustificativevacationList(List<Piecejustificativevacation> piecejustificativevacationList) {
        this.piecejustificativevacationList = piecejustificativevacationList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDossier != null ? idDossier.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dossierhsupp)) {
            return false;
        }
        Dossierhsupp other = (Dossierhsupp) object;
        if ((this.idDossier == null && other.idDossier != null) || (this.idDossier != null && !this.idDossier.equals(other.idDossier))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Dossierhsupp[ idDossier=" + idDossier + " ]";
    }
    
}
