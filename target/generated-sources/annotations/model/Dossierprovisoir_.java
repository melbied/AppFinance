package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Dossierhsupp;
import model.Dossiervacataire;
import model.Graddiplome;
import model.Releve;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Dossierprovisoir.class)
public class Dossierprovisoir_ { 

    public static volatile SingularAttribute<Dossierprovisoir, Integer> nbrHeures;
    public static volatile SingularAttribute<Dossierprovisoir, Date> dateDebut;
    public static volatile SingularAttribute<Dossierprovisoir, String> dernierDiplome;
    public static volatile SingularAttribute<Dossierprovisoir, String> module;
    public static volatile ListAttribute<Dossierprovisoir, Dossiervacataire> dossiervacataireList;
    public static volatile SingularAttribute<Dossierprovisoir, Integer> idDossierProv;
    public static volatile SingularAttribute<Dossierprovisoir, String> nomComplet;
    public static volatile SingularAttribute<Dossierprovisoir, Date> dateFin;
    public static volatile SingularAttribute<Dossierprovisoir, Graddiplome> idGrade;
    public static volatile ListAttribute<Dossierprovisoir, Dossierhsupp> dossierhsuppList;
    public static volatile SingularAttribute<Dossierprovisoir, Boolean> etat;
    public static volatile SingularAttribute<Dossierprovisoir, Releve> idRelever;

}