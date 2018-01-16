/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


/**
 *
 * @author varga
 */
public class Zenelejatszo implements Runnable {
    private   FileInputStream fis;

    public Zenelejatszo(FileInputStream fis) {
        this.fis = fis;
    }
    
    @Override
    public void run() {
        try {
            Player playMP3 = new Player(fis);
            playMP3.play();
        } catch (JavaLayerException ex) {
            Logger.getLogger(Zenelejatszo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
