package sfa.nav.nav.calculs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.model.tools.DataOrthoDromie;

public class NavSelonLesAxesPrincipaux {
	private static final Logger logger = LoggerFactory.getLogger(NavSelonLesAxesPrincipaux.class);

	private static double precisionCap = Constantes.DemiDegreEnDegre;
	private static double precisionDistance = 100.0; // 100 m ... Constantes.milleMarinEnMetre;

	private static CalculsDeNavigation ca = null;

	// ------------------------------------------------------------------------------------
	// Init zone
	// ------------------------------------------------------------------------------------
	// Paris :-)

	@BeforeClass
	public static void initOnlyOnce() throws NavException {
		ca = new CalculsDeNavigation();
	}

	@AfterClass
	public static void cleanOnlyOnce() {
		ca = null;
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
	public void LoxoPleinSud() throws NavException {
		DataLoxodromieCapDistance infos = ca.capLoxodromique(zaListePositionsPredefinies.Paris,
				zaListePositionsPredefinies.EnDessousDeParis5degre);

		assertEquals(infos.distance().distanceInMilleNautique(), 300, precisionDistance);
		assertEquals(infos.cap().asDegre(), 180.0, precisionCap);
	}

	@Test
	public void LoxoPleinNord() throws NavException {
		DataLoxodromieCapDistance infos = ca.capLoxodromique(zaListePositionsPredefinies.Paris,
				zaListePositionsPredefinies.EnDessusDeParis5degre);

		assertEquals(infos.distance().distanceInMilleNautique(), 300, precisionDistance);
		assertEquals(infos.cap().asDegre(), 0.0, precisionCap);
	}

	@Test
	public void LoxoPleinEst() throws NavException {
		DataLoxodromieCapDistance infos = ca.capLoxodromique(zaListePositionsPredefinies.Paris,
				zaListePositionsPredefinies.ADroiteDeParis5degre);

		assertEquals(infos.distance().distanceInMilleNautique(), 197.4, precisionDistance);
		assertEquals(infos.cap().asDegre(), 90.0, precisionCap);
	}

	@Test
	public void LoxoPleinOuest() throws NavException {
		DataLoxodromieCapDistance infos = ca.capLoxodromique(zaListePositionsPredefinies.Paris,
				zaListePositionsPredefinies.AGaucheDeParis5degre);

		assertEquals(infos.distance().distanceInMilleNautique(), 197.4, precisionDistance);
		assertEquals(infos.cap().asDegre(), 270.0, precisionCap);
	}

	@Test
	public void OrthoPleinSud() throws NavException {
		DataOrthoDromie infos = ca.capOrthodromique(zaListePositionsPredefinies.Paris,
				zaListePositionsPredefinies.EnDessousDeParis5degre);

		assertEquals(infos.distance().distanceInMilleNautique(), 300, precisionDistance);
		assertEquals(infos.cap().asDegre(), 180.0, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), 46.35, precisionCap);
	}

