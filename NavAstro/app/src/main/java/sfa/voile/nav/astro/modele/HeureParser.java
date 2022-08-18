package sfa.voile.nav.astro.modele;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pierr
 */
public class HeureParser extends GeneriqueParser {
	private static Logger _logger = LoggerFactory.getLogger(HeureParser.class);

	private static HeureParser _instance = null;

	public HeureParser() {
		super();
		_allRegex = new GeneriqueDataFormat[] {
			// Pb de l'encodage Windows du signe degre ....
			new GeneriqueDataFormat(1, "([+-]?)(\\d+[,\\.]\\d+)", "-25.89 [en decimal]"),
			new GeneriqueDataFormat(2, "([+-]?)(\\d+[,\\.]\\d+)'", "-25.89 [en decimal]"),
			new GeneriqueDataFormat(3, "([+-]?)(\\d{1,2}):(\\d{1,2}):(\\d{1,2})", "-25:07:58 [format horaire]"),
			new GeneriqueDataFormat(4, "([+-]?)(\\d{1,2}):(\\d+[,\\.]\\d*)", "-25:59.99 [format horaire/ minute sexadecimake / seconde decimale ]")
	};

	}
	public static HeureParser getInstance() {
		if (_instance  == null) 
			_instance = new HeureParser();
		return _instance;
	}

	public boolean parse(String s, Heure d) {
		boolean retour = false;
		double val = 0.0;

		for (GeneriqueDataFormat unFormat : _allRegex) {
			Pattern pattern = Pattern.compile(unFormat.getRegExp(), Pattern.UNICODE_CHARACTER_CLASS);
			Matcher matcher = pattern.matcher(s);

			if (matcher.matches()) {
				_logger.debug("Full match group(0): ==>{}<==", matcher.group(0));

				int iCas = unFormat.getIndice();

				String signe = matcher.group(1);
				double dSigne = -1.0;
				if ((signe == null) || (signe.isEmpty()) || (signe.equals("+"))) {
					dSigne = +1.0;
				}

				double heure = 0.0;
				String sHeure = matcher.group(2);
				boolean rc = false;
				HandleDouble d2 = new HandleDouble();
				rc = this.parseDouble(sHeure, d2);
				heure = d2.d();

				if (!rc) 
					return rc;

				switch (iCas) {
				case 1:
				case 2: 
					// minute decimale
					break;
				case 4:
					double minute = 0.0;
					String sMinute = matcher.group(3);
					rc = this.parseDouble(sMinute, d2);
					minute = d2.d();
					if (!rc) 
						return rc;
					if (minute >= 60.0)
						return false;
					minute = minute / 60.0;
					heure += minute;
					break;

				case 3:
					double seconde = 0.0;
					sMinute = matcher.group(3);
					rc = this.parseDouble(sMinute, d2);
					minute = d2.d();
					if (!rc) 
						return rc;
					if (minute >= 60.0)
						return false;

					String sSeconde = matcher.group(4);
					rc = this.parseDouble(sSeconde, d2);
					seconde = d2.d();
					if (!rc) 
						return rc;
					if (seconde >= 60.0)
						return false;
					heure += (minute + seconde / 60.0) / 60.0;
					break;

				default: 
					return false;
				}
				val = heure * dSigne;
				retour = true;
				d.setVal(val);
				break;
			}
			else {
				_logger.debug("No match ...");
			}
		}
		_logger.debug("Read reel: {} / {}", retour, d);
		return retour;

	}


}
