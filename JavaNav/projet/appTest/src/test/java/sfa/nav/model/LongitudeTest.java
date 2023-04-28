package sfa.nav.model;

import sfa.nav.model.Angle;
import sfa.nav.tools.Constantes;
import sfa.nav.tools.NavException;

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

public class LongitudeTest extends Angle {
    File output;
    private static final Logger logger = LoggerFactory.getLogger(LongitudeTest.class);
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

}
