/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.nav.astro.calculs;

import java.io.NotActiveException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.Declinaison;
import sfa.nav.model.DeclinaisonFactory;
import sfa.nav.model.Latitude;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.tools.ePointsCardinaux;


/**
 *
 * @author pierr
 */
public class CalculsAstro {
	private Logger _logger = LoggerFactory.getLogger(CalculsAstro.class);

	public CalculsAstro() {
	}

	public Latitude LatitudeMeridienne(Angle Ho, Angle Ei, Angle Hc, Angle Hv, Declinaison D) throws NavException {
		_logger.debug("Arg droite hauteur");

		//  distanceZenitale
		double dz = 90.0 - Hv.asDegre();
		Latitude L = LatitudeFactory.fromAngle(D.plus(AngleFactory.fromDegre(dz)));
		
		System.out.println("Latitude: " + L.toString());
		return L;
	}


	public Declinaison DeclinaisonSoleilParGradiant (
			NavDateHeure hReference, 
			Declinaison DReference,
			Angle TauxVariation,
			NavDateHeure HMeusure) throws NavException 
	{
		_logger.debug("Calcul declinaison par gradiant");

		Declinaison D = new Declinaison();
		double NbHeure = 0;
		try {
			NbHeure = HMeusure.moins(hReference);
		} catch (NavException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double declinaison1AsDouble = DReference.asDegre() * ((DReference.getSens() == ePointsCardinaux.Nord) ? (+1.0) : (-1.0)); 
		double newDeclinaison = declinaison1AsDouble + TauxVariation.multiplyByDouble(NbHeure).asDegre();
		D = (Declinaison) DeclinaisonFactory.fromDegre(Math.abs(newDeclinaison));
		if (newDeclinaison < 0.0) {
			//D.setSens((newDeclinaison < 0.0) ? eSensByPointsCardinaux.inverse(DReference.getSens()) : DReference.getSens());
			D.inverseSens();
		}
		_logger.debug("H  {}", HMeusure);
		_logger.debug("H1 {}", hReference);
		_logger.debug("Declinaison D1 {}", DReference);
		_logger.debug("tauxDeVariation  {} / jour", TauxVariation);
		_logger.debug("NbHeure: {}", NbHeure);
		_logger.debug("Declinaison D {} == {}", D, newDeclinaison);
		return D;
	}
	
	public Declinaison DeclinaisonSoleilParInterval (
		NavDateHeure hDebutInterval, NavDateHeure hFinInterval, 
		Declinaison dDebutInterval, Declinaison dFinInterval,
		NavDateHeure HMeusure) throws NavException, NotActiveException 
	{
		_logger.debug("Calcul declinaison par interval");
		Declinaison D = new Declinaison();

		if ((HMeusure.apres (hFinInterval)) || HMeusure.avant(hDebutInterval)){
			_logger.error("Heure de meusure ou interval incorrect: on devrai avoir Hd < H < Hf [et on a: {} < {} < {}]", hDebutInterval.toString(), HMeusure.toString(), hFinInterval.toString());
			throw new NavException("Heure de meusure invalide");
		}
		double taux = HMeusure.moins(hDebutInterval) / hFinInterval.moins(hDebutInterval);


		double declinaison1AsDouble = dDebutInterval.asDegre() * ((dDebutInterval.getSens() == ePointsCardinaux.Nord) ? (+1.0) : (-1.0)); 
		double declinaison2AsDouble = dFinInterval.asDegre() * ((dFinInterval.getSens() == ePointsCardinaux.Nord) ? (+1.0) : (-1.0)); 

		double newDeclinaison = declinaison1AsDouble + taux * (declinaison2AsDouble - declinaison1AsDouble);
		D = (Declinaison) LatitudeFactory.fromDegre(Math.abs(newDeclinaison));
		if (newDeclinaison < 0.0) {
			//D.setSens((newDeclinaison < 0.0) ? eSensByPointsCardinaux.Sud : eSensByPointsCardinaux.Nord);
			D.inverseSens();
		}
		
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
