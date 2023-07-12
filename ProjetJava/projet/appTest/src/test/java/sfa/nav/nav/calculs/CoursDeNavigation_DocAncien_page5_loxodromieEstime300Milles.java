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
import sfa.nav.model.Cap;
import sfa.nav.model.CapFactory;
import sfa.nav.model.Distance;
import sfa.nav.model.DistanceFactory;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.tools.DataLoxodromieCapDistance;

public class CoursDeNavigation_DocAncien_page5_loxodromieEstime300Milles {
	private static final Logger logger = LoggerFactory
			.getLogger(CoursDeNavigation_DocAncien_page5_loxodromieEstime300Milles.class);

	private static double precisionDistanceEnMn = 0.1; // 200 m

	private static CalculsDeNavigation ca = null;

	// ------------------------------------------------------------------------------------
	// Init zone
	// ------------------------------------------------------------------------------------

	@BeforeClass
	public static void initOnlyOnce() throws NavException {
		logger.debug("Start ODT");
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
	public void test001_EstCeQueCaMarche() {
		int sum = 6;
		assertEquals(sum, 6);

		assertNotNull(ca);
	}

	@Test
	public void test001_page5_Cas1_EstimeLoxodromie_Distanceinferieur300milles() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("39°51 S"),
				LongitudeFactory.fromString("129°13 W"));

		Distance distanceOrtho = DistanceFactory.fromMn(150.3);
		Cap capOrtho = CapFactory.fromDegre(338);
		PointGeographique estimeLoxo = ca.estimeLoxodromique(pgDepart, capOrtho, distanceOrtho);

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("37°31.6 S"),
				LongitudeFactory.fromString("130°25.1 W"));
		DataLoxodromieCapDistance delta = ca.capLoxodromique(estimeLoxo, pgArrivee);

		assertEquals(delta.distance().distanceInMilleNautique(), 0.0, precisionDistanceEnMn);
	}

	@Test
	public void test002_page5_Cas2_EstimeLoxodromie_Distanceinferieur300milles() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("52°28.3 N"),
				LongitudeFactory.fromString("2°14.6 W"));

		Distance distanceOrtho = DistanceFactory.fromMn(21.5);
		Cap capOrtho = CapFactory.fromDegre(65);
		PointGeographique estimeLoxo = ca.estimeLoxodromique(pgDepart, capOrtho, distanceOrtho);

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("52°37.4 N"),
				LongitudeFactory.fromString("1°42.6 W"));
		DataLoxodromieCapDistance delta = ca.capLoxodromique(estimeLoxo, pgArrivee);

		assertEquals(delta.distance().distanceInMilleNautique(), 0.0, precisionDistanceEnMn);
	}

	@Test
	public void test003_page3_Cas2_EstimeLoxodromie_Distanceinferieur300milles() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("37°42.5 S"),
				LongitudeFactory.fromString("178°48.7 E"));

		Distance distanceOrtho = DistanceFactory.fromMn(244);
		Cap capOrtho = CapFactory.fromDegre(93.5);
		PointGeographique estimeLoxo = ca.estimeLoxodromique(pgDepart, capOrtho, distanceOrtho);

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("37°57.4 S"),
				LongitudeFactory.fromString("176°02.9 W"));
		DataLoxodromieCapDistance delta = ca.capLoxodromique(estimeLoxo, pgArrivee);

		assertEquals(delta.distance().distanceInMilleNautique(), 0.0, precisionDistanceEnMn);
	}

	@Test
	public void test004_page4_Cas2_EstimeLoxodromie_Distanceinferieur300milles() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("62°29.3 N"),
				LongitudeFactory.fromString("1°57 E"));

		Distance distanceOrtho = DistanceFactory.fromMn(168.7);
		Cap capOrtho = CapFactory.fromDegre(221);
		PointGeographique estimeLoxo = ca.estimeLoxodromique(pgDepart, capOrtho, distanceOrtho);

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("60°21.7 N"),
				LongitudeFactory.fromString("1°54.4 W"));
		DataLoxodromieCapDistance delta = ca.capLoxodromique(estimeLoxo, pgArrivee);

		assertEquals(delta.distance().distanceInMilleNautique(), 0.0, precisionDistanceEnMn);
	}

	@Test
	public void test005_page5_Cas5_EstimeLoxodromie_Distanceinferieur300milles() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("29°50 N"),
				LongitudeFactory.fromString("164°16.5 E"));

		Distance distanceOrtho = DistanceFactory.fromMn(74.2);
		Cap capOrtho = CapFactory.fromDegre(265);
		PointGeographique estimeLoxo = ca.estimeLoxodromique(pgDepart, capOrtho, distanceOrtho);

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("29°43.5 N"),
				LongitudeFactory.fromString("162°51.3 E"));
		DataLoxodromieCapDistance delta = ca.capLoxodromique(estimeLoxo, pgArrivee);

		assertEquals(delta.distance().distanceInMilleNautique(), 0.0, precisionDistanceEnMn);
	}

	@Test
	public void test005_page5_Cas6_KO_EstimeLoxodromie_Distanceinferieur300milles() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("29°50 N"),
				LongitudeFactory.fromString("164°16.5 E"));

		Distance distanceOrtho = DistanceFactory.fromMn(400);
		Cap capOrtho = CapFactory.fromDegre(265);
		PointGeographique estimeLoxo = ca.estimeLoxodromique(pgDepart, capOrtho, distanceOrtho);

		assertEquals(estimeLoxo, null);
	}
}
