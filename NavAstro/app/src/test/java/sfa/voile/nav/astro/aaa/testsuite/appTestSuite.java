/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5Suite.java to edit this template
 */
package sfa.voile.nav.astro.aaa.testsuite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 *
 * @author pfs
 */


// https://junit.org/junit5/docs/current/user-guide/#junit-platform-suite-engine
@Suite
@SuiteDisplayName("JUnit Suite test pour l'app NavAstro")
@SelectClasses({
	// sfa.voile.nav.astro.test.App.class, 
	sfa.voile.nav.astro.test.tools.GeneriqueDataFormatTst.class,
	sfa.voile.nav.astro.test.tools.AngleFormatTst.class,
	sfa.voile.nav.astro.test.tools.HeureFormatTst.class,
	sfa.voile.nav.astro.test.tools.LatitudeFormatTst.class,
	sfa.voile.nav.astro.test.tools.DeclinaisonFormatTst.class,
	})

public class appTestSuite {
}
