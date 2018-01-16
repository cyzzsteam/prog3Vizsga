/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

/**
 *Tárolja a jelszó hash értékét, és a salt-ot hozzá
 * @author varga
 */
public class Password {
    private String pwhash;
    private String salt;

    public Password(String pwhash, String salt) {
        this.pwhash = pwhash;
        this.salt = salt;
    }

    public String getPwhash() {
        return pwhash;
    }

    public String getSalt() {
        return salt;
    }
    
}
