package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Dotationsecteur;
import model.Users;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Boncommande.class)
public class Boncommande_ { 

    public static volatile SingularAttribute<Boncommande, Users> idUser;
    public static volatile SingularAttribute<Boncommande, Integer> idFournisseur;
    public static volatile SingularAttribute<Boncommande, Dotationsecteur> idDotation;
    public static volatile SingularAttribute<Boncommande, Double> montant;
    public static volatile SingularAttribute<Boncommande, Date> dateCommande;
    public static volatile SingularAttribute<Boncommande, String> type;
    public static volatile SingularAttribute<Boncommande, String> NomComplet;
    public static volatile SingularAttribute<Boncommande, Integer> idBC;
    public static volatile SingularAttribute<Boncommande, String> etat;
    public static volatile SingularAttribute<Boncommande, Integer> tva;
    public static volatile SingularAttribute<Boncommande, Date> dateReception;

}