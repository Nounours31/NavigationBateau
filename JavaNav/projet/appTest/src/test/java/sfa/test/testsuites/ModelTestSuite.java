package sfa.test.testsuites;

import org.junit.runners.Suite;

import sfa.test.nav.model.AngleTest;
import sfa.test.nav.model.CapTest;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({AngleTest.class, CapTest.class})

public class ModelTestSuite {
}
