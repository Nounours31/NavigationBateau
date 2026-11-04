package sfa.nav.infra.tools.i18n;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;

public class I18n {
    private final static String MESSAGES_KEY = "messages";
    protected I18n() {
    }

    private static ResourceBundle bundle;
    
    public static Locale getLocale() {
        Locale defaultLocale = Locale.getDefault();
        return defaultLocale;
    }
    public static boolean isSupported(Locale l) {
        Locale[] availableLocales = Locale.getAvailableLocales();
        return Arrays.asList(availableLocales).contains(l);
    }
    
    public static void setLocale(Locale l) {
        Locale.setDefault(l);
    }
    
    public static String getMessage(String key) {
        if(bundle == null) {
            bundle = ResourceBundle.getBundle(MESSAGES_KEY);
        }
        return bundle.getString(key);
    }
    
    public static String getMessage(String key, String ... arguments) {
    	String nls = I18n.getMessage(key);
        String expandedTxt = MessageFormat.format(nls, (Object[])arguments);
        return expandedTxt;
    }
}
