package sfa.nav.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;

public class NavDateHeureFactory extends NavDateHeure {
	private static Logger _logger = LoggerFactory.getLogger(NavDateHeureFactory.class);
	static private final String regexp_Heure = "^([0-9]+)$";	 // 10
	static private final String regexp_heureDecimale = "^([0-9]+)\\.([0-9]+)$"; // 10.25
	static private final String regexp_heusesexadecimale = "^([0-9]+):([0-9]{1,2})(:([0-9]{1,2})(\\.([0-9]+))?)?$";

	static private final String regexp_Date = "^([0-9]{1,2})/([0-9]{1,2})(/([0-9]{2,4}))?$";	 // 1/1/2001
	static private final String regexp_Date2 = "^([0-9]{4})/([0-9]{1,2})/([0-9]{1,2})$";	 // 2001/1/1

	static private final String regexp_zone = "^(CET|CEST|GMT|UTC)?$"; 

	private enum eHelper2Parse {
		AnneeEnFin, AnneeAuDebut, Heure, Heuredecimale, heusesexadecimale, Zone;
	}

	private enum eCollectedInfo {
		HeureDecimale, HeureSexadecimale, NoCollectee;
	}
	static private class DateParserHandler {
		public int Y;
		public int M;
		public int D;
		public double h;
		public int hAsInt;
		public int mAsInt;
		public double sAsDouble;
		public ZoneId zone;
		public eCollectedInfo collectedInfo;
		
		DateParserHandler() {
			Y = M = D = hAsInt = mAsInt = 0;
			h = sAsDouble = 0.0;
			collectedInfo = eCollectedInfo.NoCollectee;
		}
	}
	

	public static NavDateHeure fromString(String sIn) throws NavException {
		NavDateHeure h = new NavDateHeure();
		boolean find = false;
		DateParserHandler dph = new DateParserHandler();

		// ------------------------
		// Recup des info
		// ------------------------
		String[] aSplited = sIn.split(" ");
		List<String>  sSplited = new ArrayList<>();
		
		//---
		// Split sur le 'T' 2002/01/01T1:00
		//---
		for (String s : aSplited) {
			int iPos = s.indexOf('T', 0);
			if ((iPos > 0) && (iPos < (s.length() -1))) {
				if (Character.isDigit(s.charAt(iPos -1)) && Character.isDigit(s.charAt(iPos + 1))) {
					sSplited.add(s.substring(0, iPos));
					sSplited.add(s.substring(iPos + 1));
				}
				else
					sSplited.add(s);
			}
			else
				sSplited.add(s);
		}		
		
		// ------------------------
		// decodage Date
		// ------------------------
		for (String s : sSplited) {
			find = decodeAllDatePossible(s.trim(), dph);
			if (find)
				break;
		}
		if (!find) {
			LocalDate z = LocalDate.now();
			_logger.error("DateHeure KO - NO DATE {} - Take current Day {}", sIn, z);
			dph.Y = z.getYear();
			dph.M = z.getMonthValue();
			dph.D = z.getDayOfMonth();
		}

		// ------------------------
		// decodage heure
		// ------------------------
		find = false;
		for (String s : sSplited) {
			find = decodeAllHeurePossible(s.trim(), dph);
			if (find)
				break;
		}
		if (!find) {
			_logger.error("DateHeure KO - NO TIME {} - Take midi {}", sIn, "12:00:00");
			dph.hAsInt = 12;
			dph.mAsInt = 0;
			dph.sAsDouble = 0.0;
			dph.collectedInfo = eCollectedInfo.HeureSexadecimale;
		}
		
		
		// ------------------------
		// decodage zone
		// ------------------------
		find = false;
		for (String s : sSplited) {
			find = decodeAllZonePossible(s.trim(), dph);
			if (find)
				break;
		}
		if (!find) {
			if (NavDateHeure.myZone() != null)
				dph.zone = NavDateHeure.myZone();
			else
				dph.zone = ZoneId.of("Europe/Paris");
			
			_logger.error("DateHeure KO - NO ZONE {} - Take current {}", sIn, dph.zone);
		}


		ZonedDateTime zdt = null;
		if (dph.collectedInfo == eCollectedInfo.NoCollectee) {
			dph.collectedInfo = eCollectedInfo.HeureSexadecimale;
			dph.hAsInt = 0;
			dph.mAsInt = 0;
			dph.sAsDouble = 0.0;
		}
		if (dph.collectedInfo == eCollectedInfo.HeureDecimale) {
			int heure, min, sec, nanosec;
			heure = (int)Math.floor(dph.h);
			min = (int)Math.floor((dph.h - heure * 1.0) * 60.0);
			sec = (int)Math.floor((dph.h - heure * 1.0) * 3600.0 - min * 60.0);
			nanosec = (int)(((dph.h - heure * 1.0) * 3600.0 - min * 60.0 - sec * 1.0) * 1000000.0); 
			zdt = ZonedDateTime.of(
					LocalDate.of(dph.Y, dph.M, dph.D), 
					LocalTime.of(heure, min, sec, nanosec),
					dph.zone);
		}
		if (dph.collectedInfo == eCollectedInfo.HeureSexadecimale) {
			zdt = ZonedDateTime.of(dph.Y, dph.M, dph.D, dph.hAsInt, dph.mAsInt, (int)Math.floor(dph.sAsDouble), 0, dph.zone);
		}
		
		h.setValeur(zdt);
		return h;
	}

