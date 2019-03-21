package model;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Prixkilomitrique.class)
public class Prixkilomitrique_ { 

    public static volatile SingularAttribute<Prixkilomitrique, Integer> idPrixKilo;
    public static volatile SingularAttribute<Prixkilomitrique, BigDecimal> kmInf;
    public static volatile SingularAttribute<Prixkilomitrique, BigDecimal> prixPiste;
    public static volatile SingularAttribute<Prixkilomitrique, BigDecimal> kmSup;
    public static volatile SingularAttribute<Prixkilomitrique, BigDecimal> prixRoute;

}