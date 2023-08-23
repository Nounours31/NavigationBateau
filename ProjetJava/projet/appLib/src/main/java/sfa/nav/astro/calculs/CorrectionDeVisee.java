package sfa.nav.astro.calculs;

import sfa.nav.astro.calculs.DroiteDeHauteur.eBordSoleil;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.AngleOriente;
import sfa.nav.model.tools.Constantes;

public class CorrectionDeVisee {
	public enum eTypeCorrection {
		soleil, lune, venus, mars, etoile
	}

	public static class ErreurSextan {
		public ErreurSextan(AngleOriente sextan_collimasson, AngleOriente sextan_exentricite) {
			collimacon = sextan_collimasson;
			exentricite = sextan_exentricite;
		}
		final AngleOriente collimacon;
		final AngleOriente exentricite;
		
		@Override
		public String toString() {
			return "ErreurSextan [collimacon=" + collimacon + ", exentricite=" + exentricite + "]";
		}
		
	}

	private final eTypeCorrection type;
	private final ErreurSextan err;
	
	public CorrectionDeVisee (eTypeCorrection _type, ErreurSextan _err) {
		type = _type;
		err = _err;
	}
	
	public double correctionEnDegre (Angle hauteurInstrumentale_Hi, double hauteurOeilenMetre, eBordSoleil bord) throws NavException {
		return correctionEnDegre ( hauteurInstrumentale_Hi,  hauteurOeilenMetre, bord, false);
	}

