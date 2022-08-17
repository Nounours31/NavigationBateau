package sfa.voile.nav.astro.test.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sfa.voile.nav.astro.ui.menus.MenuPrincipal;


public class MenuPrincipalTst {
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
    	ScenarioAxTxt.addScenario("test1");
    	boolean rc = MenuPrincipal.display();
    	assertEquals(rc, true);
    }

    public void test2() {
    	ScenarioAxTxt.addScenario("test2");
    	boolean rc = MenuPrincipal.display();
    	assertEquals(rc, true);
    }

    public void test3() {
    	ScenarioAxTxt.addScenario("test3");
    	boolean rc = MenuPrincipal.display();
    	assertEquals(rc, true);
    }

    public void test4() {
    	ScenarioAxTxt.addScenario("test4");
    	boolean rc = MenuPrincipal.display();
    	assertEquals(rc, true);
    }
    
    public void test5() {
    	ScenarioAxTxt.addScenario("test5");
    	boolean rc = MenuPrincipal.display();
    	assertEquals(rc, true);
    }
    
    public void test6() {
    	ScenarioAxTxt.addScenario("test6");
    	boolean rc = MenuPrincipal.display();
    	assertEquals(rc, true);
    }
}
