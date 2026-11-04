package sfa.test.z.allinone;

import org.junit.runners.Suite;

import sfa.nav.infra.tools.i18n.zI18nTestSuite;
import sfa.nav.maree.calculs.zMareeTestSuite;
import sfa.nav.model.zModelTestSuite;
import sfa.nav.nav.astro.zNavAstroTestSuite;
import sfa.nav.nav.calculs.zNavTestSuite;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	zI18nTestSuite.class,
	zMareeTestSuite.class, 
	zModelTestSuite.class,
	zNavTestSuite.class,  
	zNavAstroTestSuite.class,
	})

public class AllTestSuite {
}
