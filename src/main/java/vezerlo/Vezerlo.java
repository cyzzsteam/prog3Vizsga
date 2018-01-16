/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vezerlo;

import adatkezeles.FelhasznaloDAO;
import adatkezeles.LoginDAO;
import alaposztalyok.Akadaly;
import alaposztalyok.Eredmeny;
import alaposztalyok.Felhasznalo;
import alaposztalyok.Hal;
import alaposztalyok.Horgasz;
import alaposztalyok.Kepmunka;
import alaposztalyok.Pozicio;
import alaposztalyok.ViziElem;
import alaposztalyok.Zenelejatszo;
import feluletek.BelepesPanel;
import feluletek.EredmenyPanel;
import feluletek.MenuPanel;
import feluletek.RajzPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 *
 * @author varga
 */
/**
 * Iránítja a program futását
 *
 * @author varga
 */
public class Vezerlo implements Runnable {

    private EgyebFrame egyebFrame;
    private MenuPanel menuPanel;
    private RajzPanel rajzPanel;
    private BelepesPanel belepesPanel;
    private EredmenyPanel eredmenyPanel;
    private Felhasznalo felhasznalo;
    private Connection con;
    private Horgasz horgasz;

    private int pont;

    private LoginDAO loginDAO;
    private FelhasznaloDAO felhasznaloDAO;

    private List<ViziElem> viziElemek = new CopyOnWriteArrayList<ViziElem>();

    public Vezerlo(MenuPanel menuPanel, RajzPanel rajzPanel) {
        this.menuPanel = menuPanel;
        this.rajzPanel = rajzPanel;
    }

