package sfa.test.testsuites;

import org.junit.runners.Suite;

import sfa.nav.maree.calculs.MareeTest;
import sfa.nav.nav.calculs.Loxodromie_aAngleConstantTest;
import sfa.nav.nav.calculs.Orthodromie_aDistanceMinimale_Test;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	MareeTest.class
	})

public class MareeTestSuite {
}
