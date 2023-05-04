package sfa.nav.lib.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.lib.tools.Constantes;
import sfa.nav.lib.tools.NavException;

public class Longitude extends Angle {
	private static Logger logger = LoggerFactory.getLogger(Longitude.class);
	
	
	public Longitude() throws NavException {
		super ();
	}

	public static Longitude fromAngle(Angle d) throws NavException {
		Longitude l = Longitude.fromDegre(d.asDegre());
		return l;
	}
	
	
	static public Longitude fromDegre (double x) throws NavException {
		double a = Longitude.viewAngleAsLongitudeInDegre(Angle.fromDegre(x));
		Longitude l = new Longitude();
		l.set(a);
		return l;	
	}
	
	@Override
	public double asRadian() throws NavException {
		return Angle.DegreToRadian (asDegre());
	}
	
	

	static private double viewAngleAsLongitudeInDegre(Angle a) throws NavException {
		return Longitude.viewAngleAsLongitudeInDegre(a.asDegre());
	}

	static private double viewAngleAsLongitudeInDegre(double a) throws NavException {
		if ((a >= 0.0) && (a <= 180.0)) {
			return a;
		}
		else if ((a >= 180.0) && (a < 360.0)) {
			return a - 360.0;
		}
		else
			throw new NavException(Constantes.InvalideLatitude + " [angle en degre:" + a + "°]");
	}

	
	@Override
	public double asDegre() throws NavException {
		return viewAngleAsLongitudeInDegre (super.asDegre());
	}
	
	
   	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		try {
			if (this.asDegre() >= 0.0) {
				sb.append("G: " + super.toString());
				sb.append(" E");
			}
			else {
				Angle a = Angle.fromDegre(-this.asDegre());
				sb.append("G: " + a.toString());
				sb.append(" W");
			} 
		}
		catch (Exception e) {
			sb.append(e.getMessage());
		}
		return sb.toString();
	}

	
	static public Longitude fromString (String s) throws NavException {
		final String regex = "^([0-9\\.°'\\\"]+)[ \\t]*([OoWwEe])$";
		
    	Angle a;
        
        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
        	logger.debug("Match case 1: --- Exemple[ 10°59'59.87E\" ]");
        	for (int i = 1; i <= matcher.groupCount(); i++) {
        		logger.debug ("\tgroup({}): {}", i, matcher.group(i));
			}
        	
        	if ((matcher.group(1) != null) && (matcher.group(1).length() > 0))
        		a = Angle.fromString(matcher.group(1));
        	else
        		throw new NavException(Constantes.IncapabledeDecodeUneLongitude + "("+s+")");
        	
        	if (Math.abs(a.asDegre()) > 180.0)
        		throw new NavException(Constantes.IncapabledeDecodeUneLongitude + " [|.| > 180.0]("+s+")");
        		
        	double dSens = 0.0;
        	if ((matcher.group(2) != null) && (matcher.group(2).length() > 0)) {
        		String sSens = matcher.group(2).toLowerCase();
        		if (sSens.equals("w")) dSens = -1.0;
        		else if (sSens.equals("o")) dSens = -1.0;
        		else if (sSens.equals("e")) dSens = +1.0;
        		else
            		throw new NavException(Constantes.IncapabledeDecodeUneLongitude + " [W(-) ou E(+)]("+s+")");
        	}
        	a = Angle.fromDegre(a.asDegre() * dSens);
        }
        else 
        	throw new NavException(Constantes.IncapabledeDecodeUneLongitude + "("+s+")");
        
        return Longitude.fromAngle(a);

	}
}
