/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import vezerlo.Vezerlo;

/**
 *Egy horgászt ír le
 * @author varga
 */
public class Horgasz implements Runnable {

    private BufferedImage horog;
    private int x;
    private int y;
    private boolean aktiv;
    private static long VARAKOZASI_IDO;
    private Vezerlo vezerlo;
    private int dx;
    private int dy;
    private static int jatekterSzelesseg;
    private static int jatekterMagassag;
    private boolean lefele=false;

    public static int getJatekterSzelesseg() {
        return jatekterSzelesseg;
    }

    public static void setJatekterSzelesseg(int jatekterSzelesseg) {
        Horgasz.jatekterSzelesseg = jatekterSzelesseg;
    }

    public static int getJatekterMagassag() {
        return jatekterMagassag;
    }

    public static void setJatekterMagassag(int jatekterMagassag) {
        Horgasz.jatekterMagassag = jatekterMagassag;
    }

    public static long getVARAKOZASI_IDO() {
        return VARAKOZASI_IDO;
    }

    public static void setVARAKOZASI_IDO(long VARAKOZASI_IDO) {
        Horgasz.VARAKOZASI_IDO = VARAKOZASI_IDO;
    }

    public Vezerlo getVezerlo() {
        return vezerlo;
    }

    public void setVezerlo(Vezerlo vezerlo) {
        this.vezerlo = vezerlo;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public Horgasz(BufferedImage horog, int x, int y) {
        this.horog = horog;
        this.x = x;
        this.y = y;
    }

    public Image getHorog() {
        return horog;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setHorog(BufferedImage horog) {
        this.horog = horog;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    /**
     * A szál futását írja le. Mozog, várakozik,frissít.
     */
    @Override
    public void run() {
        while (aktiv) {
            mozog();
            varakozik();
            frissit();
        }
    }
    /**
     * Kirajzolja a horog képét, és a 'damilt' hozzá
     * @param g 
     */
    public void rajzolas(Graphics g) {
        g.drawImage(horog, x, y, null);
        if(y>0){
            g.setColor(Color.DARK_GRAY);
            g.drawLine(horog.getWidth()/2+x, 0, horog.getWidth()/2+x,y);}
    }
    /**
     * A horog mozgásáért felelős
     */
    private void mozog() {
        if(y<0)y=0;        
        if(lefele){
        y+=dy;
            if(y>=jatekterMagassag-horog.getHeight()||vezerlo.pozicioEgyezes(x,y)){
                lefele=false;
            }
        }else if(!lefele&&y>0){
        y=y-2*dy;
        }
    }
/**
 * Altatja a szálat
 */
    private void varakozik() {
        try {
            Thread.sleep(VARAKOZASI_IDO);
        } catch (InterruptedException ex) {
            Logger.getLogger(Horgasz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Szól a vezérlőnek hogy frissítse a rajz panelt
     */
    private void frissit() {
        vezerlo.frissit();
    }
    /**
     * Elindítja a szálat
     */
    public void inditas() {
        aktiv = true;
        new Thread(this).start();
    }
    /**
     * A kapott értékekből eldönti merre kell mozognia a horognak
     * -1 balra, 0 lefele, 1 jobbra
     * @param i 
     */
    public void iranyba(int i) {
        switch (i) {
            case -1:
                
                if (x > 0&&y==0) {
                    x-=dx;
                }
                break;
            case 0:
                
                if(y==0){
                    lefele=true;
                }
                break;
            case 1:
              
                 if (x < jatekterSzelesseg-horog.getWidth()&&y==0) {
                    x+=dx;
                }
                break;
        }
    }


}
