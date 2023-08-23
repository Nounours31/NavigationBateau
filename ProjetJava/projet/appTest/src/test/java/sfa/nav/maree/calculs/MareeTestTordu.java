package sfa.nav.maree.calculs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Hauteur;
import sfa.nav.model.Maree;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.NavDateHeureFactory;

public class MareeTestTordu {
	private static final Logger logger = LoggerFactory.getLogger(MareeTestTordu.class);

	private static double precisionEnMetre = 0.01; // 1 cm
	private static double precisionEnMinute = 1.0 / 60.0; // 1 minute

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
	public void test001_EstCeQueCaMarche() {
		int sum = 6;
		assertEquals(sum, 6);
	}

	@Test
	public void test002_exo() throws NavException {
		Maree m1 = new Maree(NavDateHeureFactory.fromString("12:00"), new Hauteur(0.0));
		Maree m2 = new Maree(NavDateHeureFactory.fromString("18:00"), new Hauteur(6.0));

		CalculsMaree cm = new CalculsMaree();
		Hauteur piedPilote = new Hauteur(0.0);
		NavDateHeure h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(0.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("12:00").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(1.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("13:30").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(2.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("14:20").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(3.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("15:00").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(4.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("15:40").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(5.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("16:30").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(6.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("18:00").asHeureDecimaleFromMidnight(), precisionEnMinute);

		// --------------------------------------------------------

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(0.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("12:00").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(1.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("13:36").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(2.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("14:21").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(3.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("15:00").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(4.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("15:38").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(5.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("16:24").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(6.0));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("18:00").asHeureDecimaleFromMidnight(), precisionEnMinute);
	}

	@Test
	public void test003_exo() throws NavException {
		Maree m1 = new Maree(NavDateHeureFactory.fromString("12:00"), new Hauteur(0.0));
		Maree m2 = new Maree(NavDateHeureFactory.fromString("18:00"), new Hauteur(6.0));

		CalculsMaree cm = new CalculsMaree();
		Hauteur h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("12:00"));
		assertEquals(h.getValInMetre(), 0.0, precisionEnMetre);

		h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("13:30"));
		assertEquals(h.getValInMetre(), 1.0, precisionEnMetre);

		h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("14:20"));
		assertEquals(h.getValInMetre(), 2.0, precisionEnMetre);

		h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("15:00"));
		assertEquals(h.getValInMetre(), 3.0, precisionEnMetre);

		h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("15:40"));
		assertEquals(h.getValInMetre(), 4.0, precisionEnMetre);

		h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("16:30"));
		assertEquals(h.getValInMetre(), 5.0, precisionEnMetre);

		h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("18:00"));
		assertEquals(h.getValInMetre(), 6.0, precisionEnMetre);

		// --------------------------------------------------------

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("12:00"));
		assertEquals(h.getValInMetre(), 0.0, precisionEnMetre);

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("13:36"));
		assertEquals(h.getValInMetre(), 1.0, precisionEnMetre);

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("14:21"));
		assertEquals(h.getValInMetre(), 2.0, precisionEnMetre);

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("15:00"));
		assertEquals(h.getValInMetre(), 3.0, precisionEnMetre);

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("15:39"));
		assertEquals(h.getValInMetre(), 4.0, precisionEnMetre);

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("16:24"));
		assertEquals(h.getValInMetre(), 5.0, precisionEnMetre);

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("18:00"));
		assertEquals(h.getValInMetre(), 6.0, precisionEnMetre);
	}

	@Test
	public void test004_exo() throws NavException {
		Maree m1 = new Maree(NavDateHeureFactory.fromString("12:00"), new Hauteur(0.0));
		Maree m2 = new Maree(NavDateHeureFactory.fromString("18:00"), new Hauteur(6.0));

		CalculsMaree cm = new CalculsMaree();
		Hauteur piedPilote = new Hauteur(0.0);
		NavDateHeure h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(0.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("13:00").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(1.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("14:00").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(2.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("14:40").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(3.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("15:20").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(4.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("16:00").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(5.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("17:00").asHeureDecimaleFromMidnight(), precisionEnMinute);

		// --------------------------------------------------------

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(0.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("13:07").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(1.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("14:00").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(2.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("14:41").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(3.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("15:19").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(4.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("16:00").asHeureDecimaleFromMidnight(), precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(5.5));
		assertEquals(h.asHeureDecimaleFromMidnight(), NavDateHeureFactory.fromString("16:53").asHeureDecimaleFromMidnight(), precisionEnMinute);

	}

	@Test
	public void test005_exo() throws NavException {
		Maree m1 = new Maree(NavDateHeureFactory.fromString("12:00"), new Hauteur(0.0));
		Maree m2 = new Maree(NavDateHeureFactory.fromString("18:00"), new Hauteur(6.0));

		CalculsMaree cm = new CalculsMaree();
		Hauteur h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("13:00"));
		assertEquals(h.getValInMetre(), 0.5, precisionEnMetre);

		h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("14:00"));
		assertEquals(h.getValInMetre(), 1.5, precisionEnMetre);

		h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("14:40"));
		assertEquals(h.getValInMetre(), 2.5, precisionEnMetre);

		h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("15:20"));
		assertEquals(h.getValInMetre(), 3.5, precisionEnMetre);

		h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("16:00"));
		assertEquals(h.getValInMetre(), 4.5, precisionEnMetre);

		h = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("17:00"));
		assertEquals(h.getValInMetre(), 5.5, precisionEnMetre);

		// --------------------------------------------------------

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("13:07"));
		assertEquals(h.getValInMetre(), 0.5, precisionEnMetre);

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("14:00"));
		assertEquals(h.getValInMetre(), 1.5, precisionEnMetre);

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("14:41"));
		assertEquals(h.getValInMetre(), 2.5, precisionEnMetre);

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("15:19"));
		assertEquals(h.getValInMetre(), 3.5, precisionEnMetre);

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("16:00"));
		assertEquals(h.getValInMetre(), 4.5, precisionEnMetre);

		h = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("16:53"));
		assertEquals(h.getValInMetre(), 5.5, precisionEnMetre);

	}

}
