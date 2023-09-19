package sfa.nav.astro.calculs;

import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.NavDateHeure.NavMoisDeAnnee;

public class CorrectionDeViseeSoleil extends CorrectionDeVisee {

	public CorrectionDeViseeSoleil (ErreurSextan _err) {
		super (_err);
	}
	
	
	public double correctionEnDegre (Angle hauteurInstrumentale_Hi, double hauteurOeil, NavDateHeure heureObservation, eTypeVisee type) {
		double correction = correctionEnMinuteArcPourReflexionHauteurOeil(hauteurInstrumentale_Hi, hauteurOeil);
		correction += correctionEnMinuteArcTypeVisee(heureObservation, type);
		correction -= correctionEnMinuteArcPourLeSextan();

		// correction en degre
		correction = correction / 60.0;
		return correction;
	}


	public double correctionEnMinuteArcTypeVisee(NavDateHeure heureObservation, eTypeVisee typeVisee) {
		NavMoisDeAnnee mois = NavMoisDeAnnee.FromNavDateHeure(heureObservation);
		
		double correction = CorrectionDeViseeTablesDeNavigation.Soleil_DeuxiemeCorrection_BordInferieur_parmoisEnMinuteArc[mois.indice()];

		
		if (typeVisee == eTypeVisee.etoile)
			correction += (
					(CorrectionDeViseeTablesDeNavigation.Soleil_DeuxiemeCorrection_BordInferieur_parmoisEnMinuteArc[mois.indice()]
					+ CorrectionDeViseeTablesDeNavigation.Soleil_TroisiemeCorrection_BordSuperieur_parmoisEnMinuteArc[mois.indice()]) / 2.0);
		
		if (typeVisee == eTypeVisee.soleilBordSup)
			correction += CorrectionDeViseeTablesDeNavigation.Soleil_TroisiemeCorrection_BordSuperieur_parmoisEnMinuteArc[mois.indice()];

		return correction;
	}



	public double correctionEnMinuteArcPourReflexionHauteurOeil(Angle hauteurInstrumentale_Hi, double hauteurOeil) {
		double correction = 0.0;
		final int Hi = 0;
	
		final int nbHauteurinstrumentaleInTableauCorrection = CorrectionDeViseeTablesDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc.length;
		
		int iInf = 0, iSup = 0, jInfInCorrectionTable = 0, jSupInCorrectionTable = 0, jInfInOeilTable = 0, jSupInOeilTable = 0;;
		int i = 0;
		while ((i < nbHauteurinstrumentaleInTableauCorrection) && (
				CorrectionDeViseeTablesDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[i][Hi] <= hauteurInstrumentale_Hi.asDegre())) {
			i++;
		}
		if (i == 0) {
			iInf = iSup = 0;
		}
		else if (i == nbHauteurinstrumentaleInTableauCorrection) {
			iInf = iSup = nbHauteurinstrumentaleInTableauCorrection - 1;
		}
		else {
			iInf = i -1;
			iSup = i;
		}
			
		i = 0;
		while ((i < CorrectionDeViseeTablesDeNavigation.Soleil_hauteurOeilEnMetre.length) && (CorrectionDeViseeTablesDeNavigation.Soleil_hauteurOeilEnMetre[i] <= hauteurOeil)) {
			i++;
		}
		if (i == 0) {
			jInfInOeilTable = jSupInOeilTable = 0;
			jInfInCorrectionTable = jSupInCorrectionTable = jInfInOeilTable + 1;
		}
		else if (i == CorrectionDeViseeTablesDeNavigation.Soleil_hauteurOeilEnMetre.length) {
			jInfInOeilTable = jSupInOeilTable =  CorrectionDeViseeTablesDeNavigation.Soleil_hauteurOeilEnMetre.length - 1;
			jInfInCorrectionTable = jSupInCorrectionTable = jInfInOeilTable + 1;;
		}
		else {
			jInfInOeilTable = i - 1;
			jSupInOeilTable = i;
			jInfInCorrectionTable = jInfInOeilTable + 1;
			jSupInCorrectionTable = jSupInOeilTable + 1;
		}

		double correctionPourHauterOeilInferieur =  Interpolation (
				CorrectionDeViseeTablesDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iSup][Hi],
				CorrectionDeViseeTablesDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iSup][jInfInCorrectionTable],
				CorrectionDeViseeTablesDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iInf][Hi],
				CorrectionDeViseeTablesDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iInf][jInfInCorrectionTable],
				hauteurInstrumentale_Hi.asDegre());

		double correctionPourHauterOeilSuperieur = Interpolation (
				CorrectionDeViseeTablesDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iSup][Hi],
				CorrectionDeViseeTablesDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iSup][jSupInCorrectionTable],
				CorrectionDeViseeTablesDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iInf][Hi],
				CorrectionDeViseeTablesDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iInf][jSupInCorrectionTable],
				hauteurInstrumentale_Hi.asDegre());

		correction =  Interpolation (
				CorrectionDeViseeTablesDeNavigation.Soleil_hauteurOeilEnMetre[jInfInOeilTable],
				correctionPourHauterOeilInferieur,
				CorrectionDeViseeTablesDeNavigation.Soleil_hauteurOeilEnMetre[jSupInOeilTable],
				correctionPourHauterOeilSuperieur,
				hauteurOeil);
		return correction;
	}

}
