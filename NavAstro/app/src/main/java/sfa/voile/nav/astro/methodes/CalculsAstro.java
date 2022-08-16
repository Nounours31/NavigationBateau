/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.methodes;

import java.util.HashMap;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.modele.Angle;
import sfa.voile.nav.astro.modele.Declinaison;
import sfa.voile.nav.astro.modele.Heure;
import sfa.voile.nav.astro.modele.Latitude;
import sfa.voile.nav.astro.modele.Latitude.SensLatitude;

/**
 *
 * @author pierr
 */
public class CalculsAstro {
	private static Logger _logger = LoggerFactory.getLogger(CalculsAstro.class);

	public CalculsAstro() {
	}

	public static Latitude LatitudeMeridienne(HashMap<eCalculAstroConstantes, Object> args) {
		_logger.debug("Arg droite hauteur");

		Angle Ho = (Angle) args.get(eCalculAstroConstantes.Ho);
		Angle Ei = (Angle) args.get(eCalculAstroConstantes.Ei);
		Angle Hc = Ho.plus (Ei);

		Angle Hv = Hc.plus ((Angle) args.get(eCalculAstroConstantes.CorrectionSolaire));
		double dz /* distanceZenitale */ = 90.0 - Hv.getVal();

		Angle D = (Angle) args.get(eCalculAstroConstantes.DeclinaisonSolaire);

		Latitude L = new Latitude();
		L.setLatitude(D.plus(dz));
		System.out.println("Latitude: " + L.toString());
		return L;
	}

	public static Declinaison DeclinaisonSoleil (Hashtable<eCalculAstroConstantes, Object> Args) {
		if (Args.containsKey(eCalculAstroConstantes.Methode_CalculDeclinaison_ParGradient))
				return DeclinaisonSoleilParGradiant (Args);
		if (Args.containsKey(eCalculAstroConstantes.Methode_CalculDeclinaison_ParInterval))
			return DeclinaisonSoleilParInterval(Args);
		return null;
	}

	public static Declinaison DeclinaisonSoleilParGradiant (Hashtable<eCalculAstroConstantes, Object> methodeArgs) {
		Declinaison D = new Declinaison();
		_logger.debug("Calcul declinaison par gradiant");
		Heure H1 = (Heure) methodeArgs.get(eCalculAstroConstantes.HeureUTRef);
		Heure H = (Heure) methodeArgs.get(eCalculAstroConstantes.HeureMeusure);
		Declinaison D1 = new Declinaison ((Latitude) methodeArgs.get(eCalculAstroConstantes.DeclinaisonRef));
		Angle tauxDeVariation = (Angle) methodeArgs.get(eCalculAstroConstantes.PasDeDeclinaison);

		double NbHeure = H.moins(H1);
		double declinaison1AsDouble = D1.getLatitude() * ((D1.getSens() == SensLatitude.Nord) ? (+1.0) : (-1.0)); 

		double newDeclinaison = declinaison1AsDouble + tauxDeVariation.multiply(NbHeure);
		D.setLatitude(Math.abs(newDeclinaison));
		D.setSens((newDeclinaison < 0.0) ? SensLatitude.inverse(D1.getSens()) : D1.getSens());
		_logger.debug("H  {}", H);
		_logger.debug("H1 {}", H1);
		_logger.debug("Declinaison D1 {}", D1);
		_logger.debug("tauxDeVariation  {} / jour", tauxDeVariation);
		_logger.debug("NbHeure: {}", NbHeure);
		_logger.debug("Declinaison D {} == {}", D, newDeclinaison);
		return D;
	}
	
	public static Declinaison DeclinaisonSoleilParInterval (Hashtable<eCalculAstroConstantes, Object> methodeArgs) {
		Declinaison D = new Declinaison();
		_logger.debug("Calcul declinaison par interval");
		Heure H1 = (Heure) methodeArgs.get(eCalculAstroConstantes.HeureUT1);
		Heure H2 = (Heure) methodeArgs.get(eCalculAstroConstantes.HeureUT2);
		Heure H = (Heure) methodeArgs.get(eCalculAstroConstantes.HeureMeusure);

		if ((H.apres (H2)) || H.avant(H1)){
			_logger.error("Heure de meusure ou interval incorrect: on devrai avoir Hd < H < Hf [et on a: {} < {} < {}]", H1.toString(), H.toString(), H2.toString());
			return D;
		}

		double taux = H.moins(H1) / H2.moins(H1);

		Declinaison D1 = new Declinaison ((Latitude) methodeArgs.get(eCalculAstroConstantes.Declinaison1));
		Declinaison D2 = new Declinaison ((Latitude) methodeArgs.get(eCalculAstroConstantes.Declinaison2));

		double declinaison1AsDouble = D1.getLatitude() * ((D1.getSens() == SensLatitude.Nord) ? (+1.0) : (-1.0)); 
		double declinaison2AsDouble = D2.getLatitude() * ((D2.getSens() == SensLatitude.Nord) ? (+1.0) : (-1.0)); 

		double newDeclinaison = declinaison1AsDouble + taux * (declinaison2AsDouble - declinaison1AsDouble);
		D.setLatitude(Math.abs(newDeclinaison));
		D.setSens((newDeclinaison < 0.0) ? SensLatitude.Sud : SensLatitude.Nord);

		_logger.debug("H1 {}", H1);
		_logger.debug("H2 {}", H2);
		_logger.debug("H  {}", H);
		_logger.debug("Taux: {}", taux);
		_logger.debug("Declinaison D1 {}", D1);
		_logger.debug("Declinaison D2 {}", D2);
		_logger.debug("Declinaison D {} == {}", D, newDeclinaison);

		return D;
	}

}
