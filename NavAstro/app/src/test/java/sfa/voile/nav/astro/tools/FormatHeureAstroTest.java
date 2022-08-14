/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package sfa.voile.nav.astro.tools;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
/**
 *
 * @author pfs
 */
public class FormatHeureAstroTest {
    @Before
    public void setupBefore() {
        System.out.println("setupBefore");
    }

    @BeforeClass
    static public void setupBeforeClass() {
        System.out.println("setupBeforeClass");
    }
    
    @After
    public void cleanAfter() {
        System.out.println("cleanAfter");
    }

    @AfterClass
    static public void cleanAfterClass() {
        System.out.println("cleanAfterClass");
    }

    /**
     * Test of printFormet method, of class FormatHeureAstro.
     */
    @Test
    public void testPrintFormet() {
        System.out.println("printFormet");
        FormatHeureAstro instance = null;
        String expResult = "";
        String result = instance.printFormet();
        assertEquals(expResult, result);
    }

    /**
     * Test of isOK method, of class FormatHeureAstro.
     */
    @Test
    public void testIsOK() {
        System.out.println("isOK");
        String s = "";
        FormatHeureAstro instance = null;
        boolean expResult = false;
        boolean result = instance.isOK(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class FormatHeureAstro.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        String s = "";
        FormatHeureAstro instance = null;
        double expResult = 0.0;
        double result = instance.parse(s);
        assertEquals(expResult, result, 0);
    }    
}
