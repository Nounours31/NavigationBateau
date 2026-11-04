package sfa.nav.astro.calculs.correctionvisee.internal;

import sfa.nav.astro.calculs.ConstantesCelestes;
import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.model.NavDateHeure;


public class CorrectionDeViseeSoleil extends CorrectionDeVisee {

	public CorrectionDeViseeSoleil (ErreurSextan _err, eTypeVisee _visee) {
		super (_err, _visee);
	}


	@Override
	public double diametre_EnMinuteArc(NavDateHeure heureObservation, double HP) {
		int iMois = heureObservation.getMois();
		
		double correction = ConstantesCelestes.Soleil_DeuxiemeCorrection_BordInferieur_parmoisEnMinuteArc[iMois];
		correction -= ConstantesCelestes.Soleil_TroisiemeCorrection_BordSuperieur_parmoisEnMinuteArc[iMois];

		return correction;
	}
	


}
