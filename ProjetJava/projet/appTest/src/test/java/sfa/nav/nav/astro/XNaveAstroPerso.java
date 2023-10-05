package sfa.nav.nav.astro;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.DroiteDeHauteur;
import sfa.nav.astro.calculs.DroiteDeHauteur.DroiteHauteurPositionnee;
import sfa.nav.astro.calculs.DroiteDeHauteur.eSensIntercept;
import sfa.nav.astro.calculs.Ephemerides;
import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.CorrectionDeVisee.eTypeVisee;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.AngleOriente;
import sfa.nav.model.AngleOrienteFactory;
import sfa.nav.model.DeclinaisonFactory;
import sfa.nav.model.Latitude;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.Longitude;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.NavDateHeureFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.VitesseAngulaireFactory;


public class XNaveAstroPerso extends ANavAstroMotherTestClass {
	private static final Logger logger = LoggerFactory.getLogger(XNaveAstroPerso.class);
	
	// ------------------------------------------------------------------------------------
	// Init zone
	// ------------------------------------------------------------------------------------
	@BeforeClass
	public static void initOnlyOnce() throws NavException {
	}

	@AfterClass
	public static void cleanOnlyOnce() {
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
	public void test002_Exo1_ViseeMaison() throws NavException {
		DroiteDeHauteur dh = new DroiteDeHauteur();
		Latitude lat = LatitudeFactory.fromString("48.875 N");
		Longitude lon = LongitudeFactory.fromString("2.094 W");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, lon); 

		Angle HauteurInstruentale_Hi = AngleFactory.fromDegre(AngleFactory.fromString("94°52").asDegre() / 2.0);
		NavDateHeure heureObservation = NavDateHeureFactory.fromString("03/09/2023 11:05 GMT");
		double hauteurOeil = 0.0;
		AngleOriente sextan_exentricite = AngleOrienteFactory.fromString("0°0");
		AngleOriente sextan_collimasson = AngleOrienteFactory.fromString("0°00");
		ErreurSextan errSextan = new ErreurSextan(sextan_collimasson, sextan_exentricite);
		
		Ephemerides ephe = new Ephemerides(
				AngleFactory.fromString("345°08.2"), DeclinaisonFactory.fromString("07°33.2 N"), 0, NavDateHeureFactory.fromString("03/9/2023 11:00:00 UTC"), 
				AngleFactory.fromString("360°08.4"), DeclinaisonFactory.fromString("07°32.3 N"), 0, NavDateHeureFactory.fromString("03/9/2023 12:00:00 UTC") 
				);
		
		
		DroiteHauteurPositionnee res = dh.droitedeHauteurSoleil ( positionEstimee, 
				 HauteurInstruentale_Hi, 
				 heureObservation,
				 hauteurOeil,
				 eTypeVisee.soleilBordInf,
				 errSextan,
				 ephe);
		
		assertEquals(res.getIntercept().distanceInMilleNautique(), -16.6466, EPISILON_DISTANCE);
		assertEquals(res.getSens(), eSensIntercept.opposePg);
		assertEquals(res.getZ().asDegre(), 274.02628, EPISILON_ANGLE);
	}
}
