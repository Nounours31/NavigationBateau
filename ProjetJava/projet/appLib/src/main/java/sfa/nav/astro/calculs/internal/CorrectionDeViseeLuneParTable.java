package sfa.nav.astro.calculs.internal;

import sfa.nav.astro.calculs.CorrectionDeViseeLune;
import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.model.Angle;

public class CorrectionDeViseeLuneParTable extends CorrectionDeViseeLune{

	public CorrectionDeViseeLuneParTable (ErreurSextan _err) {
		super (_err);
	}
	
	
	public double correctionTotaleEnDegreLune (Angle hauteurApparente_Ha, double hauteurOeil, eTypeVisee type, double indiceRefraction_PI) {
		double correction = 0.0;
	
		double depressionApparenteHorizon = DIP_ParLaTable(hauteurOeil);
		correction += depressionApparenteHorizon;
		
		correctionLunaireDeViseeHandler parallaxe = correctionHoeilDipRefractionParallaxeDemiDiametre_EnMinuteArc(hauteurApparente_Ha, indiceRefraction_PI, type);
		correction += parallaxe.parallaxe;
		correction += parallaxe.visee;
		correction -= correctionEnMinuteArcPourLeSextan();
		
		// correction en degre
		correction = correction / 60.0;
		return correction;
	}


	public correctionLunaireDeViseeHandler correctionHoeilDipRefractionParallaxeDemiDiametre_EnMinuteArc(Angle hauteurApparente_Ha, double indiceRefraction_PI, eTypeVisee type) {
		correctionLunaireDeViseeHandler retour = new correctionLunaireDeViseeHandler();
		final int Ha = 0;
		int i;
		int jRefractionInf;
		int jRefractionSup;
		final int nbHauteurApparenteInTableauCorrection = CorrectionDeViseeTablesDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc.length;
		
		int iHauteurAppInf = 0, iHauteurAppSup = 0;
		i = 0;
		while ((i < nbHauteurApparenteInTableauCorrection) && (
				CorrectionDeViseeTablesDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[i][Ha] <= hauteurApparente_Ha.asDegre())) {
			i++;
		}
		if (i == 0) {
			iHauteurAppInf = iHauteurAppSup = 0;
		}
		else if (i == nbHauteurApparenteInTableauCorrection) {
			iHauteurAppInf = iHauteurAppSup = nbHauteurApparenteInTableauCorrection - 1;
		}
		else {
			iHauteurAppInf = i -1;
			iHauteurAppSup = i;
		}
			
		i = 0;
		while ((i < CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc.length) && 
				(CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[i] <= indiceRefraction_PI)) {
			i++;
		}
		if (i == 0) {
			jRefractionInf = jRefractionSup = 1;
		}
		else if (i == CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc.length) {
			jRefractionInf = jRefractionSup = CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc.length;
		}
		else {
			jRefractionInf = i;
			jRefractionSup = i+1;
		}

		double correctionPourHauteurApparenteInferieur =  Interpolation (
				CorrectionDeViseeTablesDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppSup][Ha],
				CorrectionDeViseeTablesDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppSup][jRefractionInf],
				CorrectionDeViseeTablesDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppInf][Ha],
				CorrectionDeViseeTablesDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppInf][jRefractionInf],
				hauteurApparente_Ha.asDegre());

		double correctionPourHauteurApparenteSuperieur = Interpolation (
				CorrectionDeViseeTablesDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppSup][Ha],
				CorrectionDeViseeTablesDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppSup][jRefractionSup],
				CorrectionDeViseeTablesDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppInf][Ha],
				CorrectionDeViseeTablesDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppInf][jRefractionSup],
				hauteurApparente_Ha.asDegre());

		double parallaxe = Interpolation (
				CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[jRefractionInf-1],
				correctionPourHauteurApparenteInferieur,
				CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[jRefractionSup-1],
				correctionPourHauteurApparenteSuperieur,
				indiceRefraction_PI);
		
		double correctionBordSup = 0.0;
		if (type == eTypeVisee.luneBordSup) {
			correctionBordSup = (-1.0) * Interpolation(
					CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[jRefractionSup -1],
					CorrectionDeViseeTablesDeNavigation.Lune_Diametre_EnMinuteDeArc[jRefractionSup-1],
					CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[jRefractionInf -1],
					CorrectionDeViseeTablesDeNavigation.Lune_Diametre_EnMinuteDeArc[jRefractionInf-1],
					indiceRefraction_PI);
		}

		retour.parallaxe = parallaxe;
		retour.visee = correctionBordSup;
		return retour;
	}
}