	public double correctionEnDegre (Angle hauteurInstrumentale_Hi, double hauteurOeilenMetre, eBordSoleil bord, boolean byCalcul) throws NavException {
		if (byCalcul)
			return correctionEnDegreCalcul(hauteurInstrumentale_Hi, hauteurOeilenMetre, bord);
		return correctionEnDegreAbaque(hauteurInstrumentale_Hi, hauteurOeilenMetre, bord);
	}
	
	
	private double correctionEnDegreAbaque (Angle hauteurInstrumentale_Hi, double hauteurOeil, eBordSoleil bord) {
		double correction = 0.0;
		final int Hi = 0;
		final double[] _hauteurOeil = { 0.0, 	2.0, 	3.0, 	4.0, 	5.0};		
		final double[][] _correctionEnMinuteDeArc = {
				// Hi, 	0m, 	2m, 	3m, 	4m, 	5m		
				{	6, 	7.5,	5,		4.5,	4,		3.5},
				{	7, 	8.7,	6,		5.5, 	5, 		4.5},
				{	8,	9.6,	7, 		6.5, 	6, 		5.5},
				{	9, 	10.3,	8,		7,		6.5, 	6},
				{	10, 10.8,	8.5, 	8,		7,		7},
				{	12, 11.7,	9,		8.5, 	8, 		7.5},
				{	15,	12.6, 	10,		9.5, 	9, 		8.5},
				{	20,	13.5,	11, 	10.5, 	10, 	9.5},
				{	30,	14.5,	12, 	11, 	11, 	10.5},
				{	50, 15.3, 	13, 	12, 	12, 	11},
				{	90,	16, 	13.5, 	13, 	12, 	12},
		};
		
		int iInf = 0, iSup = 0, jInfInCorrectionTable = 0, jSupInCorrectionTable = 0, jInfInOeilTable = 0, jSupInOeilTable = 0;;
		int i = 0;
		while ((i < _correctionEnMinuteDeArc.length) && (_correctionEnMinuteDeArc[i][Hi] <= hauteurInstrumentale_Hi.asDegre())) {
			i++;
		}
		if (i == 0) {
			iInf = iSup = 0;
		}
		else if (i == _correctionEnMinuteDeArc.length) {
			iInf = iSup = _correctionEnMinuteDeArc.length - 1;
		}
		else {
			iInf = i -1;
			iSup = i;
		}
			
		i = 0;
		while ((i < _hauteurOeil.length) && (_hauteurOeil[i] <= hauteurOeil)) {
			i++;
		}
		if (i == 0) {
			jInfInOeilTable = jSupInOeilTable = 0;
			jInfInCorrectionTable = jSupInCorrectionTable = jInfInOeilTable + 1;
		}
		else if (i == _hauteurOeil.length) {
			jInfInOeilTable = jSupInOeilTable =  _hauteurOeil.length - 1;
			jInfInCorrectionTable = jSupInCorrectionTable = jInfInOeilTable + 1;;
		}
		else {
			jInfInOeilTable = i - 1;
			jSupInOeilTable = i;
			jInfInCorrectionTable = jInfInOeilTable + 1;
			jSupInCorrectionTable = jSupInOeilTable + 1;
		}

		// pente: DELTA(Correction)/DELTA(Hauteur)
		double deltaCorrection =  (_correctionEnMinuteDeArc[iSup][jInfInCorrectionTable] - _correctionEnMinuteDeArc[iInf][jInfInCorrectionTable]);
		double deltaHauteur = (_correctionEnMinuteDeArc[iSup][Hi] - _correctionEnMinuteDeArc[iInf][Hi]);
		double pente = (deltaHauteur == 0.0 ? 0.0 : deltaCorrection / deltaHauteur);
		double correctionPourHauterOeilInferieur =  pente * (hauteurInstrumentale_Hi.asDegre() - _correctionEnMinuteDeArc[iInf][Hi])  + _correctionEnMinuteDeArc[iInf][jInfInCorrectionTable]; 

		deltaCorrection =  (_correctionEnMinuteDeArc[iSup][jSupInCorrectionTable] - _correctionEnMinuteDeArc[iInf][jSupInCorrectionTable]);
		deltaHauteur = (_correctionEnMinuteDeArc[iSup][Hi] - _correctionEnMinuteDeArc[iInf][Hi]);
		pente = (deltaHauteur == 0.0 ? 0.0 : deltaCorrection / deltaHauteur);
		double correctionPourHauterOeilSuperieur =  pente * (hauteurInstrumentale_Hi.asDegre() - _correctionEnMinuteDeArc[iInf][Hi])  + _correctionEnMinuteDeArc[iInf][jSupInCorrectionTable]; 

		deltaCorrection =  (correctionPourHauterOeilSuperieur - correctionPourHauterOeilInferieur);
		deltaHauteur = (_hauteurOeil[jSupInOeilTable] - _hauteurOeil[jInfInOeilTable]);
		pente = (deltaHauteur == 0.0 ? 0.0 : deltaCorrection / deltaHauteur);
		correction =  pente * (hauteurOeil - _hauteurOeil[jInfInOeilTable])  + correctionPourHauterOeilInferieur; 

		
		
		if (bord == eBordSoleil.etoile)
			correction -= 16.0;
		
		if (bord == eBordSoleil.sup)
			correction -= 32.0;
		
		correction = correction / 60.0;
		return correction;
	}


	
	private double correctionEnDegreCalcul (Angle hauteurInstrumentale_Hi, double hauteurOeil,  eBordSoleil bord) throws NavException {
		
		double hauteurObservee_Ho_enDegre = hauteurInstrumentale_Hi.asDegre() + err.exentricite.asDegre() + err.collimacon.asDegre();
		
		double refractionAtmospheriqueEnMinuteDeArc = (1.77 * Math.sqrt(hauteurOeil));
		double hauteurApparente_Har_enDegre = hauteurObservee_Ho_enDegre - refractionAtmospheriqueEnMinuteDeArc / 60.0;
		
		double hauteurRefractee_Ha_enDegre = hauteurApparente_Har_enDegre - 0.97 * (1.0 / Math.tan(hauteurApparente_Har_enDegre * Constantes.DEG2RAD));
		double hauteurParallaxe_Hv_enDegre = hauteurRefractee_Ha_enDegre;
		
		if (bord != eBordSoleil.etoile) {
			hauteurParallaxe_Hv_enDegre = hauteurRefractee_Ha_enDegre + 0.15 * Math.cos(hauteurRefractee_Ha_enDegre * Constantes.DEG2RAD);
		
			// demi dametre
			hauteurParallaxe_Hv_enDegre = hauteurParallaxe_Hv_enDegre + AngleFactory.fromString("0°16").asDegre();
		}
		if (bord == eBordSoleil.sup) {
			hauteurParallaxe_Hv_enDegre = hauteurParallaxe_Hv_enDegre - AngleFactory.fromString("0°32").asDegre();
		}		
		return (-hauteurInstrumentale_Hi.asDegre() + hauteurParallaxe_Hv_enDegre);
	}
	
	
	@Override
	public String toString() {
		return "CorrectionDeVisee [type=" + type + ", err=" + err + "]";
	}

}
