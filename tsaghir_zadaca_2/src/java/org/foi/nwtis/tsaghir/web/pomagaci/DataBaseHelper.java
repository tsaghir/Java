package org.foi.nwtis.tsaghir.web.pomagaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.web.slusaci.SlusacAplikacije;

/**
 * Singleton klasa za poslovnu logiku s bazom podataka
 * @author tsaghir
 */
public class DataBaseHelper {

    private static DataBaseHelper instance = null;
    private static Connection connection;
    private static Konfiguracija konf = null;
    
    private static String driver;
    private static String serverDatabase;
    private static String userDatabase;
    private static String userUsername;
    private static String userPassword;

    private DataBaseHelper() {
        konf = SlusacAplikacije.getKonfig();
        driver = konf.dajPostavku("driver.database.mysql");
        serverDatabase = konf.dajPostavku("server.database");
        userDatabase = konf.dajPostavku("user.database");
        userUsername = konf.dajPostavku("user.username");
        userPassword = konf.dajPostavku("user.password");
    }

    public static DataBaseHelper getInstance() {
        if (instance == null) {
            instance = new DataBaseHelper();
        }
        return instance;
    }
    /**
     * Dodavanje uređaja u tablicu UREĐAJI pomoću SQL upita
     * @param id uređaja 
     * @param naziv uređaja
     * @param latitude pozicija
     * @param longitude pozicija
     * @throws SQLException 
     */
    public static void dodajUredaj(int id, String naziv, float latitude, float longitude) throws SQLException {
        if (!postojiIot(id)) {
            Statement stmt = null;
            String query = String.format("INSERT INTO uredaji (id,naziv,latitude,longitude) VALUES (%d,'%s',%f,%f);", id, naziv, latitude, longitude);
            stmt = connect().createStatement();
            stmt.executeUpdate(query);
            disconnect();
        }

    }

    /**
     * Pomoću SQL upita se provjerava postoji li već takav uređaj
     * @param id
     * @return 
     */
    private static boolean postojiIot(int id) {
        Statement stmt = null;
        String query = "select*from uredaji where id=" + id;
        try {
            stmt = connect().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            disconnect();
        }
    }

    /**
     * Metoda pomoću koje dodavamo temperaturu u tablicu TEMPERATURE
     * @param id uređaja
     * @param temp u C* supnjeva
     * @param vrijeme_mjerenja kada je mjereno
     * @param vrijeme_kreiranja kada je kreiran zapis
     * @throws SQLException 
     */
    public static void dodajTemperature(int id, float temp, String vrijeme_mjerenja, String vrijeme_kreiranja) throws SQLException {
        if (postojiIot(id)) {
            Statement stmt = null;
            String query = String.format("INSERT INTO temparature (id,temp,vrijeme_mjerenja,vrijeme_kreiranja) VALUES (%d,%f,'%s','%s');", id, temp, vrijeme_mjerenja, vrijeme_kreiranja);
            stmt = connect().createStatement();
            stmt.executeUpdate(query);
            disconnect();
        }
    }

    /**
     * Metoda pomoću koje odajemo događaj u tablicu DOGADJAJI
     * @param id uređaja
     * @param vrsta događaja
     * @param vrijeme_izvrsavanja događaja
     * @param vrijeme_kreiranja poruke
     * @throws SQLException 
     */
    public static void dodajDogadaje(int id, int vrsta, String vrijeme_izvrsavanja, String vrijeme_kreiranja) throws SQLException {
        if (postojiIot(id)) {
            Statement stmt = null;
            String query = String.format("INSERT INTO dogadaji (id,vrsta,vrijeme_izvrsavanja,vrijeme_kreiranja) VALUES (%d,%d,'%s','%s');", id, vrsta, vrijeme_izvrsavanja, vrijeme_kreiranja);
            stmt = connect().createStatement();
            stmt.executeUpdate(query);
            disconnect();
        }
    }
    
    /**
     * Metoda koja vrši spajanje na MySQL server
     * @return 
     */
    private static Connection connect() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(serverDatabase + userDatabase,
                        userUsername, userPassword);
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }

    /**
     * Metoda s kojom se odspaja sa MySQL servera
     */
    private static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
