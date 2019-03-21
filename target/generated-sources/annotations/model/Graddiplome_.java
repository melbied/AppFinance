package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Dossierhsupp;
import model.Dossierprovisoir;
import model.Dossiervacataire;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Graddiplome.class)
public class Graddiplome_ { 

    public static volatile SingularAttribute<Graddiplome, String> intituleGrade;
    public static volatile SingularAttribute<Graddiplome, Integer> taux;
    public static volatile ListAttribute<Graddiplome, Dossierprovisoir> dossierprovisoirList;
    public static volatile ListAttribute<Graddiplome, Dossiervacataire> dossiervacataireList;
    public static volatile SingularAttribute<Graddiplome, Integer> idGrade;
    public static volatile ListAttribute<Graddiplome, Dossierhsupp> dossierhsuppList;

}