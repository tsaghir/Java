/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import org.foi.nwtis.tsaghir.konfiguracije.Konfiguracija;
import org.foi.nwtis.tsaghir.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.tsaghir.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.tsaghir.konfiguracije.NemaKonfiguracije;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Generalko
 */
public class DretvaHelperTest {
    Konfiguracija konf;

    public DretvaHelperTest() {
        DretvaHelper.getInstance();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws NemaKonfiguracije, NeispravnaKonfiguracija {
        konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju("NWTiS_tsaghir_zadaca_1.xml");
    }

    @After
    public void tearDown() {
    }

    /**
     * Testiranje instanciranja objekta helper klase
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        DretvaHelper rezultat = DretvaHelper.getInstance();
        assertNotNull(rezultat);
    }

    /**
     * Testiranje postavljanja konfiguracije
     */
    @Test
    public void testSetKonf() throws NemaKonfiguracije, NeispravnaKonfiguracija {
        System.out.println("setKonf");
        Konfiguracija expResult = konf;
        DretvaHelper.setKonf(konf);
        assertNotNull(DretvaHelper.getKonf());
        assertEquals(expResult, DretvaHelper.getKonf());
    }

    /**
     * Testiranje pronalazimo li korisnika u admin datoteci
     */
    @Test
    public void testProvjeriKorisnika() throws Exception {
        System.out.println("provjeriKorisnika");
        DretvaHelper.setKonf(konf);
        String korisnik = "pero";
        String lozinka = "123456";
        boolean expResult = true;
        boolean result = DretvaHelper.provjeriKorisnika(korisnik, lozinka);
        assertEquals(expResult, result);
    }

    /**
     * Testiranje dohvaćanja konfiguracije
     */
    @Test
    public void testGetKonf() {
        System.out.println("getKonf");
        Konfiguracija expResult = konf;
        DretvaHelper.setKonf(konf);
        Konfiguracija result = DretvaHelper.getKonf();
        assertEquals(expResult, result);
    }

    /**
     * Testiranje je li postavljena pauza
     */
    @Test
    public void testIsPauza() {
        System.out.println("isPauza");
        boolean expResult = false;
        boolean result = DretvaHelper.isPauza();
        assertEquals(expResult, result);
    }

    /**
     * Testiranje postavljanja pauze
     */
    @Test
    public void testSetPauza() {
        System.out.println("setPauza");
        boolean pauza = false;
        DretvaHelper.setPauza(pauza);
    }

}
