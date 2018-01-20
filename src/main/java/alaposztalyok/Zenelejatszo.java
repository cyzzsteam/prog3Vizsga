/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


/**
 *
 * @author varga
 */
public class Zenelejatszo implements Runnable {
    InputStream fis;
    BufferedInputStream bis;
    private String eleres;
    private Player player;
    
    public Zenelejatszo(String eleres) {
        this.eleres=eleres;
         

    }

    public Zenelejatszo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void inditas() throws JavaLayerException{
    fis = getClass().getClassLoader().getResourceAsStream(eleres);
    bis = new BufferedInputStream(fis);
    player = new Player(bis);
        
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        try {
            player.play();
        } catch (JavaLayerException ex) {
            Logger.getLogger(Zenelejatszo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
