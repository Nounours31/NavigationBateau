package sfa.nav.nav.astro;

import static org.junit.Assert.assertEquals;

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
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
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


public class SiteNaveAstroDebutant extends ANavAstroMotherTestClass {
	private static final Logger logger = LoggerFactory.getLogger(SiteNaveAstroDebutant.class);
	
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
		NavDateHeure heureObservation = NavDateHeureFactory.fromString("04/03/1998 15:24:04 GMT");
		double hauteurOeil = 2;
		AngleOriente sextan_exentricite = AngleOrienteFactory.fromString("0°0");
		AngleOriente sextan_collimasson = AngleOrienteFactory.fromString("-0°03");
		ErreurSextan errSextan = new ErreurSextan(sextan_collimasson, sextan_exentricite);
		
		Ephemerides ephe = new Ephemerides(
				"Soleil",
				AngleFactory.fromString("177°1.9'"),        VitesseAngulaireFactory.fromDegreParHeure(15.002),		// 15 deg/h
				DeclinaisonFactory.fromString("6°36' S"), VitesseAngulaireFactory.fromDegreParHeure(0.96 / 60.0),  // +0.96 minute/heure
				NavDateHeureFactory.fromString("04/03/1998 00:00:00 GMT"));
		
		
		DroiteHauteurPositionnee res = dh.droitedeHauteurSoleil ( positionEstimee, 
				 HauteurInstruentale_Hi, 
				 heureObservation,
				 hauteurOeil,
				 eTypeVisee.soleilBordInf,
				 errSextan,
				 ephe);
		
