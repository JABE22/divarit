/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package divarit;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jarno Matarmaa ( University of Tampere )
 */
public class QueryEngineTest {
    
    private QueryEngine instance;
    
    @Before
    public void setUp() {
        this.instance = new QueryEngine(new DatabaseConnection());
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of adminCopyQuery method, of class QueryEngine.
     */
    @Test
    public void testAdminCopyQuery() {
        System.out.println("adminCopyQuery");
        String entry = "jari";
        String divari_name = "D2";
        int expResult = 1;
        ArrayList<String> result = instance.adminCopyQuery(entry, divari_name);
        // Tarkistetaan tulosten oikea maara
        assertEquals(expResult, result.size());
        
    }
    
    /**
     * Test of adminCopyQuery method, of class QueryEngine.
     */
    @Test
    public void testAdminD1CopyQuery() {
        System.out.println("adminCopyQuery");
        String entry = "jari";
        String divari_name = "D1";
        int expResult = 1;
        ArrayList<String> result = instance.adminCopyQuery(entry, divari_name);
        // Tarkistetaan tulosten oikea maara
        assertEquals(expResult, result.size());
        
    }

    /**
     * Test of customerBookQuery method, of class QueryEngine.
     */
    @Test
    public void testCustomerBookQuery() {
        System.out.println("customerBookQuery");
        String entry = "jari";
        int expResult = 6;
        ArrayList<String> result = instance.customerBookQuery(entry);
        // Tarkistetaan tulosten oikea maara
        assertEquals(expResult, result.size());
    }

    /**
     * Test of adminBookQuery method, of class QueryEngine.
     */
    @Test
    public void testAdminBookQuery() {
        System.out.println("adminBookQuery");
        String entry = "jari";
        String divari_name = "D2";
        int expResult = 6;
        ArrayList<String> result = instance.adminBookQuery(entry, divari_name);
        // Tarkistetaan tulosten oikea maara
        assertEquals(expResult, result.size());
        
    }

    /**
     * Test of userDetails method, of class QueryEngine.
     */
    @Test
    public void testUserDetails() {
        System.out.println("userDetails");
        
    }

    /**
     * Test of addUser method, of class QueryEngine.
     */
    @Test
    public void testAddUser() {
        System.out.println("addUser");
        
    }

    /**
     * Test of insertCopy method, of class QueryEngine.
     */
    @Test
    public void testInsertCopy() {
        System.out.println("insertCopy");
        
    }

    /**
     * Test of insertBook method, of class QueryEngine.
     */
    @Test
    public void testInsertBook() {
        System.out.println("insertBook");
        
    }

    /**
     * Test of insertAuthor method, of class QueryEngine.
     */
    @Test
    public void testInsertAuthor() {
        System.out.println("insertAuthor");
    }

    /**
     * Test of insertAuthortToISBN method, of class QueryEngine.
     */
    @Test
    public void testInsertAuthortToISBN() {
        System.out.println("insertAuthortToISBN");
        
    }

    /**
     * Test of getAuthorID method, of class QueryEngine.
     */
    @Test
    public void testGetAuthorID() {
        System.out.println("getAuthorID");
        
    }

    /**
     * Test of getPurchaseReport method, of class QueryEngine.
     */
    @Test
    public void testGetPurchaseReport() {
        System.out.println("getPurchaseReport");
        
    }

    /**
     * Test of getCategoryReport method, of class QueryEngine.
     */
    @Test
    public void testGetCategoryReport() {
        System.out.println("getCategoryReport");
        
    }

    /**
     * Test of getOrderID method, of class QueryEngine.
     */
    @Test
    public void testGetOrderID() {
        System.out.println("getOrderID");
        
    }

    /**
     * Test of addToCart method, of class QueryEngine.
     */
    @Test
    public void testAddToCart() {
        System.out.println("addToCart");
        
    }

    /**
     * Test of getCartContent method, of class QueryEngine.
     */
    @Test
    public void testGetCartContent() {
        System.out.println("getCartContent");
        
    }

    /**
     * Test of getCartSum method, of class QueryEngine.
     */
    @Test
    public void testGetCartSum() {
        System.out.println("getCartSum");
        
    }

    /**
     * Test of getPackages method, of class QueryEngine.
     */
    @Test
    public void testGetPackages() {
        System.out.println("getPackages");
        
    }

    /**
     * Test of getPostage method, of class QueryEngine.
     */
    @Test
    public void testGetPostage() {
        System.out.println("getPostage");
        
    }

    /**
     * Test of setOrderStatus method, of class QueryEngine.
     */
    @Test
    public void testSetOrderStatus() {
        System.out.println("setOrderStatus");
        
    }

    /**
     * Test of setBookStatus method, of class QueryEngine.
     */
    @Test
    public void testSetBookStatus() {
        System.out.println("setBookStatus");
        
    }

    /**
     * Test of setSchema method, of class QueryEngine.
     */
    @Test
    public void testSetSchema() {
        System.out.println("setSchema");
        
    }

    /**
     * Test of closeDatabaseConnection method, of class QueryEngine.
     */
    @Test
    public void testCloseDatabaseConnection() {
        System.out.println("closeDatabaseConnection");
        
    }

    /**
     * Test of connectToDatabase method, of class QueryEngine.
     */
    @Test
    public void testConnectToDatabase() {
        System.out.println("connectToDatabase");
        
    }

    /**
     * Test of checkIntFormat method, of class QueryEngine.
     */
    @Test
    public void testCheckIntFormat() {
        System.out.println("checkIntFormat");
        
    }
    
}
