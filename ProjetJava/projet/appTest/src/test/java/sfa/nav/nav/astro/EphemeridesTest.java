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
		double hRef = 1.0;
		Ephemerides e = new Ephemerides(gha, varHA, dec, varDec, hRef);
		assertEquals (e.toString(), "Ephemerides [ephe1=Ephemerides [GHA=000.000°[00°00'00.00\"][0.00 Rad], declinaison=lat:000.000°[00°00'00.00\"][0.00 Rad] N, heureEnSeconde=1.0, varHA=VitesseAngulaire [vitesseInDegreParSeconde=2.777777777777778E-5], varDecli=VitesseAngulaire [vitesseInDegreParSeconde=2.777777777777778E-5], type=ParGradiant], ephe2=null, type=ParGradiant]");
		
		Angle a = e.AngleHoraireLocal_AHL_LHA(hRef + UNEHEUREENSECONDE);
		assertEquals (a.asDegre(), 0.1, EPSILON_ANGLE);

		Declinaison d = e.declinaison(hRef + UNEHEUREENSECONDE);
		assertEquals (d.asDegre(), 0.1, EPSILON_ANGLE);
	}

	@Test
	public void test002() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		double hRef = 1.0;

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		double hRef2 = 11.0;
		Ephemerides e = new Ephemerides(gha, dec, hRef, gha2, dec2, hRef2);		
		assertEquals (e.toString(), "Ephemerides [ephe1=Ephemerides [GHA=000.000°[00°00'00.00\"][0.00 Rad], declinaison=lat:000.000°[00°00'00.00\"][0.00 Rad] N, heureEnSeconde=1.0, varHA=null, varDecli=null, type=parInterval], ephe2=Ephemerides [GHA=010.000°[10°00'00.00\"][0.17 Rad], declinaison=lat:010.000°[10°00'00.00\"][0.17 Rad] N, heureEnSeconde=11.0, varHA=null, varDecli=null, type=parInterval], type=parInterval]");

		Angle a = e.AngleHoraireLocal_AHL_LHA(hRef + ((hRef2 - hRef) / 2.0));
		assertEquals(a.asDegre(), 5.0, EPSILON_ANGLE);

		Declinaison d = e.declinaison(hRef + ((hRef2 - hRef) / 2.0));
		assertEquals (d.asDegre(), 5.0, EPSILON_ANGLE);
	}

	@Test
	public void test003_auxfrontieres() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		double hRef = 1.0;

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		double hRef2 = 11.0;
		Ephemerides e = new Ephemerides(gha, dec, hRef, gha2, dec2, hRef2);		
		assertEquals (e.toString(), "Ephemerides [ephe1=Ephemerides [GHA=000.000°[00°00'00.00\"][0.00 Rad], declinaison=lat:000.000°[00°00'00.00\"][0.00 Rad] N, heureEnSeconde=1.0, varHA=null, varDecli=null, type=parInterval], ephe2=Ephemerides [GHA=010.000°[10°00'00.00\"][0.17 Rad], declinaison=lat:010.000°[10°00'00.00\"][0.17 Rad] N, heureEnSeconde=11.0, varHA=null, varDecli=null, type=parInterval], type=parInterval]");

		Angle a = e.AngleHoraireLocal_AHL_LHA(hRef + 0.001);
		assertEquals(a.asDegre(), 0.0, EPSILON_ANGLE);

		Declinaison d = e.declinaison(hRef + 0.001);
		assertEquals (d.asDegre(), 0.0, EPSILON_ANGLE);

		a = e.AngleHoraireLocal_AHL_LHA(hRef2 - 0.001);
		assertEquals(a.asDegre(), 10.0, EPSILON_ANGLE);

		d = e.declinaison(hRef2 - 0.001);
		assertEquals (d.asDegre(), 10.0, EPSILON_ANGLE);
	}

	@Test
	public void test003_auSud() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 S");
		double hRef = 1.0;

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 S");
		double hRef2 = 11.0;
		Ephemerides e = new Ephemerides(gha, dec, hRef, gha2, dec2, hRef2);		
		assertEquals (e.toString(), "Ephemerides [ephe1=Ephemerides [GHA=000.000°[00°00'00.00\"][0.00 Rad], declinaison=lat:000.000°[00°00'00.00\"][0.00 Rad] N, heureEnSeconde=1.0, varHA=null, varDecli=null, type=parInterval], ephe2=Ephemerides [GHA=010.000°[10°00'00.00\"][0.17 Rad], declinaison=lat:010.000°[10°00'00.00\"][0.17 Rad] S, heureEnSeconde=11.0, varHA=null, varDecli=null, type=parInterval], type=parInterval]");

		Angle a = e.AngleHoraireLocal_AHL_LHA(hRef + ((hRef2 - hRef) / 2.0));
		assertEquals(a.asDegre(), +5.0, EPSILON_ANGLE);

		Declinaison d = e.declinaison(hRef + ((hRef2 - hRef) / 2.0));
		assertEquals (d.asDegre(), -5.0, EPSILON_ANGLE);
	}
	
	@Test
	public void test004_auSud() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("1°00 S");
		VitesseAngulaire varHA = VitesseAngulaireFactory.fromDegreParHeure(-0.1);
		VitesseAngulaire varDec = VitesseAngulaireFactory.fromDegreParHeure(-0.1);
		double hRef = 1.0;
		Ephemerides e = new Ephemerides(gha, varHA, dec, varDec, hRef);
		assertEquals (e.toString(), "Ephemerides [ephe1=Ephemerides [GHA=000.000°[00°00'00.00\"][0.00 Rad], declinaison=lat:001.000°[01°00'00.00\"][0.02 Rad] S, heureEnSeconde=1.0, varHA=VitesseAngulaire [vitesseInDegreParSeconde=-2.777777777777778E-5], varDecli=VitesseAngulaire [vitesseInDegreParSeconde=-2.777777777777778E-5], type=ParGradiant], ephe2=null, type=ParGradiant]");
		
		Angle a = e.AngleHoraireLocal_AHL_LHA(hRef + UNEHEUREENSECONDE);
		assertEquals (a.asDegre(), -0.1, EPSILON_ANGLE);

		Declinaison d = e.declinaison(hRef + UNEHEUREENSECONDE);
		assertEquals (d.asDegre(), -1.1, EPSILON_ANGLE);
	}


	@Test(expected = NavException.class)
	public void test001_out() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		double hRef = 1.0;

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		double hRef2 = 11.0;
		Ephemerides e = new Ephemerides(gha, dec, hRef, gha2, dec2, hRef2);
		
		Angle a = e.AngleHoraireLocal_AHL_LHA(hRef2 + 1.0);
	}

	@Test(expected = NavException.class)
	public void test002_out() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		double hRef = 1.0;

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		double hRef2 = 11.0;
		Ephemerides e = new Ephemerides(gha, dec, hRef, gha2, dec2, hRef2);
		
		Angle a = e.AngleHoraireLocal_AHL_LHA(hRef - 1.0);
	}

	@Test(expected = NavException.class)
	public void test003_out() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		double hRef = 1.0;

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		double hRef2 = 11.0;
		Ephemerides e = new Ephemerides(gha, dec, hRef, gha2, dec2, hRef2);
		
		Angle a = e.declinaison(hRef2 + 1.0);
	}

	@Test(expected = NavException.class)
	public void test004_out() throws NavException {
		Angle gha = AngleFactory.fromString("0°00");
		Declinaison dec = DeclinaisonFactory.fromString("0°00 N");
		double hRef = 1.0;

		Angle gha2 = AngleFactory.fromString("10°00");
		Declinaison dec2 = DeclinaisonFactory.fromString("10°00 N");
		double hRef2 = 11.0;
		Ephemerides e = new Ephemerides(gha, dec, hRef, gha2, dec2, hRef2);
		
		Angle a = e.declinaison(hRef - 1.0);
	}
}
