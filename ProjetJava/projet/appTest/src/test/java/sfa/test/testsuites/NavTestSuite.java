package sfa.test.testsuites;

import org.junit.runners.Suite;

import sfa.nav.maree.calculs.MareeTest;
import sfa.nav.nav.calculs.Loxodromie_aAngleConstantTest;
import sfa.nav.nav.calculs.Orthodromie_Loxo_CoursDeNavigation_GrilleDeCalculTest;
import sfa.nav.nav.calculs.Orthodromie_aDistanceMinimale_Test;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	Loxodromie_aAngleConstantTest.class, 
	Orthodromie_aDistanceMinimale_Test.class, 
	Orthodromie_Loxo_CoursDeNavigation_GrilleDeCalculTest.class
	})

public class NavTestSuite {
}
