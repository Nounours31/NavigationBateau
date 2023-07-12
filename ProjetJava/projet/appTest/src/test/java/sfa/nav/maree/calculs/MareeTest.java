package sfa.nav.maree.calculs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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

public class MareeTest {
	private static final Logger logger = LoggerFactory.getLogger(MareeTest.class);

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
	public void test002_CasSimpleCalculHauteur() throws NavException {
		CalculsMaree cm = new CalculsMaree();
		NavDateHeure Heure1 = NavDateHeureFactory.fromString("8:00");
		Hauteur Hauteur1 = new Hauteur(10.5);
		NavDateHeure Heure2 = NavDateHeureFactory.fromString("13:50");
		Hauteur Hauteur2 = new Hauteur(1.0);
		Maree m1 = new Maree(Heure1, Hauteur1);
		Maree m2 = new Maree(Heure2, Hauteur2);
		NavDateHeure H = NavDateHeureFactory.fromString("11:00");

		Hauteur h = cm.CalculHauteur(m1, m2, H);
		assertEquals(h.getValInMetre(), 5.54, precisionEnMetre);
	}

	@Test
	public void test002_CasPMversBM() throws NavException {
		CalculsMaree cm = new CalculsMaree();
		NavDateHeure Heure1 = NavDateHeureFactory.fromString("8:00");
		Hauteur Hauteur1 = new Hauteur(10.5);
		NavDateHeure Heure2 = NavDateHeureFactory.fromString("13:50");
		Hauteur Hauteur2 = new Hauteur(1.0);

		Maree m1 = new Maree(Heure1, Hauteur1);
		Maree m2 = new Maree(Heure2, Hauteur2);

		NavDateHeure H = NavDateHeureFactory.fromString("11:00");
		Hauteur h = cm.CalculHauteur(m1, m2, H);
		assertEquals(h.getValInMetre(), 5.54, precisionEnMetre);
	}

	@Test
	public void test003_CasSimpleCalculHoraire() throws NavException {
		CalculsMaree cm = new CalculsMaree();
		NavDateHeure Heure1 = NavDateHeureFactory.fromString("8:00");
		Hauteur Hauteur1 = new Hauteur(10.5);
		NavDateHeure Heure2 = NavDateHeureFactory.fromString("13:50");
		Hauteur Hauteur2 = new Hauteur(1.0);
		Maree m1 = new Maree(Heure1, Hauteur1, 0);
		Maree m2 = new Maree(Heure2, Hauteur2, 0);

		NavDateHeure h = cm.CalculHeure(m1, m2, new Hauteur(0.0), new Hauteur(5.54));
		assertEquals(h.asHeureDecimale(), 11.0, precisionEnMinute);
	}

	@Test
	public void test004_intervaldepassage() throws NavException {
		String reponse = "{\"2023-06-21\":[[\"tide.low\",\"04:00\",\"2.56\",\"---\"],[\"tide.high\",\"09:48\",\"9.98\",\"69\"],[\"tide.low\",\"16:13\",\"2.80\",\"---\"],[\"tide.high\",\"21:56\",\"10.22\",\"66\"]],\"2023-06-22\":[[\"tide.low\",\"04:34\",\"2.78\",\"---\"],[\"tide.high\",\"10:23\",\"9.76\",\"64\"],[\"tide.low\",\"16:46\",\"3.07\",\"---\"],[\"tide.high\",\"22:30\",\"9.95\",\"61\"]],\"2023-06-23\":[[\"tide.low\",\"05:07\",\"3.07\",\"---\"],[\"tide.high\",\"10:57\",\"9.46\",\"58\"],[\"tide.low\",\"17:20\",\"3.40\",\"---\"],[\"tide.high\",\"23:05\",\"9.62\",\"56\"]],\"2023-06-24\":[[\"tide.low\",\"05:41\",\"3.40\",\"---\"],[\"tide.high\",\"11:32\",\"9.14\",\"53\"],[\"tide.low\",\"17:55\",\"3.74\",\"---\"],[\"tide.high\",\"23:42\",\"9.26\",\"50\"]],\"2023-06-25\":[[\"tide.low\",\"06:18\",\"3.71\",\"---\"],[\"tide.high\",\"12:12\",\"8.84\",\"47\"],[\"tide.low\",\"18:36\",\"4.05\",\"---\"],[\"tide.none\",\"--:--\",\"---\",\"---\"]],\"2023-06-26\":[[\"tide.high\",\"00:25\",\"8.93\",\"45\"],[\"tide.low\",\"07:02\",\"3.98\",\"---\"],[\"tide.high\",\"12:59\",\"8.59\",\"43\"],[\"tide.low\",\"19:26\",\"4.28\",\"---\"]],\"2023-06-27\":[[\"tide.high\",\"01:17\",\"8.69\",\"42\"],[\"tide.low\",\"07:56\",\"4.15\",\"---\"],[\"tide.high\",\"13:57\",\"8.48\",\"41\"],[\"tide.low\",\"20:29\",\"4.36\",\"---\"]]}";
		reponse = "{\"2023-06-23\":[[\"tide.low\",\"05:07\",\"3.07\",\"---\"],[\"tide.high\",\"10:57\",\"9.47\",\"58\"],[\"tide.low\",\"17:20\",\"3.40\",\"---\"],[\"tide.high\",\"23:05\",\"9.62\",\"56\"]],\"2023-06-24\":[[\"tide.low\",\"05:41\",\"3.40\",\"---\"],[\"tide.high\",\"11:32\",\"9.14\",\"53\"],[\"tide.low\",\"17:55\",\"3.74\",\"---\"],[\"tide.high\",\"23:42\",\"9.26\",\"50\"]],\"2023-06-25\":[[\"tide.low\",\"06:18\",\"3.71\",\"---\"],[\"tide.high\",\"12:12\",\"8.84\",\"47\"],[\"tide.low\",\"18:36\",\"4.05\",\"---\"],[\"tide.none\",\"--:--\",\"---\",\"---\"]],\"2023-06-26\":[[\"tide.high\",\"00:25\",\"8.93\",\"45\"],[\"tide.low\",\"07:02\",\"3.98\",\"---\"],[\"tide.high\",\"12:58\",\"8.59\",\"43\"],[\"tide.low\",\"19:26\",\"4.28\",\"---\"]],\"2023-06-27\":[[\"tide.high\",\"01:17\",\"8.69\",\"42\"],[\"tide.low\",\"07:56\",\"4.15\",\"---\"],[\"tide.high\",\"13:57\",\"8.48\",\"41\"],[\"tide.low\",\"20:29\",\"4.36\",\"---\"]],\"2023-06-28\":[[\"tide.high\",\"02:22\",\"8.60\",\"42\"],[\"tide.low\",\"09:03\",\"4.16\",\"---\"],[\"tide.high\",\"15:08\",\"8.58\",\"43\"],[\"tide.low\",\"21:41\",\"4.21\",\"---\"]],\"2023-06-29\":[[\"tide.high\",\"03:33\",\"8.75\",\"46\"],[\"tide.low\",\"10:13\",\"3.95\",\"---\"],[\"tide.high\",\"16:16\",\"8.92\",\"49\"],[\"tide.low\",\"22:49\",\"3.82\",\"---\"]]}";
		ArrayList<Maree> lM = Maree.fromListeShomToListeMaree(reponse);

		CalculsMaree cm = new CalculsMaree();
		List<IntervalHoraire> h = cm.CalculIntervalPassageParJour(lM, new Hauteur(0.0), new Hauteur(5.54));
		System.out.println(IntervalHoraire.toString(h));
	}
}
