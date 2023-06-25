package sfa.test.z.allinone;

import org.junit.runners.Suite;

import sfa.test.testsuites.I18nTestSuite;
import sfa.test.testsuites.MareeTestSuite;
import sfa.test.testsuites.NavTestSuite;
import sfa.test.testsuites.ModelTestSuite;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	I18nTestSuite.class,
	MareeTestSuite.class, 
	ModelTestSuite.class,
	NavTestSuite.class,  
	})

public class AllTestSuite {
}
