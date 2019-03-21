package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Filiere;
import model.Users;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Departement.class)
public class Departement_ { 

    public static volatile SingularAttribute<Departement, String> nomDep;
    public static volatile SingularAttribute<Departement, Users> idUser;
    public static volatile SingularAttribute<Departement, Integer> idDep;
    public static volatile ListAttribute<Departement, Filiere> filiereList;

}