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
import sfa.nav.model.Latitude;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.Longitude;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.model.tools.DataOrthoDromieCapDistanceVertex;
import sfa.nav.nav.calculs.CalculsAngulaires;


public class Orthodromie_Loxo_CoursDeNavigation_GrilleDeCalculTest {
    private static final Logger logger = LoggerFactory.getLogger(Orthodromie_Loxo_CoursDeNavigation_GrilleDeCalculTest.class);

    private static double precisionCap = Constantes.DemiDegreEnDegre; 
    private static double precisionDiatnce = Constantes.milleMarinEnMetre;
    
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
	public void test005_OrthoDocPasserellePage4CourteDistance () throws NavException {
		// ------------------------------
		// cas 1
		// ------------------------------
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("38°30.0 N"),
				LongitudeFactory.fromString("142°00.00 E"));
		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("37°48 N"),
				LongitudeFactory.fromString("122°30 W"));
		Cap capOrtho = CapFactory.fromDegre(56);
		Distance distanceOrtho = DistanceFactory.fromMn(4272);
		PointGeographique vertexOrtho = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("49°26.5 N"),
				LongitudeFactory.fromString("170°54.3 W"));

		Cap capLoxo = CapFactory.fromDegre(58);
		Distance distanceLoxo = DistanceFactory.fromMn(4506);
		
		DataOrthoDromieCapDistanceVertex infosOrtho = ca.getOrthoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infosOrtho._distance.equalsInMn(distanceOrtho, precisionDiatnce)); 
		assertTrue(infosOrtho._cap.equalsInDegre(capOrtho, precisionCap));
		assertTrue(infosOrtho._vertex.equals(vertexOrtho, precisionDiatnce));

		DataLoxodromieCapDistance infosLoxo = ca.getLoxoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infosLoxo._distance.equalsInMn(distanceLoxo, precisionDiatnce)); 
		assertTrue(infosLoxo._cap.equalsInDegre(capLoxo, precisionCap));
	}
}
