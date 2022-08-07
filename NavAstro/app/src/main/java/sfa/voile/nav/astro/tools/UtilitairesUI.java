/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.tools;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import sfa.voile.nav.astro.modele.Latitude;

/**
 *
 * @author pierr
 */
public class UtilitairesUI {

 
    private Logger _logger = null;

    public UtilitairesUI() {
        _logger = LoggerFactory.getLogger(UtilitairesUI.class);
    }

 
    public double readDegreMinuteSeconde() {
        FormatAngleAstro[] _allRegex = FormatAngleAstro._allRegex;
        return readDegreMinuteSeconde(_allRegex);
    }
    
    public double readDegreMinuteSeconde(FormatAngleAstro[] _allRegex) {
        double dRetour = 0.0;
        boolean fin = false;
        while (!fin) {
            System.out.println("\t\tformat angulaire supporte:");
            for (FormatAngleAstro formatToDecode : _allRegex) {
                System.out.println("\t\t\t ==>" + formatToDecode.printFormet() + "<==");
            }

            try {
                Locale loc = new Locale("fr", "FR");
                Scanner in = new Scanner(System.in, StandardCharsets.ISO_8859_1);
                String s = in.nextLine();
                _logger.debug("Saisie ==>{}<==", s);

                for (FormatAngleAstro formatToDecode : _allRegex) {
                    if (formatToDecode.isOK(s)) {
                        dRetour = formatToDecode.parse(s);
                        fin = true;
                        break;
                    }
                }
            }
            catch (Exception e) {
                _logger.error("Err: {}", e.getMessage());
                e.printStackTrace();
            }
            if (!fin) {
                System.out.println("\t\tNe matche pas, on recommence ....");
            }
        }

        _logger.debug("Saisie: {}", dRetour);
        return dRetour;
    }
    
    public double readHeure() {
        double dRetour = 0.0;
        
        boolean fin = false;
        FormatHeureAstro[] _allRegex = FormatHeureAstro._allRegex;
        
        while (!fin) {
            System.out.println("\t\tformat angulaire supporte:");
            for (FormatHeureAstro formatToDecode : _allRegex) {
                System.out.println("\t\t\t ==>" + formatToDecode.printFormet() + "<==");
            }

            try {
                Locale loc = new Locale("fr", "FR");
                Scanner in = new Scanner(System.in, StandardCharsets.ISO_8859_1);
                String s = in.nextLine();
                _logger.debug("Saisie ==>{}<==", s);

                for (FormatHeureAstro formatToDecode : _allRegex) {
                    if (formatToDecode.isOK(s)) {
                        dRetour = formatToDecode.parse(s);
                        fin = true;
                        break;
                    }
                }
            }
            catch (Exception e) {
                _logger.error("Err: {}", e.getMessage());
                e.printStackTrace();
            }
            if (!fin) {
                System.out.println("\t\tNe matche pas, on recommence ....");
            }
        }

        _logger.debug("Saisie: {}", dRetour);
        return dRetour;
    }

    public Latitude readLatitude() {
        Latitude dRetour = new Latitude ();
        
        boolean fin = false;
        FormatLatitudeAstro[] _allRegex = FormatLatitudeAstro._allRegex;
        
        while (!fin) {
            System.out.println("\t\tformat angulaire supporte:");
            for (FormatLatitudeAstro formatToDecode : _allRegex) {
                System.out.println("\t\t\t ==>" + formatToDecode.printFormet() + "<==");
            }

            try {
                Locale loc = new Locale("fr", "FR");
                Scanner in = new Scanner(System.in, StandardCharsets.ISO_8859_1);
                String s = in.nextLine();
                _logger.debug("Saisie ==>{}<==", s);

                for (FormatLatitudeAstro formatToDecode : _allRegex) {
                    if (formatToDecode.isOK(s)) {
                        dRetour = formatToDecode.parse(s);
                        fin = true;
                        break;
                    }
                }
            }
            catch (Exception e) {
                _logger.error("Err: {}", e.getMessage());
                e.printStackTrace();
            }
            if (!fin) {
                System.out.println("\t\tNe matche pas, on recommence ....");
            }
        }

        _logger.debug("Saisie: {}", dRetour.toString());
        return dRetour;
    }
}
