package sfa.nav.nav.astro;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.ICorrectionDeVisee;
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeViseeFactory;
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.AngleOrienteFactory;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.NavDateHeureFactory;


public class CorrectionsTest  {
	private static final Logger logger = LoggerFactory.getLogger(CorrectionsTest.class);
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

	// ------------------------------------------------------------------------------------
	// Les tests
	// ------------------------------------------------------------------------------------
	final double UNEHEUREENSECONDE= 1.0 * 3600.0;
	final double EPSILON_ANGLE = 0.01;
	
	@Test
	public void test001() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 7.0);
		double hOeil = 0.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 8.85, EPSILON_ANGLE);
	}

	@Test
	public void test002() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 6.0);
		double hOeil = 1.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;

		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 6.00, EPSILON_ANGLE);
	}

	@Test
	public void test002bis() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 5.0);
		double hOeil = 1.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 4.617, EPSILON_ANGLE);
	}
	@Test
	public void test003() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 91.0);
		double hOeil = 1.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 14.56, EPSILON_ANGLE);
	}
	@Test
	public void test004() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 6.0);
		double hOeil = 6.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 3.399, EPSILON_ANGLE);
	}

	@Test
	public void test005() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 7.0);
		double hOeil = 2.0;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 6.32, EPSILON_ANGLE);
	}

	@Test
	public void test006() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 8.0);
		double hOeil = 1.99;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 7.17, EPSILON_ANGLE);
	}
	
	@Test
	public void test007() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 8.0);
		double hOeil = 2.01;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 7.16, EPSILON_ANGLE);
	}

	@Test
	public void test008() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 15.0);
		double hOeil = 2;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 10.16, EPSILON_ANGLE);
	}

	@Test
	public void test009() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 91.0);
		double hOeil = 6;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 12.006, EPSILON_ANGLE);
	}

	@Test
	public void test010_etoile() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 15.0);
		double hOeil = 2;
		eTypeVisee bord = eTypeVisee.etoile;
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, -6.1327, EPSILON_ANGLE);
	}

	@Test
	public void test010_bordSup() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 15.0);
		double hOeil = 2;
		eTypeVisee bord = eTypeVisee.soleilBordSup;
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, heureObservation, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, -22.4327, EPSILON_ANGLE);
	}

	@Test
	public void test011_mois() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 15.0);
		double hOeil = 2;
		eTypeVisee bord = eTypeVisee.soleilBordInf;
		
		NavDateHeure decembre = NavDateHeureFactory.fromStringSafe("10/12/1968 10:00:00");
		ICorrectionDeVisee cvSolaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, decembre, 0.0, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 10.16, EPSILON_ANGLE);

		NavDateHeure juillet = NavDateHeureFactory.fromStringSafe("10/07/1968 10:00:00");
		hEnDegre = cvSolaire.correctionTotale_EnDegre(Hi,  hOeil, juillet, 0.0, bord);

		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 9.6672, EPSILON_ANGLE);
	}

	@Test
	public void test012_lune_HorsDomaine() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 15.0);
		double hOeil = 0;
		eTypeVisee bord = eTypeVisee.luneBordInf;
		ICorrectionDeVisee cvLunaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		
		double hEnDegre = cvLunaire.correctionTotale_EnDegre(Hi,  hOeil,  heureObservation, 54, bord);
		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 63.22, EPSILON_ANGLE);

		hEnDegre = cvLunaire.correctionTotale_EnDegre(Hi,  hOeil,  heureObservation, 53, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 62.26, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotale_EnDegre(Hi,  hOeil,  heureObservation, 62, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 72.904, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotale_EnDegre(Hi,  0.0,  heureObservation, 61, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 71.938, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotale_EnDegre(Hi,  24.0,  heureObservation, 61, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 63.319, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotale_EnDegre(AngleFactory.fromDegre(0.0),  0.0,  heureObservation, 61, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 43.197, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotale_EnDegre(AngleFactory.fromDegre(50.0),  0.0,  heureObservation, 61, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 55.02, EPSILON_ANGLE);
	}

	@Test
	public void test012_lune_01() throws NavException {
		eTypeVisee bord = eTypeVisee.luneBordInf;
		ICorrectionDeVisee cvLunaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvLunaire.correctionTotale_EnDegre(AngleFactory.fromDegre(6.0),  0,  heureObservation, 54.5, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 60.55, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotale_EnDegre(AngleFactory.fromDegre(6.0),  3.0,  heureObservation, 54.5, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 57.444, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotale_EnDegre(AngleFactory.fromDegre(10.0),  3.0,  heureObservation, 55, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 60.711, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotale_EnDegre(AngleFactory.fromDegre(10.5),  3.0,  heureObservation, 55, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 60.870, EPSILON_ANGLE);
	}

	@Test
	public void test012_lune_02() throws NavException {
		eTypeVisee bord = eTypeVisee.luneBordSup;
		ICorrectionDeVisee cvLunaire = CorrectionDeViseeFactory.getCorrectionVisse(bord, errSextan);
		double hEnDegre = cvLunaire.correctionTotale_EnDegre(AngleFactory.fromDegre(6.0),  0,  heureObservation, 54.5, bord);

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 30.848, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotale_EnDegre(AngleFactory.fromDegre(6.0),  3.0,  heureObservation, 54.5, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 27.744, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotale_EnDegre(AngleFactory.fromDegre(10.0),  3.0,  heureObservation, 55, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 30.711, EPSILON_ANGLE);

		// ----------------------------------------------------------
		hEnDegre = cvLunaire.correctionTotale_EnDegre(AngleFactory.fromDegre(10.5),  3.0,  heureObservation, 55, bord);
		hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 30.870, EPSILON_ANGLE);
	}
}
