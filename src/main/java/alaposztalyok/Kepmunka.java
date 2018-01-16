/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 *  A képmanipulációkért fele
 * @author varga
 */
public class Kepmunka {
    /**
     * BufferedImage-t ad vissz egy sima Image objektumból
     * @param img a kép amit szeretnénk átalakítani
     * @return 
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    /**
     * Elforgatja  a képet a megadott kezdő, és vég pozició alapján
     * @param img amit forgatni akarunk
     * @param kezdo
     * @param veg
     * @return  a forgatott kép
     */
    public static BufferedImage toRotatedImage(BufferedImage img, Pozicio kezdo, Pozicio veg) {

        double rotate;

        int kx, ky, vx, vy;

        double dx = 0, dy = 0;

        kx = kezdo.getX();
        ky = kezdo.getY();
        vx = veg.getX();
        vy = veg.getY();

//        if (kx < vx) {
//            if (ky < vy) {
//                dx = vx - kx;
//                dy = vy - ky;
//            } else if (ky > vy) {
//                dx = vx - kx;
//                dy = ky - vy;
//            }
//        } else if (kx > vx) {
//            if (ky < vy) {
//                dx = kx - vx;
//                dy = vy - ky;
//            } else if (ky > vy) {
//                dx = kx - vx;
//                dy = ky - vy;
//            }
//        }
        if (kx < vx) {
            dx = (kx - vx);
        } else {
            dx = -((kx - vx));
        }

        if (ky < vx) {
            dy = (ky - vy);
        } else {
            dy = -((ky - vy));
        }

            double tga = dy / dx;
            rotate = 1.0 / Math.tan(tga);

            AffineTransform tx = AffineTransform.getRotateInstance(rotate, img.getWidth() / 2, img.getHeight() / 2);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

            return op.filter(img, null);
        }
    
    
    
/**
 * Elforgatja a képet a megadott értékek alapján
 * @param img a kép amit forgatnánk
 * @param rotation a forgatás szöge
 * @param rotateX a forgatás pontjának X értéke
 * @param rotateY a forgatás pontjának Y értéke
 * @return a forgatott kép
 */
    public static BufferedImage toRotatedImage(Image img, int rotation, int rotateX, int rotateY) {
        BufferedImage bimage = toBufferedImage(img);
        double rotate;
        rotate = Math.toRadians(rotation);
        AffineTransform tx = AffineTransform.getRotateInstance(rotate, rotateX, rotateY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(bimage, null);
    }
    /**
     * Átméretezi az adott képet
     * @param img az átméretezendő kép
     * @param newW az új szélesség
     * @param newH az új magasság
     * @return az átméretezett kép
     */
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    /**
     * Átforgatja függőlegesen a képet
     * @param img 
     * @return a forgatott kép
     */
    public static BufferedImage flipImageVertical(Image img) {
        BufferedImage bimage = toBufferedImage(img);
        AffineTransform tx;
        AffineTransformOp op;
        tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-bimage.getWidth(null), 0);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(bimage, null);
    }

}
