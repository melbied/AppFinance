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
 * @author Imane Rafiq
 */
@Entity
@Table(name = "deplacement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Deplacement.findAll", query = "SELECT d FROM Deplacement d")
    , @NamedQuery(name = "Deplacement.findByIdDeplacement", query = "SELECT d FROM Deplacement d WHERE d.idDeplacement = :idDeplacement")
    , @NamedQuery(name = "Deplacement.findByNbrJours", query = "SELECT d FROM Deplacement d WHERE d.nbrJours = :nbrJours")
    , @NamedQuery(name = "Deplacement.findByDateDepart", query = "SELECT d FROM Deplacement d WHERE d.dateDepart = :dateDepart")
    , @NamedQuery(name = "Deplacement.findByDateArrive", query = "SELECT d FROM Deplacement d WHERE d.dateArrive = :dateArrive")
    , @NamedQuery(name = "Deplacement.findByAnnee", query = "SELECT d FROM Deplacement d WHERE d.annee = :annee")
    , @NamedQuery(name = "Deplacement.findByMotif", query = "SELECT d FROM Deplacement d WHERE d.motif = :motif")
    , @NamedQuery(name = "Deplacement.findByDateCreation", query = "SELECT d FROM Deplacement d WHERE d.dateCreation = :dateCreation")
    , @NamedQuery(name = "Deplacement.findByLastUptDt", query = "SELECT d FROM Deplacement d WHERE d.lastUptDt = :lastUptDt")
    , @NamedQuery(name = "Deplacement.findByIndice", query = "SELECT d FROM Deplacement d WHERE d.indice = :indice")
    , @NamedQuery(name = "Deplacement.findByEchelle", query = "SELECT d FROM Deplacement d WHERE d.echelle = :echelle")
    , @NamedQuery(name = "Deplacement.findByGrade", query = "SELECT d FROM Deplacement d WHERE d.grade = :grade")
    , @NamedQuery(name = "Deplacement.findByEtat", query = "SELECT d FROM Deplacement d WHERE d.etat = :etat")
    , @NamedQuery(name = "Deplacement.findByObservation", query = "SELECT d FROM Deplacement d WHERE d.observation = :observation")
    , @NamedQuery(name = "Deplacement.findByDateEtat", query = "SELECT d FROM Deplacement d WHERE d.dateEtat = :dateEtat")
    , @NamedQuery(name = "Deplacement.findByDatePiece", query = "SELECT d FROM Deplacement d WHERE d.datePiece = :datePiece")
    , @NamedQuery(name = "Deplacement.findByIntutilePiece", query = "SELECT d FROM Deplacement d WHERE d.intutilePiece = :intutilePiece")
    , @NamedQuery(name = "Deplacement.findByKmRoute", query = "SELECT d FROM Deplacement d WHERE d.kmRoute = :kmRoute")
    , @NamedQuery(name = "Deplacement.findByKmPiste", query = "SELECT d FROM Deplacement d WHERE d.kmPiste = :kmPiste")
    , @NamedQuery(name = "Deplacement.findByMntkm", query = "SELECT d FROM Deplacement d WHERE d.mntkm = :mntkm")
    , @NamedQuery(name = "Deplacement.findByMarque", query = "SELECT d FROM Deplacement d WHERE d.marque = :marque")
    , @NamedQuery(name = "Deplacement.findByPuissance", query = "SELECT d FROM Deplacement d WHERE d.puissance = :puissance")
    , @NamedQuery(name = "Deplacement.findByMontantDepExt", query = "SELECT d FROM Deplacement d WHERE d.montantDepExt = :montantDepExt")
    , @NamedQuery(name = "Deplacement.findByMontantDepInt", query = "SELECT d FROM Deplacement d WHERE d.montantDepInt = :montantDepInt")
    , @NamedQuery(name = "Deplacement.findByTypedep", query = "SELECT d FROM Deplacement d WHERE d.typedep = :typedep")
    , @NamedQuery(name = "Deplacement.findByNomPays", query = "SELECT d FROM Deplacement d WHERE d.nomPays = :nomPays")
    , @NamedQuery(name = "Deplacement.findByVille", query = "SELECT d FROM Deplacement d WHERE d.ville = :ville")
    , @NamedQuery(name = "Deplacement.findByJustification", query = "SELECT d FROM Deplacement d WHERE d.justification = :justification")
    , @NamedQuery(name = "Deplacement.findByPiece1", query = "SELECT d FROM Deplacement d WHERE d.piece1 = :piece1")
    , @NamedQuery(name = "Deplacement.findByPiece2", query = "SELECT d FROM Deplacement d WHERE d.piece2 = :piece2")
    , @NamedQuery(name = "Deplacement.findByPiece3", query = "SELECT d FROM Deplacement d WHERE d.piece3 = :piece3")})
