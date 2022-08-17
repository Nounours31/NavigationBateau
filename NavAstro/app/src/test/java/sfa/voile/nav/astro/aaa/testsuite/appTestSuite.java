/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5Suite.java to edit this template
 */
package sfa.voile.nav.astro.aaa.testsuite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import sfa.voile.nav.astro.test.ui.MenuPrincipalTst;

/**
 *
 * @author pfs
 */


// https://junit.org/junit5/docs/current/user-guide/#junit-platform-suite-engine
@Suite
@SuiteDisplayName("JUnit Suite test pour l'app NavAstro")
@SelectClasses({
	// sfa.voile.nav.astro.test.App.class, 
	sfa.voile.nav.astro.test.parsers.GeneriqueDataFormatTst.class,
	sfa.voile.nav.astro.test.parsers.AngleFormatTst.class,
	sfa.voile.nav.astro.test.parsers.HeureFormatTst.class,
	sfa.voile.nav.astro.test.parsers.LatitudeFormatTst.class,
	sfa.voile.nav.astro.test.parsers.LongitudeFormatTst.class,
	sfa.voile.nav.astro.test.parsers.DeclinaisonFormatTst.class,
	MenuPrincipalTst.class
	})

public class appTestSuite {
}
