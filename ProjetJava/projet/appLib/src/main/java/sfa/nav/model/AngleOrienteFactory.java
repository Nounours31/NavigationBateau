package sfa.nav.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;

public class AngleOrienteFactory extends AngleOriente {
	private static Logger logger = LoggerFactory.getLogger(AngleOrienteFactory.class);

	
	public static AngleOriente fromString (String s) throws NavException {
		AngleOriente retour = new AngleOriente();
        final Pattern regexp = Pattern.compile("([+-])?(.*)");
        
        
        Matcher matcher = regexp.matcher(s);
        if (matcher.find()) {
        	for (int i = 1; i <= matcher.groupCount(); i++) {
        		logger.debug ("\tgroup({}): {}", i, matcher.group(i));
			}
        	
        	double signe = +1.0;
        	if ((matcher.group(1) != null) && (matcher.group(1).length() > 0)) {
        		if (matcher.group(1).equals("-"))
        			signe = -1.0;
        		else if (matcher.group(1).equals("+"))
        			signe = +1.0;
        		else 
        			throw new NavException("Signe invalide pour un Angle oriente");
        	}
        	
        	try {
        		Angle a = AngleFactory.fromString(matcher.group(2));
        		retour.set(signe * a.asDegre());
        	}
        	catch (Exception e) {
        		throw new NavException("ceci n'est pas un Angle oriente: " + s + " err: " + e.getMessage());
        	}        	
        }
        else {
        	throw new NavException("ceci n'est pas un Angle oriente: " + s);
        }
        return retour;
	}

	
	static public AngleOriente fromDegre (double x) {
		AngleOriente retour = new AngleOriente();
		retour.set(x);
		return retour;
	}
	

	static public AngleOriente fromRadian(double x) {
		return AngleOrienteFactory.fromDegre(Angle.RadianToDegre(x));
	}


	static public AngleOriente fromAngle(Angle s)  {
		return AngleOrienteFactory.fromDegre(s.asDegre());
	}
}