	private static boolean decodeAllZonePossible(String s, DateParserHandler dph) {
		boolean find = false;
		Pattern p = Pattern.compile(regexp_zone);
		Matcher matcher = p.matcher(s);
		if (matcher.find()) {
			find = parseZone(eHelper2Parse.Zone, matcher, dph);
		}
		return find;
	}

	private static boolean parseZone(eHelper2Parse cas, Matcher matcher, DateParserHandler dph) {
		boolean retour = false;
		if (cas == eHelper2Parse.Zone) {
			if (((matcher.group(1) != null) && (matcher.group(1).length() > 0))) {
				switch(matcher.group(1)) {
				case "CEST": dph.zone = ZoneId.of("Europe/Paris"); break;
				case "CET": dph.zone = ZoneId.of("Europe/Paris"); break;
				case "UTC": dph.zone = ZoneId.of("UTC"); break;
				case "GMT": dph.zone = ZoneId.of("UTC"); break;
				default : dph.zone = ZoneId.of("Europe/Paris");
				}
				retour = true;
			}
		}
		return retour;
	}

	private static boolean decodeAllHeurePossible(String s, DateParserHandler dph) {
		boolean find = false;
		Pattern p = Pattern.compile(regexp_Heure);
		Matcher matcher = p.matcher(s);
		if (matcher.find()) {
			find = parseHeure(eHelper2Parse.Heure, matcher, dph);
		}
		if (!find) {
			p = Pattern.compile(regexp_heureDecimale);
			matcher = p.matcher(s);
			if (matcher.find()) {
				find = parseHeure(eHelper2Parse.Heuredecimale, matcher, dph);
			}
		}
		if (!find) {
			p = Pattern.compile(regexp_heusesexadecimale);
			matcher = p.matcher(s);
			if (matcher.find()) {
				find = parseHeure(eHelper2Parse.heusesexadecimale, matcher, dph);
			}
		}
		return find;
	}

	private static boolean decodeAllDatePossible(String s, DateParserHandler dph) {
		boolean find = false;
		
		Pattern p = Pattern.compile(regexp_Date);
		Matcher matcher = p.matcher(s);
		if (matcher.find()) {
			find = parseDate(eHelper2Parse.AnneeEnFin, matcher, dph);
		}
		if (!find) {
			p = Pattern.compile(regexp_Date2);
			matcher = p.matcher(s);
			if (matcher.find()) {
				find = parseDate(eHelper2Parse.AnneeAuDebut, matcher, dph);
			}
		}
		return find;
	}


