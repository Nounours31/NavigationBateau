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
		
		Angle a = e.AngleHoraireLocal_AHL_LHA(hRef + UNEHEUREENSECONDE);
		assertEquals (a.asDegre(), 0.1, EPSILON_ANGLE);
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
		
		Angle a = e.AngleHoraireLocal_AHL_LHA(hRef + 5);
		assertEquals(a.asDegre(), 6.0, EPSILON_ANGLE);
	}
}