public class Deplacement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDeplacement")
    private Integer idDeplacement;
    @Column(name = "nbrJours")
    private Integer nbrJours;
    @Column(name = "dateDepart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDepart;
    @Column(name = "dateArrive")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateArrive;
    @Column(name = "annee")
    private Integer annee;
    @Size(max = 254)
    @Column(name = "motif")
    private String motif;
    @Column(name = "dateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Column(name = "lastUptDt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUptDt;
    @Column(name = "indice")
    private Integer indice;
    @Column(name = "echelle")
    private Integer echelle;
    @Size(max = 254)
    @Column(name = "grade")
    private String grade;
    @Column(name = "etat")
    private Integer etat;
    @Size(max = 255)
    @Column(name = "observation")
    private String observation;
    @Column(name = "dateEtat")
    @Temporal(TemporalType.DATE)
    private Date dateEtat;
    @Column(name = "datePiece")
    @Temporal(TemporalType.DATE)
    private Date datePiece;
    @Size(max = 255)
    @Column(name = "intutilePiece")
    private String intutilePiece;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "kmRoute")
    private Double kmRoute;
    @Column(name = "kmPiste")
    private Double kmPiste;
    @Column(name = "mntkm")
    private Double mntkm;
    @Size(max = 255)
    @Column(name = "marque")
    private String marque;
    @Column(name = "puissance")
    private Integer puissance;
    @Column(name = "montantDepExt")
    private Double montantDepExt;
    @Column(name = "montantDepInt")
    private Double montantDepInt;
    @Column(name = "typedep")
    private Integer typedep;
    @Size(max = 255)
    @Column(name = "nomPays")
    private String nomPays;
    @Size(max = 255)
    @Column(name = "ville")
    private String ville;
    @Size(max = 255)
    @Column(name = "justification")
    private String justification;
    @Size(max = 255)
    @Column(name = "piece1")
    private String piece1;
    @Size(max = 255)
    @Column(name = "piece2")
    private String piece2;
    @Size(max = 255)
    @Column(name = "piece3")
    private String piece3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDeplacement", fetch = FetchType.EAGER)
    private List<Piecejustificativedeplacement> piecejustificativedeplacementList;
    @JoinColumn(name = "idDotationSect", referencedColumnName = "idDotation")
    @ManyToOne(fetch = FetchType.EAGER)
    private Dotationsecteur idDotationSect;
    @JoinColumn(name = "cinPpr", referencedColumnName = "cinPpr")
    @ManyToOne(fetch = FetchType.EAGER)
    private Intervenant cinPpr;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @ManyToOne(fetch = FetchType.EAGER)
    private Users idUser;

    public Deplacement() {
    }

    public Deplacement(Integer idDeplacement) {
        this.idDeplacement = idDeplacement;
    }

    public Integer getIdDeplacement() {
        return idDeplacement;
    }

    public void setIdDeplacement(Integer idDeplacement) {
        this.idDeplacement = idDeplacement;
    }

    public Integer getNbrJours() {
        return nbrJours;
    }

    public void setNbrJours(Integer nbrJours) {
        this.nbrJours = nbrJours;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Date getDateArrive() {
        return dateArrive;
    }

    public void setDateArrive(Date dateArrive) {
        this.dateArrive = dateArrive;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getLastUptDt() {
        return lastUptDt;
    }

    public void setLastUptDt(Date lastUptDt) {
        this.lastUptDt = lastUptDt;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public Integer getEchelle() {
        return echelle;
    }

    public void setEchelle(Integer echelle) {
        this.echelle = echelle;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getEtat() {
        return etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Date getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(Date dateEtat) {
        this.dateEtat = dateEtat;
    }

    public Date getDatePiece() {
        return datePiece;
    }

    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    public String getIntutilePiece() {
        return intutilePiece;
    }

    public void setIntutilePiece(String intutilePiece) {
        this.intutilePiece = intutilePiece;
    }

    public Double getKmRoute() {
        return kmRoute;
    }

    public void setKmRoute(Double kmRoute) {
        this.kmRoute = kmRoute;
    }

    public Double getKmPiste() {
        return kmPiste;
    }

    public void setKmPiste(Double kmPiste) {
        this.kmPiste = kmPiste;
    }

    public Double getMntkm() {
        return mntkm;
    }

    public void setMntkm(Double mntkm) {
        this.mntkm = mntkm;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Integer getPuissance() {
        return puissance;
    }

    public void setPuissance(Integer puissance) {
        this.puissance = puissance;
    }

    public Double getMontantDepExt() {
        return montantDepExt;
    }

    public void setMontantDepExt(Double montantDepExt) {
        this.montantDepExt = montantDepExt;
    }

    public Double getMontantDepInt() {
        return montantDepInt;
    }

    public void setMontantDepInt(Double montantDepInt) {
        this.montantDepInt = montantDepInt;
    }

    public Integer getTypedep() {
        return typedep;
    }

    public void setTypedep(Integer typedep) {
        this.typedep = typedep;
    }

    public String getNomPays() {
        return nomPays;
    }

    public void setNomPays(String nomPays) {
        this.nomPays = nomPays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getPiece1() {
        return piece1;
    }

    public void setPiece1(String piece1) {
        this.piece1 = piece1;
    }

    public String getPiece2() {
        return piece2;
    }

    public void setPiece2(String piece2) {
        this.piece2 = piece2;
    }

    public String getPiece3() {
        return piece3;
    }

    public void setPiece3(String piece3) {
        this.piece3 = piece3;
    }

    @XmlTransient
    public List<Piecejustificativedeplacement> getPiecejustificativedeplacementList() {
        return piecejustificativedeplacementList;
    }

    public void setPiecejustificativedeplacementList(List<Piecejustificativedeplacement> piecejustificativedeplacementList) {
        this.piecejustificativedeplacementList = piecejustificativedeplacementList;
    }

    public Dotationsecteur getIdDotationSect() {
        return idDotationSect;
    }

    public void setIdDotationSect(Dotationsecteur idDotationSect) {
        this.idDotationSect = idDotationSect;
    }

    public Intervenant getCinPpr() {
        return cinPpr;
    }

    public void setCinPpr(Intervenant cinPpr) {
        this.cinPpr = cinPpr;
    }

    public Users getIdUser() {
        return idUser;
    }

    public void setIdUser(Users idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDeplacement != null ? idDeplacement.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Deplacement)) {
            return false;
        }
        Deplacement other = (Deplacement) object;
        if ((this.idDeplacement == null && other.idDeplacement != null) || (this.idDeplacement != null && !this.idDeplacement.equals(other.idDeplacement))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Deplacement[ idDeplacement=" + idDeplacement + " ]";
    }
    
}
