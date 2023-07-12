package sfa.nav.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.ePointsCardinaux;

public class LongitudeFactory extends Longitude {
	private static Logger logger = LoggerFactory.getLogger(LongitudeFactory.class);
	
	public static Longitude fromAngle(Angle d)  {
		Longitude l = LongitudeFactory.fromDegre(d.asDegre());
		return l;
	}
	
	public static Longitude fromRadian(double inRadian)  {
		Longitude l = LongitudeFactory.fromDegre(Angle.RadianToDegre(inRadian));
		return l;
	}
	
	static public Longitude fromDegre (double x)  {
		Longitude l = new Longitude();
		if (Longitude.isValideAngleInDegre(x)) {
			l.set(x);
			return l;	
		}
		return null;
	}
	
	public static Longitude fromDegreAndSens(double d, ePointsCardinaux sensLongiVertex) throws NavException {
		return LongitudeFactory.fromString(d + " " + sensLongiVertex.getTag());
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
        		a = AngleFactory.fromString(matcher.group(1));
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
        	a = AngleFactory.fromDegre(a.asDegre() * dSens);
        }
        else 
        	throw new NavException(Constantes.IncapabledeDecodeUneLongitude + "("+s+")");
        
        return LongitudeFactory.fromAngle(a);
	}
}
