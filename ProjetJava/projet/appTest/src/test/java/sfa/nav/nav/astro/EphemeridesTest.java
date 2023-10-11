package sfa.nav.nav.astro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import sfa.nav.astro.calculs.Ephemerides;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.AngleOriente;
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


public class EphemeridesTest {
	private static final Logger logger = LoggerFactory.getLogger(EphemeridesTest.class);

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
	final double UNEHEUREENSECONDE= 1.0 * 3600.0;
	final double EPSILON_ANGLE = 0.001;
	
	@Test
	public void test001() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		VitesseAngulaire varHA = VitesseAngulaireFactory.fromDegreParHeure(0.1);
		VitesseAngulaire varDec = VitesseAngulaireFactory.fromDegreParHeure(0.1);
		NavDateHeure hRef = NavDateHeureFactory.fromString("2023/01/01 01:00:00 GMT");
		Ephemerides e = new Ephemerides("Soleil", gha, varHA, dec, varDec, hRef);
		assertEquals (e.toString(), 
				"Ephemerides [ephe1=Ephemerides [Astre= Soleil, GHA=000.000°[00°00.00][00°00'00.00\"][0.00 Rad], declinaison=lat:000.000°[00°00.00][00°00'00.00\"][0.00 Rad] N, heureEnSeconde=2023/01/01 02:00:00 CET [2023/01/01 01:00:00 UTC], varHA=VitesseAngulaire [vitesseInDegreParSeconde=2.777777777777778E-5], varDecli=VitesseAngulaire [vitesseInDegreParSeconde=2.777777777777778E-5], type=ParGradiant], ephe2=null, type=ParGradiant]");
		
		Angle a = e.AngleHoraireAHeureObservation(hRef.plusHeureDecimale(1.0));
		assertEquals (a.asDegre(), 0.1, EPSILON_ANGLE);

