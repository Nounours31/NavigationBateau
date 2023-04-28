package sfa.nav.i18n;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class I18nTest extends I18n {
    private static final Logger logger = LoggerFactory.getLogger(I18nTest.class);
    
    // ------------------------------------------------------------------------------------
    // Init zone
    // ------------------------------------------------------------------------------------
	@BeforeClass
	public static void initOnlyOnce() {
	}

	@AfterClass
	public static void cleanOnlyOnce() {
	}

    @Before 
    public void beforeMethode() {
    }
    
    @After 
    public void afterMethode() {
    }
	
    // ------------------------------------------------------------------------------------
    // Les tests
    // ------------------------------------------------------------------------------------
	@Test
	public void test001_EstCeQueCaMarche () {
		int sum = 6;
		assertEquals(sum, 6);
	}

	@Test
	public void test002_EstCeQueCaMarche () {
		Locale currentLocale = I18n.getLocale();
        String language = currentLocale.getLanguage();
        String country = currentLocale.getCountry();

        Locale locale = new Locale(language, country);
        if (!I18n.isSupported(locale)) {
            logger.error ("Specified Locale is not supported: " + locale.toString());
        }
        I18n.setLocale(locale);

        String name = I18n.getMessage("Username");
        assertEquals(name, "Toto");

        name = I18n.getMessage("Param", ">Titi<");
        assertEquals(name, "Toto >Titi< ");
	}
}
