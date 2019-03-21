/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
 
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import sun.security.provider.SHA;
 
  
@ManagedBean(name="loginBean")
@SessionScoped
public class LoginBean implements Serializable{
    private String userName;
    private String password;
    HttpSession session= (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
  
  
    private String dbuserName;
  private String dbrole;
    private String dbpassword;
    private int id_user;
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    String SQL;

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
    
    
    
    
    
    public String getDbrole() {
        return dbrole;
    }

    public void setDbrole(String dbrole) {
        this.dbrole = dbrole;
    }
     
  
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public String getDbuserName() {
        return dbuserName;
    }
 
    public void setDbuserName(String dbuserName) {
        this.dbuserName = dbuserName;
    }
 
    public String getDbpassword() {
        return dbpassword;
    }
 
    public void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }
 
    public void dbData(String userName)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bd_finance","root","root");
            statement = connection.createStatement();
            System.out.println(userName);
            SQL = "Select * from users u, role r , user_role ur where u.idUser = ur.idUser and ur.idRole = r.idRole and u.login = '"+userName+"'";
            resultSet = statement.executeQuery(SQL);
          while (resultSet.next()) {  
            //System.out.println(resultSet.getInt(0));
            id_user= resultSet.getInt(1);
            dbuserName = resultSet.getString(2).toString();
            dbpassword = resultSet.getString(3).toString();
            dbrole = resultSet.getString(10).toString();
          }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            System.out.println("Exception Occured in the process :" + ex);
        }
    }
     
    public String checkValidUser()
    {
        dbData(userName);
  
        if(userName.equalsIgnoreCase(dbuserName))
        {
            if((password).equals(dbpassword)){
                //session.setAttribute("id_user", id_user);
                if(dbrole.equals("Demandeur")){
                    //System.out.println(session.getAttribute("id_user"));
                     return "DEMANDEUR/boncommande/LancerDemande.xhtml?faces-redirect=true";
                }else if(dbrole.equals("Admin")){
                    //System.out.println("le code de ce utilisateur est"+session.getAttribute("id_user"));
                    return "ADMIN/users/List.xhtml?faces-redirect=true";
                }else if(dbrole.equals("OperateurCommande")){
                    // System.out.println("le code de ce utilisateur est  "+session.getAttribute("id_user"));
                return "OPERATEUR/BC/GererEngagements/ValiderBC.xhtml?faces-redirect=true";
                
                }else if(dbrole.equals("OperateurVacation")){
                   // System.out.println("le code de ce utilisateur est  "+session.getAttribute("id_user"));
                return "OPERATEUR_VAC/vacations/dossierIntervenant.xhtml?faces-redirect=true";
                
                }else if(dbrole.equals("vice doyen")){
                 return "/visDoyen/departement/ListeDepartement_1";
                }else if(dbrole.equals("secretariat")){
                 return "/visDoyen/SecretariaGeneral/ListeDepartement_1";
                }
                
                
                else{
                    return "index.xhtml";
                }
                
                
                
                    
            }else
            {
                return "pageError";
            }
        }
        else
        {
            return "pageError";
        }
    }
    
    public String PrepareLog(){
        return "index.xhtml?faces-redirect=true";
    }
}

