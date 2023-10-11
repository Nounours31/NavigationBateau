package sfa.nav.astro.calculs;

import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;

public interface ICorrectionDeVisee {

	double correctionTotale_EnDegre(
			Angle hauteurInstruentale_Hi, 
			double hauteurOeil, 
			NavDateHeure heureObservation,
			double indiceRefraction_PI,
			eTypeVisee t);


	// Ho : Hauteur observation
	// Hv : Hauteur vraie = Ho
	//		Hv = Ho
	//
	// Hi : Hauteur Instrumentale  --> lue sur le sextan
	// Hs : Hauteur sight
	// Ha : ?
	//		Hs = Ha = Hi + I - d                     avec I: collimaton // d = dip
	//
	//      Hv = Ho = (Hi + I - d)  - R - P +/- SD   avec R: Refraction // P : parallaxe // SD 1/2 diametre 
	//              =       Ha      - R - P +/- SD
	
	double correctionSextan_EnMinuteArc();
	double correctionHauteurOeilMetre_EnMinuteArc(double hauteurOeilEnMetre);

	double HaFromHi_EnDegre(double Hi, double HauteurOeilEnMetre);
	double correctionRefraction_EnMinuteArc(double Ha);
	double correctionRefraction_EnMinuteArc(double Ha, double pression_hectopascal, double temperatureCelcius);
	double correctionParallaxe_EnMinuteArc(double Ha, double HP_HorizontalParallaxe);

	double corrrectionTypeVisee_EnMinuteArc (eTypeVisee t, NavDateHeure heureObservation, double HP_HorizontalParallaxe);
	double correctionAjoutSemiDiametreAstre_EnMinuteArc(NavDateHeure heureVisee, double HP_HorizontalParallaxe);
	double diametre_EnMinuteArc(NavDateHeure heureVisee, double HP_HorizontalParallaxe);
}
