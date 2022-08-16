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
import sfa.voile.nav.astro.modele.Longitude;
import sfa.voile.nav.astro.modele.Longitude.SensLongitude;
import sfa.voile.nav.astro.modele.LongitudeParser;

/**
 *
 * @author pfs
 */
public class LongitudeFormatTst {
	double EPSILON = 1/ (360.0 * 60 * 60 * 10);
	static LongitudeParser parser = null;

	@BeforeAll
	static public void setupBeforeAll() {
		System.out.println("setupBeforeAll");
		parser = LongitudeParser.getInstance();
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
		Longitude L = new Longitude();
		boolean rc = parser.parse("W 21°10'59\"", L);
		assertEquals(rc, true);
		assertTrue(Math.abs(L.getLongitude() - (21.183055555555555)) < EPSILON);
		assertEquals(L.getSens(), SensLongitude.West);

		rc = parser.parse("E 21°10'59\"", L);
		assertEquals(rc, true);
		assertTrue(Math.abs(L.getLongitude() - (21.183055555555555)) < EPSILON);
		assertEquals(L.getSens(), SensLongitude.Est);
	}


	@Test
	public void test2() {
		Longitude L = new Longitude();
		boolean rc = parser.parse("E 21.99", L);
		assertEquals(rc, true);
		assertTrue(Math.abs(L.getLongitude() - (21.99)) < EPSILON);
		assertEquals(L.getSens(), SensLongitude.Est);
	}

	@Test
	public void test3() {
		Longitude L = new Longitude();
		boolean rc = parser.parse("E 21°59.99", L);
		assertEquals(rc, true);
		assertTrue(Math.abs(L.getLongitude() - (21.999833333333335)) < EPSILON);
		assertEquals(L.getSens(), SensLongitude.Est);
		
		rc = parser.parse("W 21°59.99'", L);
		assertEquals(rc, true);
		assertTrue(Math.abs(L.getLongitude() - (21.999833333333335)) < EPSILON);
		assertEquals(L.getSens(), SensLongitude.West);
	}

	@Test
	public void test4() {
		Longitude L = new Longitude();
		boolean rc = parser.parse("E 21°59'99.999\"", L);
		assertEquals(rc, false);
		rc = parser.parse("E 21°59'99.999", L);
		assertEquals(rc, false);
		rc = parser.parse("E 21°59'59.999\"", L);
		assertEquals(rc, true);
		assertTrue(Math.abs(L.getLongitude() - (21.99999972222222)) < EPSILON);
		assertEquals(L.getSens(), SensLongitude.Est);
		rc = parser.parse("W 21°59'59.999", L);
		assertEquals(rc, true);
		assertTrue(Math.abs(L.getLongitude() - (21.99999972222222)) < EPSILON);
		assertEquals(L.getSens(), SensLongitude.West);
	}

	@Test
	public void test5() {
		Longitude L = new Longitude();
		boolean rc = parser.parse("E 181°0'0\"", L);
		assertEquals(rc, false);
		rc = parser.parse("E 179°59'59\"", L);
		assertEquals(rc, false);
	}
}
