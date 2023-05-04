package sfa.nav.cli;

import java.nio.charset.Charset;
import java.util.SortedMap;

public class Tools {
    static public Charset appCharSet = null;

    public static void testCharSet() {
        System.out.println("****************************************************************");
        System.out.println("** Attention a la code page ");
        System.out.println("**     Ex: code page UTF8 -> run chcp 65001");
        System.out.println("****************************************************************");
        SortedMap<String, Charset> allCharSet = Charset.availableCharsets();
        for (String key: allCharSet.keySet()) {
            System.out.println ("Charset: " + key);
        }
        if (Charset.isSupported("windows-1252")) {
            appCharSet = Charset.forName("windows-1252");
        }
        System.out.println("Charset par défaut : " + Charset.defaultCharset());
    }
}