		Declinaison d = e.declinaison(hRef.plusHeureDecimale(1.0));
		assertEquals (d.asDegre(), 0.1, EPSILON_ANGLE);
	}

	@Test
	public void test002() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		NavDateHeure hRef = NavDateHeureFactory.fromString("2023/01/01 01:00:00 GMT");

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		NavDateHeure hRef2 = NavDateHeureFactory.fromString("2023/01/01 11:00:00 GMT");
		Ephemerides e = new Ephemerides("Soleil", gha, dec, 0.0, hRef, gha2, dec2, 0.0, hRef2);		
		assertEquals (e.toString(), 
				"Ephemerides [ephe1=Ephemerides [Astre= Soleil, GHA=000.000°[00°00.00][00°00'00.00\"][0.00 Rad], declinaison=lat:000.000°[00°00.00][00°00'00.00\"][0.00 Rad] N, heureEnSeconde=2023/01/01 02:00:00 CET [2023/01/01 01:00:00 UTC], varHA=null, varDecli=null, type=parInterval], ephe2=Ephemerides [Astre= Soleil, GHA=010.000°[10°00.00][10°00'00.00\"][0.17 Rad], declinaison=lat:010.000°[10°00.00][10°00'00.00\"][0.17 Rad] N, heureEnSeconde=2023/01/01 12:00:00 CET [2023/01/01 11:00:00 UTC], varHA=null, varDecli=null, type=parInterval], type=parInterval]"
);
		Angle a = e.AngleHoraireAHeureObservation(NavDateHeure.moyenne(hRef, hRef2));
		assertEquals(a.asDegre(), 5.0, EPSILON_ANGLE);

		Declinaison d = e.declinaison(NavDateHeure.moyenne(hRef, hRef2));
		assertEquals (d.asDegre(), 5.0, EPSILON_ANGLE);
	}

	@Test
	public void test003_auxfrontieres() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		NavDateHeure hRef = NavDateHeureFactory.fromString("2023/01/01 01:00:00 GMT");

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		NavDateHeure hRef2 = NavDateHeureFactory.fromString("2023/01/01 11:00:00 GMT");
		Ephemerides e = new Ephemerides("Soleil", gha, dec, 0.0, hRef, gha2, dec2, 0.0, hRef2);		
		assertEquals (e.toString(), 
				"Ephemerides [ephe1=Ephemerides [Astre= Soleil, GHA=000.000°[00°00.00][00°00'00.00\"][0.00 Rad], declinaison=lat:000.000°[00°00.00][00°00'00.00\"][0.00 Rad] N, heureEnSeconde=2023/01/01 02:00:00 CET [2023/01/01 01:00:00 UTC], varHA=null, varDecli=null, type=parInterval], ephe2=Ephemerides [Astre= Soleil, GHA=010.000°[10°00.00][10°00'00.00\"][0.17 Rad], declinaison=lat:010.000°[10°00.00][10°00'00.00\"][0.17 Rad] N, heureEnSeconde=2023/01/01 12:00:00 CET [2023/01/01 11:00:00 UTC], varHA=null, varDecli=null, type=parInterval], type=parInterval]"
);
		
		Angle a = e.AngleHoraireAHeureObservation(hRef.plusHeureDecimale(0.001));
		assertEquals(a.asDegre(), 0.0, EPSILON_ANGLE);

		Declinaison d = e.declinaison(hRef.plusHeureDecimale(0.001));
		assertEquals (d.asDegre(), 0.0, EPSILON_ANGLE);

		a = e.AngleHoraireAHeureObservation(hRef2.moinsHeureDecimale(0.001));
		assertEquals(a.asDegre(), 9.9988, EPSILON_ANGLE);

		d = e.declinaison(hRef2.moinsHeureDecimale(0.001));
		assertEquals (d.asDegre(), 9.9988, EPSILON_ANGLE);

		a = e.AngleHoraireAHeureObservation(hRef.plusHeureDecimale(0.001));
		assertEquals(a.asDegre(), 0.0008, EPSILON_ANGLE);

		d = e.declinaison(hRef.plusHeureDecimale(0.001));
		assertEquals (d.asDegre(), 0.0008, EPSILON_ANGLE);
	}

	@Test
	public void test003_auSud() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 S");
		NavDateHeure hRef = NavDateHeureFactory.fromString("2023/01/01 01:00:00 GMT");

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 S");
		NavDateHeure hRef2 = NavDateHeureFactory.fromString("2023/01/01 11:00:00 GMT");
		Ephemerides e = new Ephemerides("Soleil", gha, dec, 0.0, hRef, gha2, dec2, 0.0, hRef2);		
		assertEquals (e.toString(), "Ephemerides [ephe1=Ephemerides [Astre= Soleil, GHA=000.000°[00°00.00][00°00'00.00\"][0.00 Rad], declinaison=lat:000.000°[00°00.00][00°00'00.00\"][0.00 Rad] N, heureEnSeconde=2023/01/01 02:00:00 CET [2023/01/01 01:00:00 UTC], varHA=null, varDecli=null, type=parInterval], ephe2=Ephemerides [Astre= Soleil, GHA=010.000°[10°00.00][10°00'00.00\"][0.17 Rad], declinaison=lat:010.000°[10°00.00][10°00'00.00\"][0.17 Rad] S, heureEnSeconde=2023/01/01 12:00:00 CET [2023/01/01 11:00:00 UTC], varHA=null, varDecli=null, type=parInterval], type=parInterval]");
		
		Angle a = e.AngleHoraireAHeureObservation(NavDateHeure.moyenne(hRef, hRef2));
		assertEquals(a.asDegre(), +5.0, EPSILON_ANGLE);

		Declinaison d = e.declinaison(NavDateHeure.moyenne(hRef, hRef2));
		assertEquals (d.asDegre(), -5.0, EPSILON_ANGLE);
	}
	
	@Test
	public void test004_auSud() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("1°00 S");
		VitesseAngulaire varHA = VitesseAngulaireFactory.fromDegreParHeure(-0.1);
		VitesseAngulaire varDec = VitesseAngulaireFactory.fromDegreParHeure(-0.1);
		NavDateHeure hRef = NavDateHeureFactory.fromString("2023/01/01 01:00:00 GMT");
		Ephemerides e = new Ephemerides("Soleil", gha, varHA, dec, varDec, hRef);
		assertEquals (e.toString(), 
				"Ephemerides [ephe1=Ephemerides [Astre= Soleil, GHA=000.000°[00°00.00][00°00'00.00\"][0.00 Rad], declinaison=lat:001.000°[01°00.00][01°00'00.00\"][0.02 Rad] S, heureEnSeconde=2023/01/01 02:00:00 CET [2023/01/01 01:00:00 UTC], varHA=VitesseAngulaire [vitesseInDegreParSeconde=-2.777777777777778E-5], varDecli=VitesseAngulaire [vitesseInDegreParSeconde=-2.777777777777778E-5], type=ParGradiant], ephe2=null, type=ParGradiant]"
);
		
		Angle a = e.AngleHoraireAHeureObservation(hRef.plusHeureDecimale(1.0));
		assertEquals (a.asDegre(), -0.1, EPSILON_ANGLE);

		Declinaison d = e.declinaison(hRef.plusHeureDecimale(1.0));
		assertEquals (d.asDegre(), -1.1, EPSILON_ANGLE);
	}

	@Test
	public void test005_PiLune() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 S");
		NavDateHeure hRef = NavDateHeureFactory.fromString("2023/01/01 10:00:00 GMT");

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 S");
		NavDateHeure hRef2 = NavDateHeureFactory.fromString("2023/01/01 11:00:00 GMT");
		Ephemerides e = new Ephemerides("Soleil", gha, dec, 1, hRef, gha2, dec2, 11.0, hRef2);

		double pi = e.pi(NavDateHeure.moyenne(hRef, hRef2));
		assertEquals(pi, +6.0, EPSILON_ANGLE);
	}


	// @Test(expected = NavException.class)
	@Test
	public void test001_out() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		NavDateHeure hRef = NavDateHeureFactory.fromString("2023/01/01 01:00:00 GMT");

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		NavDateHeure hRef2 = NavDateHeureFactory.fromString("2023/01/01 11:00:00 GMT");
		Ephemerides e = new Ephemerides("Soleil", gha, dec, 0.0, hRef, gha2, dec2, 0.0, hRef2);
		
		Angle a = e.AngleHoraireAHeureObservation(hRef.moinsHeureDecimale(0.001));
		assertNull(a);
	}

	// @Test(expected = NavException.class)
	@Test
	public void test002_out() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		NavDateHeure hRef = NavDateHeureFactory.fromString("2023/01/01 01:00:00 GMT");

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		NavDateHeure hRef2 = NavDateHeureFactory.fromString("2023/01/01 11:00:00 GMT");
		Ephemerides e = new Ephemerides("Soleil", gha, dec, 0.0, hRef, gha2, dec2, 0.0, hRef2);
		
		Angle a = e.AngleHoraireAHeureObservation(hRef2.plusHeureDecimale(0.001));
		assertNull(a);
	}

	// @Test(expected = NavException.class)
	@Test
	public void test003_out() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		NavDateHeure hRef = NavDateHeureFactory.fromString("2023/01/01 01:00:00 GMT");

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		NavDateHeure hRef2 = NavDateHeureFactory.fromString("2023/01/01 11:00:00 GMT");
		Ephemerides e = new Ephemerides("Soleil", gha, dec, 0.0, hRef, gha2, dec2, 0.0, hRef2);
		
		Angle a = e.declinaison(hRef2.plusHeureDecimale(0.001));
		assertNull(a);
	}

	// @Test(expected = NavException.class)
	@Test
	public void test004_out() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		NavDateHeure hRef = NavDateHeureFactory.fromString("2023/01/01 01:00:00 GMT");

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		NavDateHeure hRef2 = NavDateHeureFactory.fromString("2023/01/01 11:00:00 GMT");
		Ephemerides e = new Ephemerides("Soleil", gha, dec, 0.0, hRef, gha2, dec2, 0.0, hRef2);
		
		Angle a = e.declinaison(hRef.moinsHeureDecimale(0.001));
		assertNull(a);
	}
}
