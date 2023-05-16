package sfa.test.z.allinone;

import org.junit.runners.Suite;

import sfa.test.testsuites.I18nTestSuite;
import sfa.test.testsuites.MathsTestSuite;
import sfa.test.testsuites.ModelTestSuite;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({MathsTestSuite.class, ModelTestSuite.class, I18nTestSuite.class})

public class AllTestSuite {
}
