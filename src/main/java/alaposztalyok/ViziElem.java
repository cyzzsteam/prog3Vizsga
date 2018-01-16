/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import vezerlo.Vezerlo;

/**
 *  Vizi elem, a hal és akadály őse
 * @author varga
 */
public class ViziElem implements Runnable{
    private BufferedImage kep;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private Pozicio cel;
    private Vezerlo vezerlo;
    private boolean aktiv=true;
    private static int jatekterMagassag;
    private static int jatekterSzelesseg;

    public static int getJatekterMagassag() {
        return jatekterMagassag;
    }

    public static void setJatekterMagassag(int jatekterMagassag) {
        ViziElem.jatekterMagassag = jatekterMagassag;
    }

    public static int getJatekterSzelesseg() {
        return jatekterSzelesseg;
    }

    public static void setJatekterSzelesseg(int jatekterSzelesseg) {
        ViziElem.jatekterSzelesseg = jatekterSzelesseg;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }
    
    
    private static int kepNagysag;

    public static int getKepNagysag() {
        return kepNagysag;
    }

    public static void setKepNagysag(int kepNagysag) {
        ViziElem.kepNagysag = kepNagysag;
    }
    
    private static long varakozasiIdo;
    
    /**
     * a szál futását írja le.
     * amíg aktív addig mozog, frissít, és várakozik
     */
    @Override
    public void run() {
        while(aktiv){
        mozog();
        varakozik();
        frissit();
        }
    }

    public BufferedImage getKep() {
        return kep;
    }

    public void setKep(BufferedImage kep) {
        this.kep = kep;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
    /**
     * Létrehozza a vizi elemet és irányba állítja azt
     * @param kep a képe
     * @param x az x poziciója
     * @param y az y poziciója 
     * @param poz a cél poziciója
     * @param vezerlo  a vezérlő
     */
    public ViziElem(BufferedImage kep, int x, int y, Pozicio poz, Vezerlo vezerlo) {
        this.kep = Kepmunka.resize(kep, kepNagysag, kepNagysag);
        this.x = x;
        this.y = y;
        cel=poz;
        this.vezerlo = vezerlo;
        iranybaAllit();
        
    }

    public Vezerlo getVezerlo() {
        return vezerlo;
    }

    public void setVezerlo(Vezerlo vezerlo) {
        this.vezerlo = vezerlo;
    }

    public static long getVarakozasiIdo() {
        return varakozasiIdo;
    }

    public static void setVarakozasiIdo(long varakozasiIdo) {
        ViziElem.varakozasiIdo = varakozasiIdo;
    }
    
    public void rajzolas(Graphics g){
        g.drawImage(kep, x, y, null);
    }
    /**
     * Az x, y, értékét dx dy értékkel növeli, ha kimegy a képről törli magát
     */
    private void mozog() {
        x+=dx;
        y+=dy;
        if(x<0||x>jatekterSzelesseg||y<0||y>jatekterMagassag){
            vezerlo.torol(this);
        aktiv=false;
        }
    }
    /**
     * Altatja a szálat
     */
    private void varakozik() {
        try {
            Thread.sleep(varakozasiIdo);
        } catch (InterruptedException ex) {
            Logger.getLogger(ViziElem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/**
 * Frissíti a rajz panelt
 */
    private void frissit() {
        vezerlo.frissit();
    }
/**
 * Irányba állítja , azaz megadja a dx,dy értékét a cél pozició ismeretében
 */
    private void iranybaAllit() {
        int temp_x=cel.getX(),temp_y=cel.getY();
        int sebesseg=100;
        if(x<temp_x){
        dx=(temp_x-x)/sebesseg;
        }else{
        dx=-((x-temp_x)/sebesseg);
        }
        
        if(y<temp_y){
        dy=(temp_y-y)/sebesseg;
        }else{
        dy=-((y-temp_y)/sebesseg);
        
        }
    }
    
    
}
