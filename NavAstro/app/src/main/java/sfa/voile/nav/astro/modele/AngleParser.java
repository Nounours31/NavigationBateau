/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.modele;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pierr
 */
public class AngleParser extends GeneriqueParser {
	private static Logger _logger = LoggerFactory.getLogger(AngleParser.class);


	final private GeneriqueDataFormat[] _allRegex = {
			// Pb de l'encodage Windows du signe degre ....
			new GeneriqueDataFormat(1, "([+-]?)(\\d+[,\\.]\\d+)", "-25.89 [en decimal]"),
			new GeneriqueDataFormat(2, "([+-]?)(\\d+[,\\.]\\d+)[\\u00B0]", "-25.89\u00B0 [en decimal]"),
			
			new GeneriqueDataFormat(3, "([+-]?)(\\d{1,2})[\u00B0](\\d+[,\\.]\\d*)'", "-25\u00B059.59' [minute sexagedecmale / secondes decimales]"),
			new GeneriqueDataFormat(4, "([+-]?)(\\d{1,2})[\u00B0](\\d+[,\\.]\\d*)", "-25\u00B0059.59 [minute sexagedecmale / secondes decimales]"),
			new GeneriqueDataFormat(5, "([+-]?)(\\d{1,2})[\u00B0](\\d+)",           "-25\u00B0059' [minute sexagedecmale / secondes decimales]"),
			new GeneriqueDataFormat(55,"([+-]?)(\\d{1,2})[\u00B0](\\d+)'",           "-25\u00B0059' [minute sexagedecmale / secondes decimales]"),
			
			new GeneriqueDataFormat(6, "([+-]?)(\\d{1,2})[\u00B0](\\d{1,2})'(\\d{1,2})\"", "-25\u00B059'59\" [la complete]"),
			new GeneriqueDataFormat(7, "([+-]?)(\\d{1,2})d(\\d{1,2})m(\\d{1,2})s", "-25d07m58s [format inutile ...]"),
			new GeneriqueDataFormat(8, "([+-]?)(\\d{1,2}):(\\d{1,2}):(\\d{1,2})", "-25:07:58 [format horaire]")
	};


	private static AngleParser _instance = null;

	public static AngleParser getInstance() {
		if (_instance  == null) 
			_instance = new AngleParser();
		return _instance;
	}

	public boolean parse(String s, HandleDouble d) throws NumberFormatException {
		boolean retour = false;
		double val = 0.0;

		for (GeneriqueDataFormat unFormat : _allRegex) {
			_logger.debug("Parse==>{}<==  with==>{}<==", s, unFormat.toString());

			Pattern pattern = Pattern.compile(unFormat.getRegExp(), Pattern.UNICODE_CHARACTER_CLASS);
			Matcher matcher = pattern.matcher(s);

			if (matcher.matches()) {
				_logger.debug("Full match group(0): ==>{}<==", matcher.group(0));

				boolean bErr = false;
				int iCas = unFormat.getIndice();

				String signe = matcher.group(1);
				double dSigne = -1.0;
				if ((signe == null) || (signe.isEmpty()) || (signe.equals("+"))) {
					dSigne = +1.0;
				}

				double degre = 0.0;
				String sDegre = matcher.group(2);
				boolean rc = false;
				HandleDouble d2 = new HandleDouble();
				rc = this.parseDouble(sDegre, d2);
				degre = d2.d();

				switch (iCas) {
				case 1:
				case 2:
					break;
					
				case 3:
				case 4:
				case 5:
				case 55:
					String sMinute = matcher.group(3);
					double minute = 0.0;
					rc = this.parseDouble(sMinute, d2);
					minute = d2.d();
					if (minute > 60.0)
						return false;
					degre += (minute / 60.0);
					break;
					
				case 6:
				case 7:
				case 8:
					sMinute = matcher.group(3);
					minute = 0.0;
					rc = this.parseDouble(sMinute, d2);
					minute = d2.d();
					if (minute > 60.0)
						return false;
					
					String sSecond = matcher.group(4);
					double seconde = 0.0;
					rc = this.parseDouble(sSecond, d2);
					seconde = d2.d();
					if (seconde > 60.0)
						return false;
					degre += (minute + seconde / 60.0) / 60.0;
					
					break;
				default:
					return false;
				}

				val = degre * dSigne;
				retour = true;
				d.d(val);
				break;
			}
			else {
				_logger.debug("No match ...");
			}
		}
		if (!retour)
			_logger.warn("No Match");

		_logger.debug("Read reel: {} / {}", retour, d.d());
		return retour;
	}








}
