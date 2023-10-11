package sfa.nav.astro.calculs.correctionvisee.internal;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;

public class CorrectionDeViseeLuneParCalcul extends CorrectionDeViseeLune {

	public CorrectionDeViseeLuneParCalcul (ErreurSextan _err, eTypeVisee _visee) {
		super (_err, _visee);
	}


	@Override
	public double correctionTotale_EnDegre(Angle hauteurInstruentale_Hi, 
			double hauteurOeil,
			NavDateHeure heureObservation, 
			double indiceRefraction_PI,
			eTypeVisee t) {

		double correction = 0.0;
		double Ha = hauteurInstruentale_Hi.asDegre() + correctionSextan_EnMinuteArc() / 60.0 - correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil) / 60.0;
		
		correction = correctionSextan_EnMinuteArc() - correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil);
		correction -= correctionRefraction_EnMinuteArc(Ha);
		correction -= correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI);
		correction += correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI);
		if (t == eTypeVisee.luneBordSup)
			correction -= diametre_EnMinuteArc(heureObservation, indiceRefraction_PI);
		
		// correction en degre
		correction = correction / 60.0;
		return correction;
	}
}
