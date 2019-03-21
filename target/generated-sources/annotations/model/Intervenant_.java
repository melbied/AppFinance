package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Comptebc;
import model.Deplacement;
import model.Dossierhsupp;
import model.Dossiervacataire;
import model.Users;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Intervenant.class)
public class Intervenant_ { 

    public static volatile SingularAttribute<Intervenant, String> cinPpr;
    public static volatile SingularAttribute<Intervenant, Users> idUser;
    public static volatile SingularAttribute<Intervenant, String> mail;
    public static volatile ListAttribute<Intervenant, Deplacement> deplacementList;
    public static volatile SingularAttribute<Intervenant, String> nomArabe;
    public static volatile ListAttribute<Intervenant, Dossiervacataire> dossiervacataireList;
    public static volatile SingularAttribute<Intervenant, String> nomComplet;
    public static volatile SingularAttribute<Intervenant, String> telephone;
    public static volatile ListAttribute<Intervenant, Dossierhsupp> dossierhsuppList;
    public static volatile ListAttribute<Intervenant, Comptebc> comptebcList;

}