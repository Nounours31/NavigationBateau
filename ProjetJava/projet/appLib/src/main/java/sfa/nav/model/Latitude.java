package sfa.nav.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.SensLatitude;
import sfa.nav.lib.tools.Constantes;
import sfa.nav.lib.tools.NavException;


// -----------------------------------------
// La latitude va de 0 -> +90° (N)  et 0 -> -90° (S) 
// ou [0;90] U [270;360]
// -----------------------------------------

public class Latitude extends Angle {
	private static Logger logger = LoggerFactory.getLogger(Latitude.class);
	
	
	public Latitude() {
		super ();
	}

	public static Latitude fromAngle(Angle d) throws NavException {
		Latitude l = Latitude.fromDegre(d.asDegre());
		return l;
	}
	
	
	static public Latitude fromDegre (double x) throws NavException {
		double a = Latitude.viewAngleAsLatitudeInDegre(AngleFactory.fromDegre(x));
		Latitude l = new Latitude();
		l.set(a);
		return l;
	}
	
	@Override	
	public double asRadian() throws NavException {
		return Angle.DegreToRadian (asDegre());
	}
	
	
	static private double viewAngleAsLatitudeInDegre(Angle a) throws NavException {
		return Latitude.viewAngleAsLatitudeInDegre(a.asDegre());
	}
	
	static private double viewAngleAsLatitudeInDegre(double a) throws NavException {
		if (Latitude.getSens(a) == SensLatitude.Nord) {
			return a;
		}
		else if (Latitude.getSens(a) == SensLatitude.Sud) {
			return a - 360.0;
		}
		else
			throw new NavException(Constantes.InvalideLatitude + " [angle en degre:" + a + "°]");
	}

	
	@Override
	public double asDegre() throws NavException {
		return viewAngleAsLatitudeInDegre (super.asDegre());
	}
	
	
   	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		try {
			if (this.asDegre() >= 0.0) {
				sb.append("l: "+ super.toString());
				sb.append(" N");
			}
			else {
				Angle a = AngleFactory.fromDegre(-this.asDegre());
				sb.append("l: "+ a.toString());
				sb.append(" S");
			} 
		}
		catch (Exception e) {
			sb.append(e.getMessage());
		}
		return sb.toString();
	}

	
	static public Latitude fromString (String s) throws NavException {
		final String regex = "^([0-9\\.°'\\\"]+)[ \\t]*([NSns])$";
		
    	Angle a;
        
        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
        	logger.debug("Match case 1: --- Exemple[ 10°59'59.87\" ]");
        	for (int i = 1; i <= matcher.groupCount(); i++) {
        		logger.debug ("\tgroup({}): {}", i, matcher.group(i));
			}
        	
        	if ((matcher.group(1) != null) && (matcher.group(1).length() > 0))
        		a = AngleFactory.fromString(matcher.group(1));
        	else
        		throw new NavException(Constantes.IncapabledeDecodeUneLatitude + "("+s+")");
        	
        	if (Math.abs(a.asDegre()) > 90.0)
        		throw new NavException(Constantes.IncapabledeDecodeUneLatitude + " [|.| > 90.0]("+s+")");
        		
        	double dSens = 0.0;
        	if ((matcher.group(2) != null) && (matcher.group(2).length() > 0)) {
        		String sSens = matcher.group(2).toLowerCase();
        		if (sSens.equals("s")) dSens = -1.0;
        		else if (sSens.equals("n")) dSens = +1.0;
        		else
            		throw new NavException(Constantes.IncapabledeDecodeUneLatitude + " [N(+) ou S(-)]("+s+")");
        	}
        	a = AngleFactory.fromDegre(a.asDegre() * dSens);
        }
        else 
        	throw new NavException(Constantes.IncapabledeDecodeUneLatitude + "("+s+")");
        
        return Latitude.fromAngle(a);

	}
	
	
	public SensLatitude getSens() throws NavException {
		return Latitude.getSens(asDegre());
	}

	private static SensLatitude getSens(double asDegre) throws NavException {
		if ((asDegre >= 0.0) && (asDegre <= 90.0)) {
			return SensLatitude.Nord;
		}
		else if ((asDegre >= 270.0) && (asDegre < 360.0)) {
			return SensLatitude.Sud;
		}
		return SensLatitude.Error;
	}
	
	@Deprecated
	public void setSens(Object object) throws NavException {
		throw new NavException("deprecated");
	}
}
