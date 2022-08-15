/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package sfa.voile.nav.astro.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author pfs
 */
public class FormatHeureAstroTest {
    FormatHeureAstro[] instance = FormatHeureAstro._allRegex;

    @BeforeAll
    static public void setupBeforeAll() {
        System.out.println("setupBeforeAll");
    }

    @BeforeEach
    public void setupBefore() {
        System.out.println("setupBefore");
    }

    
    @AfterAll
    static public void cleanAfter() {
        System.out.println("cleanAfter");
    }

    @AfterEach
    public void cleanAfterClass() {
        System.out.println("cleanAfterClass");
    }

    /**
     * Test of printFormet method, of class FormatHeureAstro.
     */
    @Test
    public void testPrintFormet() {
        System.out.println("printFormet");
        String expResult = "";
        String result = instance[0].printFormet();
        assertEquals(result, expResult);
    }

    /**
     * Test of isOK method, of class FormatHeureAstro.
     */
    @Test
    public void testIsOK() {
        System.out.println("isOK");
        String s = "";
        boolean expResult = false;
        boolean result = instance[0].isOK(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class FormatHeureAstro.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        String s = "";
        double expResult = 0.0;
        double result = instance[0].parse(s);
        assertEquals(expResult, result, 0);
    }    
}
