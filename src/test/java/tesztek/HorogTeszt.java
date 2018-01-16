/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesztek;

import alaposztalyok.Akadaly;
import alaposztalyok.Hal;
import alaposztalyok.Kepmunka;
import alaposztalyok.Pozicio;
import alaposztalyok.ViziElem;
import feluletek.MenuPanel;
import feluletek.RajzPanel;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.ImageIcon;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vezerlo.Global;
import vezerlo.Vezerlo;

/**
 *
 * @author varga
 */
public class HorogTeszt {
    
    public HorogTeszt() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void horogElkapasTeszt(){
        BufferedImage biHal = Kepmunka.toBufferedImage(new ImageIcon(getClass().getResource(Global.HAL_ELERES+"1.png")).getImage());
        BufferedImage biAkadaly= Kepmunka.toBufferedImage(new ImageIcon(getClass().getResource(Global.AKADALY_ELERES+"1.png")).getImage());
        
        Vezerlo vezerlo=new Vezerlo(new MenuPanel(),new RajzPanel());
        vezerlo.inditas();
        Hal hal=new Hal(biHal, 0, 0, new Pozicio(0, 0), vezerlo);
        Akadaly akadaly=new Akadaly(biAkadaly, 10, 10,new Pozicio(10, 10), vezerlo);
        List<ViziElem> elemek=new CopyOnWriteArrayList<>();
        
        elemek.add(hal);
        elemek.add(akadaly);
        vezerlo.setViziElemek(elemek);
        
        assertNotNull(vezerlo);
        
        
        //Halat talál, és növeli e a pontokat
        assertTrue(vezerlo.pozicioEgyezes(0, 0));
        assertEquals(vezerlo.getPont(),1);
        //Akadályt talál, és csökkenti a pontokat
        assertTrue(vezerlo.pozicioEgyezes(10, 10));
        assertEquals(vezerlo.getPont(), 0);
        //Találat után kitörölte e az objektumokat.
        assertFalse(vezerlo.pozicioEgyezes(0, 0));
        assertFalse(vezerlo.pozicioEgyezes(10, 10));
        
        
        
        
    }

}
