package model;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Budget;
import model.Dotationsecteur;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Compte.class)
public class Compte_ { 

    public static volatile SingularAttribute<Compte, BigDecimal> rap;
    public static volatile ListAttribute<Compte, Dotationsecteur> dotationsecteurList;
    public static volatile SingularAttribute<Compte, Integer> idCompte;
    public static volatile ListAttribute<Compte, Budget> budgetList;
    public static volatile SingularAttribute<Compte, String> intitule;

}