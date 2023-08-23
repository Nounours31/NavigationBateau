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
import sfa.nav.astro.calculs.CorrectionDeVisee.ErreurSextan;
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


public class SiteNaveAstro {
	private static final Logger logger = LoggerFactory.getLogger(SiteNaveAstro.class);
	
	private static final double EPISILON_ANGLE = 0.001;
	private static final double EPISILON_DISTANCE = 0.001;

	// ------------------------------------------------------------------------------------
	// Init zone
	// ------------------------------------------------------------------------------------
	// Paris :-)

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
	public void test001_PremierCanevasDroiteHauteur() throws NavException {
		DroiteDeHauteur dh = new DroiteDeHauteur();
		Latitude lat = LatitudeFactory.fromString("47°29 N");
		Longitude lon = LongitudeFactory.fromString("2°53 W");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, lon); 
		Angle HauteurInstruentale_Hi = AngleFactory.fromString("22°59"); 
		NavDateHeure heureObservation = NavDateHeureFactory.fromString("04/03/1998 15:24:00 GMT");
		double hauteurOeil = 2;
		AngleOriente sextan_exentricite = AngleOrienteFactory.fromString("0°0");
		AngleOriente sextan_collimasson = AngleOrienteFactory.fromString("-0°03");
		ErreurSextan errSextan = new ErreurSextan(sextan_collimasson, sextan_exentricite);
		
		Ephemerides ephe = new Ephemerides(
				AngleFactory.fromString("177°1.9'"),        VitesseAngulaireFactory.fromDegreParHeure(15.002),		// 15 deg/h
				DeclinaisonFactory.fromString("6°35.9' S"), VitesseAngulaireFactory.fromDegreParHeure(1.0 / 60.0),  // 1 minute/heure
				NavDateHeureFactory.fromString("04/03/1998 03:04:00 GMT"));
		
		
		DroiteHauteurPositionnee res = dh.droitedeHauteur ( positionEstimee, 
				 HauteurInstruentale_Hi, 
				 heureObservation,
				 hauteurOeil,
				 errSextan,
				 ephe);
		
		assertEquals(res.getIntercept().distanceInMilleNautique(), 96.7025, EPISILON_DISTANCE);
		assertEquals(res.getSens(), eSensIntercept.versPg);
		assertEquals(res.getZ().asDegre(), 232.6496, EPISILON_ANGLE);
	}
}
