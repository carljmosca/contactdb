/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.manager;

import com.vaadin.data.Item;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author moscac
 */
public class ContactQueryTest {
    
    public ContactQueryTest() {
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
     * Test of size method, of class ContactQuery.
     */
    @org.junit.Test
    public void testSize() {
        System.out.println("size");
        ContactQuery instance = new ContactQuery(null);
        int expResult = 0;
        int result = instance.size();
    }

    /**
     * Test of loadItems method, of class ContactQuery.
     */
    @org.junit.Test
    public void testLoadItems() {
        System.out.println("loadItems");
        int startIndex = 0;
        int count = 0;
        ContactQuery instance = new ContactQuery(null);
        List<Item> result = instance.loadItems(startIndex, count);
        assertTrue(result != null && result.size() > 0);

    }

    /**
     * Test of saveItems method, of class ContactQuery.
     */
    @org.junit.Test
    public void testSaveItems() {
        System.out.println("saveItems");
        List<Item> addedItems = null;
        List<Item> modifiedItems = null;
        List<Item> removedItems = null;
        ContactQuery instance = new ContactQuery(null);
        instance.saveItems(addedItems, modifiedItems, removedItems);
    }

    /**
     * Test of deleteAllItems method, of class ContactQuery.
     */
//    @org.junit.Test
//    public void testDeleteAllItems() {
//        System.out.println("deleteAllItems");
//        ContactQuery instance = new ContactQuery();
//        boolean expResult = false;
//        boolean result = instance.deleteAllItems();
//    }

    /**
     * Test of constructItem method, of class ContactQuery.
     */
    @org.junit.Test
    public void testConstructItem() {
        System.out.println("constructItem");
        ContactQuery instance = new ContactQuery(null);
        Item expResult = null;
        Item result = instance.constructItem();
    }
    
}
