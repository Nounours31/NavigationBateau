package sfa.nav.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;

public class DureeTest extends NavDuree {
	double epsilon = 0.0001;
	private static final Logger logger = LoggerFactory.getLogger(DureeTest.class);

	// ------------------------------------------------------------------------------------
	// Init zone
	// ------------------------------------------------------------------------------------
	@BeforeClass
	public static void initOnlyOnce() {
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
	public void test002_fromdecimale() throws NavException {
		NavDuree x = NavDureeFactory.fromString("-1.0");
		assertEquals(x.dureeEnHeureDecimale, -1.0, epsilon);

		x = NavDureeFactory.fromString("1.0");
		assertEquals(x.dureeEnHeureDecimale, 1.0, epsilon);

		x = NavDureeFactory.fromString(".1");
		assertEquals(x.dureeEnHeureDecimale, 0.1, epsilon);

		x = NavDureeFactory.fromString("-.1");
		assertEquals(x.dureeEnHeureDecimale, -0.1, epsilon);

		x = NavDureeFactory.fromString("-10.0");
		assertEquals(x.dureeEnHeureDecimale, -10.0, epsilon);

		x = NavDureeFactory.fromString("-10.550");
		assertEquals(x.dureeEnHeureDecimale, -10.55, epsilon);

		x = NavDureeFactory.fromString("1.0");
		assertEquals(x.dureeEnHeureDecimale, 1.0, epsilon);

		x = NavDureeFactory.fromString("1.44");
		assertEquals(x.dureeEnHeureDecimale, 1.44, epsilon);

		x = NavDureeFactory.fromString("-10");
		assertEquals(x.dureeEnHeureDecimale, -10, epsilon);

		x = NavDureeFactory.fromString("10");
		assertEquals(x.dureeEnHeureDecimale, 10.0, epsilon);

		x = NavDureeFactory.fromString("1044445");
		assertEquals(x.dureeEnHeureDecimale, 1044445, epsilon);

		x = NavDureeFactory.fromString("-1044445");
		assertEquals(x.dureeEnHeureDecimale, -1044445.0, epsilon);

	}

	@Test
	public void test003_fromSexagedecimale() throws NavException {
		NavDuree x = NavDureeFactory.fromString("0:0:0");
		assertEquals(x.dureeEnHeureDecimale, .0, epsilon);

		x = NavDureeFactory.fromString("1:0:0");
		assertEquals(x.dureeEnHeureDecimale, 1, epsilon);

		x = NavDureeFactory.fromString("4545:0");
		assertEquals(x.dureeEnHeureDecimale, 4545, epsilon);

		x = NavDureeFactory.fromString("4545:0:0");
		assertEquals(x.dureeEnHeureDecimale, 4545, epsilon);

		x = NavDureeFactory.fromString("4545:59:0");
		assertEquals(x.dureeEnHeureDecimale, 4545.98333333333, epsilon);

		x = NavDureeFactory.fromString("4545:59:59");
		assertEquals(x.dureeEnHeureDecimale, 4545.99972222223, epsilon);

		x = NavDureeFactory.fromString("4545:59:59.9999999");
		assertEquals(x.dureeEnHeureDecimale, 4545.99999999999, epsilon);

		x = NavDureeFactory.fromString("-0:0:0");
		assertEquals(x.dureeEnHeureDecimale, .0, epsilon);

		x = NavDureeFactory.fromString("-1:0:0");
		assertEquals(x.dureeEnHeureDecimale, -1, epsilon);

		x = NavDureeFactory.fromString("- 1:0:0");
		assertEquals(x.dureeEnHeureDecimale, -1, epsilon);

		x = NavDureeFactory.fromString("+ 1:0:0");
		assertEquals(x.dureeEnHeureDecimale, 1, epsilon);

		x = NavDureeFactory.fromString("+1:0:0");
		assertEquals(x.dureeEnHeureDecimale, 1, epsilon);

		x = NavDureeFactory.fromString("-4545:0");
		assertEquals(x.dureeEnHeureDecimale, -4545, epsilon);

		x = NavDureeFactory.fromString("-4545:0:0");
		assertEquals(x.dureeEnHeureDecimale, -4545, epsilon);

		x = NavDureeFactory.fromString("-4545:59:0");
		assertEquals(x.dureeEnHeureDecimale, -4545.98333333333, epsilon);

		x = NavDureeFactory.fromString("-4545:59:59");
		assertEquals(x.dureeEnHeureDecimale, -4545.99972222223, epsilon);

		x = NavDureeFactory.fromString("-4545:59:59.9999999");
		assertEquals(x.dureeEnHeureDecimale, -4545.99999999999, epsilon);
	}

	@Test
	public void test004_enerreur() throws NavException {
		boolean except = false;
		try {
			NavDureeFactory.fromString("0:450:0");
		} catch (NavException e) {
			except = true;
			assertEquals(e.getMessage(), "Duree KO");
		}
		;
		assertTrue(except);
		except = false;
		try {
			NavDureeFactory.fromString("0.5:0:0");
		} catch (NavException e) {
			except = true;
			assertEquals(e.getMessage(), "Duree KO");
		}
		;
		assertTrue(except);
		except = false;
		try {
			NavDureeFactory.fromString("0:45.0:0");
		} catch (NavException e) {
			except = true;
			assertEquals(e.getMessage(), "Duree KO");
		}
		;
		assertTrue(except);
		except = false;
		try {
			NavDureeFactory.fromString("0:45:458");
		} catch (NavException e) {
			except = true;
			assertEquals(e.getMessage(), "Duree KO");
		}
		;
		assertTrue(except);
		except = false;
		try {
			NavDureeFactory.fromString("0:45:458.23");
		} catch (NavException e) {
			except = true;
			assertEquals(e.getMessage(), "Duree KO");
		}
		;
		assertTrue(except);
		except = false;
	}

}
