package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Boncommande;
import model.Departement;
import model.Deplacement;
import model.Filiere;
import model.Intervenant;
import model.UserRole;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, String> img;
    public static volatile ListAttribute<Users, Departement> departementList;
    public static volatile ListAttribute<Users, Intervenant> intervenantList;
    public static volatile SingularAttribute<Users, String> telephone;
    public static volatile ListAttribute<Users, Boncommande> boncommandeList;
    public static volatile SingularAttribute<Users, String> login;
    public static volatile SingularAttribute<Users, String> nom;
    public static volatile ListAttribute<Users, UserRole> userRoleList;
    public static volatile SingularAttribute<Users, Integer> idUser;
    public static volatile SingularAttribute<Users, String> password;
    public static volatile ListAttribute<Users, Deplacement> deplacementList;
    public static volatile ListAttribute<Users, Filiere> filiereList;
    public static volatile SingularAttribute<Users, String> prenom;
    public static volatile SingularAttribute<Users, String> email;

}