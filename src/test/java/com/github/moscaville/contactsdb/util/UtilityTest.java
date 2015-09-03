/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author moscac
 */
public class UtilityTest {
    
    public UtilityTest() {
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
     * Test of toHumanName method, of class Utility.
     */
    @Test
    public void testToHumanName() {
        System.out.println("toHumanName");
        String camelName = "firstAndLastName";
        String expResult = "First And Last Name";
        String result = Utility.toHumanName(camelName);
        assertEquals(expResult, result);
    }
    
}
