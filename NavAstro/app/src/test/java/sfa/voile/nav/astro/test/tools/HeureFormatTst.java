/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package sfa.voile.nav.astro.test.tools;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sfa.voile.nav.astro.modele.HandleDouble;
import sfa.voile.nav.astro.modele.HeureParser;

/**
 *
 * @author pfs
 */
public class HeureFormatTst {
	double EPSILON = 1/ (360.0 * 60 * 60 * 10);
    static HeureParser parser = null;

    @BeforeAll
    static public void setupBeforeAll() {
        System.out.println("setupBeforeAll");
        parser = HeureParser.getInstance();
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
    public void test1() {
    	HandleDouble d = new HandleDouble();
    	boolean rc = parser.parse("2.0", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.d() - (2.0)) < EPSILON);

    	rc = parser.parse("-2.0", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.d() - (-2.0)) < EPSILON);
    }

    @Test
    public void test2() {
    	HandleDouble d = new HandleDouble();
    	boolean rc = parser.parse("2:0:0", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.d() - (2.0)) < EPSILON);
    	rc = parser.parse("02:00:00", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.d() - (2.0)) < EPSILON);
    }

    @Test
    public void test3() {
    	HandleDouble d = new HandleDouble();
    	boolean rc = parser.parse("2:0:30", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.d() - (2.0083333333333333)) < EPSILON);

    	rc = parser.parse("-2:0:30", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.d() - (-2.0083333333333333)) < EPSILON);
    }

    @Test
    public void test4() {
    	HandleDouble d = new HandleDouble();
    	boolean rc = parser.parse("2:0.5", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.d() - (2.0083333333333333)) < EPSILON);
    	
    	rc = parser.parse("-2:0.5", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.d() - (-2.0083333333333333)) < EPSILON);
    }
}
