package sfa.nav.lib.model;

import sfa.nav.lib.tools.NavException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class LatitudeTest extends Angle {
    File output;
    private static final Logger logger = LoggerFactory.getLogger(LatitudeTest.class);
    private static double epsilon = 0.000001;
    
    // ------------------------------------------------------------------------------------
    // Init zone
    // ------------------------------------------------------------------------------------
	@BeforeClass
	public static void initOnlyOnce() {
	}

	@AfterClass
	public static void cleanOnlyOnce() {
	}

    @Before 
    public void beforeMethode() {
    }
    
    @After 
    public void afterMethode() {
    }
	
    // ------------------------------------------------------------------------------------
    // Les tests
    // ------------------------------------------------------------------------------------
	@Test
	public void test001_EstCeQueCaMarche () {
		int sum = 6;
		assertEquals(sum, 6);
	}


	@Test
	public void test002_FromAngle () throws NavException {
		Angle a;
		double d = 10.0;
		a = Angle.fromDegre(d);
		assertEquals(a.asDegre(), d, epsilon);

		Latitude l = Latitude.fromAngle(a);
		assertEquals(l.asDegre(), d, epsilon);

		d = -10.0;
		a = Angle.fromDegre(d);
		assertEquals(a.asDegre(), 360.0 + d, epsilon);

		l = Latitude.fromAngle(a);
		assertEquals(l.asDegre(), d, epsilon);


	}

	@Test
	public void test003_FromText () throws NavException {
		String [][] casDeTest = {
				{"Cas 1", "2°56N", "l: 002.933°[02°56'00.00\"][0.05 Rad] N"},
				{"Cas 2","2°56S", "l: 002.933°[02°56'00.00\"][0.05 Rad] S"},
				{"Cas 3","2°56 S", "l: 002.933°[02°56'00.00\"][0.05 Rad] S"},
				{"Cas 4","2°56.99 S", "l: 002.933°[02°56'00.00\"][0.05 Rad] S"},
				{"Cas 6","2°56'59.99\" s", "l: 002.933°[02°56'00.00\"][0.05 Rad] S"},
				{"Cas 7",".992° s", "l: 002.933°[02°56'00.00\"][0.05 Rad] S"},
				{"Cas 8","1°45.56 s", "l: 002.933°[02°56'00.00\"][0.05 Rad] S"},
		};
		for (String[] s: casDeTest) {
			logger.debug(s[0]);
			Latitude l = Latitude.fromString(s[1]);
			assertEquals(l.toString(), s[2]);
		}
	}

	@Test
	public void test004_FromTextKo () throws NavException {
		String [][] casDeTest = {
				{"N 2°56", "l: 002.933°[02°56'00.00\"][0.05 Rad] N"},
				{"S 2°56S", "l: 002.933°[02°56'00.00\"][0.05 Rad] S"},
		};
		boolean isKO;
		for (String[] s: casDeTest) {
			try {
				isKO = false;
				Latitude l = Latitude.fromString(s[0]);
			} catch (Exception e) {
				isKO = true;
			}
			assertTrue(isKO);
		}
	}

}
