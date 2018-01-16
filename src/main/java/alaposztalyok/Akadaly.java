/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.awt.image.BufferedImage;
import vezerlo.Vezerlo;

/**
 *Akadály osztály, őse a ViziElem.
 * @author varga
 */
public class Akadaly extends ViziElem{
    
    public Akadaly(BufferedImage kep, int x, int y, Pozicio poz, Vezerlo vezerlo) {
        super(kep, x, y, poz, vezerlo);
    }
    
}
