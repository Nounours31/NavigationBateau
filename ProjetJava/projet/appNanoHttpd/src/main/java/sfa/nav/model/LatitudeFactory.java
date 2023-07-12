package sfa.nav.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.ePointsCardinaux;

public class LatitudeFactory extends Latitude {
	
	public static Latitude fromAngle(Angle d) throws NavException  {
		Latitude l = LatitudeFactory.fromDegre(d.asDegre());
		return l;
	}

	public static Latitude fromDegreAndSens(double latVertex, ePointsCardinaux sens) throws NavException {
		Latitude l = LatitudeFactory.fromString(latVertex + " " + sens.getTag());
		return l;
	}


	static public Latitude fromDegre (double x)  {
		if (Latitude.isValideAngleInDegre(x)) {
			Latitude l = new Latitude();
			l.set(x);
			return l;
		}
		return null;
	}
	
	static public Latitude fromString (String s) throws NavException  {
		final String regex = "^([0-9\\.°'\\\"]+)[ \\t]*([NSns])$";

		Angle a;

		final Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			if ((matcher.group(1) != null) && (matcher.group(1).length() > 0)) {
				a = AngleFactory.fromString(matcher.group(1));

				if (Math.abs(a.asDegre()) <= 90.0) {
					double dSens = 0.0;
					if ((matcher.group(2) != null) && (matcher.group(2).length() > 0)) {
						String sSens = matcher.group(2).toLowerCase();
						if (sSens.equals("s")) dSens = -1.0;
						if (sSens.equals("n")) dSens = +1.0;
						a = AngleFactory.fromDegre(a.asDegre() * dSens);
						
						return LatitudeFactory.fromAngle(a);
					}					
				}
			}
		}        
		throw new NavException(Constantes.IncapabledeDecodeUneLatitude + " [N(+) ou S(-)]("+s+")");
	}



}
