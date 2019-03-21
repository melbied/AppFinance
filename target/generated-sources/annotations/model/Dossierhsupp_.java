package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Bordereauautorisation;
import model.Bordereaucomptable;
import model.Dossierprovisoir;
import model.Dossierrejete;
import model.Dotationsecteur;
import model.Graddiplome;
import model.Intervenant;
import model.Piecejustificativevacation;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Dossierhsupp.class)
public class Dossierhsupp_ { 

    public static volatile SingularAttribute<Dossierhsupp, Double> amo;
    public static volatile SingularAttribute<Dossierhsupp, Integer> echelle;
    public static volatile ListAttribute<Dossierhsupp, Dossierrejete> dossierrejeteList;
    public static volatile SingularAttribute<Dossierhsupp, Graddiplome> idGrade;
    public static volatile SingularAttribute<Dossierhsupp, Bordereaucomptable> idBordComp;
    public static volatile SingularAttribute<Dossierhsupp, Double> sommeDeduire;
    public static volatile SingularAttribute<Dossierhsupp, Integer> idDossier;
    public static volatile SingularAttribute<Dossierhsupp, Integer> nbrEnfant;
    public static volatile SingularAttribute<Dossierhsupp, Double> rachatCmr;
    public static volatile SingularAttribute<Dossierhsupp, Double> mutuelleCaisse;
    public static volatile SingularAttribute<Dossierhsupp, Integer> net;
    public static volatile SingularAttribute<Dossierhsupp, Double> retenuCmr;
    public static volatile SingularAttribute<Dossierhsupp, Double> mutuelleMutialiste;
    public static volatile SingularAttribute<Dossierhsupp, Date> dateCreance;
    public static volatile SingularAttribute<Dossierhsupp, Double> chargeFamiliale;
    public static volatile SingularAttribute<Dossierhsupp, Intervenant> cinPpr;
    public static volatile SingularAttribute<Dossierhsupp, Integer> nbrHeures;
    public static volatile ListAttribute<Dossierhsupp, Piecejustificativevacation> piecejustificativevacationList;
    public static volatile SingularAttribute<Dossierhsupp, Double> salaireAnnuelleBrut;
    public static volatile SingularAttribute<Dossierhsupp, Bordereauautorisation> idBordAut;
    public static volatile SingularAttribute<Dossierhsupp, Integer> ir;
    public static volatile SingularAttribute<Dossierhsupp, Dossierprovisoir> idDossierProv;
    public static volatile SingularAttribute<Dossierhsupp, String> statutDossier;
    public static volatile SingularAttribute<Dossierhsupp, Double> irComplement;
    public static volatile SingularAttribute<Dossierhsupp, Double> irSource;
    public static volatile SingularAttribute<Dossierhsupp, Integer> montantHsupp;
    public static volatile SingularAttribute<Dossierhsupp, Dotationsecteur> idDotation;
    public static volatile SingularAttribute<Dossierhsupp, Double> brutAdditionner;
    public static volatile SingularAttribute<Dossierhsupp, String> semestre;
    public static volatile SingularAttribute<Dossierhsupp, Double> allocationFamiliale;
    public static volatile SingularAttribute<Dossierhsupp, String> echelon;
    public static volatile SingularAttribute<Dossierhsupp, String> mois;

}