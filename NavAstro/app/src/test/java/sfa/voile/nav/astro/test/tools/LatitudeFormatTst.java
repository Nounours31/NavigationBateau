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

import sfa.voile.nav.astro.modele.HeureParser;
import sfa.voile.nav.astro.modele.Latitude;
import sfa.voile.nav.astro.modele.Latitude.SensLatitude;
import sfa.voile.nav.astro.modele.LatitudeParser;

/**
 *
 * @author pfs
 */
public class LatitudeFormatTst {
	double EPSILON = 1/ (360.0 * 60 * 60 * 10);
    static LatitudeParser parser = null;
    
    @BeforeAll
    static public void setupBeforeAll() {
        System.out.println("setupBeforeAll");
        parser = LatitudeParser.getInstance();
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
    	Latitude L = new Latitude();
    	boolean rc = parser.parse("N 21°10'59\"", L);
    	assertEquals(rc, true);
    	assertTrue(Math.abs(L.getLatitude() - (21.183055555555555)) < EPSILON);
    	assertEquals(L.getSens(), SensLatitude.Nord);
    }

}
