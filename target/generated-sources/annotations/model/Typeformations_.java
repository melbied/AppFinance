package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Filiere;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Typeformations.class)
public class Typeformations_ { 

    public static volatile SingularAttribute<Typeformations, Integer> idType;
    public static volatile SingularAttribute<Typeformations, String> nomType;
    public static volatile ListAttribute<Typeformations, Filiere> filiereList;

}