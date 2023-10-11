package sfa.nav.astro.calculs.correctionvisee.internal;

import org.checkerframework.common.reflection.qual.GetClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;


public class CorrectionDeViseeSoleilParTable extends CorrectionDeViseeSoleil
{
	final static Logger logger = LoggerFactory.getLogger(CorrectionDeViseeSoleilParTable.class);
	
	public CorrectionDeViseeSoleilParTable (ErreurSextan _err, eTypeVisee _visee) {
		super (_err, _visee);
	}

	@Override
	public double correctionTotale_EnDegre(Angle hauteurInstrumentale_Hi, 
			double hauteurOeil,
			NavDateHeure heureObservation, 
			double indiceRefraction_PI,
			eTypeVisee t) {

		correctionDeViseeHandler correctionH = correctionHoeilDipRefractionParallaxeDemiDiametre_EnMinuteArc(hauteurInstrumentale_Hi, hauteurOeil, indiceRefraction_PI);
	
		double correction = correctionH.correctionRefractionParallaxeEventuellementSDetDIP;
		logger.debug("Corrrection correctionRefractionParallaxeEventuellementSDetDIP {}", correction);
		
		correction -= correctionSextan_EnMinuteArc();
		logger.debug("Corrrection avec sextan {}", correction);

		if ((t == eTypeVisee.etoile) || (t == eTypeVisee.mars) ||(t == eTypeVisee.planete) ||(t == eTypeVisee.venus))
			correction -= this.diametre_EnMinuteArc(heureObservation, indiceRefraction_PI) / 2.0;
		logger.debug("Corrrection avec etoile {}", correction);
		
		correction += this.corrrectionTypeVisee_EnMinuteArc(t, heureObservation, indiceRefraction_PI);
		logger.debug("Corrrection avec bordDSup {}", correction);

		// correction en degre
		correction = correction / 60.0;
		return correction;
	}



	public correctionDeViseeHandler correctionHoeilDipRefractionParallaxeDemiDiametre_EnMinuteArc(
			Angle hauteurInstrumentale_Hi,
			double hauteurOeil, 
			double indiceRefraction_PI) {
		
		correctionDeViseeHandler retour = new correctionDeViseeHandler();
		retour.correctionDIP = DIP_ParLaTable(hauteurOeil);
		
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
		
		retour.correctionRefractionParallaxeEventuellementSDetDIP = correction;
		retour.correctionSemiDiametre = 0.0;
		retour.correctionDIP = 0.0;
		
		return retour;
	}


}
