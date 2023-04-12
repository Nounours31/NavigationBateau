package sfa.nav.model;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Angle {
	private static Logger logger = LoggerFactory.getLogger(Angle.class);
	private double _angle = 0.0;
	
	protected Angle() {}
	
	static private final String regexp_3ChiffresSecondeDecimale_Groupe1DegreOptionel = "^([0-9]*)°([0-9]+)'([0-9]+)\\.([0-9]*)[\\\"']?$";
	/*
		10°25'52.235"
		10°25'52.235'
		10°25'52.235
		°0'0.
		°0'0.0
					 */
	static private final String regexp_3ChiffresSecondeSexa_Groupe1DegreOptionel = "^([0-9]*)°([0-9]+)'([0-9]+)[\\\"']?$";
	/*
		10°25'52"
		10°25'52'
		10°25'52
	*/

	static private final String regexp_2ChiffresMinuteDecimale_Groupe1DegreOptionel = "^([0-9]*)°([0-9]+)\\.([0-9]*)[\\\"']?$";
	/*
		10°25.52'
		10°25.52
	*/
	static private final String regexp_2ChiffresMinuteSexa_Groupe1DegreOptionel = "^([0-9]*)°([0-9]+)[\\\"']?$";
	/*
		10°25'
		10°25
	*/
			
	static private final String regexp_1ChiffreHeureDecimale_Groupe1DegreOptionel = "^([0-9]+[0-9\\.]*)[°]?$";
	/*
		10.25225°
		10.25225
	*/
	
			
	
	static public Angle fromString (String s) {
        Angle retour = new Angle();
        final Pattern pattern_regexp_3ChiffresSecondeDecimale_Groupe1DegreOptionel = Pattern.compile(regexp_3ChiffresSecondeDecimale_Groupe1DegreOptionel);
        final Pattern pattern_regexp_3ChiffresSecondeSexa_Groupe1DegreOptionel = Pattern.compile(regexp_3ChiffresSecondeSexa_Groupe1DegreOptionel);
        final Pattern pattern_regexp_2ChiffresMinuteDecimale_Groupe1DegreOptionel = Pattern.compile(regexp_2ChiffresMinuteDecimale_Groupe1DegreOptionel);
        final Pattern pattern_regexp_2ChiffresMinuteSexa_Groupe1DegreOptionel = Pattern.compile(regexp_2ChiffresMinuteSexa_Groupe1DegreOptionel);
        final Pattern pattern_regexp_1ChiffreHeureDecimale_Groupe1DegreOptionel = Pattern.compile(regexp_1ChiffreHeureDecimale_Groupe1DegreOptionel);
        
        
        retour._angle = 0.0;
        boolean find = false;
        Matcher matcher = pattern_regexp_3ChiffresSecondeDecimale_Groupe1DegreOptionel.matcher(s);
        if (matcher.find()) {
        	logger.debug("Match case 1");
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

        	boolean isSecondeDecimale = false;
        	if ((matcher.group(4) != null) && (matcher.group(4).length() > 0)) {
        		seconde += Double.parseDouble("." + matcher.group(4));
        		// attention si '.' on est en minutes decimale
        		isSecondeDecimale = true; 
        	}
        	
        	retour._angle = heure + minute / 60.0 + seconde / (3600.0);
        	find = true;
        }	
        
        if (!find) {
        	matcher = pattern_regexp_3ChiffresSecondeSexa_Groupe1DegreOptionel.matcher(s);
        
	        if (matcher.find()) {
	        	logger.debug("Match case 2");
	        	for (int i = 1; i <= matcher.groupCount(); i++) {
	        		logger.debug ("\tgroup({}): {}", i, matcher.group(i));
				}

	        	double heure = 0.0;
	        	if ((matcher.group(1) != null) && (matcher.group(1).length() > 0))
	        		heure = Double.parseDouble(matcher.group(1));
	
	        	double minute = 0.0;
	        	if ((matcher.group(2) != null) && (matcher.group(2).length() > 0)) {
	        		String m = matcher.group(2);
	        		if (matcher.group(2).length() == 1)
	        			m = m + "0";
	        		minute = Double.parseDouble(m);
	        	}
	
	        	double seconde = 0.0;
	        	if ((matcher.group(3) != null) && (matcher.group(3).length() > 0)) {
	        		String m = matcher.group(2);
	        		if (matcher.group(3).length() == 1)
	        			m = m + "0";
	        		seconde = Double.parseDouble(m);
	        	}
		        	
	        	retour._angle = heure + minute / 60.0 + seconde / 3600.0;
	        	find = true;
	        }	
        }        

        if (!find) {
        	matcher = pattern_regexp_2ChiffresMinuteDecimale_Groupe1DegreOptionel.matcher(s);
        	boolean isMinuteDecimale = true;
        	
	        if (matcher.find()) {
	        	logger.debug("Match case 3");
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
		        	
	        	retour._angle = heure + minute / (isMinuteDecimale ? 100.0 : 60.0);
	        	find = true;
	        }	
        }        
        
        if (!find) {
        	matcher = pattern_regexp_2ChiffresMinuteSexa_Groupe1DegreOptionel.matcher(s);
	        if (matcher.find()) {
	        	logger.debug("Match case 3.5");
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
		        	
	        	retour._angle = heure + minute / 60.0;
	        	find = true;
	        }	
        }        
        
        if (!find) {
        	matcher = pattern_regexp_1ChiffreHeureDecimale_Groupe1DegreOptionel.matcher(s);
        
	        if (matcher.find()) {
	        	logger.debug("Match case 4");
	        	for (int i = 1; i <= matcher.groupCount(); i++) {
	        		logger.debug ("\tgroup({}): {}", i, matcher.group(i));
				}

	        	double heure = 0.0;
	        	if ((matcher.group(1) != null) && (matcher.group(1).length() > 0))
	        		heure = Double.parseDouble(matcher.group(1));
	
	        	retour._angle = heure;
	        	find = true;
	        }	
        }        
        
        return retour;
	}



	public double getAngle() {
		return _angle;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(_angle);
		double h = Math.floor(   _angle);
		double m = Math.floor(  (_angle - h) * 60.0);
		double s = ((((_angle - h) * 60.0) - m ) * 60.0);
		logger.debug("_angle", _angle);
		logger.debug("m", (_angle - h) * 60.0);
		logger.debug("s", s);
		
		DecimalFormat df=new DecimalFormat("00.00");
		sb.append(String.format("[%02d°%02d'%s\"]", (int)Math.floor(h), (int)Math.floor(m), df.format(s)));
		return sb.toString();
	}
}
