package sfa.nav.astro.calculs.correctionvisee.internal;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.model.NavDateHeure;


public class CorrectionDeViseePlanete extends CorrectionDeVisee {

	public CorrectionDeViseePlanete (ErreurSextan _err, eTypeVisee _visee) {
		super (_err, _visee);
	}

	@Override
	public double diametre_EnMinuteArc(NavDateHeure heureObservation, double HP) {
		double correction = 0.0;
		return correction;
	}
}
