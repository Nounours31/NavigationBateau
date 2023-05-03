package sfa.nav.lib.mylittlemath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.lib.model.Latitude;
import sfa.nav.lib.model.Longitude;
import sfa.nav.lib.model.PointGeographique;
import sfa.nav.lib.tools.Constantes;
import sfa.nav.lib.tools.HandlerOnCapDistance;
import sfa.nav.lib.tools.NavException;


public class CalculsAngulairesTest {
    private static final Logger logger = LoggerFactory.getLogger(CalculsAngulairesTest.class);

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
		
		Paris = new PointGeographique(Latitude.fromDegre(48.85), Longitude.fromDegre(2.35));
 		NewYork = new PointGeographique(Latitude.fromDegre(40.705628), Longitude.fromDegre(-74.013519));
 		GreenWitch = new PointGeographique(Latitude.fromDegre(0.0), Longitude.fromDegre(0.0));
 		MelhPointB = new PointGeographique(Latitude.fromDegre(60.0), Longitude.fromDegre(120.0));
 		MelhMemeLatitudePointA = new PointGeographique(Latitude.fromDegre(40.0), Longitude.fromDegre(-140.0));
 		MelhMemeLatitudePointB = new PointGeographique(Latitude.fromDegre(40.0), Longitude.fromDegre(160.0));
 		MelhMemeLongitudePointA = new PointGeographique(Latitude.fromDegre(78.0), Longitude.fromDegre(107.0));
 		MelhMemeLongitudePointB = new PointGeographique(Latitude.fromDegre(-59.1), Longitude.fromDegre(107.0));
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
		Latitude latitudeA = Latitude.fromDegre(47.0), latitudeB = Latitude.fromDegre(10.0);
		Longitude longitudeA = Longitude.fromDegre(2.0), longitudeB = Longitude.fromDegre(106.0);
		
		PointGeographique A = new PointGeographique(latitudeA, longitudeA);
		PointGeographique B = new PointGeographique(latitudeB, longitudeB);
		
		HandlerOnCapDistance infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(A, B);
		
		assertEquals(infos._distance.distanceInKm(), 10709.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 112.5, precisionCap);
	}

	@Test
	public void test002_OrthoCasSimple () throws NavException {
		Latitude latitudeA = Latitude.fromDegre(47.0), latitudeB = Latitude.fromDegre(10.0);
		Longitude longitudeA = Longitude.fromDegre(2.0), longitudeB = Longitude.fromDegre(106.0);
		
		PointGeographique A = new PointGeographique(latitudeA, longitudeA);
		PointGeographique B = new PointGeographique(latitudeB, longitudeB);
		
		HandlerOnCapDistance infos = ca.getOrthoDromieCapDistanceEntreDeuxPoints(A, B);
		
		assertEquals(infos._distance.distanceInKm(), 10244.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 73.0, precisionCap);
	}

	
	@Test
	public void test003_LoxoCasMehl () throws NavException {
		HandlerOnCapDistance infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(GreenWitch, MelhPointB);
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
	public void test003_OrthoCasMehl () throws NavException {
		
		HandlerOnCapDistance infos = ca.getOrthoDromieCapDistanceEntreDeuxPoints(GreenWitch, MelhPointB);
		assertEquals(infos._distance.distanceInKm(), 11630.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 26.5, precisionCap);

		// meme latitude
		infos = ca.getOrthoDromieCapDistanceEntreDeuxPoints(MelhMemeLatitudePointA, MelhMemeLatitudePointB);
		assertEquals(infos._distance.distanceInKm(), 5013.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 290.0, precisionCap);

		// meme longitude
		infos = ca.getOrthoDromieCapDistanceEntreDeuxPoints(MelhMemeLongitudePointA, MelhMemeLongitudePointB);
		assertEquals(infos._distance.distanceInKm(), 15261.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 180.0, precisionCap);
	}

	
	
	@Test
	public void test004_LoxoCasWikipedia () throws NavException {
		HandlerOnCapDistance infos = ca.getLoxoDromieCapDistanceEntreDeuxPoints(Paris, NewYork);
		
		assertEquals(infos._distance.distanceInKm(), 6086.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 261.0, precisionCap);
	}

	@Test
	public void test004_OrthoCasWikipedia () throws NavException {
		HandlerOnCapDistance infos = ca.getOrthoDromieCapDistanceEntreDeuxPoints(Paris, NewYork);

		assertEquals(infos._distance.distanceInKm(), 5844.0, precisionDiatnce); 
		assertEquals(infos._cap.asDegre(), 291.79, precisionCap);
	}
}
