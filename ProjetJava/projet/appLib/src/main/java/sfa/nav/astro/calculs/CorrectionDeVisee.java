package sfa.nav.astro.calculs;

import sfa.nav.astro.calculs.CorrectionDeVisee_TableDeNavigation.eTypeVisee;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleOriente;
import sfa.nav.model.NavDateHeure.NavMoisDeAnnee;

public class CorrectionDeVisee {
	public static class ErreurSextan {
		public ErreurSextan(AngleOriente sextan_collimasson, AngleOriente sextan_exentricite) {
			collimacon = sextan_collimasson;
			exentricite = sextan_exentricite;
		}
		final AngleOriente collimacon;
		final AngleOriente exentricite;
		
		@Override
		public String toString() {
			return "ErreurSextan [collimacon=" + collimacon 
					+ ", exentricite=" + exentricite + "]";
		}

		public String toCanevas() {
			return "Sextan [collimacon=[" + collimacon.toCanevas() + "], exentricite=[" + exentricite.toCanevas() + "]]";
		}
		
	}
	
	public enum eTypeCorrectonVisee {
		correctionSoleil, correctionLune
	}

	private final ErreurSextan err;
	private eTypeCorrectonVisee typeCorrection;
	
	public CorrectionDeVisee (ErreurSextan _err) {
		err = _err;
	}
	
	
	public double correctionEnDegreLune (Angle hauteurApparente_Ha, double hauteurOeil, eTypeVisee type, double indiceRefraction_PI) {
		typeCorrection = eTypeCorrectonVisee.correctionLune;
		double correction = 0.0;
		final int Ha = 0;
	
		int i = 0;
		int iInf = 0, iSup = 0, jRefractionInf = 0, jRefractionSup = 0;
		int borneSup = CorrectionDeVisee_TableDeNavigation._hauteurOeilEnMetre_Lune.length;
		while ( (i < borneSup) && (CorrectionDeVisee_TableDeNavigation._hauteurOeilEnMetre_Lune[i] < hauteurOeil)) {
			i++;
		}
		if (i == 0) {
			iInf = iSup = 0;
		}
		else if (i == borneSup) {
			iSup = borneSup -1;
			iInf = iSup;
		}
		else {
			iInf = i -1;
			iSup = i;
		}
		double depressionApparenteHorizon = Interpolation(
				CorrectionDeVisee_TableDeNavigation._hauteurOeilEnMetre_Lune[iInf],
				CorrectionDeVisee_TableDeNavigation.Lune_PremiereCorrection_Depression_EnMinuteDeArc[iInf], 
				CorrectionDeVisee_TableDeNavigation._hauteurOeilEnMetre_Lune[iSup],
				CorrectionDeVisee_TableDeNavigation.Lune_PremiereCorrection_Depression_EnMinuteDeArc[iSup], 
				hauteurOeil);
		
		correction += depressionApparenteHorizon;
		
		
		// -------------------------
		// Paralaxe
		final int nbHauteurApparenteInTableauCorrection = CorrectionDeVisee_TableDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc.length;
		
		int iHauteurAppInf = 0, iHauteurAppSup = 0;
		i = 0;
		while ((i < nbHauteurApparenteInTableauCorrection) && (
				CorrectionDeVisee_TableDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[i][Ha] <= hauteurApparente_Ha.asDegre())) {
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
		while ((i < CorrectionDeVisee_TableDeNavigation.Lune_PI_HP_HorizontaleParallaxe.length) && 
				(CorrectionDeVisee_TableDeNavigation.Lune_PI_HP_HorizontaleParallaxe[i] <= indiceRefraction_PI)) {
			i++;
		}
		if (i == 0) {
			jRefractionInf = jRefractionSup = 1;
		}
		else if (i == CorrectionDeVisee_TableDeNavigation.Lune_PI_HP_HorizontaleParallaxe.length) {
			jRefractionInf = jRefractionSup = CorrectionDeVisee_TableDeNavigation.Lune_PI_HP_HorizontaleParallaxe.length;
		}
		else {
			jRefractionInf = i;
			jRefractionSup = i+1;
		}

		double correctionPourHauteurApparenteInferieur =  Interpolation (
				CorrectionDeVisee_TableDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppSup][Ha],
				CorrectionDeVisee_TableDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppSup][jRefractionInf],
				CorrectionDeVisee_TableDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppInf][Ha],
				CorrectionDeVisee_TableDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppInf][jRefractionInf],
				hauteurApparente_Ha.asDegre());

		double correctionPourHauteurApparenteSuperieur = Interpolation (
				CorrectionDeVisee_TableDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppSup][Ha],
				CorrectionDeVisee_TableDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppSup][jRefractionSup],
				CorrectionDeVisee_TableDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppInf][Ha],
				CorrectionDeVisee_TableDeNavigation.Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc[iHauteurAppInf][jRefractionSup],
				hauteurApparente_Ha.asDegre());

		correction +=  Interpolation (
				CorrectionDeVisee_TableDeNavigation.Lune_PI_HP_HorizontaleParallaxe[jRefractionInf-1],
				correctionPourHauteurApparenteInferieur,
				CorrectionDeVisee_TableDeNavigation.Lune_PI_HP_HorizontaleParallaxe[jRefractionSup-1],
				correctionPourHauteurApparenteSuperieur,
				indiceRefraction_PI);

		correction -= (err.collimacon.asDegre() * 60.0);
		correction -= (err.exentricite.asDegre() * 60.0);
		
		if (type == eTypeVisee.luneBordSup) {
			double correctionBordSup = Interpolation(
					CorrectionDeVisee_TableDeNavigation.Lune_PI_HP_HorizontaleParallaxe[jRefractionSup -1],
					CorrectionDeVisee_TableDeNavigation.Lune_Diametre_EnMinuteDeArc[jRefractionSup-1],
					CorrectionDeVisee_TableDeNavigation.Lune_PI_HP_HorizontaleParallaxe[jRefractionInf -1],
					CorrectionDeVisee_TableDeNavigation.Lune_Diametre_EnMinuteDeArc[jRefractionInf-1],
					indiceRefraction_PI);
			correction -= correctionBordSup;
		}
		
		// correction en degre
		correction = correction / 60.0;
		return correction;
	}

	
	public double correctionEnDegre (Angle hauteurInstrumentale_Hi, double hauteurOeil, NavMoisDeAnnee mois, eTypeVisee type) {
		typeCorrection = eTypeCorrectonVisee.correctionSoleil;
		double correction = 0.0;
		final int Hi = 0;
	
		final int nbHauteurinstrumentaleInTableauCorrection = CorrectionDeVisee_TableDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc.length;
		
		int iInf = 0, iSup = 0, jInfInCorrectionTable = 0, jSupInCorrectionTable = 0, jInfInOeilTable = 0, jSupInOeilTable = 0;;
		int i = 0;
		while ((i < nbHauteurinstrumentaleInTableauCorrection) && (
				CorrectionDeVisee_TableDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[i][Hi] <= hauteurInstrumentale_Hi.asDegre())) {
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
		while ((i < CorrectionDeVisee_TableDeNavigation._hauteurOeilEnMetre.length) && (CorrectionDeVisee_TableDeNavigation._hauteurOeilEnMetre[i] <= hauteurOeil)) {
			i++;
		}
		if (i == 0) {
			jInfInOeilTable = jSupInOeilTable = 0;
			jInfInCorrectionTable = jSupInCorrectionTable = jInfInOeilTable + 1;
		}
		else if (i == CorrectionDeVisee_TableDeNavigation._hauteurOeilEnMetre.length) {
			jInfInOeilTable = jSupInOeilTable =  CorrectionDeVisee_TableDeNavigation._hauteurOeilEnMetre.length - 1;
			jInfInCorrectionTable = jSupInCorrectionTable = jInfInOeilTable + 1;;
		}
		else {
			jInfInOeilTable = i - 1;
			jSupInOeilTable = i;
			jInfInCorrectionTable = jInfInOeilTable + 1;
			jSupInCorrectionTable = jSupInOeilTable + 1;
		}

		double correctionPourHauterOeilInferieur =  Interpolation (
				CorrectionDeVisee_TableDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iSup][Hi],
				CorrectionDeVisee_TableDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iSup][jInfInCorrectionTable],
				CorrectionDeVisee_TableDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iInf][Hi],
				CorrectionDeVisee_TableDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iInf][jInfInCorrectionTable],
				hauteurInstrumentale_Hi.asDegre());

		double correctionPourHauterOeilSuperieur = Interpolation (
				CorrectionDeVisee_TableDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iSup][Hi],
				CorrectionDeVisee_TableDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iSup][jSupInCorrectionTable],
				CorrectionDeVisee_TableDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iInf][Hi],
				CorrectionDeVisee_TableDeNavigation.Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc[iInf][jSupInCorrectionTable],
				hauteurInstrumentale_Hi.asDegre());

		correction =  Interpolation (
				CorrectionDeVisee_TableDeNavigation._hauteurOeilEnMetre[jInfInOeilTable],
				correctionPourHauterOeilInferieur,
				CorrectionDeVisee_TableDeNavigation._hauteurOeilEnMetre[jSupInOeilTable],
				correctionPourHauterOeilSuperieur,
				hauteurOeil);

		correction += CorrectionDeVisee_TableDeNavigation.Soleil_DeuxiemeCorrection_BordInferieur_parmois[mois.indice()];

		correction -= (err.collimacon.asDegre() * 60.0);
		correction -= (err.exentricite.asDegre() * 60.0);
		
		if (type == eTypeVisee.etoile)
			correction += (
					(CorrectionDeVisee_TableDeNavigation.Soleil_DeuxiemeCorrection_BordInferieur_parmois[mois.indice()]
					+ CorrectionDeVisee_TableDeNavigation.Soleil_TroisiemeCorrection_BordSuperieur_parmois[mois.indice()]) / 2.0);
		
		if (type == eTypeVisee.soleilBordSup)
			correction += CorrectionDeVisee_TableDeNavigation.Soleil_TroisiemeCorrection_BordSuperieur_parmois[mois.indice()];
		
		// correction en degre
		correction = correction / 60.0;
		return correction;
	}


	private double Interpolation (double xDebut, double yDebut, double xFin, double yFin, double x) {
		if (Math.abs(xFin - xDebut) < 0.001) {
			return (yFin + yDebut) / 2.0;
		}

		double pente = (yFin - yDebut) / (xFin - xDebut);
		double interpolation = pente * (x - xDebut) + yDebut;
		return interpolation;
	}


	@Override
	public String toString() {
		return "CorrectionDeVisee [" + 
				"typeCorrection=" + typeCorrection + 
				", ErreurSextan = " + err +" ]";
	}


	public String forCanevas() {
		StringBuffer sb = new StringBuffer();
		sb.append("CorrectionDeVisee [" + "typeCorrection=" + typeCorrection +  ", ErreurSextan = " + err.toCanevas() + " ]");
		return sb.toString();
	}
	
	
	


}
