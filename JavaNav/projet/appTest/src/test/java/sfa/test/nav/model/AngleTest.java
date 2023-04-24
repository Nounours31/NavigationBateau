package sfa.test.nav.model;

import sfa.nav.model.Angle;
import sfa.nav.tools.NavException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class AngleTest {
    File output;
    private static final Logger logger = LoggerFactory.getLogger(AngleTest.class);
    private static double epsilon = 0.000001;
    
    // ------------------------------------------------------------------------------------
    // Init zone
    // ------------------------------------------------------------------------------------
	@BeforeClass
	public static void initOnlyOnce() {
		logger.debug("initOnlyOnce");
	}

	@AfterClass
	public static void cleanOnlyOnce() {
		logger.debug("cleanOnlyOnce");
	}

    @Before 
    public void beforeMethode() {
		logger.debug("beforeMethode");
    }
    
    @After 
    public void afterMethode() {
		logger.debug("afterMethode");
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
	public void test002_FromDegre1 () throws NavException {
		Angle a;
		double d = 10.0;
		a = Angle.fromDegre(d);
		assertEquals(a.asDegre(), d, epsilon);
	}

	@Test
	public void test003_FromDegre2 () throws NavException {
		Angle a;
		double d = 10.0;
		double ref = 10.0;
		a = Angle.fromDegre(d);
		assertEquals(a.asDegre(), d, epsilon);
		
		d = -10.0;
		ref = 360.0 + d;
		a = Angle.fromDegre(d);
		assertEquals(a.asDegre(), ref, epsilon);

		double e = d + 10.0 * 360.0;
		a = Angle.fromDegre(e);
		assertEquals(a.asDegre(), ref, epsilon);

		e = d - 20.0 * 360.0;
		a = Angle.fromDegre(e);
		assertEquals(a.asDegre(), ref, epsilon);
	}

	@Test
	public void test004_FromRadian () throws NavException {
		Angle a;
		double d = Math.PI / 2.0;
		double ref = 90.0;
		a = Angle.fromRadian(d);
		assertEquals(a.asDegre(), ref, epsilon);
		
		d = Math.PI / 4.0;
		ref = 45.0;
		a = Angle.fromRadian(d);
		assertEquals(a.asDegre(), ref, epsilon);

		d = Math.PI;
		ref = 180.0;
		a = Angle.fromRadian(d);
		assertEquals(a.asDegre(), ref, epsilon);

		d = 3.0 * Math.PI / 2.0;
		ref = 270.0;
		a = Angle.fromRadian(d);
		assertEquals(a.asDegre(), ref, epsilon);

		d = Math.PI * 2.0;
		ref = 0.0;
		a = Angle.fromRadian(d);
		assertEquals(a.asDegre(), ref, epsilon);

		d = - Math.PI / 2.0;
		ref = 270.0;
		a = Angle.fromRadian(d);
		assertEquals(a.asDegre(), ref, epsilon);
	}

	@Test
	public void test005_asRadian () throws NavException {
		Angle a;
		double d = Math.PI / 2.0;
		double ref = d;
		a = Angle.fromRadian(d);
		assertEquals(a.asRadian(), ref, epsilon);
		
		d = Math.PI / 4.0;
		ref = d;
		a = Angle.fromRadian(d);
		assertEquals(a.asRadian(), ref, epsilon);

		d = Math.PI;
		ref = d;
		a = Angle.fromRadian(d);
		assertEquals(a.asRadian(), ref, epsilon);

		d = 3.0 * Math.PI / 2.0;
		ref = d;
		a = Angle.fromRadian(d);
		assertEquals(a.asRadian(), ref, epsilon);

		d = Math.PI * 2.0;
		ref = 0.0;
		a = Angle.fromRadian(d);
		assertEquals(a.asRadian(), ref, epsilon);

		d = - Math.PI / 2.0;
		ref = 2.0 * Math.PI + d;
		a = Angle.fromRadian(d);
		assertEquals(a.asRadian(), ref, epsilon);
		
		d = 5 * Math.PI / 2.0;
		ref = Math.PI / 2.0;
		a = Angle.fromRadian(d);
		assertEquals(a.asRadian(), ref, epsilon);
	}

	class handlerStringDouble {
		String _s;
		double _v;
		handlerStringDouble(String s, double v) {_s = s; _v = v;}
	}

	class handlerStringString {
		String _s;
		String _v;
		handlerStringString(String s, String v) {_s = s; _v = v;}
	}

	@Test
	public void test006_fromText_HeureDecimaleOK () throws NavException {
		Angle a;
		handlerStringDouble[] allVals = {
				new handlerStringDouble("25.25", 25.25),
				new handlerStringDouble("25°25", (25.00 + 25.0/60.0)),
				new handlerStringDouble("25.25°", 25.25),

				new handlerStringDouble(".25°", 0.25),
				new handlerStringDouble("0.25°", 0.25),
				new handlerStringDouble("0°25", (0.0 + 25.0/60.0)),
		};
		
		for (handlerStringDouble val : allVals) {
			String s = val._s;
			double ref = val._v;
			logger.debug("s: " + s + "| v:" + ref);

			a = Angle.fromString(s);
			assertEquals(a.asDegre(), ref, epsilon);			
		}
	}

	@Test
	public void test007_fromText_HeureDecimaleKO () throws NavException {
		Angle a;
		handlerStringDouble[] allVals = {
				new handlerStringDouble("°25", 1.0),
		};
		
		for (handlerStringDouble val : allVals) {
			String s = val._s;
			double ref = val._v;
			logger.debug("s: " + s + "| v:" + ref);
			a = Angle.fromString(s);

			assertFalse(a.isInitialised());			
		}
	}

	@Test
	public void test008_fromText_MinutesDecimale () throws NavException {
		Angle a;
		handlerStringDouble[] allVals = {
				new handlerStringDouble("5.25", 5.25),
				new handlerStringDouble("5.25°", 5.25),
				new handlerStringDouble("5°25", 5 + 25.0/60.0),
				new handlerStringDouble("5°25'", 5 + 25.0/60.0),
				new handlerStringDouble("5°25\"", 5 + 25.0/60.0),
				new handlerStringDouble("5°25.99", 5 + 25.99/60.0),
				new handlerStringDouble("5°25.99'", 5 + 25.99/60.0),
				new handlerStringDouble("5°05.50'", 5 + 5.5/60.0),
				new handlerStringDouble("5°5.50'", 5 + 5.5/60.0),
				new handlerStringDouble("5°5.50", 5 + 5.5/60.0),
				new handlerStringDouble("5°25'00\"", (5.00 + 25.0/60.0)),
				new handlerStringDouble("5°25'59\"", (5.00 + 25.0/60.0 + 59.0/(60.0*60.0))),
				new handlerStringDouble("5°05'00\"", (5.00 + 5.0/60.0)),
				new handlerStringDouble("5°5'00\"", (5.00 + 5.0/60.0)),
				new handlerStringDouble("5°5'", (5.00 + 5.0/60.0)),
				new handlerStringDouble("5°5", (5.00 + 5.0/60.0)),
				new handlerStringDouble("5°5\"", (5.00 + 5.0/60.0)),
		};
		
		for (handlerStringDouble val : allVals) {
			String s = val._s;
			double ref = val._v;
			logger.debug("s: " + s + "| v:" + ref);
			a = Angle.fromString(s);
			assertEquals(a.asDegre(), ref, epsilon);			
		}
	}

	@Test
	public void test009_fromText_MinutesDecimaleKO () throws NavException {
		Angle a;
		handlerStringDouble[] allVals = {
				new handlerStringDouble("25°60", 1),
				new handlerStringDouble("25°60'", 1),
				new handlerStringDouble("25°60\"", 1),
				new handlerStringDouble("25°61\"", 1),
		};
		
		for (handlerStringDouble val : allVals) {
			String s = val._s;
			double ref = val._v;
			logger.debug("s: " + s + "| v:" + ref);
			a = Angle.fromString(s);
			assertFalse(a.isInitialised());			
		}
	}
	
	@Test
	public void test010_fromText_SecondesDecimale () throws NavException {
		Angle a;
		handlerStringDouble[] allVals = {
				new handlerStringDouble("5°25'09", (5.00 + 25.0/60.0 + 9.0/(60.0*60.0))),
				new handlerStringDouble("5°25'09\"", (5.00 + 25.0/60.0 + 9.0/(60.0*60.0))),
				new handlerStringDouble("5°25'9", (5.00 + 25.0/60.0 + 9.0/(60.0*60.0))),
				new handlerStringDouble("5°25'9\"", (5.00 + 25.0/60.0 + 9.0/(60.0*60.0))),
				new handlerStringDouble("5°25'9.99", (5.00 + 25.0/60.0 + 9.99/(60.0*60.0))),
				new handlerStringDouble("5°25'9.99\"", (5.00 + 25.0/60.0 + 9.99/(60.0*60.0))),
		};
		
		for (handlerStringDouble val : allVals) {
			String s = val._s;
			double ref = val._v;
			logger.debug("s: " + s + "| v:" + ref);
			a = Angle.fromString(s);
			assertEquals(a.asDegre(), ref, epsilon);			
		}
	}

	@Test
	public void test011_fromText_SecondesDecimaleKO () throws NavException {
		Angle a;
		handlerStringDouble[] allVals = {
				new handlerStringDouble("25°25'99", 1),
				new handlerStringDouble("25°25'99\"", 1),
		};
		
		for (handlerStringDouble val : allVals) {
			String s = val._s;
			double ref = val._v;
			a = Angle.fromString(s);
			logger.debug("s: " + s + "| v:" + ref);

			assertFalse(a.isInitialised());			
		}
	}

	@Test
	public void test101_tostring () throws NavException {
		Angle a;
		handlerStringString[] allVals = {
				new handlerStringString("25°25.99", "25.433166666666665[25°25'59.40\"]"),
				new handlerStringString("25°5'59",  "25.099722222222223[25°05'59.00\"]"),
		};
		
		for (handlerStringString val : allVals) {
			String s = val._s;
			String ref = val._v;
			logger.debug("s: " + s + "| v:" + ref);
			a = Angle.fromString(s);
			logger.debug("a: >" + a.toString() + "<");
			assertEquals(a.toString(), ref);			
		}
	}
}
