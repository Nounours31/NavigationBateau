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
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.DataLoxodromieCapDistance;

public class CoursDeNavigation_DocAncien_page6_loxodromie {
	private static final Logger logger = LoggerFactory.getLogger(CoursDeNavigation_DocAncien_page6_loxodromie.class);

	private static double precisionCap = 2.0 * Constantes.DemiDegreEnDegre; // 1 degre
	private static double precisionDistanceEnMn = 1; // mlongue diatnce

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
	public void test001_page6_Cas1_LongueDistance() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("27°30 N"),
				LongitudeFactory.fromString("79°30 W"));

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("39°00 N"),
				LongitudeFactory.fromString("30°00 W"));

		Cap capOrtho = CapFactory.fromDegre(74.5);
		Distance distanceOrtho = DistanceFactory.fromMn(2572.0);

		DataLoxodromieCapDistance infosOrtho = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
	}

	@Test
	public void test001_page6_Cas2_LongueDistance() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("11°45 N"),
				LongitudeFactory.fromString("49°26 W"));

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("19°30 S"),
				LongitudeFactory.fromString("10°21 W"));

		Cap capOrtho = CapFactory.fromDegre(129);
		Distance distanceOrtho = DistanceFactory.fromMn(2977);

		DataLoxodromieCapDistance infosOrtho = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
	}

	@Test
	public void test001_page6_Cas3_LongueDistance() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("52°48 S"),
				LongitudeFactory.fromString("10°37 W"));

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("22°32 S"),
				LongitudeFactory.fromString("20°36 E"));

		Cap capOrtho = CapFactory.fromDegre(38.5);
		Distance distanceOrtho = DistanceFactory.fromMn(2321.0);

		DataLoxodromieCapDistance infosOrtho = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
	}

	@Test
	public void test001_page6_Cas4_LongueDistance() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("58°10 N"),
				LongitudeFactory.fromString("158°25 W"));

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("35°22 N"),
				LongitudeFactory.fromString("163°57 E"));

		Cap capOrtho = CapFactory.fromDegre(228);
		Distance distanceOrtho = DistanceFactory.fromMn(2040);

		DataLoxodromieCapDistance infosOrtho = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
	}

	@Test
	public void test001_page6_Cas5_LongueDistance() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("5°45 S"),
				LongitudeFactory.fromString("35°11 E"));

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("48°40 N"),
				LongitudeFactory.fromString("5°30 E"));

		Cap capOrtho = CapFactory.fromDegre(334.5);
		Distance distanceOrtho = DistanceFactory.fromMn(3626.0);

		DataLoxodromieCapDistance infosOrtho = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
	}
}