    /**
     * Beállítja az inditáshoz szükséges változókat.
     */
    public void inditas() {
        try {
           
            
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(Global.HANG_ELERES).getFile());
            Zenelejatszo zene=new Zenelejatszo(new FileInputStream(file));
            new Thread(zene).start();
            
            
            
            statikusBeallitas();

            menuPanel.setVezerlo(this);
            rajzPanel.setVezerlo(this);
            menuPanel.gombAllapot(false);

            con = kapcsolatEpit();
            loginDAO = new LoginDAO(con);
            felhasznaloDAO = new FelhasznaloDAO(con);

            rajzPanel.setHatterKep(new ImageIcon(getClass().getResource(Global.HATTERKEP_ELERES)).getImage());

        } catch (Exception ex) {
            Logger.getLogger(Vezerlo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Az eredmény gomb megnyomására előugrik a frame, rajta a panellel.
     */
    public void eredmenyClick() {
        egyebFrame = new EgyebFrame();
        eredmenyPanel = new EredmenyPanel();
        eredmenyPanel.setBounds(0, 0, Global.EGYEB_FRAME_SZELESSEG, Global.EGYEB_FRAME_MAGASSAG);
        egyebFrame.setLocationRelativeTo(null);
        egyebFrame.getContentPane().add(eredmenyPanel);
        egyebFrame.setVisible(true);
        egyebFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        egyebFrame.setTitle(Global.EGYEB_FRAME_EREDMENY_CIM);
        eredmenyPanel.setVezerlo(this);
    }

    /**
     * A belépés gomb megnyomására előugrik a frame, rajta a panellel.
     */
    public void belepesClick() {
        egyebFrame = new EgyebFrame();
        belepesPanel = new BelepesPanel();
        belepesPanel.setBounds(0, 0, Global.EGYEB_FRAME_SZELESSEG, Global.EGYEB_FRAME_MAGASSAG);
        egyebFrame.setLocationRelativeTo(null);
        egyebFrame.getContentPane().add(belepesPanel);
        egyebFrame.setVisible(true);
        egyebFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        egyebFrame.setTitle(Global.EGYEB_FRAME_BELEPES_CIM);
        belepesPanel.setVezerlo(this);

    }

    /**
     * A start gomb megnyomására elindul a játék
     */
    public void startClick() {

        pont = 0;
        menuPanel.setPont(pont);
        viziElemek.clear();

        Image horgaszKep = new ImageIcon(getClass().getResource(Global.HOROG_ELERES)).getImage();
        BufferedImage bi = Kepmunka.toBufferedImage(horgaszKep);
        bi = Kepmunka.resize(bi, Global.HOROG_NAGYSAG, Global.HOROG_NAGYSAG);

        horgasz = new Horgasz(bi, rajzPanel.getWidth() / 2 - Global.HOROG_NAGYSAG / 2, 0);
        horgasz.setVezerlo(this);
        horgasz.inditas();
        rajzPanel.grabFocus();
        horgasz.setDx(Global.HORGASZ_DX);
        horgasz.setDy(Global.HORGASZ_DY);

        Thread t = new Thread(this);
        t.start();

    }

    /**
     * Előhozza a súgót
     */
    public void sugoClick() {
        JOptionPane.showMessageDialog(null, Global.SUGO_TEXT);

    }

    /**
     * Visszatér true értékkel ha sikerült a belépés a betáplált adatok alapján
     *
     * @param felh
     * @param jelsz
     * @return a belépés sikerességét adja vissza
     */
    public boolean sikeresBelepes(String felh, String jelsz) {
        if (loginDAO.azonositas(felh, jelsz)) {
            felhasznalo = felhasznaloDAO.felhasznaloBetoltese(felh);
            menuPanel.gombAllapot(true);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Visszatér igazzal, ha sikerült a regisztráció
     *
     * @param felh
     * @param jelsz
     * @return sikeresség
     */
    public boolean regisztracio(String felh, String jelsz) {
        return loginDAO.regisztracio(felh, jelsz);
    }

    /**
     * Eltűnteti a segéd frame-t..
     */
    public void egyebBezar() {
        egyebFrame.dispose();
    }

    /**
     * Betölti a ranglistát
     */
    public void ranglistaBetoltes() {
        eredmenyPanel.jatekosNevBeallit(felhasznalo.getFelhasznaloNev());
        eredmenyPanel.jatekosEredmenyKiiras(felhasznalo.getEredmenyek());
        eredmenyPanel.osszesEredmenyKiiras(felhasznaloDAO.osszesEredmeny());
    }

    /**
     * Létrehozza az adatbázis kapcsolatot, ha nincs tábla, akkor készít.
     *
     * @return kapcsolat
     * @throws Exception
     */
    private Connection kapcsolatEpit() throws Exception {
        Connection kapcsolat = null;

        String url = "jdbc:derby:HalaszDB;create=true;";
        String className = "org.apache.derby.jdbc.EmbeddedDriver";
        Class.forName(className);
        kapcsolat = DriverManager.getConnection(url);

        String tablaEllenorzes = "select * from SYS.SYSTABLES where tablename= 'FELHASZNALO'";

        try (Statement utasitas = kapcsolat.createStatement();
                ResultSet rs = utasitas.executeQuery(tablaEllenorzes)) {
            if (!rs.next()) {
                System.err.println("Nincs tabla, Tabla keszites.");
                String tablaKeszites_f = Global.SQL_TABLECREATE_FELHASZNALO_STRING, tablaKeszites_e = Global.SQL_TABLECREATE_EREDMENY_STRING;
                utasitas.execute(tablaKeszites_f);
                utasitas.execute(tablaKeszites_e);
            }

        }
        if (kapcsolat != null) {
            System.err.println("Sikeres adatbazis kapcsolat!");
        }
        return kapcsolat;
    }

    /**
     * Kirajzolja a horgászt, és a vizi elemeket.
     *
     * @param g
     */
    public void rajzolas(Graphics g) {
        if (horgasz != null) {
            horgasz.rajzolas(g);
            for (ViziElem viziElem : viziElemek) {
                viziElem.rajzolas(g);
            }
            rajzPanel.repaint();
        }
    }

    /**
     * Megkéri a rajz panelt a frissítésre
     */
    public void frissit() {
        rajzPanel.repaint();
    }

    /**
     * 30 másodpercen át működik, generálja a halakat és akadályokat, frissíti
     * másodpercenként pontokat jelző lbl-t
     */
    @Override
    public void run() {
        



        int lefutasok = 30;
        while (lefutasok >= 0) {
            menuPanel.setIdo(lefutasok);
            if (viziElemek.size() < Global.listaNagysag) {
                int x = ThreadLocalRandom.current().nextInt(Global.GENERALAS_X_MIN, Global.GENERALAS_X_MAX + 1);
                int micsoda = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                int y = ThreadLocalRandom.current().nextInt(Global.GENERALAS_Y_MIN, Global.GENERALAS_Y_MAX + 1);
                int temp_x = ThreadLocalRandom.current().nextInt(Global.GENERALAS_X_MIN, Global.GENERALAS_X_MAX + 1);
                int temp_y = ThreadLocalRandom.current().nextInt(Global.GENERALAS_Y_MIN, Global.GENERALAS_Y_MAX + 1);
                Pozicio pozicio = new Pozicio(temp_x, temp_y);
                if (micsoda != 3) {
                    int randomNum = ThreadLocalRandom.current().nextInt(1, 8 + 1);
                    BufferedImage kep = Kepmunka.toBufferedImage(new ImageIcon(getClass().getResource(Global.HAL_ELERES + randomNum + ".png")).getImage());
                    Hal hal = new Hal(kep, x, y, pozicio, this);
                    new Thread(hal).start();
                    viziElemek.add(hal);
                } else {
                    int randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                    BufferedImage kep = Kepmunka.toBufferedImage(new ImageIcon(getClass().getResource(Global.AKADALY_ELERES + randomNum + ".png")).getImage());
                    Akadaly akadaly = new Akadaly(kep, x, y, pozicio, this);
                    new Thread(akadaly).start();
                    viziElemek.add(akadaly);
                }
            }
            varakozik();
            lefutasok--;
            menuPanel.lblFrissit();
        }
        Eredmeny eredmeny = new Eredmeny(pont, Calendar.getInstance().getTimeInMillis());
        felhasznalo.addEredmeny(eredmeny);
        felhasznaloDAO.eredmenyMentes(felhasznalo);
        horgasz.setAktiv(false);
        viziElemek.clear();

    }

    /**
     * Továbbküldi a gomblenyomásról szóló értékeket a Horgasz-nak -1 balra,0
     * lefele,1 jobbra
     *
     * @param i a gomblenyomás típusa
     */
    public void gomblenyom(int i) {
        switch (i) {
            case -1:
                horgasz.iranyba(-1);
                break;
            case 0:
                horgasz.iranyba(0);
                break;
            case 1:
                horgasz.iranyba(1);
                break;

        }

    }

    /**
     * Az átadott értékek alapján megvizsgálja hogy találat érte e valamelik
     * objektumot a viziElemek listából. Növeli vagy épp csökkenti a találat
     * típusátol függően a pontokat
     *
     * @param x
     * @param y
     * @return igaz/hamis a találattól függően
     */
    public boolean pozicioEgyezes(int x, int y) {
        boolean talalat = false;
        for (ViziElem viziElem : viziElemek) {
            if (x >= viziElem.getX() && x <= viziElem.getX() + Global.VIZIELEM_NAGYSAG && y >= viziElem.getY() && y <= viziElem.getY() + Global.VIZIELEM_NAGYSAG) {
                if (viziElem instanceof Hal) {
                    pont++;
                } else if (viziElem instanceof Akadaly) {
                    pont--;
                }
                talalat = true;
                torol(viziElem);
            }
        }
        menuPanel.setPont(pont);
        return talalat;
    }

    public Horgasz getHorgasz() {
        return horgasz;
    }

    public void setHorgasz(Horgasz horgasz) {
        this.horgasz = horgasz;
    }

    public int getPont() {
        return pont;
    }

    public void setPont(int pont) {
        this.pont = pont;
    }

    public List<ViziElem> getViziElemek() {
        return viziElemek;
    }

    public void setViziElemek(List<ViziElem> viziElemek) {
        this.viziElemek = viziElemek;
    }

    /**
     * Altatja a szálat
     */
    private void varakozik() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Vezerlo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Törli az átadott elemet a listából.
     *
     * @param aThis
     */
    public void torol(ViziElem aThis) {
        viziElemek.remove(aThis);
    }

    /**
     * Beállítja a statikus változókat
     */
    private void statikusBeallitas() {
        Horgasz.setVARAKOZASI_IDO(Global.HORGASZ_VARAKOZASI_IDO);
        Horgasz.setJatekterMagassag(rajzPanel.getHeight());
        Horgasz.setJatekterSzelesseg(rajzPanel.getWidth());
        ViziElem.setKepNagysag(Global.VIZIELEM_NAGYSAG);
        ViziElem.setJatekterMagassag(600);
        ViziElem.setJatekterSzelesseg(800);
        ViziElem.setVarakozasiIdo(100);

    }

}
