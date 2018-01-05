/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.servisi.rest.serveri;

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
public class UredajiRESTsResourceTest {
    
    public UredajiRESTsResourceTest() {
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

   


    /**
     * Test of azurirajUredaj method, of class UredajiRESTsResource.
     */
    @Test
    public void testAzurirajUredaj() {
        System.out.println("azurirajUredaj");
        String sadrzaj = "{\"vrijemePromjene\":\"2017-05-25 20:24:18.0\",\"latitude\":\"46.3077\",\"naziv\":\"FOI 1 - knjižnica\",\"adresa\":\"Pavlinska ul. 2, 42000, Varaždin, Croatia\",\"vrijemeKreiranja\":\"2017-05-25 20:24:18.0\",\"longitude\":\"16.3381\",\"status\":5}";
        UredajiRESTsResource instance = new UredajiRESTsResource();
        String expResult = "";
        String result = instance.azurirajUredaj(sadrzaj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

   


    

   

   
    
}
