
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
public class UserInterfaceTest {
    
    private UserInterface instance;
    
    
    @Before
    public void setUp() {
        this.instance = new UserInterface();
    }
    
    @After
    public void tearDown() {
    }


    /**
     * Test of printSpace method, of class UserInterface.
     */
    @Test
    public void testPrintSpace() {
        System.out.println("printSpace");
        int count = 0;
        instance.printSpace(count);
        
    }


    /**
     * Test of stringLimiter method, of class UserInterface.
     */
    @Test
    public void testStringLimiter() {
        System.out.println("stringLimiter");
        String text = "Tämä teksti on ylimittainen!";
        int limit = 10;
        // Halutaan annetun merkkijonon alkuosa, jonka pituus 10+[...].
        String expResult = "Tämä tekst...";
        String result = instance.stringLimiter(text, limit);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of postageCalculator method, of class UserInterface.
     */
    @Test
    public void testPostageCalculator() {
        System.out.println("postageCalculator");
        // Odotetaan tyhjää listaa, koska tilaus_id:tä ei asetettu
        ArrayList<String> expResult = new ArrayList<>(); 
        ArrayList<String> result = instance.postageCalculator();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of checkUserInput method, of class UserInterface.
     * 
     */
    @Test
    public void testCheckUserInput() {
        System.out.println("checkUserInput");
        // Tarkistetaan, että tyhjällä merkkijonolla palautuu false.
        String input = "";
        boolean expResult = false;
        boolean result = instance.checkUserInput(input);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of checkDoubleFormat method, of class UserInterface.
     */
    @Test
    public void testCheckDoubleFormat() {
        System.out.println("checkDoubleFormat");
        // Testataan, että oikealla syötteellä lukumuunnos onnistuu
        String input = "2.5";
        double expResult = 2.5;
        double result = instance.checkDoubleFormat(input);
        assertEquals(expResult, result, 0.0);
        
    }

    /**
     * Test of checkIntFormat method, of class UserInterface.
     */
    @Test
    public void testCheckIntFormat() {
        System.out.println("checkIntFormat");
        // Testataan, että oikealla syötteellä lukumuunnos onnistuu
        String input = "10";
        int expResult = 10;
        int result = instance.checkIntFormat(input);
        assertEquals(expResult, result);
        
    }

}
