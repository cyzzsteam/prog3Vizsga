/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import vezerlo.Vezerlo;

/**
 * Őse a ViziElem, leírja egy hal értékeit
 * @author varga
 */
public class Hal extends ViziElem {

    private BufferedImage forgatniValoKep;

    public Hal(BufferedImage kep, int x, int y, Pozicio poz, Vezerlo vezerlo) {
        super(kep, x, y, poz, vezerlo);

        forgatniValoKep = Kepmunka.resize(kep, 50, 50);
        if (super.getDx() < 0) {
            forgatniValoKep = Kepmunka.flipImageVertical(forgatniValoKep);
        }

    }
    
    @Override
    public void rajzolas(Graphics g) {

        g.drawImage(forgatniValoKep, super.getX(), super.getY(), null);
    }

}
