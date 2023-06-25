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


public class CoursDeNavigation_DocAncien_page4_loxodromie300Milles {
    private static final Logger logger = LoggerFactory.getLogger(CoursDeNavigation_DocAncien_page4_loxodromie300Milles.class);

    private static double precisionCap = 2.0 * Constantes.DemiDegreEnDegre; // 1 degre 
    private static double precisionDistanceEnMn = 0.1; // 200 m
    
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
	public void test001_page4_Cas1_Loxodromie_Distanceinferieur300milles () throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("35°54.2 N"),
				LongitudeFactory.fromString("14°30.5 E"));
		
		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("38°11.3 N"),
				LongitudeFactory.fromString("15°34.7 E"));

		Distance distanceOrtho = DistanceFactory.fromMn(146.61);
		Cap capOrtho = CapFactory.fromDegre(20.5);

		DataLoxodromieCapDistance infosOrtho = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),  precisionDistanceEnMn); 
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
	}
	
	@Test
	public void test002_page4_Cas2_Loxodromie_Distanceinferieur300milles () throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("50°53.7 N"),
				LongitudeFactory.fromString("1°23.5 W"));
		
		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("51°03.8 N"),
				LongitudeFactory.fromString("2°22.7 E"));

		Distance distanceOrtho = DistanceFactory.fromMn(143);
		Cap capOrtho = CapFactory.fromDegre(86);

		DataLoxodromieCapDistance infosOrtho = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),  precisionDistanceEnMn); 
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
	}

	@Test
	public void test003_page4_Cas3_Loxodromie_Distanceinferieur300milles () throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("27°50.0 S"),
				LongitudeFactory.fromString("178°30 E"));
		
		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("29°17 S"),
				LongitudeFactory.fromString("179°05.7 W"));

		Distance distanceOrtho = DistanceFactory.fromMn(154);
		Cap capOrtho = CapFactory.fromDegre(124.5);

		DataLoxodromieCapDistance infosOrtho = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),  precisionDistanceEnMn); 
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
	}
	
	@Test
	public void test004_page4_Cas4_Loxodromie_Distanceinferieur300milles () throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("37°29.8 S"),
				LongitudeFactory.fromString("9°12 E"));
		
		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("37°29.1 S"),
				LongitudeFactory.fromString("7°36.5 E"));

		Distance distanceOrtho = DistanceFactory.fromMn(75.9);
		Cap capOrtho = CapFactory.fromDegre(270.5);

		DataLoxodromieCapDistance infosOrtho = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),  precisionDistanceEnMn); 
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
	}

	@Test
	public void test005_page4_Cas5_Loxodromie_Distanceinferieur300milles () throws NavException {
		PointGeographique pgDepart = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("1°06.7 N"),
				LongitudeFactory.fromString("15°36 W"));
		
		PointGeographique pgArrivee = PointGeographiqueFactory.fromLatLong (
				LatitudeFactory.fromString("0°30 S"),
				LongitudeFactory.fromString("13°20 W"));

		Distance distanceOrtho = DistanceFactory.fromMn(167.1);
		Cap capOrtho = CapFactory.fromDegre(125);

		DataLoxodromieCapDistance infosOrtho = ca.capLoxodromique(pgDepart, pgArrivee);
		assertEquals(infosOrtho.distance().distanceInMilleNautique(), distanceOrtho.distanceInMilleNautique(),  precisionDistanceEnMn); 
		assertEquals(infosOrtho.cap().asDegre(), capOrtho.asDegre(), precisionCap);
	}

}
