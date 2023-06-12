package sfa.nav.maree.calculs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Cap;
import sfa.nav.model.CapFactory;
import sfa.nav.model.Distance;
import sfa.nav.model.DistanceFactory;
import sfa.nav.model.Hauteur;
import sfa.nav.model.Heure;
import sfa.nav.model.HeureFactory;
import sfa.nav.model.Latitude;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.Longitude;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.Maree;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.nav.calculs.CalculsAngulaires;


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
	public void test001_EstCeQueCaMarche () {
		int sum = 6;
		assertEquals(sum, 6);		
	}

	@Test
	public void test002_CasSimpleCalculHauteur () throws NavException {
		CalculsMaree cm = new CalculsMaree();
		Heure Heure1 = HeureFactory.fromString("8:00");
		Hauteur Hauteur1 = new Hauteur(10.5);
		Heure Heure2 = HeureFactory.fromString("13:50");
		Hauteur Hauteur2 = new Hauteur(1.0);
		Maree m1 = new Maree(Heure1, Hauteur1);
		Maree m2 = new Maree(Heure2, Hauteur2);
		Heure H = HeureFactory.fromString("11:00");
		
		Hauteur h = cm.CalculHauteur(m1, m2, H);
		assertEquals(h.getValInMetre(), 5.54, precisionEnMetre);
	}

	@Test
	public void test003_CasSimpleCalculHoraire () throws NavException {
		CalculsMaree cm = new CalculsMaree();
		Heure Heure1 = HeureFactory.fromString("8:00");
		Hauteur Hauteur1 = new Hauteur(10.5);
		Heure Heure2 = HeureFactory.fromString("13:50");
		Hauteur Hauteur2 = new Hauteur(1.0);
		Maree m1 = new Maree(Heure1, Hauteur1);
		Maree m2 = new Maree(Heure2, Hauteur2);
		
		Heure h = cm.CalculHeure(m1, m2, 0.0, 5.54);
		assertEquals(h.getHeureDecimale(), 11.0, precisionEnMinute);
	}
}
