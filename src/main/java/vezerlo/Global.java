/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vezerlo;

/**
 *
 * @author varga
 */
public class Global {

    public static int EGYEB_FRAME_MAGASSAG = 300;
    public static int EGYEB_FRAME_SZELESSEG = 400;
    
    public static int HOROG_NAGYSAG=45;
    public static long HORGASZ_VARAKOZASI_IDO=100;
    public static int HORGASZ_MAXROTATION=60;
    
    public static String HATTERKEP_ELERES="/kepek/background.png";
    
    public static String EGYEB_FRAME_EREDMENY_CIM = "Eredmények";
    public static String EGYEB_FRAME_BELEPES_CIM = "Belépés";
    
    
    public static String SUGO_TEXT = "Horgász játék\n Bejelentkezés után megtekinthető a felhasználó eredményei,legjobb eredmények,az Eredmények gombra kattintva \n A Játék inditása gombra kattintva elindul a játék, melyet a SZÓKÖZ lenyomásával lehet játszani. Pontokat a kifogott dolgok függvényében kapunk, vagy épp vesztünk\n Csak lefele menet foghatunk ki dolgokat. \n Az eredmények mentése nem működik megfelelően.";
    public static String SQL_TABLECREATE_FELHASZNALO_STRING = "CREATE TABLE Felhasznalo ( id int not null primary key  GENERATED ALWAYS AS IDENTITY\n" +
"        (START WITH 1, INCREMENT BY 1), \n"
            + "                            username varchar(32) not null,\n"
            + "							password varchar(64)   not null,\n"
            + "					salt varchar(16)   not null)";
    public static String SQL_TABLECREATE_EREDMENY_STRING = "CREATE TABLE Eredmeny ( id int not null primary key  GENERATED ALWAYS AS IDENTITY\n" +
"        (START WITH 1, INCREMENT BY 1),\n"
            + "						userid int not null,\n"
            + "                            datum date not null ,\n"
            + "							pont int not null)";
    static String HOROG_ELERES="/kepek/hook.png";
    static int listaNagysag=45;
    public static String HAL_ELERES="/kepek/hal_";
    static int GENERALAS_X_MAX=800;
    static int GENERALAS_X_MIN=0;
    static int GENERALAS_Y_MAX=600;
    static int GENERALAS_Y_MIN=0;
   public static String AKADALY_ELERES="/kepek/akadaly_";
    static int VIZIELEM_NAGYSAG=50;
    static int HORGASZ_DX=10;
    static int HORGASZ_DY=20;
    static String HANG_ELERES="zene/bensound-ukulele.mp3";

}
