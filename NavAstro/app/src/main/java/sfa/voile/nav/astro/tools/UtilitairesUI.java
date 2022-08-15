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

import sfa.voile.nav.astro.modele.AngleParser;
import sfa.voile.nav.astro.modele.HeureParser;
import sfa.voile.nav.astro.modele.Latitude;
import sfa.voile.nav.astro.modele.LatitudeParser;

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
        

        double dRetour = 0.0;
        boolean fin = false;
        while (!fin) {
            System.out.println("\t\tformat angulaire supporte:");
            /*for (GeneriqueFormatItf formatToDecode : _allRegex) {
                myIO.println("\t\t\t ==>" + formatToDecode.toString() + "<==");
            }*/

            try {
                Locale loc = new Locale("fr", "FR");
                Scanner in = new Scanner(System.in, StandardCharsets.UTF_8);
                String s = in.nextLine();
                _logger.debug("Saisie ==>{}<==", s);
/*
                for (GeneriqueFormatItf formatToDecode : _allRegex) {
                    if (formatToDecode.isOK(s)) {
                        dRetour = formatToDecode.parse(s);
                        fin = true;
                        break;
                    }
                } */
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
        /*HeureParser[] _allRegex = HeureParser._allRegex;
        
        while (!fin) {
            System.out.println("\t\tformat angulaire supporte:");
            for (HeureParser formatToDecode : _allRegex) {
                System.out.println("\t\t\t ==>" + formatToDecode.printFormet() + "<==");
            }

            try {
                Locale loc = new Locale("fr", "FR");
                Scanner in = new Scanner(System.in, StandardCharsets.ISO_8859_1);
                String s = in.nextLine();
                _logger.debug("Saisie ==>{}<==", s);

                for (HeureParser formatToDecode : _allRegex) {
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
*/
        _logger.debug("Saisie: {}", dRetour);
        return dRetour;
    }

    public Latitude readLatitude() {
        Latitude dRetour = new Latitude ();
        /*
        boolean fin = false;
        LatitudeParser[] _allRegex = LatitudeParser._allRegex;
        
        while (!fin) {
            System.out.println("\t\tformat angulaire supporte:");
            for (LatitudeParser formatToDecode : _allRegex) {
                System.out.println("\t\t\t ==>" + formatToDecode.printFormet() + "<==");
            }

            try {
                Locale loc = new Locale("fr", "FR");
                Scanner in = new Scanner(System.in, StandardCharsets.ISO_8859_1);
                String s = in.nextLine();
                _logger.debug("Saisie ==>{}<==", s);

                for (LatitudeParser formatToDecode : _allRegex) {
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
*/
        _logger.debug("Saisie: {}", dRetour.toString());
        return dRetour;
    }
}
