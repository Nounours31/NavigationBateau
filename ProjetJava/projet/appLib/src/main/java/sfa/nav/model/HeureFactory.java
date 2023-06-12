package sfa.nav.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sfa.nav.infra.tools.error.NavException;

public class HeureFactory extends Heure {
	static private final String regexp_Heure = "^([0-9]+)h?$";
	/*
		10
		10h
	 */
	static private final String regexp_heureDecimale = "^([0-9]+)\\.([0-9]+)h?$";
	/*
		10°25'52"
		10°25'52'
		10°25'52
	 */

	static private final String regexp_heusesexadecimale= "^([0-9]+):([0-9]{1,2})(:[0-9]{1,2}(\\.[0-9]+)?)?h?$?$";
	/*
		10:10
		10:10:10
		10:12:10.89666 - G1=10 G2=12 G3=:10.89666
		0:00:01
	 */


	public static Heure fromString (String s) throws NavException {
		final Pattern pattern_regexp_Heure = Pattern.compile(regexp_Heure);
		final Pattern pattern_regexp_heureDecimale = Pattern.compile(regexp_heureDecimale);
		final Pattern pattern_regexp_heusesexadecimale = Pattern.compile(regexp_heusesexadecimale);

		Heure h = new Heure();
		boolean find = false;
		Matcher matcher = pattern_regexp_Heure.matcher(s);
		if (matcher.find()) {
			h.set(Double.parseDouble(matcher.group(1)));
			find = true;
		}
		if (!find) {
			matcher = pattern_regexp_heureDecimale.matcher(s);
			if (matcher.find()) {
				double ValEntiere = Double.parseDouble(matcher.group(1));
				double ValDecimale = Double.parseDouble(matcher.group(2));
				while (ValDecimale > 1.0)
					ValDecimale /= 10.0;
				h.set(ValEntiere + ValDecimale); 
				find = true;
			}
		}
		if (!find) {
			matcher = pattern_regexp_heusesexadecimale.matcher(s);
			if (matcher.find()) {
				double heure = Double.parseDouble(matcher.group(1));
				double minutes = Double.parseDouble(matcher.group(2));
				double secondes = 0.0;
				if ((matcher.groupCount() <= 4) && (matcher.group(3) != null))
					secondes = Double.parseDouble(matcher.group(3).replaceAll(":", ""));

				h.set(heure + minutes / 60.0 + secondes / 3600.0); 
				find = true;
			}
		}
		if (!find) {
			throw new NavException("Heure KO");
		}

		return h;
	}


	public static Heure fromHeureDecimale(double d) {
		Heure h = new Heure();
		h.set(d);
		return h;
	}
}
