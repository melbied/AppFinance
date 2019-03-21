package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Dossierhsupp;
import model.Dossiervacataire;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(Piecejustificativevacation.class)
public class Piecejustificativevacation_ { 

    public static volatile SingularAttribute<Piecejustificativevacation, Date> datePiece;
    public static volatile SingularAttribute<Piecejustificativevacation, Dossiervacataire> idDossier;
    public static volatile SingularAttribute<Piecejustificativevacation, String> piece;
    public static volatile SingularAttribute<Piecejustificativevacation, Dossierhsupp> dosidDossier;
    public static volatile SingularAttribute<Piecejustificativevacation, Integer> idPiece;
    public static volatile SingularAttribute<Piecejustificativevacation, String> intutilePiece;

}