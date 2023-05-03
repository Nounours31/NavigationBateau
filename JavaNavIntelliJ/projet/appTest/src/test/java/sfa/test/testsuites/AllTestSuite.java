package sfa.test.testsuites;

import org.junit.runners.Suite;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({MathsTestSuite.class, ModelTestSuite.class, I18nTestSuite.class})

public class AllTestSuite {
}
