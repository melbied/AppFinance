package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Dossierhsupp;
import model.Dossiervacataire;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Dossierrejete.class)
public class Dossierrejete_ { 

    public static volatile SingularAttribute<Dossierrejete, Integer> idDossierRejete;
    public static volatile SingularAttribute<Dossierrejete, Dossiervacataire> idDossier;
    public static volatile SingularAttribute<Dossierrejete, Dossierhsupp> dosidDossier;
    public static volatile SingularAttribute<Dossierrejete, String> motifRejet;

}