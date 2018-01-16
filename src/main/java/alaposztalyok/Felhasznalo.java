/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.util.ArrayList;
import java.util.List;

/**
 * Egy felhasználó eredményeit, és nevét tárolja
 * @author varga
 */
public class Felhasznalo {

    private String felhasznaloNev;
    private List<Eredmeny> eredmenyek;

    public String getFelhasznaloNev() {
        return felhasznaloNev;
    }

    public List<Eredmeny> getEredmenyek() {
        return new ArrayList<>(eredmenyek);
    }

    public Felhasznalo(String felhasznaloNev) {
        this.felhasznaloNev = felhasznaloNev;
        eredmenyek = new ArrayList<>();
    }
    /**
     * Hozzáad egy elemet a listához
     * @param e 
     */
    public void addEredmeny(Eredmeny e) {
        eredmenyek.add(e);
    }

}
