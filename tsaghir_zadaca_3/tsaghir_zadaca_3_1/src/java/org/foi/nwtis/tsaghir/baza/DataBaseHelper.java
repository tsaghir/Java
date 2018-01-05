package org.foi.nwtis.tsaghir.baza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.web.podaci.Lokacija;
import org.foi.nwtis.tsaghir.web.podaci.MeteoPodaci;
import org.foi.nwtis.tsaghir.web.podaci.MaxMin;
import org.foi.nwtis.tsaghir.web.podaci.Uredjaj;

/**
 * Singleton klasa za rad s bazom podataka
 * @author tsaghir
 */
public class DataBaseHelper {

    /**
     * VARIJABLE
     */
    private static DataBaseHelper instance = null;
    private static Connection connection;

    private static String driver;
    private static String serverDatabase;
    private static String userDatabase;
    private static String userUsername;
    private static String userPassword;

    /**
     * Konstruktor
     * @param konf 
     */
    private DataBaseHelper(Konfiguracija konf) {
        driver = konf.dajPostavku("driver.database.derby");
        serverDatabase = konf.dajPostavku("server.database");
        userDatabase = konf.dajPostavku("user.database");
        userUsername = konf.dajPostavku("user.username");
        userPassword = konf.dajPostavku("user.password");
    }

    /**
     * Metoda koja stvara instancu klase
     * @param konf
     * @return 
     */
    public static DataBaseHelper getInstance(Konfiguracija konf) {
        if (instance == null) {
            instance = new DataBaseHelper(konf);
        }
        return instance;
    }

