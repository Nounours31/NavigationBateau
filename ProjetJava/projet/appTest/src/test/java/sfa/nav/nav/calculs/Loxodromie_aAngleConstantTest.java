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
import sfa.nav.nav.calculs.CalculsAngulaires;


public class Loxodromie_aAngleConstantTest {
    private static final Logger logger = LoggerFactory.getLogger(Loxodromie_aAngleConstantTest.class);

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
	public void test002_LoxoCasSimple () throws NavException {
		Latitude latitudeA = LatitudeFactory.fromDegre(47.0), latitudeB = LatitudeFactory.fromDegre(10.0);
		Longitude longitudeA = LongitudeFactory.fromDegre(2.0), longitudeB = LongitudeFactory.fromDegre(106.0);
		
		PointGeographique A = PointGeographiqueFactory.fromLatLong(latitudeA, longitudeA);
		PointGeographique B = PointGeographiqueFactory.fromLatLong(latitudeB, longitudeB);
		
		DataLoxodromieCapDistance infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(A, B);
		
		assertEquals(infos._distance.distanceInKm(), 10709.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 112.5, precisionCap);
	}


	
	@Test
	public void test003_LoxoCasMehl () throws NavException {
		DataLoxodromieCapDistance infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(GreenWitch, MelhPointB);
		assertEquals(infos._distance.distanceInKm(), 12547.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 58.0, precisionCap);
		
		// meme latitude
		infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(MelhMemeLatitudePointA, MelhMemeLatitudePointB);
		assertEquals(infos._distance.distanceInKm(), 5116.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 90.0, precisionCap);

		// meme longitude
		infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(MelhMemeLongitudePointA, MelhMemeLongitudePointB);
		assertEquals(infos._distance.distanceInKm(), 15524.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 180.0, precisionCap);
	}


	
	
	@Test
	public void test004_LoxoCasWikipedia () throws NavException {
		DataLoxodromieCapDistance infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(Paris, NewYork);
		
		assertEquals(infos._distance.distanceInKm(), 6086.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 261.0, precisionCap);
	}

	@Test
	public void test005_LoxoDocPasserellePage4CourteDistance () throws NavException {
		// ------------------------------
		// cas 1
		// ------------------------------
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("35°54.2 N"),
				LongitudeFactory.fromString("14°30.5 E"));
		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("38°11.3 N"),
				LongitudeFactory.fromString("15°34.7 E"));
		Cap cap = CapFactory.fromDegre(20.5);
		Distance distance = DistanceFactory.fromMn(146.4);
		
		DataLoxodromieCapDistance infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));

		// ------------------------------
		// cas 2
		// ------------------------------
		pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("50°53.7 N"),
				LongitudeFactory.fromString("1°23.5 W"));
		pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("51°03.8 N"),
				LongitudeFactory.fromString("2°22 E"));
		cap = CapFactory.fromDegre(86);
		distance = DistanceFactory.fromMn(142.3);
		
		infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));

		// ------------------------------
		// cas 3
		// ------------------------------
		pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("27°50 S"),
				LongitudeFactory.fromString("178°30 E"));
		pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("29°17 S"),
				LongitudeFactory.fromString("179°05 W"));
		cap = CapFactory.fromDegre(124.5);
		distance = DistanceFactory.fromMn(154.2);
		
		infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));

		// ------------------------------
		// cas 4
		// ------------------------------
		pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("37°29.8 S"),
				LongitudeFactory.fromString("9°12 E"));
		pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("37°29.1 S"),
				LongitudeFactory.fromString("7°36.5 E"));
		cap = CapFactory.fromDegre(270.5);
		distance = DistanceFactory.fromMn(75.8);
		
		infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));

		// ------------------------------
		// cas 5
		// ------------------------------
		pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("1°06 N"),
				LongitudeFactory.fromString("15°36 W"));
		pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("0°30 S"),
				LongitudeFactory.fromString("13°20 W"));
		cap = CapFactory.fromDegre(125);
		distance = DistanceFactory.fromMn(166.5);
		
		infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));

	}
	
	
	@Test
	public void test006_LoxoDocPasserellePage6LongueDistance () throws NavException {
		// ------------------------------
		// cas 1
		// ------------------------------
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("27°30.0 N"),
				LongitudeFactory.fromString("79°30.00 W"));
		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("39°00 N"),
				LongitudeFactory.fromString("30°00 W"));
		Cap cap = CapFactory.fromDegre(74.5);
		Distance distance = DistanceFactory.fromMn(2570.0);
		
		DataLoxodromieCapDistance infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));

		// ------------------------------
		// cas 2
		// ------------------------------
		pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("11°45.0 N"),
				LongitudeFactory.fromString("49°26.00 w"));
		pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("19°30 s"),
				LongitudeFactory.fromString("10°21 W"));
		cap = CapFactory.fromDegre(129);
		distance = DistanceFactory.fromMn(2975);
		
		infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));

		// ------------------------------
		// cas 3
		// ------------------------------
		pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("52°48.0 s"),
				LongitudeFactory.fromString("10°37.00 w"));
		pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("22°32 s"),
				LongitudeFactory.fromString("20°36 e"));
		cap = CapFactory.fromDegre(38);
		distance = DistanceFactory.fromMn(2320);
		
		infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));

		// ------------------------------
		// cas 4
		// ------------------------------
		pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("58°10.0 n"),
				LongitudeFactory.fromString("158°25.00 w"));
		pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("35°22 n"),
				LongitudeFactory.fromString("163°57 e"));
		cap = CapFactory.fromDegre(228);
		distance = DistanceFactory.fromMn(2040);
		
		infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));

		// ------------------------------
		// cas 5
		// ------------------------------
		pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("5°45.0 s"),
				LongitudeFactory.fromString("35°11.00 e"));
		pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("48°40 n"),
				LongitudeFactory.fromString("5°30 e"));
		cap = CapFactory.fromDegre(334);
		distance = DistanceFactory.fromMn(3624);
		
		infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(pgDepart, pgArrivee);
		assertTrue(infos._distance.equalsInMn(distance, precisionDiatnce)); 
		assertTrue(infos._cap.equalsInDegre(cap, precisionCap));
	}

	
}
