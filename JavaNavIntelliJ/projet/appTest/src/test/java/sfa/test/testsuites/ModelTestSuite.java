package sfa.test.testsuites;

import org.junit.runners.Suite;

import sfa.nav.lib.model.AngleTest;
import sfa.nav.lib.model.CapTest;
import sfa.nav.lib.model.LatitudeTest;
import sfa.nav.lib.model.LongitudeTest;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({AngleTest.class, LatitudeTest.class, LongitudeTest.class, CapTest.class})

public class ModelTestSuite {
}
