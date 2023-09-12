package sfa.nav.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.odt.tools.HandlerStringString;

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
		a = AngleFactory.fromDegre(d);
		assertEquals(a.asDegre(), d, epsilon);

		Latitude l = LatitudeFactory.fromAngle(a);
		assertEquals(l.asDegre(), d, epsilon);

		d = -10.0;
		a = AngleFactory.fromDegre(d);
		assertEquals(a.asDegre(), 360.0 + d, epsilon);

		l = LatitudeFactory.fromAngle(a);
		assertNull(l);
	}

	@Test
	public void test002_FromAngleOriente () throws NavException {
		AngleOriente a;
		double d = 10.0;
		a = AngleOrienteFactory.fromDegre(d);
		assertEquals(a.asDegre(), d, epsilon);

		Latitude l = LatitudeFactory.fromAngle(a);
		assertEquals(l.asDegre(), d, epsilon);

		d = -10.0;
		a = AngleOrienteFactory.fromDegre(d);
		assertEquals(a.asDegre(), -10.0, epsilon);

		l = LatitudeFactory.fromAngle(a);
		assertEquals(l.asDegre(), d, epsilon);
	}

	@Test
	public void test003_FromText () throws NavException {
		String[][] x = 				
			{
				{"2°56N", "lat:002.933°[02°56.00][02°56'00.00\"][0.05 Rad] N"},
				{"2°56S", "lat:002.933°[02°56.00][02°56'00.00\"][0.05 Rad] S"},
				{"2°56 S", "lat:002.933°[02°56.00][02°56'00.00\"][0.05 Rad] S"},
				{"2°56.99 S", "lat:002.950°[02°56.99][02°56'59.40\"][0.05 Rad] S"},
				{"2°56'59.99\" s", "lat:002.950°[02°57.00][02°56'59.99\"][0.05 Rad] S"},
				{".992° s", "lat:000.992°[00°59.52][00°59'31.20\"][0.02 Rad] S"},
				{"1°45.56 s", "lat:001.759°[01°45.56][01°45'33.60\"][0.03 Rad] S"},
			}; 
		List<HandlerStringString> casDeTest = HandlerStringString.fromStringStringArray(x);
		for (HandlerStringString s: casDeTest) {
			logger.debug(s._s);
			Latitude l = LatitudeFactory.fromString(s._s);
			assertEquals(l.toString(), s._v);
		}
	}

	@Test
	public void test004_FromTextKo () throws NavException {
		String [][] casDeTest = {
				{"N 2°56", "lat:002.933°[02°56'00.00\"][0.05 Rad] N"},
				{"S 2°56S", "lat:002.933°[02°56'00.00\"][0.05 Rad] S"},
		};
		boolean isKO;
		for (String[] s: casDeTest) {
			try {
				isKO = false;
				Latitude l = LatitudeFactory.fromString(s[0]);
			} catch (Exception e) {
				isKO = true;
			}
			assertTrue(isKO);
		}
	}

	@Test
	public void test005_asRadian () throws NavException {
		Latitude l = LatitudeFactory.fromString("45.0 N");
		assertEquals(l.asDegre(), 45.0, epsilon);
		assertEquals(l.asRadian(), Math.PI / 4.0, epsilon);

		l = LatitudeFactory.fromString("45.0 S");
		assertEquals(l.asDegre(), -45.0, epsilon);
		assertEquals(l.asRadian(), -Math.PI / 4.0, epsilon);
	}

}
