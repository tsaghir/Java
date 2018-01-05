/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.zadaca_1;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import static org.foi.nwtis.tsaghir.zadaca_1.EvidencijaHelper.kreirajEvidencijaObjekt;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Generalko
 */
public class EvidencijaHelperTest {
    Evidencija evidencija;
    
    public EvidencijaHelperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        EvidencijaHelper.getInstance();
        evidencija = new Evidencija();
        evidencija.setBrojPrekinutihZahtjeva(3);
        evidencija.setBrojUspjesnihZahtjeva(1);
    }
    
    @After
    public void tearDown() {
    }

    
    /**
     * Testiranje serijalizacije evidencije u binarnu datoteku
     */
    @Test
    public void testSerijalizirajEvidencijuBinarnuDat() throws Exception {
        System.out.println("serijalizirajEvidencijuBinarnuDat");
        boolean expResult = true;
        boolean result = EvidencijaHelper.serijalizirajEvidencijuBinarnuDat("DZ_1_evidencija.bin");
        assertEquals(expResult, result);
    }

    /**
     * Testiranje kreiranja objekta evidencije
     */
    @Test
    public void testKreirajEvidencijaObjekt() {
        System.out.println("kreirajEvidencijaObjekt");
        Evidencija result = EvidencijaHelper.kreirajEvidencijaObjekt();
        assertNotNull(result);
    }
    
    /**
     * Testiranje dobivamo li byte polje kao rezultat
     */
    @Test
    public void testSerijalizirajEvidencijuBytePolje(){
        System.out.println("serijalizirajEvidencijuBytePolje");
        byte [] result = EvidencijaHelper.serijalizirajEvidencijuBytePolje();
        assertNotNull(result);
    }
    
    /**
     * Testiranje učitavanje datoteke evidencije ako postoji
     */
    @Test
    public void testUcitajEvidencijuIzBinarneDat(){
        System.out.println("ucitajEvidencijuIzBinarneDat");
        Evidencija reuslt = EvidencijaHelper.ucitajEvidencijuIzBinarneDat();
        assertNotNull(reuslt);
    }

   
}
