package org.foi.nwtis.tsaghir.pomagaci;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Singleton klasa koja služi za pomoć u određivanju regexa
 *
 * @author tsaghir
 */
public class RegexHelper {

    private static final String SERVER_SINTAKSA = "^USER ([A-Za-z0-9_!#-]+); PASSWD ([A-Za-z0-9_!#-]+); (?:(PAUSE|STOP|START|STATUS);)$";
    private static final String IOT_MASTER_SINTAKSA = "^USER ([A-Za-z0-9_!#-]+); PASSWD ([A-Za-z0-9_!#-]+); (?:(IoT_Master) (START|STOP|WORK|WAIT|LOAD|CLEAR|STATUS|LIST);)$";
    private static final String IOT_SINTAKSA = "^USER ([A-Za-z0-9_!#-]+); PASSWD ([A-Za-z0-9_!#-]+); (IoT) (\\d{1,6}) (ADD|WORK|WAIT|REMOVE|STATUS)(?:\\s\"(.+)\"\\s\"(.+)\")?;$";

    private static Pattern pattern = null;
    private static Matcher matcher = null;
    private static RegexHelper instance = null;
    private static String[] sintakse = null;

    private RegexHelper() {
        sintakse = new String[3];
        sintakse[0] = SERVER_SINTAKSA;
        sintakse[1] = IOT_MASTER_SINTAKSA;
        sintakse[2] = IOT_SINTAKSA;
    }

    public static RegexHelper getInstance() {
        if (instance == null) {
            instance = new RegexHelper();
        }
        return instance;
    }

    /**
     * metoda koja određuje korisnika i vraća matcher
     *
     * @param naredba koju je korisnik poslao
     * @return Matcher za određivanje grupa
     */
    public static synchronized Matcher odrediKorisnika(String naredba) {
        for (int i = 0; i < 3; i++) {
            pattern = Pattern.compile(sintakse[i]);
            matcher = pattern.matcher(naredba);
            boolean status = matcher.matches();
            if (status) {
                return matcher;
            }
        }
        return null;
    }
}