    /**
     * Spremanje uređaja u bazu podataka
     * @param ured objekt
     * @return 
     */
    public static boolean spremiUredaj(Uredjaj ured) {
        if (!postojiUredaj(ured.getNaziv())) {
            try {
                Statement stmt = null;
                String query = String.format("INSERT INTO uredaji (id,naziv,latitude,longitude) VALUES ((SELECT id FROM uredaji ORDER BY id DESC FETCH FIRST ROW ONLY) + 1,'%s',%f,%f)",
                        ured.getNaziv(), Float.parseFloat(ured.getGeoloc().getLatitude()), Float.parseFloat(ured.getGeoloc().getLongitude()));
                stmt = connect().createStatement();
                stmt.executeUpdate(query);
                disconnect();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    /**
     * Metoda koja provjerava da li postoji već takav uređaj u bazi podataka
     * @param naziv
     * @return 
     */
    private static boolean postojiUredaj(String naziv) {
        Statement stmt = null;
        String query = "select*from uredaji where naziv='" + naziv + "'";
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
     * Metoda koja dohvaća sve uređaje
     * @return lista svih uređaja
     */
    public static List<Uredjaj> dajSveUredaje() {
        Statement stmt = null;
        List<Uredjaj> ured = new ArrayList<>();
        String query = "select*from uredaji";
        try {
            stmt = connect().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Uredjaj u = new Uredjaj();
                u.setId(rs.getInt("ID"));
                u.setNaziv(rs.getString("NAZIV"));
                u.setGeoloc(new Lokacija(String.valueOf(rs.getFloat("LATITUDE")), String.valueOf(rs.getFloat("LONGITUDE"))));
                ured.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            disconnect();
        }
        return ured;
    }

    /**
     * Metoda koja zapisuje meteo podatke u tablicu METEO
     * @param uredajId 
     * @param adresaStanice
     * @param latitude pozicija
     * @param longitude pozicija
     * @param mp
     * @return 
     */
    public static boolean zapisiMeteoPodatke(int uredajId, String adresaStanice, float latitude, float longitude, MeteoPodaci mp) {
        try {
            Statement stmt = null;
            String query = String.format("INSERT INTO meteo (id,adresastanice,latitude,longitude,vrijeme,vrijemeopis,temp,tempmin,tempmax,vlaga,tlak,vjetar) VALUES (%d,'%s',%f,%f,'%s','%s',%f,%f,%f,%f,%f,%f)",
                    uredajId, adresaStanice, latitude, longitude, System.currentTimeMillis(), mp.getWeatherValue(), mp.getTemperatureValue(),
                    mp.getTemperatureMin(), mp.getTemperatureMax(), mp.getHumidityValue(), mp.getPressureValue(), mp.getWindSpeedValue());
            stmt = connect().createStatement();
            stmt.executeUpdate(query);
            disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Metoda koja daje sve meteo podatke za uređa od datuma do datuma
     * @param id
     * @param pocetak
     * @param kraj
     * @return 
     */
    public static List<MeteoPodaci> dajSveMeteoPodatkeZaUredajOdDo(int id, long pocetak, long kraj) {
        Statement stmt = null;
        List<MeteoPodaci> meteoPodaci = new ArrayList<>();
        String query = String.format("select*from meteo where id=" + id + " and vrijeme between " + "'" + pocetak + "'" + " and " + "'" + kraj + "'");
        try {
            stmt = connect().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                MeteoPodaci mp = new MeteoPodaci();
                mp.setWeatherValue(rs.getString("vrijemeopis"));
                mp.setTemperatureValue(rs.getFloat("temp"));
                mp.setTemperatureMin(rs.getFloat("tempmin"));
                mp.setTemperatureMax(rs.getFloat("tempmax"));
                mp.setHumidityValue(rs.getFloat("vlaga"));
                mp.setPressureValue(rs.getFloat("tlak"));
                mp.setWindSpeedValue(rs.getFloat("vjetar"));
                mp.setLastUpdate(rs.getDate("preuzeto"));
                meteoPodaci.add(mp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            disconnect();
        }
        return meteoPodaci;
    }

    /**
     * Metoda koja vraća zadnje meteo podatke iz baze podataka
     * @param id
     * @return 
     */
    public static List<MeteoPodaci> dajZadnjeMeteoPodatke(int id) {
        Statement stmt = null;
        List<MeteoPodaci> meteoPodaci = new ArrayList<>();
        String query = String.format("SELECT * FROM meteo where id = %d order by vrijeme desc FETCH FIRST 100 ROW ONLY", id);
        try {
            stmt = connect().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                MeteoPodaci mp = new MeteoPodaci();
                mp.setWeatherValue(rs.getString("vrijemeopis"));
                mp.setTemperatureValue(rs.getFloat("temp"));
                mp.setTemperatureMin(rs.getFloat("tempmin"));
                mp.setTemperatureMax(rs.getFloat("tempmax"));
                mp.setHumidityValue(rs.getFloat("vlaga"));
                mp.setPressureValue(rs.getFloat("tlak"));
                mp.setWindSpeedValue(rs.getFloat("vjetar"));
                mp.setLastUpdate(rs.getDate("preuzeto"));
                meteoPodaci.add(mp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            disconnect();
        }
        return meteoPodaci;
    }

    /**
     * Metoda koja vraća min i max temperaturu iz baze podataka datuma od-do
     * @param id
     * @param pocetak
     * @param kraj
     * @return 
     */
    public static List<MaxMin> dajMaxMinTemperaturu(int id, float pocetak, float kraj) {
        Statement stmt = null;
        List<MaxMin> temp = new ArrayList<>();
        String query = String.format("select min(tempmin) as tempmin, max(tempmax) as tempmax from meteo where id=" + id + "and vrijeme between" + "'" + pocetak + "'" + "' and" + "'" + kraj + "'");
        try {
            stmt = connect().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                MaxMin t = new MaxMin();
                t.setMin(rs.getFloat("tempmin"));
                t.setMax(rs.getFloat("tempmax"));
                temp.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            disconnect();
        }
        return temp;
    }

    /**
     * Metoda koja vraća min i max vlagu za određeni uređaj datuma od-do
     * @param id
     * @param pocetak
     * @param kraj
     * @return 
     */
    public static List<MaxMin> dajMaxMinVlagu(int id, float pocetak, float kraj) {
        Statement stmt = null;
        List<MaxMin> vlag = new ArrayList<>();
        String query = String.format("select min(vlaga) as vlagmin, max(vlaga) as vlagmax from meteo where id=" + id + "and vrijeme between" + "'" + pocetak + "'" + "' and" + "'" + kraj + "'");
        try {
            stmt = connect().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                MaxMin v = new MaxMin();
                v.setMin(rs.getFloat("vlagmin"));
                v.setMax(rs.getFloat("vlagmax"));
                vlag.add(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            disconnect();
        }
        return vlag;
    }

    /**
     * Metoda koja vraća min i max tlak za određeni uređaj datuma od-do
     * @param id
     * @param pocetak
     * @param kraj
     * @return 
     */
    public static List<MaxMin> dajMaxMinTlak(int id, float pocetak, float kraj) {
        Statement stmt = null;
        List<MaxMin> tlak = new ArrayList<>();
        String query = String.format("select min(tlak) as tlakmin, max(tlak) as tlakmax from meteo where id=" + id + "and vrijeme between" + "'" + pocetak + "'" + "' and" + "'" + kraj + "'");
        try {
            stmt = connect().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                MaxMin tl = new MaxMin();
                tl.setMin(rs.getFloat("tlakmin"));
                tl.setMax(rs.getFloat("tlakmax"));
                tlak.add(tl);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            disconnect();
        }
        return tlak;
    }
    
    /**
     * Metoda koja vrši spajanje na Derby server
     *
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
     * Metoda s kojom se odspaja sa Derby servera
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
