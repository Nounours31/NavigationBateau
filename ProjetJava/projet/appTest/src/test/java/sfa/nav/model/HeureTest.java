package sfa.nav.model;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.tools.Constantes;
import sfa.nav.odt.tools.HandlerStringString;

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

public class HeureTest extends Heure {
    File output;
    private static final Logger logger = LoggerFactory.getLogger(HeureTest.class);
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
	public void test002 () throws NavException {

		HandlerStringString[] allVals = {
				new HandlerStringString("1.00", "025.433°[25°25'59.40\"][0.44 Rad]"),
				new HandlerStringString("1.5",  "025.100°[25°05'59.00\"][0.44 Rad]"),
				new HandlerStringString("1:50",  "025.100°[25°05'59.00\"][0.44 Rad]"),
				new HandlerStringString("1:5",  "025.100°[25°05'59.00\"][0.44 Rad]"),
				new HandlerStringString("1:05",  "025.100°[25°05'59.00\"][0.44 Rad]"),
				new HandlerStringString("1:5:2",  "025.100°[25°05'59.00\"][0.44 Rad]"),
				new HandlerStringString("1:5:20",  "025.100°[25°05'59.00\"][0.44 Rad]"),
				new HandlerStringString("1:5:02",  "025.100°[25°05'59.00\"][0.44 Rad]"),
				new HandlerStringString("1:5:20.55",  "025.100°[25°05'59.00\"][0.44 Rad]"),
		};
		
		Heure h = null; 
		for (HandlerStringString val : allVals) {
			String s = val._s;
			String ref = val._v;
			logger.debug("s: " + s + "| v:" + ref);
			h = HeureFactory.fromString(s);
			logger.debug("heure: >" + h.toString() + "<");
			assertEquals(h.toString(), ref);			
		}
	}
}
