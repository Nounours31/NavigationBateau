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
public class LatitudeParser extends GeneriqueParser {
	private static Logger _logger = LoggerFactory.getLogger(LatitudeParser.class);


	protected LatitudeParser() {
		super();
		 _allRegex = new GeneriqueDataFormat[] {
					// Pb de l'encodage Windows du signe degre ....
					new GeneriqueDataFormat(1, "([NnSs]) (.*)", "N 25.89 [en decimal]")
		 };
	}
	
	private static LatitudeParser _instance = null;

	public static LatitudeParser getInstance() {
		if (_instance  == null) 
			_instance = new LatitudeParser();
		return _instance;
	}

	public boolean parse(String s, Latitude L) {
		boolean retour = false;

		for (GeneriqueDataFormat unFormat : _allRegex) {
			Pattern pattern = Pattern.compile(unFormat.getRegExp(), Pattern.UNICODE_CHARACTER_CLASS);
			Matcher matcher = pattern.matcher(s);

			if (matcher.matches()) {
				_logger.debug("Full match group(0): ==>{}<==", matcher.group(0));

				boolean bErr = false;
				String mErr = "";

				String signe = matcher.group(1);
				if ((signe == null) || (signe.isEmpty()) || (signe.equalsIgnoreCase("n"))) {
					L.setSens(Latitude.SensLatitude.Nord); 
				}
				else
					L.setSens(Latitude.SensLatitude.Sud); 


				AngleParser a = AngleParser.getInstance(); 
				Angle angle = new Angle(0.0);
				retour = a.parse(matcher.group(2), angle);
				double latitudeAsDouble = angle.getVal();
				if (retour) {
					if (Math.abs(latitudeAsDouble) <= 90.0) {
						L.setLatitude(latitudeAsDouble);
					}
					else
						retour = false;
				}
			}
			else {
				_logger.debug("No match ...");
			}
		}
		_logger.info("Read reel: {} - {}", retour, L);
		return retour;
	}
}