		assertEquals(res.getIntercept().distanceInMilleNautique(), 9.517, EPISILON_DISTANCE);
		assertEquals(res.getSens(), eSensIntercept.versPg);
		assertEquals(res.getZ().asDegre(), 230.033, EPISILON_ANGLE);
	}
	
	@Test
	public void test001_PremierCanevasDroiteHauteurV2() throws NavException {
		DroiteDeHauteur dh = new DroiteDeHauteur();
		Latitude lat = LatitudeFactory.fromString("47°29 N");
		Longitude lon = LongitudeFactory.fromString("2°53 W");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, lon); 

		Angle HauteurInstruentale_Hi = AngleFactory.fromString("22°59"); 
		NavDateHeure heureObservation = NavDateHeureFactory.fromString("04/03/2015 15:24:04 GMT");
		double hauteurOeil = 2;
		AngleOriente sextan_exentricite = AngleOrienteFactory.fromString("0°0");
		AngleOriente sextan_collimasson = AngleOrienteFactory.fromString("-0°03");
		ErreurSextan errSextan = new ErreurSextan(sextan_collimasson, sextan_exentricite);
		
		Ephemerides ephe = new Ephemerides(
				"Soleil",
				AngleFactory.fromString("177°2.0'"),        VitesseAngulaireFactory.fromDegreParHeure(15.002),		// 15 deg/h
				DeclinaisonFactory.fromString("6°38.8' S"), VitesseAngulaireFactory.fromDegreParHeure(0.96 / 60.0),  // +0.96 minute/heure
				NavDateHeureFactory.fromString("04/03/2015 00:00:00 GMT"));
		
		
		DroiteHauteurPositionnee res = dh.droitedeHauteurSoleil ( positionEstimee, 
				 HauteurInstruentale_Hi, 
				 heureObservation,
				 hauteurOeil,
				 eTypeVisee.soleilBordInf,
				 errSextan,
				 ephe);
		
		assertEquals(res.getIntercept().distanceInMilleNautique(), 11.959, EPISILON_DISTANCE);
		assertEquals(res.getSens(), eSensIntercept.versPg);
		assertEquals(res.getZ().asDegre(), 230.008, EPISILON_ANGLE);
	}

	@Test
	public void test001_PremierCanevasDroiteHauteurEtoile() throws NavException {
		DroiteDeHauteur dh = new DroiteDeHauteur();
		Latitude lat = LatitudeFactory.fromString("30° N");
		Longitude lon = LongitudeFactory.fromString("24° W");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, lon); 
		Angle HauteurInstruentale_Hi = AngleFactory.fromString("78°57"); 
		NavDateHeure heureObservation = NavDateHeureFactory.fromString("29/05/2010 23:18:13 GMT");
		double hauteurOeil = 2;
		AngleOriente sextan_exentricite = AngleOrienteFactory.fromString("0°0");
		AngleOriente sextan_collimasson = AngleOrienteFactory.fromString("-0°02");
		ErreurSextan errSextan = new ErreurSextan(sextan_collimasson, sextan_exentricite);
		
		String astre = "Aldebaran";
		Ephemerides epheEtoileArcturus = new Ephemerides(
				astre,
				AngleFactory.fromString("145°57.4"),        VitesseAngulaireFactory.fromDegreParHeure(0.0),		// 15 deg/h
				DeclinaisonFactory.fromString("19°07.6 N"), VitesseAngulaireFactory.fromDegreParHeure(0),  // +0.96 minute/heure
				NavDateHeureFactory.fromString("29/05/2010 00:00:00 GMT"));

		Ephemerides ephePointVernal = new Ephemerides(
				"Point Vernal - Aries",
				AngleFactory.fromString("232°21.7"), DeclinaisonFactory.fromString("0° N"), 0.0, NavDateHeureFactory.fromString("29/05/2010 23:00:00 GMT"),
				AngleFactory.fromString("247°24.2"), DeclinaisonFactory.fromString("0° N"), 0.0, NavDateHeureFactory.fromString("30/05/2010 00:00:00 GMT"));

		
		
		DroiteHauteurPositionnee res = dh.droitedeHauteurEtoile ( 
				astre,
				positionEstimee, 
				HauteurInstruentale_Hi, 
				heureObservation,
				hauteurOeil,
				eTypeVisee.etoile,
				errSextan,
				ephePointVernal,
				epheEtoileArcturus
		);
		
		assertEquals(res.getIntercept().distanceInMilleNautique(), -8.467, EPISILON_DISTANCE);
		assertEquals(res.getSens(), eSensIntercept.opposePg);
		assertEquals(res.getZ().asDegre(), 174.431, EPISILON_ANGLE);
	}

	@Test
	public void test001_PremierCanevasDroiteHauteurLune() throws NavException {
		DroiteDeHauteur dh = new DroiteDeHauteur();
		Latitude lat = LatitudeFactory.fromString("40°30' N");
		Longitude lon = LongitudeFactory.fromString("3°30' E");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, lon); 
		Angle HauteurInstruentale_Hi = AngleFactory.fromString("18°55"); 
		NavDateHeure heureObservation = NavDateHeureFactory.fromString("05/05/2010 08:18:53 GMT");
		double hauteurOeil = 2;
		AngleOriente sextan_exentricite = AngleOrienteFactory.fromString("0°0");
		AngleOriente sextan_collimasson = AngleOrienteFactory.fromString("0°03");
		ErreurSextan errSextan = new ErreurSextan(sextan_collimasson, sextan_exentricite);
		
		Ephemerides epheLune = new Ephemerides(
				"Lune",
				AngleFactory.fromString("35°40.5"), DeclinaisonFactory.fromString("17°02.5 S"), 54.5, NavDateHeureFactory.fromString("05/05/2010 08:00:00 GMT"),
				AngleFactory.fromString("50°13.4"), DeclinaisonFactory.fromString("16°52.9 S"), 54.4, NavDateHeureFactory.fromString("05/05/2010 09:00:00 GMT"));

		
		
		DroiteHauteurPositionnee res = dh.droitedeHauteurLune ( positionEstimee, 
				 HauteurInstruentale_Hi, 
				 heureObservation,
				 hauteurOeil,
				 eTypeVisee.luneBordSup,
				 errSextan,
				 epheLune);
		
		assertEquals(res.getIntercept().distanceInMilleNautique(), -12.825, EPISILON_DISTANCE);
		assertEquals(res.getSens(), eSensIntercept.opposePg);
		assertEquals(res.getZ().asDegre(), 224.592, EPISILON_ANGLE);
	}

	@Test
	public void test001_PremierCanevasDroiteHauteurPlanete() throws NavException {
		DroiteDeHauteur dh = new DroiteDeHauteur();
		Latitude lat = LatitudeFactory.fromString("30° N");
		Longitude lon = LongitudeFactory.fromString("24° W");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, lon); 
		Angle HauteurInstruentale_Hi = AngleFactory.fromString("24°06'"); 
		NavDateHeure heureObservation = NavDateHeureFactory.fromString("12/05/2010 20:37:42 GMT");
		double hauteurOeil = 2.0;
		AngleOriente sextan_exentricite = AngleOrienteFactory.fromString("0°0");
		AngleOriente sextan_collimasson = AngleOrienteFactory.fromString("-0°02");
		ErreurSextan errSextan = new ErreurSextan(sextan_collimasson, sextan_exentricite);
		
		String astre = "Venus";
		Ephemerides ephe = new Ephemerides(
				astre,
				AngleFactory.fromString("90°00.3'"), DeclinaisonFactory.fromString("24°27.1' N"), 0.1, NavDateHeureFactory.fromString("12/05/2010 20:00:00 GMT"),
				AngleFactory.fromString("104°59.4'"), DeclinaisonFactory.fromString("24°27.4' N"), 0.1, NavDateHeureFactory.fromString("12/05/2010 21:00:00 GMT"));
		
		
		
		DroiteHauteurPositionnee res = dh.droitedeHauteurPlanete ( 
				astre,
				positionEstimee, 
				HauteurInstruentale_Hi, 
				heureObservation,
				hauteurOeil,
				eTypeVisee.planete,
				errSextan,
				ephe
		);
		
		assertEquals(res.getIntercept().distanceInMilleNautique(), 8.309, EPISILON_DISTANCE);
		assertEquals(res.getSens(), eSensIntercept.versPg);
		assertEquals(res.getZ().asDegre(), 285.477, EPISILON_ANGLE);
	}
}
