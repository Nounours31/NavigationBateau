package sfa.nav.astro.calculs.internal;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;

public class CorrectionDeViseeLuneParTable extends CorrectionDeViseeLune{

	public CorrectionDeViseeLuneParTable (ErreurSextan _err, eTypeVisee _visee) {
		super (_err, _visee);
	}


	@Override
	public double correctionTotale_EnDegre(Angle hauteurInstruentale_Hi, 
			double hauteurOeil,
			NavDateHeure heureObservation, 
			double indiceRefraction_PI) {

		double correction = 0.0;
	
		double depressionApparenteHorizon = DIP_ParLaTable(hauteurOeil);
		correction += depressionApparenteHorizon;
		
		correctionDeViseeHandler parallaxe = correctionHoeilDipRefractionParallaxeDemiDiametre_EnMinuteArc(hauteurInstruentale_Hi, hauteurOeil, indiceRefraction_PI);
		correction += parallaxe.correctionRefractionParallaxeEventuellementSDetDIP;
		correction += parallaxe.correctionSemiDiametre;
		correction -= correctionEnMinuteArcPourLeSextan();
		
		// correction en degre
		correction = correction / 60.0;
		return correction;
	}

	@Override
	public correctionDeViseeHandler correctionHoeilDipRefractionParallaxeDemiDiametre_EnMinuteArc(Angle hauteurApparente_Ha,
			double hauteurOeil, 
			double indiceRefraction_PI) {
		correctionDeViseeHandler retour = new correctionDeViseeHandler();
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
		if (visee == eTypeVisee.luneBordSup) {
			correctionBordSup = (-1.0) * Interpolation(
					CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[jRefractionSup -1],
					CorrectionDeViseeTablesDeNavigation.Lune_Diametre_EnMinuteDeArc[jRefractionSup-1],
					CorrectionDeViseeTablesDeNavigation.Lune_PI_HP_HorizontaleParallaxeEnMinuteARc[jRefractionInf -1],
					CorrectionDeViseeTablesDeNavigation.Lune_Diametre_EnMinuteDeArc[jRefractionInf-1],
					indiceRefraction_PI);
		}


		retour.correctionRefractionParallaxeEventuellementSDetDIP = parallaxe;
		retour.correctionSemiDiametre = correctionBordSup;
		retour.correctionDIP = DIP_ParLaTable(hauteurOeil);
		return retour;
	}


	@Override
	public double correctionSemiDiametre_EnMinuteArc(NavDateHeure heureVisee) throws NoSuchMethodException {
		throw new NoSuchMethodException();
	}
}
