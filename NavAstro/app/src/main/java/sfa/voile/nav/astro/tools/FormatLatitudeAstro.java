/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sfa.voile.nav.astro.modele.Latitude;

/**
 *
 * @author pierr
 */
public class FormatLatitudeAstro {
    private static Logger _logger = LoggerFactory.getLogger(FormatLatitudeAstro.class);
    
    String _regExp = null;
    String _exemple = null;
    int _nbGroup = 0;
    private boolean _checkVal = true;
    private boolean _isDecimal = false;

    final public static FormatLatitudeAstro[] _allRegex = {
        // Pb de l'encodage Windows du signe degre ....
        new FormatLatitudeAstro("([NnSs]) (\\d+)[,\\.](\\d+)'", "N 25.89 [en decimal]", 3, false, true),
        new FormatLatitudeAstro("([NnSs]) (\\d{1,2})[\u00B0](\\d{1,2})'", "S 25\u00B007' [sans les secondes]", 3),
        new FormatLatitudeAstro("([NnSs]) (\\d{1,2})[\u00B0](\\d{1,2})'(\\d{1,2})\"", "n 25\u00B007'54\" [la complete]", 4),
        new FormatLatitudeAstro("([NnSs]) (\\d{1,2})[\u00B0](\\d{1,2}[,\\.]\\d{1,2})'", "s 25\u00B007.89' [seconde decimale dans ls minutes]", 3), // attention seconde decimale
        new FormatLatitudeAstro("([NnSs]) (\\d{1,2})d(\\d{1,2})m(\\d{1,2})s", "N 25d07m58s [format inutile ...]", 4),
        new FormatLatitudeAstro("([NnSs]) (\\d{1,2}):(\\d{1,2}):(\\d{1,2})", "S 25:07:58 [format horaire]", 4)
    };

    FormatLatitudeAstro(String s, String e, int i, boolean b, boolean c) {
        _regExp = s;
        _exemple = e;
        _nbGroup = i;
        _checkVal = b;
        _isDecimal = c;
    }

    FormatLatitudeAstro(String s, String e, int i) {
        _regExp = s;
        _exemple = e;
        _nbGroup = i;
    }

    String printFormet() {
        StringBuffer sb = new StringBuffer("Format: ==>");
        sb.append(_regExp);
        sb.append("<== ex: ");
        sb.append(_exemple);
        return sb.toString();
    }

    boolean isOK(String s) {
        Pattern pattern = Pattern.compile(_regExp, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(s);
        _logger.debug("test ==>{}<==>{}", matcher.pattern().toString(), matcher.matches());
        return matcher.matches();

    }

    Latitude parse(String s) {
        Latitude retour = new Latitude();
        Pattern pattern = Pattern.compile(_regExp, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(s);

        if (matcher.matches()) {
            _logger.debug("Full match group(0): ==>{}<==", matcher.group(0));

            boolean bErr = false;
            String mErr = "";

            String signe = matcher.group(1);
            if ((signe == null) || (signe.isEmpty()) || (signe.equalsIgnoreCase("n"))) {
                retour.setSens(Latitude.SensLatitude.Nord); 
            }
            else
                retour.setSens(Latitude.SensLatitude.Sud); 
            

            double degre = Double.parseDouble(matcher.group(2));
            if ((degre > 90) || (degre < 0)) {
                bErr = true;
                mErr += String.format("Degre invalide: %f", degre);
            }
            double minute = 0.0;
            if (_nbGroup > 2) {
                try {
                    minute = Double.parseDouble(matcher.group(3));
                }
                catch (NumberFormatException e) {
                    minute = Double.parseDouble(matcher.group(3).replaceAll(",", "."));
                }
                if (_checkVal) {
                    if ((minute > 60) || (minute < 0)) {
                        bErr = true;
                        mErr += String.format("Minute invalide: %f", minute);
                    }
                }
            }
            double seconde = 0.0;
            if (_nbGroup > 3) {
                if (matcher.groupCount() > 2) {
                    seconde = Double.parseDouble(matcher.group(4));

                    if ((seconde > 60) || (seconde < 0)) {
                        bErr = true;
                        mErr += String.format("Seconde invalide: %f", seconde);
                    }
                }
            }
            if (bErr) {
                _logger.error("Erreur de saisie: {}", mErr);
                System.out.println("On recommence ...");
            }
            else {
                if (_isDecimal)
                    retour.setLatitude(degre + (minute / 100.0));
                else
                    retour.setLatitude(degre + (minute + seconde / 60.0) / 60.0);
            }
        }
        else {
            _logger.error("No match ...: {}", _regExp);
        }
        _logger.debug("Read reel: {}", retour);
        return retour;
    }
}
