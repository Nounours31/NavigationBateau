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
import sfa.nav.astro.calculs.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.astro.calculs.Ephemerides;
import sfa.nav.astro.calculs.ErreurSextan;
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


public class SiteNaveAstroExercices extends ANavAstroMotherTestClass {
	private static final Logger logger = LoggerFactory.getLogger(SiteNaveAstroExercices.class);
	

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
	public void Exo1NavAstro() throws NavException {
		DroiteDeHauteur dh = new DroiteDeHauteur();
		Latitude lat = LatitudeFactory.fromString("45°20 N");
		Longitude lon = LongitudeFactory.fromString("14°15 W");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, lon); 

		Angle HauteurInstruentale_Hi = AngleFactory.fromString("29°28.3"); 
		NavDateHeure heureObservation = NavDateHeureFactory.fromString("13/06/2007 17:43:27 GMT");
		double hauteurOeil = 2;
		AngleOriente sextan_exentricite = AngleOrienteFactory.fromString("0°0");
		AngleOriente sextan_collimasson = AngleOrienteFactory.fromString("-0°02");
		ErreurSextan errSextan = new ErreurSextan(sextan_collimasson, sextan_exentricite);
		
		Ephemerides ephe = new Ephemerides(
				AngleFactory.fromString("180°2.0'"),        VitesseAngulaireFactory.fromDegreParHeure(14.998),		// 15 deg/h
				DeclinaisonFactory.fromString("23°10.6 N"), VitesseAngulaireFactory.fromDegreParHeure(0.2 / 60.0),  // +0.96 minute/heure
				NavDateHeureFactory.fromString("13/06/2007 00:00:00 GMT"));
		
		
		DroiteHauteurPositionnee res = dh.droitedeHauteurSoleil ( positionEstimee, 
				 HauteurInstruentale_Hi, 
				 heureObservation,
				 hauteurOeil,
				 eTypeVisee.soleilBordInf,
				 errSextan,
				 ephe);
		
		assertEquals(res.getIntercept().distanceInMilleNautique(), 43.779, EPISILON_DISTANCE);
		assertEquals(res.getSens(), eSensIntercept.versPg);
		assertEquals(res.getZ().asDegre(), 274.6658, EPISILON_ANGLE);
	}

	@Test
	public void Exo1NavAstroVersion2() throws NavException {
		DroiteDeHauteur dh = new DroiteDeHauteur();
		Latitude lat = LatitudeFactory.fromString("44°40 N");
		Longitude lon = LongitudeFactory.fromString("15°45 W");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, lon); 

		Angle HauteurInstruentale_Hi = AngleFactory.fromString("29°28.3"); 
		NavDateHeure heureObservation = NavDateHeureFactory.fromString("13/06/2007 17:43:27 GMT");
		double hauteurOeil = 2;
		AngleOriente sextan_exentricite = AngleOrienteFactory.fromString("0°0");
		AngleOriente sextan_collimasson = AngleOrienteFactory.fromString("-0°02");
		ErreurSextan errSextan = new ErreurSextan(sextan_collimasson, sextan_exentricite);
		
		Ephemerides ephe = new Ephemerides(
				AngleFactory.fromString("180°2.0'"),        VitesseAngulaireFactory.fromDegreParHeure(14.998),		// 15 deg/h
				DeclinaisonFactory.fromString("23°10.6 N"), VitesseAngulaireFactory.fromDegreParHeure(0.2 / 60.0),  // +0.96 minute/heure
				NavDateHeureFactory.fromString("13/06/2007 00:00:00 GMT"));
		
		
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

	@Test
	public void Exo2NavAstro() throws NavException {
		DroiteDeHauteur dh = new DroiteDeHauteur();
		Latitude lat = LatitudeFactory.fromString("41°05 N");
		Longitude lon = LongitudeFactory.fromString("6°00 E");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, lon); 

		double hauteurOeil = 2.0;
		AngleOriente sextan_exentricite = AngleOrienteFactory.fromString("0°0");
		AngleOriente sextan_collimasson = AngleOrienteFactory.fromString("-0°02");
		ErreurSextan errSextan = new ErreurSextan(sextan_collimasson, sextan_exentricite);

		NavDateHeure heureObservation = NavDateHeureFactory.fromString("27/06/2007 17:43:27 GMT");
		Angle HauteurInstruentale_Hi = AngleFactory.fromString("29°28.3"); 

		
		Ephemerides epheVernal = new Ephemerides(
				AngleFactory.fromString("180°2.0'", false),        VitesseAngulaireFactory.fromDegreParHeure(14.998),		// 15 deg/h
				DeclinaisonFactory.fromString("23°10.6 N"), VitesseAngulaireFactory.fromDegreParHeure(0.2 / 60.0),  // +0.96 minute/heure
				NavDateHeureFactory.fromString("13/06/2007 00:00:00 GMT"));

		Ephemerides epheAstre = new Ephemerides(
				AngleFactory.fromString("180°2.0'"),        VitesseAngulaireFactory.fromDegreParHeure(14.998),		// 15 deg/h
				DeclinaisonFactory.fromString("23°10.6 N"), VitesseAngulaireFactory.fromDegreParHeure(0.2 / 60.0),  // +0.96 minute/heure
				NavDateHeureFactory.fromString("13/06/2007 00:00:00 GMT"));
		
		
		DroiteHauteurPositionnee res = dh.droitedeHauteurEtoile("Vega", 
				positionEstimee, 
				HauteurInstruentale_Hi, 
				heureObservation, 
				hauteurOeil, 
				errSextan, 
				epheVernal, 
				epheAstre); 
		
		assertEquals(res.getIntercept().distanceInMilleNautique(), -16.6466, EPISILON_DISTANCE);
		assertEquals(res.getSens(), eSensIntercept.opposePg);
		assertEquals(res.getZ().asDegre(), 274.02628, EPISILON_ANGLE);
	}
}
