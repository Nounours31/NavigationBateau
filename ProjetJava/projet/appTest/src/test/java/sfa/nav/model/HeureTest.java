package sfa.nav.model;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.odt.tools.HandlerIntStringStringString;
import sfa.nav.odt.tools.HandlerStringStringString;

public class HeureTest extends NavDateHeure {
	File output;
	private static final Logger logger = LoggerFactory.getLogger(HeureTest.class);

	// ------------------------------------------------------------------------------------
	// Init zone
	// ------------------------------------------------------------------------------------
	@BeforeClass
	public static void initOnlyOnce() {
		NavDateHeure.myZone("Europe/Paris");
	}

	@AfterClass
	public static void cleanOnlyOnce() {
		NavDateHeure.myZone("Europe/Paris");
	}

	@Before
	public void beforeMethode() {
		NavDateHeure.myZone("Europe/Paris");
	}

	@After
	public void afterMethode() {
		NavDateHeure.myZone("Europe/Paris");
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
		ZonedDateTime instant = ZonedDateTime.now();
		ZonedDateTime hier = instant.minusDays(1);
		DateTimeFormatter formatterZ = DateTimeFormatter.ofPattern("yyyy/MM/dd");

		HandlerStringStringString[] allVals = {
				new HandlerStringStringString("Cas 1", "1.00", "xxxx 01:00:00 CEST [yyyy 23:00:00 UTC]"),
				new HandlerStringStringString("Cas 2", "1.5", "xxxx 01:30:00 CEST [yyyy 23:30:00 UTC]"),
				new HandlerStringStringString("Cas 3", "1:50", "xxxx 01:50:00 CEST [yyyy 23:50:00 UTC]"),
				new HandlerStringStringString("Cas 4", "1:5", "xxxx 01:05:00 CEST [yyyy 23:05:00 UTC]"),
				new HandlerStringStringString("Cas 5", "1:05", "xxxx 01:05:00 CEST [yyyy 23:05:00 UTC]"),
				new HandlerStringStringString("Cas 6", "1:5:2", "xxxx 01:05:02 CEST [yyyy 23:05:02 UTC]"),
				new HandlerStringStringString("Cas 7", "1:5:20", "xxxx 01:05:20 CEST [yyyy 23:05:20 UTC]"),
				new HandlerStringStringString("Cas 8", "1:5:02", "xxxx 01:05:02 CEST [yyyy 23:05:02 UTC]"),
				new HandlerStringStringString("Cas 9", "1:5:20", "xxxx 01:05:20 CEST [yyyy 23:05:20 UTC]"),
				new HandlerStringStringString("Cas 10", "09:5:20", "xxxx 09:05:20 CEST [xxxx 07:05:20 UTC]"),
				new HandlerStringStringString("Cas 11", "9:5:20", "xxxx 09:05:20 CEST [xxxx 07:05:20 UTC]"),
				new HandlerStringStringString("Cas 12", "14:5:20", "xxxx 14:05:20 CEST [xxxx 12:05:20 UTC]"), };

		NavDateHeure h = null;
		for (HandlerStringStringString val : allVals) {
			String s = val._v;
			String ref = val._w.replaceAll("xxxx", formatterZ.format(instant)).replaceAll("yyyy",
					formatterZ.format(hier));
			h = NavDateHeureFactory.fromString(s);
			logger.debug("Cas: {} / heure: >{}< / depuis {} -- Refrence {}", val._u, h.toString(), val._v, val._w);
			assertEquals(h.toString(), ref);
		}
	}

