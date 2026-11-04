package sfa.nav.astro.calculs.correctionvisee.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.ConstantesCelestes;
import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;


public class CorrectionDeViseeLune extends CorrectionDeVisee {
	final Logger logger = LoggerFactory.getLogger(getClass()) ;
	
	public CorrectionDeViseeLune (ErreurSextan _err, eTypeVisee _visee) {
		super (_err, _visee);
	}

	@Override
	public double diametre_EnMinuteArc(NavDateHeure heureObservation, double HP) {
		double diametreLune = 0.0;
		int iBorneInf = 0;
		int iBorneSup = 0;
		final int iMax = ConstantesCelestes.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc.length;
		while ((iBorneInf < iMax) && (ConstantesCelestes.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[iBorneInf] < HP))
			iBorneInf++;

		// en dessous du tableau
		if (iBorneInf == 0) {
			iBorneInf = iBorneSup = 0;
		}
		else if (iBorneInf == iMax) {
			iBorneInf = iBorneSup = iMax - 1;
		}
		else {
			iBorneSup = iBorneInf;
			iBorneInf = iBorneInf - 1;
		}
			
		diametreLune = Interpolation(
				ConstantesCelestes.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[iBorneInf],
				ConstantesCelestes.Lune_Diametre_EnMinuteDeArc[iBorneInf],
				ConstantesCelestes.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[iBorneSup],
				ConstantesCelestes.Lune_Diametre_EnMinuteDeArc[iBorneSup],
				HP);

		logger.debug("diametre_EnMinuteArc {}", diametreLune);
		return diametreLune;
	}
}
