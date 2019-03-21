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
@StaticMetamodel(Dossiervacataire.class)
public class Dossiervacataire_ { 

    public static volatile SingularAttribute<Dossiervacataire, Intervenant> cinPpr;
    public static volatile SingularAttribute<Dossiervacataire, Integer> nbrHeures;
    public static volatile ListAttribute<Dossiervacataire, Piecejustificativevacation> piecejustificativevacationList;
    public static volatile SingularAttribute<Dossiervacataire, Bordereauautorisation> idBordAut;
    public static volatile SingularAttribute<Dossiervacataire, Integer> ir;
    public static volatile SingularAttribute<Dossiervacataire, Dossierprovisoir> idDossierProv;
    public static volatile ListAttribute<Dossiervacataire, Dossierrejete> dossierrejeteList;
    public static volatile SingularAttribute<Dossiervacataire, String> statutDossier;
    public static volatile SingularAttribute<Dossiervacataire, Graddiplome> idGrade;
    public static volatile SingularAttribute<Dossiervacataire, Bordereaucomptable> idBordComp;
    public static volatile SingularAttribute<Dossiervacataire, Integer> idDossier;
    public static volatile SingularAttribute<Dossiervacataire, Dotationsecteur> idDotation;
    public static volatile SingularAttribute<Dossiervacataire, String> semestre;
    public static volatile SingularAttribute<Dossiervacataire, Integer> net;
    public static volatile SingularAttribute<Dossiervacataire, Date> dateCreance;
    public static volatile SingularAttribute<Dossiervacataire, String> mois;

}