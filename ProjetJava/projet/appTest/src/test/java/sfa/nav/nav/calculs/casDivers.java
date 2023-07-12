package sfa.nav.nav.calculs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Latitude;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.Longitude;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.model.tools.DataOrthoDromie;

public class casDivers {
	private static final Logger logger = LoggerFactory.getLogger(casDivers.class);

	private static double precisionCap = Constantes.DemiDegreEnDegre;
	private static double precisionDistance = 100.0; // 100 m ... Constantes.milleMarinEnMetre;

	private static CalculsDeNavigation ca = null;

	// ------------------------------------------------------------------------------------
	// Init zone
	// ------------------------------------------------------------------------------------
	// Paris :-)

	@BeforeClass
	public static void initOnlyOnce() throws NavException {
		ca = new CalculsDeNavigation();
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
	public void test002_LoxoCasSimple() throws NavException {
		Latitude latitudeA = LatitudeFactory.fromDegre(47.0), latitudeB = LatitudeFactory.fromDegre(10.0);
		Longitude longitudeA = LongitudeFactory.fromDegre(2.0), longitudeB = LongitudeFactory.fromDegre(106.0);

		PointGeographique A = PointGeographiqueFactory.fromLatLong(latitudeA, longitudeA);
		PointGeographique B = PointGeographiqueFactory.fromLatLong(latitudeB, longitudeB);

		DataLoxodromieCapDistance infos = ca.capLoxodromique(A, B);

		assertEquals(infos.distance().distanceInMilleNautique(), 5797.0, precisionDistance);
		assertEquals(infos.cap().asDegre(), 112.5, precisionCap);
	}

	@Test
	public void test002_orthoCasSimple() throws NavException {
		Latitude latitudeA = LatitudeFactory.fromDegre(47.0), latitudeB = LatitudeFactory.fromDegre(10.0);
		Longitude longitudeA = LongitudeFactory.fromDegre(2.0), longitudeB = LongitudeFactory.fromDegre(106.0);

		PointGeographique A = PointGeographiqueFactory.fromLatLong(latitudeA, longitudeA);
		PointGeographique B = PointGeographiqueFactory.fromLatLong(latitudeB, longitudeB);

		DataOrthoDromie infos = ca.capOrthodromique(A, B);

		assertEquals(infos.distance().distanceInMilleNautique(), 5522.7, precisionDistance);
		assertEquals(infos.cap().asDegre(), 73, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), 49.3, precisionCap);
	}

	@Test
	public void test003_LoxoCasSimple() throws NavException {
		PointGeographique A = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("47 N"),
				LongitudeFactory.fromString("2 E"));
		PointGeographique B = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("10 N"),
				LongitudeFactory.fromString("106 E"));

		DataLoxodromieCapDistance infos = ca.capLoxodromique(A, B);

		assertEquals(infos.distance().distanceInMilleNautique(), 5797.0, precisionDistance);
		assertEquals(infos.cap().asDegre(), 112.5, precisionCap);
	}

	@Test
	public void test0033_LoxoCasMehl() throws NavException {
		DataLoxodromieCapDistance infos = ca.capLoxodromique(zaListePositionsPredefinies.GreenWitch,
				zaListePositionsPredefinies.MelhPointB);
		assertEquals(infos.distance().distanceInMilleNautique(), 6784.0, precisionDistance);
		assertEquals(infos.cap().asDegre(), 58.0, precisionCap);

		// meme latitude
		infos = ca.capLoxodromique(zaListePositionsPredefinies.MelhMemeLatitudePointA,
				zaListePositionsPredefinies.MelhMemeLatitudePointB);
		assertEquals(infos.distance().distanceInMilleNautique(), 2758.0, precisionDistance);
		assertEquals(infos.cap().asDegre(), 270.0, precisionCap);

		// meme longitude
		infos = ca.capLoxodromique(zaListePositionsPredefinies.MelhMemeLongitudePointA,
				zaListePositionsPredefinies.MelhMemeLongitudePointB);
		assertEquals(infos.distance().distanceInMilleNautique(), 8221.0, precisionDistance);
		assertEquals(infos.cap().asDegre(), 180.0, precisionCap);
	}

	@Test
	public void test0033_OrthoCasMehl() throws NavException {
		DataOrthoDromie infos = ca.capOrthodromique(zaListePositionsPredefinies.GreenWitch,
				zaListePositionsPredefinies.MelhPointB);
		assertEquals(infos.distance().distanceInMilleNautique(), 6269.0, precisionDistance);
		assertEquals(infos.cap().asDegre(), 27.0, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), 63.4, precisionCap);

		// meme latitude
		infos = ca.capOrthodromique(zaListePositionsPredefinies.MelhMemeLatitudePointA,
				zaListePositionsPredefinies.MelhMemeLatitudePointB);
		assertEquals(infos.distance().distanceInMilleNautique(), 2702.5, precisionDistance);
		assertEquals(infos.cap().asDegre(), 290.4, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), 44.1, precisionCap);

		// meme longitude
		infos = ca.capOrthodromique(zaListePositionsPredefinies.MelhMemeLongitudePointA,
				zaListePositionsPredefinies.MelhMemeLongitudePointB);
		assertEquals(infos.distance().distanceInMilleNautique(), 8221.0, precisionDistance);
		assertEquals(infos.cap().asDegre(), 180.0, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), 9.44, precisionCap);
	}

	@Test
	public void test004_LoxoCasWikipedia() throws NavException {
		DataLoxodromieCapDistance infos = ca.capLoxodromique(zaListePositionsPredefinies.Paris,
				zaListePositionsPredefinies.NewYork);

		assertEquals(infos.distance().distanceInMilleNautique(), 3291.0, precisionDistance);
		assertEquals(infos.cap().asDegre(), 261.5, precisionCap);
	}

	@Test
	public void test004_OrthoCasWikipedia() throws NavException {
		DataOrthoDromie infos = ca.capOrthodromique(zaListePositionsPredefinies.Paris,
				zaListePositionsPredefinies.NewYork);

		assertEquals(infos.distance().distanceInMilleNautique(), 3150.0, precisionDistance);
		assertEquals(infos.cap().asDegre(), 291.8, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), 52.3, precisionCap);
	}

	@Test
	public void test001_EstCeQueCaMarche() {
		int sum = 5; // valeur a controler
		assertEquals(sum, 5);
		assertNotNull(ca);
	}
}
