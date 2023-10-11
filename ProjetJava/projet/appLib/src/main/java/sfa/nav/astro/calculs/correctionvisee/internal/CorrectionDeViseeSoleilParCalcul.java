package sfa.nav.astro.calculs.correctionvisee.internal;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;


public class CorrectionDeViseeSoleilParCalcul extends CorrectionDeViseeSoleil {

	public CorrectionDeViseeSoleilParCalcul (ErreurSextan _err, eTypeVisee _visee) {
		super (_err, _visee);
	}

	@Override
	public double correctionTotale_EnDegre(Angle hauteurInstrumentale_Hi, 
			double hauteurOeil,
			NavDateHeure heureObservation, 
			double indiceRefraction_PI,
			eTypeVisee t) {
		
		double correction = 0.0;
		double Ha = HaFromHi_EnDegre(hauteurInstrumentale_Hi.asDegre(), hauteurOeil);
		
		// [I -d -R + P +SD - typeVisee]
		correction = 
			- correctionSextan_EnMinuteArc() 
			- correctionHauteurOeilMetre_EnMinuteArc(hauteurOeil)
			- correctionRefraction_EnMinuteArc(Ha)
			+ correctionParallaxe_EnMinuteArc(Ha, indiceRefraction_PI)
			+ correctionAjoutSemiDiametreAstre_EnMinuteArc(heureObservation, indiceRefraction_PI)
			- corrrectionTypeVisee_EnMinuteArc(t, heureObservation, Ha);

		correction = correction / 60.0;
		return correction;
	}
}
