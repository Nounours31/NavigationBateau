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

import sfa.nav.astro.calculs.CorrectionDeVisee;
import sfa.nav.astro.calculs.CorrectionDeVisee.ErreurSextan;
import sfa.nav.astro.calculs.CorrectionDeVisee.eTypeCorrection;
import sfa.nav.astro.calculs.DroiteDeHauteur;
import sfa.nav.astro.calculs.DroiteDeHauteur.DroiteHauteurPositionnee;
import sfa.nav.astro.calculs.DroiteDeHauteur.eBordSoleil;
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


public class CorrectionsTest  {
	private static final Logger logger = LoggerFactory.getLogger(CorrectionsTest.class);
	private static final CorrectionDeVisee cv = new CorrectionDeVisee(eTypeCorrection.soleil, 
			new ErreurSextan(AngleOrienteFactory.fromDegre(0.0), AngleOrienteFactory.fromDegre(0.0)));

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
		eBordSoleil bord = eBordSoleil.inf;
		double hEnDegre = cv.correctionEnDegre(Hi,  hOeil, bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 8.7, EPSILON_ANGLE);
	}

	@Test
	public void test002() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 6.0);
		double hOeil = 1.0;
		eBordSoleil bord = eBordSoleil.inf;
		double hEnDegre = cv.correctionEnDegre(Hi,  hOeil,  bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 7.5, EPSILON_ANGLE);
	}

	@Test
	public void test002bis() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 5.0);
		double hOeil = 1.0;
		eBordSoleil bord = eBordSoleil.inf;
		double hEnDegre = cv.correctionEnDegre(Hi,  hOeil,  bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 8.7, EPSILON_ANGLE);
	}
	@Test
	public void test003() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 91.0);
		double hOeil = 1.0;
		eBordSoleil bord = eBordSoleil.inf;
		double hEnDegre = cv.correctionEnDegre(Hi,  hOeil,  bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 8.7, EPSILON_ANGLE);
	}
	@Test
	public void test004() throws NavException {
		Angle Hi = AngleFactory.fromDegre( 6.0);
		double hOeil = 6.0;
		eBordSoleil bord = eBordSoleil.inf;
		double hEnDegre = cv.correctionEnDegre(Hi,  hOeil,  bord );

		double hEnMinuteDArc =hEnDegre * 60.0;
		assertEquals (hEnMinuteDArc, 8.7, EPSILON_ANGLE);
	}
}
