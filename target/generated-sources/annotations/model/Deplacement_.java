package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Dotationsecteur;
import model.Intervenant;
import model.Piecejustificativedeplacement;
import model.Users;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Deplacement.class)
public class Deplacement_ { 

    public static volatile SingularAttribute<Deplacement, Integer> nbrJours;
    public static volatile SingularAttribute<Deplacement, Date> lastUptDt;
    public static volatile SingularAttribute<Deplacement, Date> dateCreation;
    public static volatile SingularAttribute<Deplacement, Integer> echelle;
    public static volatile SingularAttribute<Deplacement, Date> dateArrive;
    public static volatile SingularAttribute<Deplacement, String> nomPays;
    public static volatile SingularAttribute<Deplacement, Date> dateDepart;
    public static volatile SingularAttribute<Deplacement, Integer> annee;
    public static volatile SingularAttribute<Deplacement, Integer> etat;
    public static volatile SingularAttribute<Deplacement, String> intutilePiece;
    public static volatile SingularAttribute<Deplacement, Integer> idDeplacement;
    public static volatile SingularAttribute<Deplacement, String> piece3;
    public static volatile SingularAttribute<Deplacement, Users> idUser;
    public static volatile SingularAttribute<Deplacement, String> piece2;
    public static volatile SingularAttribute<Deplacement, Double> montantDepInt;
    public static volatile SingularAttribute<Deplacement, String> piece1;
    public static volatile SingularAttribute<Deplacement, Double> kmPiste;
    public static volatile SingularAttribute<Deplacement, Double> montantDepExt;
    public static volatile SingularAttribute<Deplacement, String> motif;
    public static volatile SingularAttribute<Deplacement, String> justification;
    public static volatile SingularAttribute<Deplacement, Dotationsecteur> idDotationSect;
    public static volatile SingularAttribute<Deplacement, Intervenant> cinPpr;
    public static volatile SingularAttribute<Deplacement, String> ville;
    public static volatile SingularAttribute<Deplacement, Date> dateEtat;
    public static volatile SingularAttribute<Deplacement, Integer> indice;
    public static volatile SingularAttribute<Deplacement, String> observation;
    public static volatile SingularAttribute<Deplacement, Double> mntkm;
    public static volatile ListAttribute<Deplacement, Piecejustificativedeplacement> piecejustificativedeplacementList;
    public static volatile SingularAttribute<Deplacement, String> marque;
    public static volatile SingularAttribute<Deplacement, Date> datePiece;
    public static volatile SingularAttribute<Deplacement, Integer> typedep;
    public static volatile SingularAttribute<Deplacement, Integer> puissance;
    public static volatile SingularAttribute<Deplacement, Double> kmRoute;
    public static volatile SingularAttribute<Deplacement, String> grade;

}