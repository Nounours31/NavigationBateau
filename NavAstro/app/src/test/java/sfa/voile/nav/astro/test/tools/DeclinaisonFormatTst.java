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

import sfa.voile.nav.astro.modele.Latitude.SensLatitude;
import sfa.voile.nav.astro.modele.Declinaison;
import sfa.voile.nav.astro.modele.DeclinaisonParser;

/**
 *
 * @author pfs
 */
public class DeclinaisonFormatTst {
	double EPSILON = 1/ (360.0 * 60 * 60 * 10);
    static DeclinaisonParser parser = null;

    @BeforeAll
    static public void setupBeforeAll() {
        System.out.println("setupBeforeAll");
        parser = (DeclinaisonParser) DeclinaisonParser.getInstance();
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
		Declinaison D = new Declinaison();
		boolean rc = parser.parse("n 21°10'59\"", D);
		assertEquals(rc, true);
		assertTrue(Math.abs(D.getDeclinaison() - (21.183055555555555)) < EPSILON);
		assertEquals(D.getSens(), SensLatitude.Nord);

		rc = parser.parse("s 21°10'59\"", D);
		assertEquals(rc, true);
		assertTrue(Math.abs(D.getDeclinaison() - (21.183055555555555)) < EPSILON);
		assertEquals(D.getSens(), SensLatitude.Sud);
	}


	@Test
	public void test2() {
		Declinaison D = new Declinaison();
		boolean rc = parser.parse("N 21.99", D);
		assertEquals(rc, true);
		assertTrue(Math.abs(D.getDeclinaison() - (21.99)) < EPSILON);
		assertEquals(D.getSens(), SensLatitude.Nord);
	}

	@Test
	public void test3() {
		Declinaison D = new Declinaison();
		boolean rc = parser.parse("N 21°59.99", D);
		assertEquals(rc, true);
		assertTrue(Math.abs(D.getDeclinaison() - (21.999833333333335)) < EPSILON);
		assertEquals(D.getSens(), SensLatitude.Nord);
		
		rc = parser.parse("S 21°59.99'", D);
		assertEquals(rc, true);
		assertTrue(Math.abs(D.getDeclinaison() - (21.999833333333335)) < EPSILON);
		assertEquals(D.getSens(), SensLatitude.Sud);
	}

	@Test
	public void test4() {
		Declinaison D = new Declinaison();
		boolean rc = parser.parse("E 21°59'99.999\"", D);
		assertEquals(rc, false);
		rc = parser.parse("S 21°59'99.999", D);
		assertEquals(rc, false);
		rc = parser.parse("S 21°59'59.999\"", D);
		assertEquals(rc, true);
		assertTrue(Math.abs(D.getDeclinaison() - (21.99999972222222)) < EPSILON);
		assertEquals(D.getSens(), SensLatitude.Sud);
		rc = parser.parse("N 21°59'59.999", D);
		assertEquals(rc, true);
		assertTrue(Math.abs(D.getDeclinaison() - (21.99999972222222)) < EPSILON);
		assertEquals(D.getSens(), SensLatitude.Nord);
	}

	@Test
	public void test5() {
		Declinaison D = new Declinaison();
		boolean rc = parser.parse("E 91°0'0\"", D);
		assertEquals(rc, false);
		rc = parser.parse("E 89°59'59\"", D);
		assertEquals(rc, false);
	}

    
}
