package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Role;
import model.Users;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-27T13:56:08")
@StaticMetamodel(UserRole.class)
public class UserRole_ { 

    public static volatile SingularAttribute<UserRole, Users> idUser;
    public static volatile SingularAttribute<UserRole, Role> idRole;
    public static volatile SingularAttribute<UserRole, Integer> idUserRole;

}