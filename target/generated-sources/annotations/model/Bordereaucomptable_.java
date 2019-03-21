package model;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Anneebudgetaire;
import model.Dossierhsupp;
import model.Dossiervacataire;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Bordereaucomptable.class)
public class Bordereaucomptable_ { 

    public static volatile SingularAttribute<Bordereaucomptable, BigDecimal> totalIr;
    public static volatile SingularAttribute<Bordereaucomptable, Date> dateExercice;
    public static volatile ListAttribute<Bordereaucomptable, Dossiervacataire> dossiervacataireList;
    public static volatile SingularAttribute<Bordereaucomptable, Anneebudgetaire> annee;
    public static volatile SingularAttribute<Bordereaucomptable, Integer> idBordComp;
    public static volatile ListAttribute<Bordereaucomptable, Dossierhsupp> dossierhsuppList;
    public static volatile SingularAttribute<Bordereaucomptable, BigDecimal> totalNet;

}