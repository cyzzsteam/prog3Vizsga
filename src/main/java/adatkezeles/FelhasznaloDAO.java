/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adatkezeles;

import alaposztalyok.Eredmeny;
import alaposztalyok.Felhasznalo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Felelős az adatbázissal való kapcsolat tartására, és a felhasználó adatai olvasására, eredményei mentésére.
 * @author varga
 */
public class FelhasznaloDAO {
    private static List<Eredmeny> aktualisEredmenyek;
    private Connection kapcsolat;

    public FelhasznaloDAO(Connection kapcsolat) {
        this.kapcsolat = kapcsolat;
    }
    /**
     * Betölti a felhasználó adatait
     * @param felh a kívánt felhasználó
     * @return a betöltött felhasználó
     */
    public Felhasznalo felhasznaloBetoltese(String felh) {
        Felhasznalo felhasznalo = new Felhasznalo(felh);
        try {
            int userID = getUserID(felh);
            Eredmeny eredmeny;
            int id, pont;
            java.sql.Date datum;
            String felhNevSQL = "Select * from Eredmeny where userID = ?";

            PreparedStatement ps = kapcsolat.prepareStatement(felhNevSQL);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pont = rs.getInt("pont");
                datum = rs.getDate("datum");
                
                eredmeny = new Eredmeny(pont, datum.getTime());
                felhasznalo.addEredmeny(eredmeny);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FelhasznaloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        aktualisEredmenyek=felhasznalo.getEredmenyek();
        return felhasznalo;
    }
/**
 * Lekéri a felhasználónévhez tartozó userID-t
 * @param felh a kért felhasználó
 * @return userid -1 ha nem található
 * @throws SQLException 
 */
    private int getUserID(String felh) throws SQLException {
        String userIdSQL = "Select id from Felhasznalo where username = ?";

        PreparedStatement ps = kapcsolat.prepareStatement(userIdSQL);
        ps.setString(1, felh);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        } else {
            return -1;
        }
    }
    /**
     * Kiolvassa az összes eredményt az adatbázisból
     * @return az eredményeket tartalmazó lista
     */
    public List<Eredmeny> osszesEredmeny() {
        List<Eredmeny> eredmenyek = new ArrayList<>();
        try {

            int id, pont;
            java.sql.Date datum;

            Statement st = kapcsolat.createStatement();
            ResultSet rs = st.executeQuery("Select pont,datum From Eredmeny order by pont desc");

            while (rs.next()) {
                pont = rs.getInt("pont");
                datum = rs.getDate("datum");
                //id=rs.getInt("id");
                eredmenyek.add(new Eredmeny(pont, datum.getTime()));
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            Logger.getLogger(FelhasznaloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return eredmenyek;
    }
    /**
     * Elmenti a felhasznélóhoz tartozó eredményeket
     * @param felhasznalo 
     */
    public void eredmenyMentes(Felhasznalo felhasznalo) {
        
        String sql = "INSERT INTO Eredmeny (pont, datum, userid) values (?, ?, ?)";
        List<Eredmeny> kulonbozet=new ArrayList<>();
        for(int i=0;i<felhasznalo.getEredmenyek().size();i++){
        if(!aktualisEredmenyek.contains(felhasznalo.getEredmenyek().get(i))){
        
            kulonbozet.add(felhasznalo.getEredmenyek().get(i));
        }
        }
        
      List<Eredmeny> eredmenyek = felhasznalo.getEredmenyek();
        PreparedStatement stmt = null;
        try {
            stmt = kapcsolat.prepareStatement(sql);
            int userid = getUserID(felhasznalo.getFelhasznaloNev());
            for (Eredmeny eredmeny : kulonbozet) {
              
                stmt.setInt(1, eredmeny.getPont());
                java.sql.Date datum = new java.sql.Date(eredmeny.getDatum());
                stmt.setDate(2, datum);
                stmt.setInt(3, userid);
         
                stmt.executeUpdate();
                
                
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(FelhasznaloDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Duplikalt eredmeny.");
    }
        kulonbozet.clear();
        aktualisEredmenyek=felhasznalo.getEredmenyek();
        
        
    }
}
    