	@Test
	public void OrthoPleinNord() throws NavException {
		DataOrthoDromie infos = ca.capOrthodromique(zaListePositionsPredefinies.Paris,
				zaListePositionsPredefinies.EnDessusDeParis5degre);

		assertEquals(infos.distance().distanceInMilleNautique(), 300, precisionDistance);
		assertEquals(infos.cap().asDegre(), 0.0, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), 51.35, precisionCap);
	}

	@Test
	public void OrthoPleinEst() throws NavException {
		DataOrthoDromie infos = ca.capOrthodromique(zaListePositionsPredefinies.Paris,
				zaListePositionsPredefinies.ADroiteDeParis5degre);

		assertEquals(infos.distance().distanceInMilleNautique(), 197.5, precisionDistance);
		assertEquals(infos.cap().asDegre(), 88, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), zaListePositionsPredefinies.Paris.latitude().asDegre(),
				precisionCap);
	}

	@Test
	public void OrthoPleinOuest() throws NavException {
		DataOrthoDromie infos = ca.capOrthodromique(zaListePositionsPredefinies.Paris,
				zaListePositionsPredefinies.AGaucheDeParis5degre);

		assertEquals(infos.distance().distanceInMilleNautique(), 197.4, precisionDistance);
		assertEquals(infos.cap().asDegre(), 272.0, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), zaListePositionsPredefinies.Paris.latitude().asDegre(),
				precisionCap);
	}

	@Test
	public void LoxoPleinSudEquatorial() throws NavException {
		DataLoxodromieCapDistance infos = ca.capLoxodromique(zaListePositionsPredefinies.EquateurDessus,
				zaListePositionsPredefinies.EquateurDessous);

		assertEquals(infos.distance().distanceInMilleNautique(), 600, precisionDistance);
		assertEquals(infos.cap().asDegre(), 180.0, precisionCap);
	}

	@Test
	public void LoxoPleinNordEquatorial() throws NavException {
		DataLoxodromieCapDistance infos = ca.capLoxodromique(zaListePositionsPredefinies.EquateurDessous,
				zaListePositionsPredefinies.EquateurDessus);

		assertEquals(infos.distance().distanceInMilleNautique(), 600, precisionDistance);
		assertEquals(infos.cap().asDegre(), 0.0, precisionCap);
	}

	@Test
	public void LoxoPleinEstEquatorial() throws NavException {
		DataLoxodromieCapDistance infos = ca.capLoxodromique(zaListePositionsPredefinies.EquateurGauche,
				zaListePositionsPredefinies.EquateurDroite);

		assertEquals(infos.distance().distanceInMilleNautique(), 600, precisionDistance);
		assertEquals(infos.cap().asDegre(), 90.0, precisionCap);
	}

	@Test
	public void LoxoPleinOuestEquatorial() throws NavException {
		DataLoxodromieCapDistance infos = ca.capLoxodromique(zaListePositionsPredefinies.EquateurDroite,
				zaListePositionsPredefinies.EquateurGauche);

		assertEquals(infos.distance().distanceInMilleNautique(), 600, precisionDistance);
		assertEquals(infos.cap().asDegre(), 270.0, precisionCap);
	}

	@Test
	public void OrthoPleinSudEquatorial() throws NavException {
		DataOrthoDromie infos = ca.capOrthodromique(zaListePositionsPredefinies.EquateurDessus,
				zaListePositionsPredefinies.EquateurDessous);

		assertEquals(infos.distance().distanceInMilleNautique(), 600, precisionDistance);
		assertEquals(infos.cap().asDegre(), 180.0, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), 0.0, precisionCap);
	}

	@Test
	public void OrthoPleinNordEquatorial() throws NavException {
		DataOrthoDromie infos = ca.capOrthodromique(zaListePositionsPredefinies.EquateurDessous,
				zaListePositionsPredefinies.EquateurDessus);

		assertEquals(infos.distance().distanceInMilleNautique(), 600, precisionDistance);
		assertEquals(infos.cap().asDegre(), 0.0, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), 0.0, precisionCap);
	}

	@Test
	public void OrthoPleinEstEquatorial() throws NavException {
		DataOrthoDromie infos = ca.capOrthodromique(zaListePositionsPredefinies.EquateurGauche,
				zaListePositionsPredefinies.EquateurDroite);

		assertEquals(infos.distance().distanceInMilleNautique(), 600, precisionDistance);
		assertEquals(infos.cap().asDegre(), 90, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), 0.0, precisionCap);
	}

	@Test
	public void OrthoPleinOuestEquatorial() throws NavException {
		DataOrthoDromie infos = ca.capOrthodromique(zaListePositionsPredefinies.EquateurDroite,
				zaListePositionsPredefinies.EquateurGauche);

		assertEquals(infos.distance().distanceInMilleNautique(), 600, precisionDistance);
		assertEquals(infos.cap().asDegre(), 270.0, precisionCap);
		assertEquals(infos.vertex().latitude().asDegre(), 0.0, precisionCap);
	}

	@Test
	public void test001_EstCeQueCaMarche() {
		int sum = 5; // valeur a controler
		assertEquals(sum, 5);
		assertNotNull(ca);
	}
}