	@Test
	public void test003_ZONE() throws NavException {

		HandlerIntStringStringString[] allVals = {
				new HandlerIntStringStringString(1, "Cas 1", "2023/06/12 01:00:00 CEST",
						"2023/06/12 01:00:00 CEST [2023/06/11 23:00:00 UTC]"),
				new HandlerIntStringStringString(2, "Cas 2", "2023/06/12 01:01:00 CEST",
						"2023/06/12 01:01:00 CEST [2023/06/11 23:01:00 UTC]"),
				new HandlerIntStringStringString(3, "Cas 3", "12/06/2023 01:02:00 CEST",
						"2023/06/12 01:02:00 CEST [2023/06/11 23:02:00 UTC]"),
				new HandlerIntStringStringString(4, "Cas 4", "12/06/23 01:03:00 CEST",
						"2023/06/12 01:03:00 CEST [2023/06/11 23:03:00 UTC]"),
				new HandlerIntStringStringString(5, "Cas 5", "12/06 01:04:00 CEST",
						"2023/06/12 01:04:00 CEST [2023/06/11 23:04:00 UTC]"),

				new HandlerIntStringStringString(21, "Cas 21", "2023/06/12 01:00:00 UTC",
						"2023/06/12 03:00:00 CEST [2023/06/12 01:00:00 UTC]"),
				new HandlerIntStringStringString(22, "Cas 22", "2023/06/12 01:01:00 UTC",
						"2023/06/12 03:01:00 CEST [2023/06/12 01:01:00 UTC]"),
				new HandlerIntStringStringString(23, "Cas 23", "12/06/2023 01:02:00 UTC",
						"2023/06/12 03:02:00 CEST [2023/06/12 01:02:00 UTC]"),
				new HandlerIntStringStringString(24, "Cas 24", "12/06/23 01:03:00 UTC",
						"2023/06/12 03:03:00 CEST [2023/06/12 01:03:00 UTC]"),
				new HandlerIntStringStringString(25, "Cas 25", "12/06 01:04:00 UTC",
						"2023/06/12 03:04:00 CEST [2023/06/12 01:04:00 UTC]"),

				new HandlerIntStringStringString(31, "Cas 31", "2023/06/12 01:00:00 CEST",
						"2023/06/11 20:30:00 NDT [2023/06/11 23:00:00 UTC]"),
				new HandlerIntStringStringString(32, "Cas 32", "2023/06/12 01:01:00",
						"2023/06/12 01:01:00 GMT-04:00 [2023/06/12 05:01:00 UTC]"), };

		NavDateHeure h = null;
		for (HandlerIntStringStringString val : allVals) {
			if (val._i == 31)
				NavDateHeure.myZone("America/St_Johns");

			if (val._i == 32)
				NavDateHeure.myZone("GMT-4"); // antilles

			String s = val._v;
			String ref = val._w;
			h = NavDateHeureFactory.fromString(s);
			logger.debug("{}{} / heure: >{}< / depuis {} -- Refrence {}", val._u, val._i, h.toString(), val._v, val._w);

			assertEquals(h.toString(), ref);
		}
	}

	@Test
	public void test004_HeureLocale() throws NavException {

		HandlerStringStringString[] allVals = {
				new HandlerStringStringString("Cas 1", "2023/06/12 01:00:00",
						"2023/06/12 01:00:00 CEST [2023/06/11 23:00:00 UTC]"),
				new HandlerStringStringString("Cas 2", "2023/06/12 01:01:00",
						"2023/06/12 01:01:00 CEST [2023/06/11 23:01:00 UTC]"),
				new HandlerStringStringString("Cas 3", "12/06/2023 01:02:00",
						"2023/06/12 01:02:00 CEST [2023/06/11 23:02:00 UTC]"),
				new HandlerStringStringString("Cas 4", "12/06/23 01:03:00",
						"2023/06/12 01:03:00 CEST [2023/06/11 23:03:00 UTC]"),
				new HandlerStringStringString("Cas 5", "12/06 01:04:00",
						"2023/06/12 01:04:00 CEST [2023/06/11 23:04:00 UTC]"), };

		NavDateHeure h = null;
		for (HandlerStringStringString val : allVals) {
			String s = val._v;
			String ref = val._w;
			h = NavDateHeureFactory.fromString(s);
			logger.debug("Cas: {} / heure: >{}< / depuis {} -- Refrence {}", val._u, h.toString(), val._v, val._w);
			assertEquals(h.toString(), ref);
		}
	}
}
