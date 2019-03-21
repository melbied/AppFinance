package model;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Anneebudgetaire;
import model.BudgetPK;
import model.Compte;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Budget.class)
public class Budget_ { 

    public static volatile SingularAttribute<Budget, Anneebudgetaire> anneebudgetaire;
    public static volatile SingularAttribute<Budget, BigDecimal> reliquat;
    public static volatile SingularAttribute<Budget, BigDecimal> budgetAnnuel;
    public static volatile SingularAttribute<Budget, BudgetPK> budgetPK;
    public static volatile SingularAttribute<Budget, Compte> compte;

}