/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import trip.utilities.HashAlgorithm;

/**
 *
 * @author NLens
 */
public class HashAlgorithmTest {

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

    @Test
    public void testGenerateSameHash() {
        System.out.println("Testing generateHash()");

        String salt = HashAlgorithm.generateSalt();
        String password = "1234";

        String expectedPassword = HashAlgorithm.generateHash(password, salt);

        assertEquals(expectedPassword, HashAlgorithm.generateHash(password, salt));

    }

    @Test
    public void testRandomSalt() {
        System.out.println("Testing RandomtSalt()");

        int identical = 0;
        int different = 0;

        String salt = HashAlgorithm.generateSalt();
        String password = "1234";

        String expectedPassword = HashAlgorithm.generateHash(password, salt);

        for (int i = 0; i < 100000; i++)
        {
            if (expectedPassword.equals(HashAlgorithm.generateHash(password, HashAlgorithm.generateSalt())))
            {
                identical++;
            } else
            {
                different++;
            }
        }
        assertTrue(identical < 5);
    }

}
