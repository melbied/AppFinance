package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Dossierhsupp;
import model.Dossiervacataire;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Bordereauautorisation.class)
public class Bordereauautorisation_ { 

    public static volatile SingularAttribute<Bordereauautorisation, Integer> anneeUniversitaire;
    public static volatile SingularAttribute<Bordereauautorisation, Integer> idBordAut;
    public static volatile ListAttribute<Bordereauautorisation, Dossiervacataire> dossiervacataireList;
    public static volatile ListAttribute<Bordereauautorisation, Dossierhsupp> dossierhsuppList;

}