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


public class Orthodromie_aDistanceMinimale_Test {
    private static final Logger logger = LoggerFactory.getLogger(Orthodromie_aDistanceMinimale_Test.class);

    private static double precisionCap = Constantes.DemiDegreEnDegre; 
    private static double precisionDiatnce = Constantes.milleMarinEnMetre;
    
    private static CalculsAngulaires ca = null;
    
    // ------------------------------------------------------------------------------------
    // Init zone
    // ------------------------------------------------------------------------------------
    // Paris :-)
 	static PointGeographique Paris = null;
	static PointGeographique NewYork = null;
	static PointGeographique GreenWitch = null;
	static PointGeographique MelhPointB = null;
	static PointGeographique MelhMemeLatitudePointA = null;
	static PointGeographique MelhMemeLatitudePointB = null;
	static PointGeographique MelhMemeLongitudePointA = null;
	static PointGeographique MelhMemeLongitudePointB = null;
 		
	@BeforeClass
	public static void initOnlyOnce() throws NavException {
		ca = new CalculsAngulaires();
		
		Paris = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(48.85), LongitudeFactory.fromDegre(2.35));
 		NewYork = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(40.705628), LongitudeFactory.fromDegre(-74.013519));
 		GreenWitch = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(0.0), LongitudeFactory.fromDegre(0.0));
 		MelhPointB = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(60.0), LongitudeFactory.fromDegre(120.0));
 		MelhMemeLatitudePointA = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(40.0), LongitudeFactory.fromDegre(-140.0));
 		MelhMemeLatitudePointB = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(40.0), LongitudeFactory.fromDegre(160.0));
 		MelhMemeLongitudePointA = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(78.0), LongitudeFactory.fromDegre(107.0));
 		MelhMemeLongitudePointB = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(-59.1), LongitudeFactory.fromDegre(107.0));
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
	public void test002_OrthoCasSimple () throws NavException {
		Latitude latitudeA = LatitudeFactory.fromDegre(47.0), latitudeB = LatitudeFactory.fromDegre(10.0);
		Longitude longitudeA = LongitudeFactory.fromDegre(2.0), longitudeB = LongitudeFactory.fromDegre(106.0);
		
		PointGeographique A = PointGeographiqueFactory.fromLatLong(latitudeA, longitudeA);
		PointGeographique B = PointGeographiqueFactory.fromLatLong(latitudeB, longitudeB);
		
		DataOrthoDromieCapDistanceVertex infos = ca.capOrthodromique(A, B);
		
		assertEquals(infos._distance.distanceInKm(), 10244.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 73.0, precisionCap);
	}

	
	@Test
	public void test003_OrthoCasMehl () throws NavException {
		
		DataOrthoDromieCapDistanceVertex infos = ca.capOrthodromique(GreenWitch, MelhPointB);
		assertEquals(infos._distance.distanceInKm(), 11630.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 26.5, precisionCap);

		// meme latitude
		infos = ca.capOrthodromique(MelhMemeLatitudePointA, MelhMemeLatitudePointB);
		assertEquals(infos._distance.distanceInKm(), 5013.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 290.0, precisionCap);

		// meme longitude
		infos = ca.capOrthodromique(MelhMemeLongitudePointA, MelhMemeLongitudePointB);
		assertEquals(infos._distance.distanceInKm(), 15261.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 180.0, precisionCap);
	}

	
	
	@Test
	public void test004_OrthoCasWikipedia () throws NavException {
		DataOrthoDromieCapDistanceVertex infos = ca.capOrthodromique (Paris, NewYork);

		assertEquals(infos._distance.distanceInKm(), 5844.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 291.79, precisionCap);
	}

	
	@Test
	public void test005_OrthoDocPasserellePage7 () throws NavException {
		// ------------------------------
		// cas 1
		// ------------------------------
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("38°30.0 N"),
				LongitudeFactory.fromString("142°00.00 E"));
		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("37°48 N"),
				LongitudeFactory.fromString("122°30 W"));
		Cap cap = CapFactory.fromDegre(56);
		Distance distance = DistanceFactory.fromMn(4272);
		PointGeographique vertex = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("49°26.5 N"),
				LongitudeFactory.fromString("170°54.3 W"));
		
		Cap capLoxo = CapFactory.fromDegre(56);
		Distance distanceloxo = DistanceFactory.fromMn(4514);

		DataOrthoDromieCapDistanceVertex infos = ca.capOrthodromique(pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));
		assertTrue(infos._vertex.equals(vertex, precisionCap));

		DataLoxodromieCapDistance infos2 = ca.capLoxodromique (pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));
	}
}
