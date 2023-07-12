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

public class MareeTest2 {
	private static final Logger logger = LoggerFactory.getLogger(MareeTest2.class);

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
		Maree m1 = new Maree(NavDateHeureFactory.fromString("16:53"), new Hauteur(4.0));
		Maree m2 = new Maree(NavDateHeureFactory.fromString("22:05"), new Hauteur(1.1));

		CalculsMaree cm = new CalculsMaree();
		Hauteur piedPilote = new Hauteur(0.0);
		NavDateHeure h = cm.CalculHeure(m1, m2, piedPilote, new Hauteur(3.5));
		assertEquals(h.asHeureDecimale(), NavDateHeureFactory.fromString("18:12").asHeureDecimale(), precisionEnMinute);

		Hauteur H = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("19:00"));
		assertEquals(H.getValInMetre(), 2.95, precisionEnMetre);

		h = cm.CalculHeureV2(m1, m2, piedPilote, new Hauteur(3.5));
		assertEquals(h.asHeureDecimale(), NavDateHeureFactory.fromString("18:18").asHeureDecimale(), precisionEnMinute);

		H = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("19:00"));
		assertEquals(H.getValInMetre(), 2.97, precisionEnMetre);
	}

	@Test
	public void test003_exo() throws NavException {
		Maree m1 = new Maree(NavDateHeureFactory.fromString("13:25"), new Hauteur(2.7));
		Maree m2 = new Maree(NavDateHeureFactory.fromString("19:21"), new Hauteur(6.8));

		CalculsMaree cm = new CalculsMaree();
		Hauteur H = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("16:25"));
		assertEquals(H.getValInMetre(), 4.78, precisionEnMetre);

		H = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("16:25"));
		assertEquals(H.getValInMetre(), 4.78, precisionEnMetre);
	}

	@Test
	public void test004_exo() throws NavException {
		Maree m1 = new Maree(NavDateHeureFactory.fromString("04:45"), new Hauteur(4.75));
		Maree m2 = new Maree(NavDateHeureFactory.fromString("10:55"), new Hauteur(0.9));

		CalculsMaree cm = new CalculsMaree();
		Hauteur H = cm.CalculHauteur(m1, m2, NavDateHeureFactory.fromString("8:00"));
		assertEquals(H.getValInMetre(), 2.668, precisionEnMetre);

		H = cm.CalculHauteurV2(m1, m2, NavDateHeureFactory.fromString("8:00"));
		assertEquals(H.getValInMetre(), 2.661, precisionEnMetre);

		Hauteur piedPilote = new Hauteur(0.0);
		Hauteur HPassage = new Hauteur(3.5);
		NavDateHeure h = cm.CalculHeure(m1, m2, piedPilote, HPassage);
		assertEquals(h.asHeureDecimale(), 7.11, precisionEnMinute);

		h = cm.CalculHeureV2(m1, m2, piedPilote, HPassage);
		assertEquals(h.asHeureDecimale(), 7.13, precisionEnMinute);
	}

}
