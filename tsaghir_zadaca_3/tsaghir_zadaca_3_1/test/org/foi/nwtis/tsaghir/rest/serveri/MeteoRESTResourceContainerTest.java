/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.tsaghir.rest.serveri;

import javax.ws.rs.core.Response;
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
public class MeteoRESTResourceContainerTest {
    
    public MeteoRESTResourceContainerTest() {
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
     * Test of getJson method, of class MeteoRESTResourceContainer.
     */
    @Test
    public void testGetJson() {
        System.out.println("getJson");
        MeteoRESTResourceContainer instance = new MeteoRESTResourceContainer();
        String expResult = "";
        String result = instance.getJson();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of postJson method, of class MeteoRESTResourceContainer.
     */
    @Test
    public void testPostJson() {
        System.out.println("postJson");
        String content = "";
        MeteoRESTResourceContainer instance = new MeteoRESTResourceContainer();
        Response expResult = null;
        Response result = instance.postJson(content);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMeteoRESTResource method, of class MeteoRESTResourceContainer.
     */
    @Test
    public void testGetMeteoRESTResource() {
        System.out.println("getMeteoRESTResource");
        String id = "";
        MeteoRESTResourceContainer instance = new MeteoRESTResourceContainer();
        MeteoRESTResource expResult = null;
        MeteoRESTResource result = instance.getMeteoRESTResource(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
