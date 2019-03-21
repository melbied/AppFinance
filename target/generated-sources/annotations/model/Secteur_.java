package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Dotationsecteur;
import model.Secteurprincipal;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Secteur.class)
public class Secteur_ { 

    public static volatile ListAttribute<Secteur, Dotationsecteur> dotationsecteurList;
    public static volatile SingularAttribute<Secteur, Integer> idSecteur;
    public static volatile SingularAttribute<Secteur, String> intituleSecteur;
    public static volatile SingularAttribute<Secteur, Secteurprincipal> idSecteurP;

}