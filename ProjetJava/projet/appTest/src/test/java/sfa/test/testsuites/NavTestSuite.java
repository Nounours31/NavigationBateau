package sfa.test.testsuites;

import org.junit.runners.Suite;

import sfa.nav.maree.calculs.MareeTest;
import sfa.nav.nav.calculs.CoursDeNavigation_DocAncien_page4_loxodromie300Milles;
import sfa.nav.nav.calculs.CoursDeNavigation_DocAncien_page5_loxodromieEstime300Milles;
import sfa.nav.nav.calculs.CoursDeNavigation_DocAncien_page6_loxodromie;
import sfa.nav.nav.calculs.CoursDeNavigation_DocAncien_page7_orthodromie;
import sfa.nav.nav.calculs.Loxodromie_aAngleConstantTest;
import sfa.nav.nav.calculs.Orthodromie_Loxo_CoursDeNavigation_GrilleDeCalculTest;
import sfa.nav.nav.calculs.Orthodromie_aDistanceMinimale_Test;

import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	CoursDeNavigation_DocAncien_page4_loxodromie300Milles.class,
	CoursDeNavigation_DocAncien_page5_loxodromieEstime300Milles.class,
	CoursDeNavigation_DocAncien_page6_loxodromie.class,
	CoursDeNavigation_DocAncien_page7_orthodromie.class,
	Loxodromie_aAngleConstantTest.class, 
	Orthodromie_aDistanceMinimale_Test.class, 
	Orthodromie_Loxo_CoursDeNavigation_GrilleDeCalculTest.class,
	})

public class NavTestSuite {
}
