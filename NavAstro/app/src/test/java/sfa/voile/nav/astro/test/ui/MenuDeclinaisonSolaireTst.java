package sfa.voile.nav.astro.test.ui;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sfa.voile.nav.astro.modele.Declinaison;
import sfa.voile.nav.astro.ui.menus.MenuPrincipal;


public class MenuDeclinaisonSolaireTst {
	double EPSILON = 1/ (360.0 * 60 * 60 * 10);
	
    @BeforeAll
    static public void setupBeforeAll() {
        System.out.println("setupBeforeAll");
        ScenarioAxTxt.setODTMode();        
    }

    @BeforeEach
    public void setupBefore() {
        System.out.println("setupBefore");
    }

    
    @AfterAll
    static public void cleanAfter() {
        System.out.println("cleanAfter");
        ScenarioAxTxt.cleanODTScenario();
    }

    @AfterEach
    public void cleanAfterClass() {
        System.out.println("cleanAfterClass");
    }

    @Test
    public void test1() {
    	ScenarioDeclinaisonSolaieAxTxt.addScenario("test1");
    	MenuPrincipal m = new MenuPrincipal();
    	boolean rc = m.affichageItemsDuMenu();
    	assertEquals(rc, true);
    	Object x = m.getLastComputedObject();
    	assertTrue(x instanceof Declinaison);
    	assertTrue( Math.abs(((Declinaison)x).getVal() - 3.00) < EPSILON );    	
    }


}
