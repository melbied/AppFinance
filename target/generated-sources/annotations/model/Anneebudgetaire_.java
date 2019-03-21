package model;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Bordereaucomptable;
import model.Budget;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Anneebudgetaire.class)
public class Anneebudgetaire_ { 

    public static volatile SingularAttribute<Anneebudgetaire, BigDecimal> reliquatRap;
    public static volatile ListAttribute<Anneebudgetaire, Budget> budgetList;
    public static volatile SingularAttribute<Anneebudgetaire, Integer> annee;
    public static volatile SingularAttribute<Anneebudgetaire, BigDecimal> montantRap;
    public static volatile ListAttribute<Anneebudgetaire, Bordereaucomptable> bordereaucomptableList;

}