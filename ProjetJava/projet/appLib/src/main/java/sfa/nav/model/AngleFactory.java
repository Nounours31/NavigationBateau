package sfa.nav.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.lib.tools.Constantes;
import sfa.nav.lib.tools.NavException;

public class AngleFactory extends Angle {
	private static Logger logger = LoggerFactory.getLogger(AngleFactory.class);


	static private final String regexp_3ChiffresSecondeDecimale_Groupe1DegreOptionel = "^([0-9]+)°([0-9]+)'([0-9]+)\\.([0-9]*)[\\\"']?$";
	/*
		10°25'52.235"
		10°25'52.235'
		10°25'52.235
		°0'0.  -- NON
		°0'0.0 -- NON
					 */
	static private final String regexp_3ChiffresSecondeSexa_Groupe1DegreOptionel = "^([0-9]+)°([0-9]+)'([0-9]+)[\\\"']?$";
	/*
		10°25'52"
		10°25'52'
		10°25'52
	*/

	static private final String regexp_2ChiffresMinuteDecimale_Groupe1DegreOptionel = "^([0-9]+)°([0-9]+)\\.([0-9]*)[\\\"']?$";
	/*
		10°25.52'
		10°25.52
	*/
	static private final String regexp_2ChiffresMinuteSexa_Groupe1DegreOptionel = "^([0-9]+)°([0-9]+)[\\\"']?$";
	/*
		10°25'
		10°25
	*/
			
