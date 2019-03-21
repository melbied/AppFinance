/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import model.Fournisseur;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Imane Rafiq
 */
@ManagedBean (name="OJ")
@ViewScoped
public class OJBEAN implements Serializable{
    private List<String> values;

    @PostConstruct
    public void init() {
        /*values = new ArrayList();
        values.add(new Fournisseur());*/
        values = new ArrayList();
        values.add("");
        
    }

        public OJBEAN() {
        }
    

    public void submit() {
        // save values in database
        System.out.println(values);
    }

    public void extend() {
         values.add("");
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }
}
