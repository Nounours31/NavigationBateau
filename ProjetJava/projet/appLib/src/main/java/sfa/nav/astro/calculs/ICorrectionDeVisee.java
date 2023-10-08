package sfa.nav.astro.calculs;

import sfa.nav.astro.calculs.internal.CorrectionDeVisee.correctionDeViseeHandler;
import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;

public interface ICorrectionDeVisee {

	double correctionTotale_EnDegre(
			Angle hauteurInstruentale_Hi, 
			double hauteurOeil, 
			NavDateHeure heureObservation,
			double indiceRefraction_PI);

	String forCanevas();

	double correctionEnMinuteArcPourLeSextan();

	double correctionSemiDiametre_EnMinuteArc(NavDateHeure heureVisee) throws NoSuchMethodException;

	correctionDeViseeHandler correctionHoeilDipRefractionParallaxeDemiDiametre_EnMinuteArc(
			Angle hi,
			double hauteurOeil,
			double indiceRefraction_PI);


}
