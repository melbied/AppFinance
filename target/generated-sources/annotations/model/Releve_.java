package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Dossierprovisoir;
import model.Filiere;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Releve.class)
public class Releve_ { 

    public static volatile SingularAttribute<Releve, String> anneeUniversitaire;
    public static volatile SingularAttribute<Releve, Filiere> idFiliere;
    public static volatile ListAttribute<Releve, Dossierprovisoir> dossierprovisoirList;
    public static volatile SingularAttribute<Releve, String> semestre;
    public static volatile SingularAttribute<Releve, Integer> idRelever;

}