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
import sfa.voile.nav.astro.tools.NavAstroException;

/**
 *
 * @author pierr
 */
public class CalculsAstro {
	private Logger _logger = LoggerFactory.getLogger(CalculsAstro.class);

	public CalculsAstro() {
	}

	public Latitude LatitudeMeridienne(Angle Ho, Angle Ei, Angle Hc, Angle Hv, Declinaison D) {
		_logger.debug("Arg droite hauteur");

		//  distanceZenitale
		double dz = 90.0 - Hv.getVal();
		Latitude L = new Latitude();
		L.setLatitude(D.plus(dz));
		
		System.out.println("Latitude: " + L.toString());
		return L;
	}


	public Declinaison DeclinaisonSoleilParGradiant (
			Heure hReference, 
			Declinaison DReference,
			Angle TauxVariation,
			Heure HMeusure) 
	{
		_logger.debug("Calcul declinaison par gradiant");

		Declinaison D = new Declinaison();
		double NbHeure = HMeusure.moins(hReference);
		double declinaison1AsDouble = DReference.getLatitude() * ((DReference.getSens() == SensLatitude.Nord) ? (+1.0) : (-1.0)); 
		double newDeclinaison = declinaison1AsDouble + TauxVariation.multiply(NbHeure);
		D.setLatitude(Math.abs(newDeclinaison));
		D.setSens((newDeclinaison < 0.0) ? SensLatitude.inverse(DReference.getSens()) : DReference.getSens());
		_logger.debug("H  {}", HMeusure);
		_logger.debug("H1 {}", hReference);
		_logger.debug("Declinaison D1 {}", DReference);
		_logger.debug("tauxDeVariation  {} / jour", TauxVariation);
		_logger.debug("NbHeure: {}", NbHeure);
		_logger.debug("Declinaison D {} == {}", D, newDeclinaison);
		return D;
	}
	
	public Declinaison DeclinaisonSoleilParInterval (
		Heure hDebutInterval, Heure hFinInterval, 
		Declinaison dDebutInterval, Declinaison dFinInterval,
		Heure HMeusure) throws NavAstroException 
	{
		_logger.debug("Calcul declinaison par interval");
		Declinaison D = new Declinaison();

		if ((HMeusure.apres (hFinInterval)) || HMeusure.avant(hDebutInterval)){
			_logger.error("Heure de meusure ou interval incorrect: on devrai avoir Hd < H < Hf [et on a: {} < {} < {}]", hDebutInterval.toString(), HMeusure.toString(), hFinInterval.toString());
			throw new NavAstroException("Heure de meusure invalide");
		}
		double taux = HMeusure.moins(hDebutInterval) / hFinInterval.moins(hDebutInterval);


		double declinaison1AsDouble = dDebutInterval.getLatitude() * ((dDebutInterval.getSens() == SensLatitude.Nord) ? (+1.0) : (-1.0)); 
		double declinaison2AsDouble = dFinInterval.getLatitude() * ((dFinInterval.getSens() == SensLatitude.Nord) ? (+1.0) : (-1.0)); 

		double newDeclinaison = declinaison1AsDouble + taux * (declinaison2AsDouble - declinaison1AsDouble);
		D.setLatitude(Math.abs(newDeclinaison));
		D.setSens((newDeclinaison < 0.0) ? SensLatitude.Sud : SensLatitude.Nord);

		_logger.debug("H1 {}", hDebutInterval);
		_logger.debug("H2 {}", hFinInterval);
		_logger.debug("H  {}", HMeusure);
		_logger.debug("Taux: {}", taux);
		_logger.debug("Declinaison D1 {}", dDebutInterval);
		_logger.debug("Declinaison D2 {}", dFinInterval);
		_logger.debug("Declinaison D {} == {}", D, newDeclinaison);

		return D;
	}

}