	static private final String regexp_1ChiffreHeureDecimale_Groupe1DegreOptionel = "^([0-9\\.]+[0-9]*)[°]?$";
	private static final String Full = null;
	/*
		10.25225°
		10.25225
		.25225
		.25225°
	*/

	
	public static Angle fromString (String s) throws NavException {
        Angle retour = new Angle();
        final Pattern pattern_regexp_3ChiffresSecondeDecimale_Groupe1DegreOptionel = Pattern.compile(regexp_3ChiffresSecondeDecimale_Groupe1DegreOptionel);
        final Pattern pattern_regexp_3ChiffresSecondeSexa_Groupe1DegreOptionel = Pattern.compile(regexp_3ChiffresSecondeSexa_Groupe1DegreOptionel);
        final Pattern pattern_regexp_2ChiffresMinuteDecimale_Groupe1DegreOptionel = Pattern.compile(regexp_2ChiffresMinuteDecimale_Groupe1DegreOptionel);
        final Pattern pattern_regexp_2ChiffresMinuteSexa_Groupe1DegreOptionel = Pattern.compile(regexp_2ChiffresMinuteSexa_Groupe1DegreOptionel);
        final Pattern pattern_regexp_1ChiffreHeureDecimale_Groupe1DegreOptionel = Pattern.compile(regexp_1ChiffreHeureDecimale_Groupe1DegreOptionel);
        
        
        boolean find = false;
        Matcher matcher = pattern_regexp_3ChiffresSecondeDecimale_Groupe1DegreOptionel.matcher(s);
        if (matcher.find()) {
        	logger.debug("Match case 1: --- Exemple[ 10°59'59.87\" ]");
        	for (int i = 1; i <= matcher.groupCount(); i++) {
        		logger.debug ("\tgroup({}): {}", i, matcher.group(i));
			}
        	
        	double heure = 0.0;
        	if ((matcher.group(1) != null) && (matcher.group(1).length() > 0))
        		heure = Double.parseDouble(matcher.group(1));

        	double minute = 0.0;
        	if ((matcher.group(2) != null) && (matcher.group(2).length() > 0))
        		minute = Double.parseDouble(matcher.group(2));

        	double seconde = 0.0;
        	if ((matcher.group(3) != null) && (matcher.group(3).length() > 0))
        		seconde = Double.parseDouble(matcher.group(3));

        	if ((matcher.group(4) != null) && (matcher.group(4).length() > 0))
        		seconde = seconde + Double.parseDouble(matcher.group(4)) / 100.0;
        	
        	if ((minute < 60.0) && (seconde < 60.0)) {
	        	retour.set(heure + minute / 60.0 + seconde / (3600.0));
	        	find = true;
        	}
        }	
        
        if (!find) {
        	matcher = pattern_regexp_3ChiffresSecondeSexa_Groupe1DegreOptionel.matcher(s);
        
	        if (matcher.find()) {
	        	logger.debug("Match case 2: --- Exemple[ 10°59'59\" ]");
	        	for (int i = 1; i <= matcher.groupCount(); i++) {
	        		logger.debug ("\tgroup({}): {}", i, matcher.group(i));
				}

	        	double heure = 0.0;
	        	if ((matcher.group(1) != null) && (matcher.group(1).length() > 0))
	        		heure = Double.parseDouble(matcher.group(1));
	
	        	double minute = 0.0;
	        	if ((matcher.group(2) != null) && (matcher.group(2).length() > 0)) {
	        		minute = Double.parseDouble(matcher.group(2));
	        	}
	
	        	double seconde = 0.0;
	        	if ((matcher.group(3) != null) && (matcher.group(3).length() > 0)) {
	        		seconde = Double.parseDouble(matcher.group(3));
	        	}
		        	
	        	if ((minute < 60.0) && (seconde < 60.0)) {
	        		retour.set(heure + minute / 60.0 + seconde / 3600.0);
		        	find = true;
	        	}
	        }	
        }        

        if (!find) {
        	matcher = pattern_regexp_2ChiffresMinuteDecimale_Groupe1DegreOptionel.matcher(s);
        	
	        if (matcher.find()) {
	        	logger.debug("Match case 3: --- Exemple[ 10°25.99 ]");
	        	for (int i = 1; i <= matcher.groupCount(); i++) {
	        		logger.debug ("\tgroup({}): {}", i, matcher.group(i));
				}

	        	double heure = 0.0;
	        	if ((matcher.group(1) != null) && (matcher.group(1).length() > 0))
	        		heure = Double.parseDouble(matcher.group(1));
	
	        	double minute = 0.0;
	        	if ((matcher.group(2) != null) && (matcher.group(2).length() > 0)) {
	        		String m = matcher.group(2);
	        		minute = Double.parseDouble(m);
	        	}
	
	        	if ((matcher.group(3) != null) && (matcher.group(3).length() > 0)) {
	        		// attention si '.' on est en minutes decimale
	        		minute += Double.parseDouble("." + matcher.group(3));
	        	}
		        
	        	if (minute < 60.0) {
		        	retour.set(heure + minute / 60.0);
		        	find = true;
	        	}
	        }	
        }        
        
        if (!find) {
        	matcher = pattern_regexp_2ChiffresMinuteSexa_Groupe1DegreOptionel.matcher(s);
	        if (matcher.find()) {
	        	logger.debug("Match case 3.5: --- Exemple[ 10°59 ]");
	        	for (int i = 1; i <= matcher.groupCount(); i++) {
	        		logger.debug ("\tgroup({}): {}", i, matcher.group(i));
				}

	        	double heure = 0.0;
	        	if ((matcher.group(1) != null) && (matcher.group(1).length() > 0))
	        		heure = Double.parseDouble(matcher.group(1));
	
	        	double minute = 0.0;
	        	if ((matcher.group(2) != null) && (matcher.group(2).length() > 0)) {
	        		String m = matcher.group(2);
	        		minute = Double.parseDouble(m);
	        	}
		        	
	        	if (minute < 60.0) {
	        		retour.set(heure + minute / 60.0);
		        	find = true;
	        	}
	        }	
        }        
        
        if (!find) {
        	matcher = pattern_regexp_1ChiffreHeureDecimale_Groupe1DegreOptionel.matcher(s);
        
	        if (matcher.find()) {
	        	logger.debug("Match case 4:  --- Exemple[ 10.99 ] ");
	        	for (int i = 1; i <= matcher.groupCount(); i++) {
	        		logger.debug ("\tgroup({}): {}", i, matcher.group(i));
				}

	        	double heure = 0.0;
	        	if ((matcher.group(1) != null) && (matcher.group(1).length() > 0))
	        		heure = Double.parseDouble(matcher.group(1));
	
	        	retour.set(heure);
	        	find = true;
	        }	
        }        
        
        if (find)
        	retour.valide(true);
        else
        	throw new NavException(Constantes.IncapabledeDecodeUnAngle + "("+s+")");
        return retour;
	}

	
	static public Angle fromDegre (double x) throws NavException {
		Angle retour = new Angle();
		retour.set(x);
		return retour;
	}
	

	static public Angle fromRadian(double x) throws NavException {
		return AngleFactory.fromDegre(Angle.RadianToDegre(x));
	}


	static public Angle fromAngle(Angle s) throws NavException {
		return AngleFactory.fromDegre(s.asDegre());
	}


	static public Angle fromMe(Angle s) throws NavException {
		return AngleFactory.fromDegre(s.asDegre());
	}
}
