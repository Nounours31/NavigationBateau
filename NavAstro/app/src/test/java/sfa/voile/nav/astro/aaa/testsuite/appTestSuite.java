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
@SuiteDisplayName("JUnit Platform Suite Demo")
@SelectClasses({sfa.voile.nav.astro.NavAstro.AppTest.class, sfa.voile.nav.astro.tools.FormatHeureAstroTest.class})
public class appTestSuite {
	
}
