package sfa.nav.model;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.tools.NavException;

public class Angle {
	private static Logger logger = LoggerFactory.getLogger(Angle.class);
	private double _angleInDegre;
	private boolean _status;
	
	protected Angle() {
		_angleInDegre = 0.0;
		_status = false;		
	}

	protected Angle(Angle a) {
		if (a == null) {
			_angleInDegre = 0.0;
			_status = false;					
		}
		else {
			_angleInDegre = a._angleInDegre;
			_status = a._status;		
		}
	}
	
	public double asRadian() throws NavException {
		if (!_status)
			throw new NavException("Non valuee");
		return _angleInDegre * Math.PI / 180.0;
	}
	public double asDegre() throws NavException {
		if (!_status)
			throw new NavException("Non valuee");
		return _angleInDegre;
	}

	private void set(double d) {
		while (d >= 360.0)
			d -= 360.0;

		while (d < 0.0)
			d += 360.0;
		
		_angleInDegre = d;
	}
	
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
	/*
		10.25225°
		10.25225
		.25225
		.25225°
	*/
	
	
	
	static public Angle fromDegre (double x) {
		Angle retour = new Angle();
		retour.set(x);
		retour._status = true;
		return retour;
	}
	
	static public Angle fromRadian(double x) {
		return Angle.fromDegre(x * 180.0 / Math.PI);
	}

	static public Angle fromString (String s) {
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
        	retour._status = true;
        return retour;
	}



	
	@Override
	public String toString() {
		if (!_status)
			return "Non valuee";
		
		StringBuffer sb = new StringBuffer();
		sb.append(_angleInDegre);
		double h = Math.floor(   _angleInDegre);
		double m = Math.floor(  (_angleInDegre - h) * 60.0);
		double s = ((((_angleInDegre - h) * 60.0) - m ) * 60.0);
		logger.debug("_angle", _angleInDegre);
		logger.debug("m", (_angleInDegre - h) * 60.0);
		logger.debug("s", s);
		
		DecimalFormat df=new DecimalFormat("00.00");
		sb.append(String.format("[%02d°%02d'%s\"]", (int)Math.floor(h), (int)Math.floor(m), df.format(s)));
		return sb.toString();
	}

	public boolean isInitialised() {
		// TODO Auto-generated method stub
		return _status;
	}
}
