package sfa.nav.astro.calculs.correctionvisee.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;


public abstract class CorrectionDeViseeLune extends CorrectionDeVisee {
	final Logger logger = LoggerFactory.getLogger(getClass()) ;
	
	public CorrectionDeViseeLune (ErreurSextan _err, eTypeVisee _visee) {
		super (_err, _visee);
	}

	@Override
	public double diametre_EnMinuteArc(NavDateHeure heureObservation, double HP) {
		double diametreLune = 0.0;
		int i = 0;
		while (CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[i] < HP)
			i++;

		diametreLune = Interpolation(
				CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[i -1],
				CorrectionDeViseeTablesDeNavigation.Lune_Diametre_EnMinuteDeArc[i-1],
				CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[i],
				CorrectionDeViseeTablesDeNavigation.Lune_Diametre_EnMinuteDeArc[i],
				HP);

		logger.debug("diametre_EnMinuteArc {}", diametreLune);
		return diametreLune;
	}

	@Override
	public double correctionAjoutSemiDiametreAstre_EnMinuteArc (NavDateHeure heureObservation, double HP) {
		double d = diametre_EnMinuteArc(heureObservation, HP) / 2.0;
		logger.debug("corrrectionDiametre_EnMinuteArc {}", d);
		return d;
	}

	@Override
	public double corrrectionTypeVisee_EnMinuteArc (eTypeVisee t, NavDateHeure heureObservation, double HP) {
		double d = 0.0;
		if (t == eTypeVisee.luneBordSup)
			d = 1.0 * diametre_EnMinuteArc(heureObservation, HP);
		
		logger.debug("corrrectionVisee_EnMinuteArc {}", d);
		return d;
	}

}
