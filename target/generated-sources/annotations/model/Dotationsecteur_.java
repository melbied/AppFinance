package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Boncommande;
import model.Compte;
import model.Deplacement;
import model.Dossierhsupp;
import model.Dossiervacataire;
import model.Secteur;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Dotationsecteur.class)
public class Dotationsecteur_ { 

    public static volatile SingularAttribute<Dotationsecteur, Double> reliquat;
    public static volatile SingularAttribute<Dotationsecteur, Compte> idCompte;
    public static volatile SingularAttribute<Dotationsecteur, Double> montantInitial;
    public static volatile ListAttribute<Dotationsecteur, Deplacement> deplacementList;
    public static volatile ListAttribute<Dotationsecteur, Dossiervacataire> dossiervacataireList;
    public static volatile SingularAttribute<Dotationsecteur, Integer> idDotation;
    public static volatile ListAttribute<Dotationsecteur, Boncommande> boncommandeList;
    public static volatile SingularAttribute<Dotationsecteur, Secteur> idSecteur;
    public static volatile ListAttribute<Dotationsecteur, Dossierhsupp> dossierhsuppList;

}