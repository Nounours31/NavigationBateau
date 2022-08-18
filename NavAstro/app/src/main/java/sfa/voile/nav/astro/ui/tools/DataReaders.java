/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.tools;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.modele.Angle;
import sfa.voile.nav.astro.modele.AngleParser;
import sfa.voile.nav.astro.modele.Declinaison;
import sfa.voile.nav.astro.modele.DeclinaisonParser;
import sfa.voile.nav.astro.modele.GeneriqueDataFormat;
import sfa.voile.nav.astro.modele.Heure;
import sfa.voile.nav.astro.modele.HeureParser;
import sfa.voile.nav.astro.modele.Latitude;

/**
 *
 * @author pierr
 */
public class DataReaders {

 
    private static final String [] HELP_FORMAT = {
    		"? [pour avoir tous les formats]",
    		"?"
    };
	private static final String[] SEP = {
			"  ",
			""
	};
    private static Logger _logger = LoggerFactory.getLogger(DataReaders.class);

    public DataReaders() {
    }

 
    public static String readInput () {
    	myInputReaderItf reader = myInputReaderFactory.getMyInputReader();
    	return reader.readInput();
    }
    
    public Angle readAngle() {
        Angle dRetour = new Angle(0.0);
        boolean fin = false;
        AngleParser a = new AngleParser();
        while (!fin) {
            try {
                String s = DataReaders.readInput();
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
    
    public Heure readHeure() {
    	Heure dRetour = new Heure(0.0);
        boolean fin = false;
        HeureParser a = new HeureParser();
        while (!fin) {
            try {
                String s = DataReaders.readInput();
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

    public Latitude readLatitude() {
        Latitude dRetour = new Latitude ();
        _logger.debug("Saisie: {}", dRetour.toString());
        return dRetour;
    }


	public void println(String s) {
		System.out.println(s + SEP[0] + HELP_FORMAT[0]);
	}


	public Declinaison readDeclinaison() {
		Declinaison dRetour = new Declinaison(0.0);
        boolean fin = false;
        DeclinaisonParser a = new DeclinaisonParser();
        while (!fin) {
            try {
                String s = DataReaders.readInput();
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
}
