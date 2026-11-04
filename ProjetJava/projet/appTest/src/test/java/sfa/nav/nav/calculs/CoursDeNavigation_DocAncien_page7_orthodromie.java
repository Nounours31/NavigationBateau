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
import sfa.nav.model.Vitesse;
import sfa.nav.model.VitesseFactory;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.model.tools.DataOrthoDromie;
import sfa.nav.odt.tools.ToolsForAllOdts;

public class CoursDeNavigation_DocAncien_page7_orthodromie {
	private static final Logger logger = LoggerFactory.getLogger(CoursDeNavigation_DocAncien_page7_orthodromie.class);

	private static double precisionCap = 2.0 * Constantes.DemiDegreEnDegre; // 1 degre
	private static double precisionDistanceEnMn = 1; // longue distance

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
	public void page7_Cas1_LongueDistance() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("38°30 N"),
				LongitudeFactory.fromString("142°00 E"));

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("37°48 N"),
				LongitudeFactory.fromString("122°30 W"));

		Vitesse vitesseRoutefondInitiale = VitesseFactory.fromNoeud(16);
		double dureeDeNavEnHeureDecimale = 23.0;

		// reference ortho
		Cap capOrtho = CapFactory.fromDegre(56);
		Distance distanceOrtho = DistanceFactory.fromMn(4274);
		Cap routeInitiale = CapFactory.fromDegre(58);
		PointGeographique vertex = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("49°26.5 N"),
				LongitudeFactory.fromString("170°54.3 W"));

		// check ortho
		DataOrthoDromie infosOrtho = ca.capOrthodromique(pgDepart, pgArrivee, vitesseRoutefondInitiale,
				dureeDeNavEnHeureDecimale);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
		assertEquals(
				ToolsForAllOdts.distanceEntreDeuxPointsProches(infosOrtho.vertex(), vertex).distanceInMilleNautique(),
				0.0, precisionDistanceEnMn);
		assertEquals(infosOrtho.Rfinitiale().asDegre(), routeInitiale.asDegre(), precisionCap);

		// reference loxo + check loxo
		Cap capLoxo = CapFactory.fromDegre(90.5);
		Distance distanceLoxo = DistanceFactory.fromMn(4509);
		DataLoxodromieCapDistance infosLoxo = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosLoxo.distance().distanceInMilleNautique(), distanceLoxo.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosLoxo.cap().asDegre(), capLoxo.asDegre(), precisionCap);
	}

	@Test
	public void page7_Cas2_LongueDistance() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("5°22 N"),
				LongitudeFactory.fromString("82°25 W"));

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("38°30 S"),
				LongitudeFactory.fromString("179°08 W"));

		Vitesse vitesseRoutefondInitiale = VitesseFactory.fromNoeud(13);
		double dureeDeNavEnHeureDecimale = 12.0;

		Cap capOrtho = CapFactory.fromDegre(232);
		Distance distanceOrtho = DistanceFactory.fromMn(5919);
		Cap routeInitiale = CapFactory.fromDegre(231.5);
		PointGeographique vertex = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("38°30 S"),
				LongitudeFactory.fromString("179°11.9 W"));

		// check ortho
		DataOrthoDromie infosOrtho = ca.capOrthodromique(pgDepart, pgArrivee, vitesseRoutefondInitiale,
				dureeDeNavEnHeureDecimale);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
		assertEquals(
				ToolsForAllOdts.distanceEntreDeuxPointsProches(infosOrtho.vertex(), vertex).distanceInMilleNautique(),
				0.0, precisionDistanceEnMn);
		assertEquals(infosOrtho.Rfinitiale().asDegre(), routeInitiale.asDegre(), precisionCap);

		Cap capLoxo = CapFactory.fromDegre(244);
		Distance distanceLoxo = DistanceFactory.fromMn(6010);
		DataLoxodromieCapDistance infosLoxo = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosLoxo.distance().distanceInMilleNautique(), distanceLoxo.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosLoxo.cap().asDegre(), capLoxo.asDegre(), precisionCap);
	}

	@Test
	public void page7_Cas3_LongueDistance() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("34°05 S"),
				LongitudeFactory.fromString("25°37 E"));

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("31°58 S"),
				LongitudeFactory.fromString("115°24 E"));

		Vitesse vitesseRoutefondInitiale = VitesseFactory.fromNoeud(15.4);
		double dureeDeNavEnHeureDecimale = 20.0;

		Cap capOrtho = CapFactory.fromDegre(117);
		Distance distanceOrtho = DistanceFactory.fromMn(4357);
		Cap routeInitiale = CapFactory.fromDegre(115.5);
		PointGeographique vertex = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("42°34.5 S"),
				LongitudeFactory.fromString("68°11.1 E"));

		// check ortho
		DataOrthoDromie infosOrtho = ca.capOrthodromique(pgDepart, pgArrivee, vitesseRoutefondInitiale,
				dureeDeNavEnHeureDecimale);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
		assertEquals(
				ToolsForAllOdts.distanceEntreDeuxPointsProches(infosOrtho.vertex(), vertex).distanceInMilleNautique(),
				0.0, precisionDistanceEnMn);
		assertEquals(infosOrtho.Rfinitiale().asDegre(), routeInitiale.asDegre(), precisionCap);

		Cap capLoxo = CapFactory.fromDegre(88.4);
		Distance distanceLoxo = DistanceFactory.fromMn(4521);
		DataLoxodromieCapDistance infosLoxo = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosLoxo.distance().distanceInMilleNautique(), distanceLoxo.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosLoxo.cap().asDegre(), capLoxo.asDegre(), precisionCap);
	}

	@Test
	public void page7_Cas4_LongueDistance() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("53°01 N"),
				LongitudeFactory.fromString("158°39 E"));

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("33°02 S"),
				LongitudeFactory.fromString("71°38 W"));

		Vitesse vitesseRoutefondInitiale = VitesseFactory.fromNoeud(16.5);
		double dureeDeNavEnHeureDecimale = 24.0;

		Cap capOrtho = CapFactory.fromDegre(81);
		Distance distanceOrtho = DistanceFactory.fromMn(8361);
		Cap routeInitiale = CapFactory.fromDegre(85.5);
		PointGeographique vertex = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("53°31.5 N"),
				LongitudeFactory.fromString("169°38.1 E"));

		// check ortho
		DataOrthoDromie infosOrtho = ca.capOrthodromique(pgDepart, pgArrivee, vitesseRoutefondInitiale,
				dureeDeNavEnHeureDecimale);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
		assertEquals(
				ToolsForAllOdts.distanceEntreDeuxPointsProches(infosOrtho.vertex(), vertex).distanceInMilleNautique(),
				0.0, precisionDistanceEnMn);
		assertEquals(infosOrtho.Rfinitiale().asDegre(), routeInitiale.asDegre(), precisionCap);

		Cap capLoxo = CapFactory.fromDegre(127);
		Distance distanceLoxo = DistanceFactory.fromMn(8582);
		DataLoxodromieCapDistance infosLoxo = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosLoxo.distance().distanceInMilleNautique(), distanceLoxo.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosLoxo.cap().asDegre(), capLoxo.asDegre(), precisionCap);
	}

	@Test
	public void page7_Cas5_LongueDistance() throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("0°27 S"),
				LongitudeFactory.fromString("48°11 W"));

		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("51°46 N"),
				LongitudeFactory.fromString("3°56 W"));

		Vitesse vitesseRoutefondInitiale = VitesseFactory.fromNoeud(23);
		double dureeDeNavEnHeureDecimale = 24.0;

		Cap capOrtho = CapFactory.fromDegre(28.5);
		Distance distanceOrtho = DistanceFactory.fromMn(3847);
		Cap routeInitiale = CapFactory.fromDegre(28.5);
		PointGeographique vertex = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromString("61°18.4 N"),
				LongitudeFactory.fromString("42°03.8 E"));

		// check ortho
		DataOrthoDromie infosOrtho = ca.capOrthodromique(pgDepart, pgArrivee, vitesseRoutefondInitiale,
				dureeDeNavEnHeureDecimale);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
		assertEquals(
				ToolsForAllOdts.distanceEntreDeuxPointsProches(infosOrtho.vertex(), vertex).distanceInMilleNautique(),
				0.0, precisionDistanceEnMn);
		assertEquals(infosOrtho.Rfinitiale().asDegre(), routeInitiale.asDegre(), precisionCap);

		Cap capLoxo = CapFactory.fromDegre(36);
		Distance distanceLoxo = DistanceFactory.fromMn(3869);
		DataLoxodromieCapDistance infosLoxo = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosLoxo.distance().distanceInMilleNautique(), distanceLoxo.distanceInMilleNautique(),
				precisionDistanceEnMn);
		assertEquals(infosLoxo.cap().asDegre(), capLoxo.asDegre(), precisionCap);
	}
}
