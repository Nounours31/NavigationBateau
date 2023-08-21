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
		double heureObservation = new Date(1998, 03, 04, 15, 24, 0).getTime()/1000.0;
		double hauteurOeil = 2;
		AngleOriente sextan_collimasson = AngleOrienteFactory.fromString("-0°03");
		Ephemerides ephe = new Ephemerides(AngleFactory.fromString("177°1.9'"), VitesseAngulaireFactory.fromDegreParHeure(15.002),
				DeclinaisonFactory.fromString("6°35.9' S"), VitesseAngulaireFactory.fromDegreParHeure(1.0 / 60.0), 
				(new Date(1998, 03, 04, 00, 00, 0).getTime()) * 1.0 / 1000.0);
		
		DroiteHauteurPositionnee res = dh.droitedeHauteur ( positionEstimee, 
				 HauteurInstruentale_Hi, 
				 heureObservation,
				 hauteurOeil,
				 sextan_collimasson,
				 ephe);
		
		assertEquals(res.getIntercept().distanceInMilleNautique(), 99.2025, EPISILON_DISTANCE);
		assertEquals(res.getSens(), eSensIntercept.versPg);
		assertEquals(res.getZ().asDegre(), 232.65, EPISILON_ANGLE);
	}
}
