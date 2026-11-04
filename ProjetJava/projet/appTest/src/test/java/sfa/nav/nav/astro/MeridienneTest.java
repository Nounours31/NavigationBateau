package sfa.nav.nav.astro;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.Ephemerides;
import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.Meridienne;
import sfa.nav.astro.calculs.correctionvisee.internal.EphemerideParInterval;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.AngleOrienteFactory;
import sfa.nav.model.Declinaison;
import sfa.nav.model.DeclinaisonFactory;
import sfa.nav.model.Latitude;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.Longitude;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.NavDateHeureFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.VitesseAngulaire;
import sfa.nav.model.VitesseAngulaireFactory;


public class MeridienneTest  {
	private static final Logger logger = LoggerFactory.getLogger(MeridienneTest.class);
	private static final ErreurSextan errSextan = new ErreurSextan(AngleOrienteFactory.fromDegre(0.0), AngleOrienteFactory.fromDegre(0.0));
	private static final NavDateHeure heureObservation = NavDateHeureFactory.fromStringSafe("10/01/1968 10:00:00");
	
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

	
	@Test
	public void AvecEphemerideParGardient() throws NavException {
		Meridienne m = new Meridienne();
		Angle GHA = AngleFactory.fromStringSafe("176°26.2'");
		Declinaison dec = DeclinaisonFactory.fromString("13°58.1' S");
		VitesseAngulaire varHA = VitesseAngulaireFactory.fromDegreParHeure(15.000);
		VitesseAngulaire varDec = VitesseAngulaireFactory.fromDegreParHeure(-9.0/60.0);

		Ephemerides eph = new Ephemerides("Soleil", GHA, varHA, dec, varDec, NavDateHeureFactory.fromString("12/02/2008 0:0:0 UTC"));
		Longitude longi = LongitudeFactory.fromString("18°20' W");
		Latitude lat = LatitudeFactory.fromString("41°04.7' N");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, longi);
		NavDateHeure culmination = m.LatitudeMeridienneSoleil_EstimationHeureLocaleCulmination(positionEstimee, eph);
		assertEquals(culmination.toString(), "2008/02/12 14:27:35 CET [2008/02/12 13:27:35 UTC]");
	}

	@Test
	public void AvecEphemerideParInterval() throws NavException {
		Meridienne m = new Meridienne();
		Angle GHA = AngleFactory.fromStringSafe("356°26.2'");
		Angle GHA2 = AngleFactory.fromString("371°26.2'", false);
		
		Declinaison dec = DeclinaisonFactory.fromString("13°48.2' S");
		Declinaison dec2 = DeclinaisonFactory.fromString("13°47.3' S");
		
		double Pi = 0.0;
		NavDateHeure heureRef = NavDateHeureFactory.fromString("12/02/2008 12:0:0 UTC");
		NavDateHeure heureRef2 = NavDateHeureFactory.fromString("12/02/2008 13:0:0 UTC");
		Ephemerides eph = new Ephemerides("Soleil", GHA, dec, Pi, heureRef, GHA2, dec2, Pi, heureRef2);
		Longitude longi = LongitudeFactory.fromString("18°20' W");
		Latitude lat = LatitudeFactory.fromString("41°04.7' N");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, longi);
		NavDateHeure culmination = m.LatitudeMeridienneSoleil_EstimationHeureLocaleCulmination(positionEstimee, eph);
		assertEquals(culmination.toString(), "2008/02/12 14:27:35 CET [2008/02/12 13:27:35 UTC]");
	}

	@Test
	public void AvecEphemerideParIntervalMultiple() throws NavException {
		Meridienne m = new Meridienne();
		Angle GHA0 = AngleFactory.fromStringSafe("341°26.2'");
		Angle GHA1 = AngleFactory.fromStringSafe("356°26.2'");
		Angle GHA2 = AngleFactory.fromString("371°26.2'", false);
		Angle GHA3 = AngleFactory.fromString("386°26.2'", false);
		
		Declinaison dec0 = DeclinaisonFactory.fromString("13°49' S");
		Declinaison dec1 = DeclinaisonFactory.fromString("13°48.2' S");
		Declinaison dec2 = DeclinaisonFactory.fromString("13°47.3' S");
		Declinaison dec3 = DeclinaisonFactory.fromString("13°46.5' S");
		
		double Pi = 0.0;
		NavDateHeure heureRef0 = NavDateHeureFactory.fromString("12/02/2008 11:0:0 UTC");
		NavDateHeure heureRef1 = NavDateHeureFactory.fromString("12/02/2008 12:0:0 UTC");
		NavDateHeure heureRef2 = NavDateHeureFactory.fromString("12/02/2008 13:0:0 UTC");
		NavDateHeure heureRef3 = NavDateHeureFactory.fromString("12/02/2008 14:0:0 UTC");
		Ephemerides eph = new Ephemerides("Soleil", GHA0, dec0, Pi, heureRef0, GHA1, dec1, Pi, heureRef1);
		eph.add(new EphemerideParInterval().astre("Soleil").declinaison(dec2).GHA(GHA2).heureDeRef(heureRef2).HP_PI(Pi));
		eph.add(new EphemerideParInterval().astre("Soleil").declinaison(dec3).GHA(GHA3).heureDeRef(heureRef3).HP_PI(Pi));

		Longitude longi = LongitudeFactory.fromString("18°20' W");
		Latitude lat = LatitudeFactory.fromString("41°04.7' N");
		PointGeographique positionEstimee = PointGeographiqueFactory.fromLatLong(lat, longi);
		NavDateHeure culmination = m.LatitudeMeridienneSoleil_EstimationHeureLocaleCulmination(positionEstimee, eph);
		assertEquals(culmination.toString(), "2008/02/12 14:27:35 CET [2008/02/12 13:27:35 UTC]");
	}
}
