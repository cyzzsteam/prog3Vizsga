/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adatkezeles;

import alaposztalyok.Password;
import gnu.crypto.hash.Whirlpool;
import java.security.SecureRandom;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author varga
 */
public class LoginDAO {

    private Connection kapcsolat;

    public LoginDAO(Connection kapcsolat) {
        this.kapcsolat = kapcsolat;
    }
    /**
     * Azonosítja a felhasználót a betáplált adatok alapján
     * @param felh akit keresünk
     * @param jelsz a jelszava
     * @return a sikerességét adja vissza
     */
    public boolean azonositas(String felh, String jelsz) {
        boolean sikeres = false;
        String salt, pwhash, hash;
        try {
            if (checkUsername(felh)) {
                System.err.println("talat felh.");

                String sql = "Select * from Felhasznalo where username = ?";

                PreparedStatement ps = kapcsolat.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                ps.setString(1, felh);
                ResultSet rs = ps.executeQuery();
                rs.beforeFirst();
                rs.next();
                pwhash = rs.getString("password");
                salt = rs.getString("salt");

                hash = getHash(jelsz, salt);

                if (hash.equals(pwhash)) {
                    sikeres = true;
                    System.err.println("Sikeres login.");
                } else {
                    sikeres = false;
                    System.out.println("Sikertelen login");
                    System.err.println(hash);
                    System.err.println(pwhash);
                }

                ps.close();
                rs.close();
            } else {
                System.err.println("Nem talalt felh.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sikeres;
    }
    /**
     * Létehoz a pw szövegből egy hash-t a Whirlpool algoritmus segítségével
     * @param pw
     * @return visszatér egy Password típussal ami tárolja a hash és salt értéket
     */
    private Password createHash(String pw) {
        SecureRandom random = new SecureRandom();
        Whirlpool wp = new Whirlpool();
        byte pw_bytes[] = pw.getBytes();
        byte salt[] = new byte[16];
        random.nextBytes(salt);
        byte allbytes[] = new byte[salt.length + pw_bytes.length];
        System.arraycopy(pw_bytes, 0, allbytes, 0, pw_bytes.length);
        System.arraycopy(salt, 0, allbytes, 0, salt.length);
        wp.update(allbytes, 0, allbytes.length);
        return new Password(new String(wp.digest()), new String(salt));
    }
    
    /**
     * Létrehoz egy hash-t a megadott jelszó, és salt alapján
     * @param pw
     * @param salt
     * @return visszatér egy Stringel, ami a hash-t tartalmazza
     */
    private String getHash(String pw, String salt) {
        Whirlpool wp = new Whirlpool();
        byte pw_bytes[] = pw.getBytes();
        byte salt_bytes[] = salt.getBytes();
        byte allbytes[] = new byte[salt_bytes.length + pw_bytes.length];
        System.arraycopy(pw_bytes, 0, allbytes, 0, pw_bytes.length);
        System.arraycopy(salt_bytes, 0, allbytes, 0, salt_bytes.length);
        wp.update(allbytes, 0, allbytes.length);
        return new String(wp.digest());
    }
    /**
     * Ellenőrzi hogy a felhasználónév létezik e
     * @param username akit keresünl
     * @return ha igaz akkor van ilyen felhasználó
     * @throws SQLException 
     */
    public boolean checkUsername(String username) throws SQLException {
        String sql = "Select * from Felhasznalo where username = ?";
        boolean talalt=false;
        try(PreparedStatement ps = kapcsolat.prepareStatement(sql)){
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        talalt=rs.next();
        rs.close();
        }
        return talalt;
    }
    /**
     * Létrehozza az adatbázisban az értékeket a betáplált adatokból, ha még nem létezik
     * @param felh 
     * @param jelsz
     * @return a művelet sikeressége
     */
    public boolean regisztracio(String felh, String jelsz) {
        boolean sikeres = false;
        String sql = "INSERT INTO Felhasznalo ( username, password, salt) values (?, ?, ?)";
        Password pw = createHash(jelsz);
        String pwhash = pw.getPwhash(), salt = pw.getSalt();
        try {
            if (!checkUsername(felh)) {

                try (PreparedStatement stmt = kapcsolat.prepareStatement(sql)) {
                    stmt.setString(1, felh);
                    stmt.setString(2, pwhash);
                    stmt.setString(3, salt);
                    stmt.executeUpdate();
                    
                    sikeres = true;
                    System.out.println("sikeres regisztracio");
                }

            } else {
                System.err.println("Foglalt felh-név.");
                //ESETLEG TODO: Errorcode, jelzés a panelnek hogy mit írjon ki... bonyolult.
                sikeres = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return sikeres;
    }
}
