package sfa.test.testsuites;

import org.junit.runners.Suite;

import sfa.nav.model.AngleTest;
import sfa.nav.model.CapTest;
import sfa.nav.model.LatitudeTest;
import sfa.nav.model.LongitudeTest;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({MathsTestSuite.class, ModelTestSuite.class, I18nTestSuite.class})

public class AllTestSuite {
}
