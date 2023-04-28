package sfa.nav.model;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.tools.Constantes;
import sfa.nav.tools.NavException;
import sfa.nav.tools.eToStringMode;

public class Angle {
	private static Logger logger = LoggerFactory.getLogger(Angle.class);
	private double _angleInDegre;
	private boolean _status;

	final private static String NonInitMsg = Constantes.AngleNonInitialisedMsg;


	protected Angle() {
		set (0);
		valide(false);
	}

	protected Angle(Angle a) throws NavException {
		if (a == null) {
			set (0);
			valide(false);
		}
		else {
			set (a.asDegre());
			valide(a.valide());
		}
	}

	
	public double asRadian() throws NavException {
		if (!_status)
			throw new NavException(NonInitMsg);
		return Angle.DegreToRadian(_angleInDegre);
	}
	
	public double asDegre() throws NavException {
		if (!_status)
			throw new NavException(NonInitMsg);
		return _angleInDegre;
	}

	protected void valide(boolean b) {
		_status = b;
	}
	protected boolean valide() {
		return _status;
	}

	protected void set(double d) {
		while (d >= 360.0)
			d -= 360.0;

		while (d < 0.0)
			d += 360.0;
		
		_angleInDegre = d;
		
		valide(true);
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
	private static final String Full = null;
	/*
		10.25225°
		10.25225
		.25225
		.25225°
	*/
	
	
	
	static public Angle fromDegre (double x) throws NavException {
		Angle retour = new Angle();
		retour.set(x);
		return retour;
	}
	

	static public Angle fromRadian(double x) throws NavException {
		return Angle.fromDegre(Angle.RadianToDegre(x));
	}

	static public Angle fromString (String s) throws NavException {
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



	
	@Override
	public String toString() {
		if (!valide())
			return NonInitMsg;
		
		return myToString(eToStringMode.or(eToStringMode.full, eToStringMode.deg, eToStringMode.rad));
	}

	public String myToString(int mode) {
		if (!valide())
			return NonInitMsg;
		
		double angle2Print = 0.0;
		try { angle2Print = this.asDegre();} catch (NavException e1) {}
				
		double h = Math.floor(   angle2Print);
		double m = Math.floor(  (angle2Print - h) * 60.0);
		double s = ((((angle2Print - h) * 60.0) - m ) * 60.0);
		
		DecimalFormat dfPosition = new DecimalFormat("000.000°");
		DecimalFormat dfSeconde = new DecimalFormat("00.00\"");
		DecimalFormat dfRad = new DecimalFormat("0.00 Rad");
		StringBuffer sb = new StringBuffer();

		if (eToStringMode.isA(mode, eToStringMode.light)) {
			sb.append(dfPosition.format(angle2Print));
		}
		else if (eToStringMode.isA(mode, eToStringMode.full)) {
			sb.append(dfPosition.format(angle2Print));
			
			if (eToStringMode.isA(mode, eToStringMode.deg))
				sb.append(String.format("[%02d°%02d'%s]", (int)Math.floor(h), (int)Math.floor(m), dfSeconde.format(s)));			

			if (eToStringMode.isA(mode, eToStringMode.rad)) {
				double asRad = 0.0;
				try { asRad = this.asRadian(); } catch (NavException e) {}
				sb.append(String.format("[%s]", dfRad.format(asRad)));		
			}
		}
		else {
			sb.append("xxxxxxxxxxx");
		}
		return sb.toString();
	}


	public static double DegreToRadian(double asDegre) {
		return asDegre * Constantes.DEG2RAD;
	}
	public static double RadianToDegre(double asRadian) {
		return asRadian * Constantes.RAD2DEG;
	}
}
