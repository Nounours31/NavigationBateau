package sfa.nav.nav.calculs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Cap;
import sfa.nav.model.CapFactory;
import sfa.nav.model.Distance;
import sfa.nav.model.DistanceFactory;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.model.tools.DataOrthoDromieCapDistanceVertex;


public class CoursDeNavigation_DocAncien_page7_orthodromie {
    private static final Logger logger = LoggerFactory.getLogger(CoursDeNavigation_DocAncien_page7_orthodromie.class);

    private static double precisionCap = 2.0 * Constantes.DemiDegreEnDegre; // 1 degre 
    private static double precisionDistanceEnMn = 1; // longue distance
    
    private static CalculsAngulaires ca = null;
    
    // ------------------------------------------------------------------------------------
    // Init zone
    // ------------------------------------------------------------------------------------
 		
	@BeforeClass
	public static void initOnlyOnce() throws NavException {
		ca = new CalculsAngulaires();		
	}

	@AfterClass
	public static void cleanOnlyOnce() {
		ca = null;
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
		
		assertNotNull(ca);
	}


	
	@Test
	public void test001_page7_Cas1_LongueDistance () throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("38°30 N"),
				LongitudeFactory.fromString("142°00 E"));
		
		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("37°48 N"),
				LongitudeFactory.fromString("122°30 W"));

		Cap capOrtho = CapFactory.fromDegre(56);
		Distance distanceOrtho = DistanceFactory.fromMn(4279);
		PointGeographique vertex = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("49°26.5 N"),
				LongitudeFactory.fromString("170°54.3 W"));

		DataOrthoDromieCapDistanceVertex infosOrtho = ca.capOrthodromique(pgDepart, pgArrivee);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),  precisionDistanceEnMn); 
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
	
		Cap capLoxo = CapFactory.fromDegre(58);
		Distance distanceLoxo = DistanceFactory.fromMn(4514);
		DataLoxodromieCapDistance infosLoxo = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosLoxo.distance().distanceInMilleNautique(), distanceLoxo.distanceInMilleNautique(),  precisionDistanceEnMn); 
		assertEquals(infosLoxo.cap().asDegre(), capLoxo.asDegre(), precisionCap);
	}
	

}
