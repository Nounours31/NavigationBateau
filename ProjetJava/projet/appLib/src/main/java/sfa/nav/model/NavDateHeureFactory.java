package sfa.nav.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;

public class NavDateHeureFactory extends NavDateHeure {
	private static Logger _logger = LoggerFactory.getLogger(NavDateHeureFactory.class);
	static private final String regexp_Heure = "^([0-9]+)h?$";
	/*
	 * 10 10h
	 */
	static private final String regexp_heureDecimale = "^([0-9]+)\\.([0-9]+)h?$";
	/*
	 * 10°25'52" 10°25'52' 10°25'52
	 */

	static private final String regexp_heusesexadecimale = "^([0-9]+):([0-9]{1,2})(:[0-9]{1,2}(\\.[0-9]+)?)?h?$?$";
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

	public static NavDateHeure fromString(String s) throws NavException {
		NavDateHeureFactory pipo = new NavDateHeureFactory();
		InfoFormatter[] formatters = {
				pipo.new InfoFormatter(new DateTimeFormatterBuilder().appendPattern("uuuu/MM/dd[ [HH][:mm][:ss][.SSS]]")
						.parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
						.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
						.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0).parseStrict().toFormatter()
						.withResolverStyle(ResolverStyle.STRICT), false, "uuuu/MM/dd[ [HH][:mm][:ss][.SSS]]"),
				pipo.new InfoFormatter(new DateTimeFormatterBuilder()
						.appendPattern("uuuu/MM/dd[ [HH][:mm][:ss][.SSS]] z")
						.parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
						.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
						.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0).parseStrict().toFormatter()
						.withResolverStyle(ResolverStyle.STRICT), true, "uuuu/MM/dd[ [HH][:mm][:ss][.SSS]] z"),
				pipo.new InfoFormatter(new DateTimeFormatterBuilder()
						.appendPattern("dd/MM[/yyyy][ [HH][:mm][:ss][.SSS]]")
						.parseDefaulting(ChronoField.YEAR, LocalDate.now().get(ChronoField.YEAR))
						.parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
						.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
						.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0).parseStrict().toFormatter()
						.withResolverStyle(ResolverStyle.STRICT), false, "dd/MM[/yyyy][ [HH][:mm][:ss][.SSS]]"),
				pipo.new InfoFormatter(
						new DateTimeFormatterBuilder().appendPattern("dd/MM[/yyyy][ [HH][:mm][:ss][.SSS]] z")
								.parseDefaulting(ChronoField.YEAR, LocalDate.now().get(ChronoField.YEAR))
								.parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
								.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
								.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
								.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0).parseStrict().toFormatter()
								.withResolverStyle(ResolverStyle.STRICT),
						true, "dd/MM[/yyyy][ [HH][:mm][:ss][.SSS]] z"),
				pipo.new InfoFormatter(new DateTimeFormatterBuilder().appendPattern("dd/MM[/yy][ [HH][:mm][:ss][.SSS]]")
						.parseDefaulting(ChronoField.YEAR, LocalDate.now().get(ChronoField.YEAR))
						.parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
						.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
						.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0).parseStrict().toFormatter()
						.withResolverStyle(ResolverStyle.STRICT), false, "dd/MM[/yy][ [HH][:mm][:ss][.SSS]]"),
				pipo.new InfoFormatter(
						new DateTimeFormatterBuilder().appendPattern("dd/MM[/yy][ [HH][:mm][:ss][.SSS]] z")
								.parseDefaulting(ChronoField.YEAR, LocalDate.now().get(ChronoField.YEAR))
								.parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
								.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
								.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
								.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0).parseStrict().toFormatter()
								.withResolverStyle(ResolverStyle.STRICT),
						true, "dd/MM[/yy][ [HH][:mm][:ss][.SSS]] z"), };

		for (InfoFormatter dtf : formatters) {
			try {
				TemporalAccessor d = dtf.DATE_FORMAT.parse(s);
				if (dtf.isZoulou) {
					NavDateHeure h = new NavDateHeure();
					h.setValeur(ZonedDateTime.from(d));
					return h;
				} else {
					NavDateHeure h = new NavDateHeure();
					h.setValeur(ZonedDateTime.of(LocalDateTime.from(d), NavDateHeureFactory.myZone()));
					return h;
				}
			} catch (Exception e) {
				_logger.error("{}\n{}\n123456789012345678901234567890123456789", dtf.format, s);
				_logger.error("Impossible de parser avec sdfZoulou - {} / {}", s, e.getMessage());
			}
		}

		final Pattern pattern_regexp_Heure = Pattern.compile(regexp_Heure);
		final Pattern pattern_regexp_heureDecimale = Pattern.compile(regexp_heureDecimale);
		final Pattern pattern_regexp_heusesexadecimale = Pattern.compile(regexp_heusesexadecimale);

		NavDateHeure h = new NavDateHeure();
		boolean find = false;
		Matcher matcher = pattern_regexp_Heure.matcher(s);
		if (matcher.find()) {
			h.setTodayHeureDecimale(Double.parseDouble(matcher.group(1)));
			find = true;
		}
		if (!find) {
			matcher = pattern_regexp_heureDecimale.matcher(s);
			if (matcher.find()) {
				double ValEntiere = Double.parseDouble(matcher.group(1));
				double ValDecimale = Double.parseDouble(matcher.group(2));
				while (ValDecimale > 1.0)
					ValDecimale /= 10.0;
				h.setTodayHeureDecimale(ValEntiere + ValDecimale);
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

				h.setTodaysecondes((long)(heure * 3600.0 + minutes * 60.0 + secondes * 1.0));
				find = true;
			}
		}
		if (!find) {
			_logger.error("Heure KO - {}", s);
			throw new NavException("Heure KO");
		}

		return h;
	}

	public static NavDateHeure fromSecondes(long s) {
		NavDateHeure h = new NavDateHeure();
		h.setEpochsecondes(s);
		return h;
	}

	public static NavDateHeure fromHeureDecimale(double d) {
		NavDateHeure h = new NavDateHeure();
		h.setEpochHeureDecimale(d);
		return h;
	}

	public static NavDateHeure fromZonedDateTime(ZonedDateTime plus) {
		NavDateHeure retour = new NavDateHeure();
		retour.setValeur(plus);
		return retour;
	}
}