	private static boolean parseHeure(eHelper2Parse cas, Matcher matcher, DateParserHandler dph) {
		boolean retour = false;
		if (cas == eHelper2Parse.Heure) {
			if (((matcher.group(1) != null) && (matcher.group(1).length() > 0))) {
				dph.h = Double.parseDouble(matcher.group(1));
				dph.collectedInfo = eCollectedInfo.HeureDecimale;
				retour = true;
			}
		}
		if (cas == eHelper2Parse.Heuredecimale) {
			if (((matcher.group(1) != null) && (matcher.group(1).length() > 0)) && 
					((matcher.group(2) != null) && (matcher.group(2).length() > 0))) {
				dph.h = Double.parseDouble(matcher.group(1));
				double z = Double.parseDouble(matcher.group(2));
				while (Math.abs(z) > 1) z /= 10.0;
				dph.h += z;
				dph.collectedInfo = eCollectedInfo.HeureDecimale;
				retour = true;
			}
		}
		if (cas == eHelper2Parse.heusesexadecimale) {
			// "([0-9]+):([0-9]{1,2})(:([0-9]{1,2})(\\.([0-9]+))?)?"
			if (((matcher.group(1) != null) && (matcher.group(1).length() > 0))) {
				dph.hAsInt = Integer.parseInt(matcher.group(1));
				dph.collectedInfo = eCollectedInfo.HeureSexadecimale;
			}
			else
				return retour;

			if (((matcher.group(2) != null) && (matcher.group(2).length() > 0))) {
				dph.mAsInt = Integer.parseInt(matcher.group(2));
			}
			else
				return retour;
			
			if (((matcher.group(4) != null) && (matcher.group(4).length() > 0))) {
				dph.sAsDouble = Double.parseDouble(matcher.group(4));
			}

			if (((matcher.group(6) != null) && (matcher.group(6).length() > 0))) {
				double z = Double.parseDouble(matcher.group(6));
				while (Math.abs(z) > 1) z /= 10.0;
				dph.sAsDouble += z;	
			}			
				
			retour = true;
		}
		return retour;
	}

	private static boolean parseDate(eHelper2Parse cas, Matcher matcher, DateParserHandler dph) {
		boolean retour = false;
		if (cas == eHelper2Parse.AnneeAuDebut) {
			if (((matcher.group(1) != null) && (matcher.group(1).length() == 4)) && 
					((matcher.group(2) != null) && (matcher.group(2).length() > 0) && (matcher.group(2).length() < 3)) && 
					((matcher.group(3) != null) && (matcher.group(3).length() > 0) && (matcher.group(3).length() < 3))) {
				dph.Y = Integer.parseInt(matcher.group(1));
				dph.M = Integer.parseInt(matcher.group(2));
				dph.D = Integer.parseInt(matcher.group(3));
				retour = true;
			}
		}
		if (cas == eHelper2Parse.AnneeEnFin) {
				if(((matcher.group(2) != null) && (matcher.group(2).length() > 0) && (matcher.group(2).length() < 3)) && 
					((matcher.group(1) != null) && (matcher.group(1).length() > 0) && (matcher.group(1).length() < 3))) {
				
				if((matcher.group(3) != null) && (matcher.group(3).length() > 0)) 
						dph.Y = Integer.parseInt(matcher.group(4));
				else
					dph.Y = LocalDate.now().getYear();
				
				if (dph.Y < 100)
					dph.Y += 2000;
				dph.M = Integer.parseInt(matcher.group(2));
				dph.D = Integer.parseInt(matcher.group(1));
				retour = true;
			}
		}
		return retour;
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

	public static NavDateHeure fromStringSafe(String s) {
		try {
			return fromString(s);
		} catch (NavException e) {
			try {
				return fromString("1/1/2000 0:0:0 UTC");
			} catch (NavException e1) {
				return null;
			}
		}
	}
}
