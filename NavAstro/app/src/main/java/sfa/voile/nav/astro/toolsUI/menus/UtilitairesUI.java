/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.toolsUI.menus;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.modele.Angle;
import sfa.voile.nav.astro.modele.AngleParser;
import sfa.voile.nav.astro.modele.GeneriqueDataFormat;
import sfa.voile.nav.astro.modele.Latitude;

/**
 *
 * @author pierr
 */
public class UtilitairesUI {

 
    private static final String [] HELP_FORMAT = {
    		"? [pour avoir tous les formats]",
    		"?"
    };
	private static final String[] SEP = {
			"  ",
			""
	};
    private static Logger _logger = LoggerFactory.getLogger(UtilitairesUI.class);

    public UtilitairesUI() {
    }

 
    public static String readInput () {
    	
		// Scanner in = new Scanner(System.in);
		// String s = in.nextLine();
    	
    	Scanner console = null;
		try {
			console = new Scanner(new InputStreamReader(System.in, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String s = console.nextLine();
    	// console.close();
    	
		_logger.debug("Saisie ==>{}<==", s);
    	return s;
    }
    
    public Angle readDegreMinuteSeconde() {
        Angle dRetour = new Angle(0.0);
        boolean fin = false;
        AngleParser a = new AngleParser();
        while (!fin) {
        	
            try {
            	
                String s = UtilitairesUI.readInput();
                if (s.contains(HELP_FORMAT[1])) {
                    System.out.println("\t\tformat angulaire supporte: " + a.getAllAvailableFormat().length);
                    for (GeneriqueDataFormat formatToDecode : a.getAllAvailableFormat()) {
                        System.out.println("\t\t\t ==>" + formatToDecode.toString() + "<==");
                    }
                    continue;
                }
                _logger.debug("Saisie ==>{}<==", s);
                
                fin = a.parse(s, dRetour);
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


	public void println(String s) {
		System.out.println(s + SEP[0] + HELP_FORMAT[0]);
	}
}
