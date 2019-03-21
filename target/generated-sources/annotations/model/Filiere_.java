package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Departement;
import model.Releve;
import model.Typeformations;
import model.Users;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Filiere.class)
public class Filiere_ { 

    public static volatile SingularAttribute<Filiere, Users> idUser;
    public static volatile SingularAttribute<Filiere, Departement> idDep;
    public static volatile SingularAttribute<Filiere, Typeformations> idType;
    public static volatile SingularAttribute<Filiere, Integer> idFiliere;
    public static volatile SingularAttribute<Filiere, String> intitule;
    public static volatile ListAttribute<Filiere, Releve> releveList;

}