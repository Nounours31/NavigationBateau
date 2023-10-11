package sfa.nav.astro.calculs.correctionvisee.internal;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.model.NavDateHeure;


public abstract class CorrectionDeViseeSoleil extends CorrectionDeVisee {

	public CorrectionDeViseeSoleil (ErreurSextan _err, eTypeVisee _visee) {
		super (_err, _visee);
	}

	@Override
	public double correctionAjoutSemiDiametreAstre_EnMinuteArc(NavDateHeure heureObservation, double HP) {
		int iMois = heureObservation.getMois();
		
		double correction = CorrectionDeViseeTablesDeNavigation.Soleil_DeuxiemeCorrection_BordInferieur_parmoisEnMinuteArc[iMois];

		
		if ((visee == eTypeVisee.etoile) || (visee == eTypeVisee.planete) || (visee == eTypeVisee.mars) || (visee == eTypeVisee.venus))
			correction = 0.0;
		
		else
			correction += (diametre_EnMinuteArc(heureObservation, HP) / 2.0);

		return correction;
	}

	@Override
	public double diametre_EnMinuteArc(NavDateHeure heureObservation, double HP) {
		int iMois = heureObservation.getMois();
		
		double correction = CorrectionDeViseeTablesDeNavigation.Soleil_DeuxiemeCorrection_BordInferieur_parmoisEnMinuteArc[iMois];
		correction -= CorrectionDeViseeTablesDeNavigation.Soleil_TroisiemeCorrection_BordSuperieur_parmoisEnMinuteArc[iMois];

		return correction;
	}
	
	@Override
	public double corrrectionTypeVisee_EnMinuteArc (eTypeVisee t, NavDateHeure heureObservation, double HP) {
		double d = 0.0;
		if (t == eTypeVisee.soleilBordSup)
			d = 1.0 * diametre_EnMinuteArc(heureObservation, HP);
		
		logger.debug("corrrectionVisee_EnMinuteArc {}", d);
		return d;
	}

}
