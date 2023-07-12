package sfa.nav.model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;

public class NavDureeFactory extends NavDateHeure {
	private static Logger _logger = LoggerFactory.getLogger(NavDureeFactory.class);
	static private final String regexp_DureeDecimale = "^([\\-\\+])?\\s*([0-9]*)(\\.)*([0-9]*)h?$";
	/*
	 * 10°25'52" 10°25'52' 10°25'52
	 */

	static private final String regexp_DureeExadecimale = "^([\\-\\+])?\\s*([0-9]+)(:[0-9]{1,2})(:[0-9]{1,2}(\\.[0-9]+)?)?h?$?$";
	/*
	 * 10:10 10:10:10 10:12:10.89666 - G1=10 G2=12 G3=:10.89666 0:00:01
	 */

	public class InfoFormatter {
		final DateTimeFormatter DATE_FORMAT;
		final boolean isZoulou;
		final String format;

		InfoFormatter(DateTimeFormatter d, boolean z, String s) {
			DATE_FORMAT = d;
			isZoulou = z;
			format = s;
		}
	}

	public static NavDuree fromString(String s) throws NavException {
		NavDuree retour = new NavDuree();
		final Pattern pattern_regexp_DureeDecimale = Pattern.compile(regexp_DureeDecimale);
		final Pattern pattern_regexp_DureeExadecimale = Pattern.compile(regexp_DureeExadecimale);

		boolean find = false;
		Matcher matcher = pattern_regexp_DureeDecimale.matcher(s);
		if (matcher.find()) {
			double signe = +1.0;
			if ((matcher.group(1) != null) && (matcher.group(1).length() > 0)) {
				if (matcher.group(1).equals("-"))
					signe = -1.0;
			}
			double ValEntiere = 0.0;
			if ((matcher.group(2) != null) && (matcher.group(2).length() > 0)) {
				ValEntiere = Double.parseDouble(matcher.group(2));
			}
			boolean hasPoint = false;
			if ((matcher.group(3) != null) && (matcher.group(3).length() > 0)) {
				hasPoint = true;
			}
			double ValDecimale = 0.0;
			if ((matcher.group(4) != null) && (matcher.group(4).length() > 0)) {
				if (hasPoint)
					ValDecimale = Double.parseDouble(matcher.group(4));
				else
					ValEntiere = Double.parseDouble(matcher.group(1) + matcher.group(4));
			}
			while (ValDecimale >= 1.0)
				ValDecimale /= 10.0;
			retour.setTodayHeureDecimale(signe * (ValEntiere + ValDecimale));
			find = true;
		}
		if (!find) {
			matcher = pattern_regexp_DureeExadecimale.matcher(s);
			if (matcher.find()) {
				double signe = +1.0;
				if ((matcher.group(1) != null) && (matcher.group(1).length() > 0)) {
					if (matcher.group(1).equals("-"))
						signe = -1.0;
				}
				double heure = 0.0;
				if ((matcher.group(2) != null) && (matcher.group(2).length() > 0)) {
					heure = Double.parseDouble(matcher.group(2));
				}
				double minute = 0.0;
				if ((matcher.group(3) != null) && (matcher.group(3).length() > 0)) {
					minute = Double.parseDouble(matcher.group(3).substring(1));
				}
				double seconde = 0.0;
				if ((matcher.group(4) != null) && (matcher.group(4).length() > 0)) {
					seconde = Double.parseDouble(matcher.group(4).substring(1));
				}

				retour.setTodayHeureDecimale(signe * (heure + minute / 60.0 + seconde / 3600.0));
				find = true;
			}
		}
		if (!find) {
			_logger.error("Heure KO - {}", s);
			throw new NavException("Duree KO");
		}
		return retour;
	}

	public static NavDateHeure fromHeureDecimale(double d) {
		NavDateHeure h = new NavDateHeure();
		h.setTodayHeureDecimale(d);
		return h;
	}

	public static NavDateHeure fromZonedDateTime(ZonedDateTime plus) {
		NavDateHeure retour = new NavDateHeure();
		retour.setValeur(plus);
		return retour;
	}
}
