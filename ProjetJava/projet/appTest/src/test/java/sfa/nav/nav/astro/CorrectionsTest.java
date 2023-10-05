package sfa.nav.nav.astro;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.CorrectionDeVisee.eTypeVisee;
import sfa.nav.astro.calculs.CorrectionDeViseeLune;
import sfa.nav.astro.calculs.CorrectionDeViseeSoleil;
import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.AngleOrienteFactory;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.NavDateHeureFactory;


public class CorrectionsTest  {
	private static final Logger logger = LoggerFactory.getLogger(CorrectionsTest.class);
	private static final ErreurSextan errSextan = new ErreurSextan(AngleOrienteFactory.fromDegre(0.0), AngleOrienteFactory.fromDegre(0.0));
	private static final CorrectionDeViseeSoleil cvSolaire = new CorrectionDeViseeSoleil(errSextan);
	private static final CorrectionDeViseeLune cvLunaire = new CorrectionDeViseeLune( errSextan);

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

	// ------------------------------------------------------------------------------------
	// Les tests
	// ------------------------------------------------------------------------------------
	final double UNEHEUREENSECONDE= 1.0 * 3600.0;
	final double EPSILON_ANGLE = 1.0 / 3600.0;
	
	@Test
	public void test001() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 7.0);
		double hOeil = 0.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 9.0, EPSILON_ANGLE);
	}

	@Test
	public void test002() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 6.0);
		double hOeil = 1.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 6.55, EPSILON_ANGLE);
	}

	@Test
	public void test002bis() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 5.0);
		double hOeil = 1.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 6.55, EPSILON_ANGLE);
	}
	@Test
	public void test003() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 91.0);
		double hOeil = 1.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 15.05, EPSILON_ANGLE);
	}
	@Test
	public void test004() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 6.0);
		double hOeil = 6.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 3.5, EPSILON_ANGLE);
	}

	@Test
	public void test005() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 7.0);
		double hOeil = 2.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 6.5, EPSILON_ANGLE);
	}

	@Test
	public void test006() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 8.0);
		double hOeil = 1.99;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 7.4125, EPSILON_ANGLE);
	}
	
	@Test
	public void test007() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 8.0);
		double hOeil = 2.01;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 7.3945, EPSILON_ANGLE);
	}

	@Test
	public void test008() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 15.0);
		double hOeil = 2;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 10.4, EPSILON_ANGLE);
	}

	@Test
	public void test009() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 91.0);
		double hOeil = 6;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 12.0, EPSILON_ANGLE);
	}

	@Test
	public void test010_etoile() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 15.0);
		double hOeil = 2;
		eTypeVisee bord = eTypeVisee.etoile;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, -5.599999999999, EPSILON_ANGLE);
	}

	@Test
	public void test010_bordSup() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 15.0);
		double hOeil = 2;
		eTypeVisee bord = eTypeVisee.soleilBordSup;
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, -21.9, EPSILON_ANGLE);
	}

	@Test
	public void test011_mois() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 15.0);
		double hOeil = 2;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		
		NavDateHeure decembre = NavDateHeureFactory.fromStringSafe("10/12/1968 10:00:00");
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, decembre, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 10.4, EPSILON_ANGLE);

		NavDateHeure juillet = NavDateHeureFactory.fromStringSafe("10/07/1968 10:00:00");
		hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil,  juillet, bord );
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 9.9, EPSILON_ANGLE);
	}

	@Test
	public void test012_lune_HorsDomaine() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 15.0);
		double hOeil = 0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvLunaire.correctionTotaleEnDegreLune(Hi,  hOeil,  bord, 53);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 63.4, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotaleEnDegreLune(Hi,  hOeil,  bord, 62);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 72.1, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotaleEnDegreLune(Hi,  0.0,  bord, 61);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 72.1, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotaleEnDegreLune(Hi,  24.0,  bord, 61);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 66.0, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotaleEnDegreLune(AngleFactory.fromDegre(0.0),  0.0,  bord, 61);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 67.4, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotaleEnDegreLune(AngleFactory.fromDegre(50.0),  0.0,  bord, 61);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 58.9, EPSILON_ANGLE);
	}

	@Test
	public void test012_lune_01() throws NavException {
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		double hEnDegre = cvLunaire.correctionTotaleEnDegreLune(AngleFactory.fromDegre(6.0),  0,  bord, 54.5);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 60.8, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotaleEnDegreLune(AngleFactory.fromDegre(6.0),  3.0,  bord, 54.5);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 57.8, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotaleEnDegreLune(AngleFactory.fromDegre(10.0),  3.0,  bord, 55);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 61.0, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotaleEnDegreLune(AngleFactory.fromDegre(10.5),  3.0,  bord, 55);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 61.1, EPSILON_ANGLE);
	}

}
