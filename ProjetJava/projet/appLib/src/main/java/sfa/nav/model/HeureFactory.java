package sfa.nav.model;

import java.util.regex.Pattern;

import sfa.nav.infra.tools.error.NavException;

public class HeureFactory extends Heure {
	static private final String regexp_Heure = "^([0-9]+)h?$";
	/*
		10
		10h
					 */
	static private final String regexp_heureDecimale = "^([0-9]+).([0-9]+)h?$";
	/*
		10°25'52"
		10°25'52'
		10°25'52
	*/

	static private final String regexp_heusesexadecimale= "^([0-9]+):([0-9]{2})(:[0-9]{2}(\\.[0-9]+)?)?h?$?$";
	/*
		10:10
		10:10:10
		10:12:10.89666 - G1=10 G2=12 G3=:10.89666
		0:00:01
	*/

	
	public static Heure fromString (String s) throws NavException {
        Angle retour = new Angle();
        final Pattern pattern_regexp_Heure = Pattern.compile(regexp_Heure);
        final Pattern pattern_regexp_heureDecimale = Pattern.compile(regexp_heureDecimale);
        final Pattern pattern_regexp_heusesexadecimale = Pattern.compile(regexp_heusesexadecimale);
        
        return new Heure();
        
        }


	public static Heure fromHeureDecimale(double d) {
		Heure h = new Heure();
		h.set(d);
		return h;
	}
}
