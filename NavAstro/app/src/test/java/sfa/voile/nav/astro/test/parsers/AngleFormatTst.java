/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package sfa.voile.nav.astro.test.parsers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sfa.voile.nav.astro.modele.AngleParser;
import sfa.voile.nav.astro.modele.Angle;
import sfa.voile.nav.astro.modele.HeureParser;

/**
 *
 * @author pfs
 */
public class AngleFormatTst {
	double EPSILON = 1/ (360.0 * 60 * 60 * 10);
    static AngleParser parser = null;

    @BeforeAll
    static public void setupBeforeAll() {
        System.out.println("setupBeforeAll");
        parser = AngleParser.getInstance();
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

    @Test
    public void test1() {
    	Angle d = new Angle(0.0);
    	boolean rc = parser.parse("21.0", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (21.0)) < EPSILON);
    }

    @Test
    public void test2() {
    	Angle d = new Angle(0.0);
    	boolean rc = parser.parse("21°54'", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (21.9)) < EPSILON);
    }

    @Test
    public void test3() {
    	Angle d = new Angle(0.0);
    	boolean rc = parser.parse("21°54'57\"", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (21.91583333333333)) < EPSILON);
    }

    @Test
    public void test33() {
    	Angle d = new Angle(0.0);
    	boolean rc = parser.parse("21°54'57.1\"", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (21.915861111111113)) < EPSILON);
    }

    @Test
    public void test4() {
    	Angle d = new Angle(0.0);
    	boolean rc = parser.parse("21°54.87'", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (21.9145)) < EPSILON);
    }

    @Test
    public void test4bis() {
    	Angle d = new Angle(0.0);
    	boolean rc = parser.parse("21°54'30\"", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (21.908333333333335)) < EPSILON);
    	rc = parser.parse("21°54.500", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (21.908333333333335)) < EPSILON);    
    }

    public void test11() {
    	Angle d = new Angle(0.0);
    	boolean rc = parser.parse("-21.0", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (-21.0)) < EPSILON);

    	rc = parser.parse("+21.0", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (21.0)) < EPSILON);
    }

    @Test
    public void test12() {
    	Angle d = new Angle(0.0);
    	boolean rc = parser.parse("-21°54'", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (-21.9)) < EPSILON);

    	rc = parser.parse("+21°54'", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (21.9)) < EPSILON);
    }

    @Test
    public void test13() {
    	Angle d = new Angle(0.0);
    	boolean rc = parser.parse("-21°54'57\"", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (-21.91583333333333)) < EPSILON);

    	rc = parser.parse("+21°54'57\"", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (21.91583333333333)) < EPSILON);
    }

    @Test
    public void test14() {
    	Angle d = new Angle(0.0);
    	boolean rc = parser.parse("-21°54.87'", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (-21.9145)) < EPSILON);

    	rc = parser.parse("+21°54.87'", d);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(d.getVal() - (21.9145)) < EPSILON);
    }

    @Test
    public void test21() {
    	Angle d = new Angle(0.0);
    	boolean rc = parser.parse("-620°54.87'", d);
    	assertEquals(rc, false);
    	rc = parser.parse("+620°54.87'", d);
    	assertEquals(rc, false);
    }
}
