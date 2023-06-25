package sfa.test.testsuites;

import org.junit.runners.Suite;

import sfa.nav.model.AngleTest;
import sfa.nav.model.CapTest;
import sfa.nav.model.HeureTest;
import sfa.nav.model.LatitudeTest;
import sfa.nav.model.LongitudeTest;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AngleTest.class, 
	CapTest.class,
	HeureTest.class, 
	LatitudeTest.class, 
	LongitudeTest.class, 
	})

public class ModelTestSuite {
}
