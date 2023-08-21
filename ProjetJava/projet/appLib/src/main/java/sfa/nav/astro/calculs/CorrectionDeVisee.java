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
	}

	private final eTypeCorrection type;
	private final ErreurSextan err;
	
	public CorrectionDeVisee (eTypeCorrection _type, ErreurSextan _err) {
		type = _type;
		err = _err;
	}
	
	public double correctionEnDegre (Angle hauteurInstrumentale_Hi, double hauteurOeilenMetre, eBordSoleil bord) throws NavException {
		return correctionEnDegreAbaque(hauteurInstrumentale_Hi, hauteurOeilenMetre, bord);
	}
	
	private double correctionEnDegreCalcul (double hauteurInstrumentale_Hi_enDegre, double hauteurOeil,  eBordSoleil bord) throws NavException {
		
		double hauteurObservee_Ho_enDegre = hauteurInstrumentale_Hi_enDegre + err.exentricite.asDegre() + err.collimacon.asDegre();
		
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
		return (-hauteurInstrumentale_Hi_enDegre + hauteurParallaxe_Hv_enDegre);
	}
	
	private double correctionEnDegreAbaque (Angle hauteurInstrumentale_Hi, double hauteurOeil, eBordSoleil bord) {
		boolean noInterval = false;
		double correction = 0.0;
		final int Hi = 0;
		final double[] _hauteurOeil = { 0, 	2, 	3, 	4, 	5};		
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
		
		int i = 0;
		int iLigne = 0, jColone = 0;
		

		i = 0;
		while ((i < _correctionEnMinuteDeArc.length) && (_correctionEnMinuteDeArc[i][Hi] < hauteurInstrumentale_Hi.asDegre())) {
			i++;
		}
		iLigne = i;
		if ((iLigne == 0) || (iLigne == _correctionEnMinuteDeArc.length)) {
			noInterval = true;
			iLigne = ((iLigne == 0) ? iLigne: iLigne-1);
		}
			
		i = 0;
		while ((i < _hauteurOeil.length) && (_hauteurOeil[i] < hauteurOeil)) {
			i++;
		}
		jColone = i;
		if ((jColone == 0) || (jColone == _hauteurOeil.length)) {
			jColone = ((jColone == 0) ? 1: jColone);
			noInterval = true;
		}
		
		if (noInterval)
			correction = _correctionEnMinuteDeArc[iLigne][jColone];
		else {
			double penteHauteurOeilInferieur = ((_correctionEnMinuteDeArc[iLigne][jColone -1] - _correctionEnMinuteDeArc[iLigne-1][jColone -1]) / (_correctionEnMinuteDeArc[iLigne][Hi] - _correctionEnMinuteDeArc[iLigne-1][Hi]));
			double correctionHauteurOeilInferieur =  penteHauteurOeilInferieur * hauteurInstrumentale_Hi.asDegre() + _correctionEnMinuteDeArc[iLigne-1][jColone -1]; 

			double penteHauteurOeilSuperieur = ((_correctionEnMinuteDeArc[iLigne][jColone] - _correctionEnMinuteDeArc[iLigne-1][jColone]) / (_correctionEnMinuteDeArc[iLigne][Hi] - _correctionEnMinuteDeArc[iLigne-1][Hi]));
			double correctionHauteurOeilSuperieur =  penteHauteurOeilSuperieur * hauteurInstrumentale_Hi.asDegre() + _correctionEnMinuteDeArc[iLigne-1][jColone]; 

			double pente = ((correctionHauteurOeilSuperieur - correctionHauteurOeilInferieur) / (_hauteurOeil[jColone] - _hauteurOeil[jColone-1]));
			correction =  pente * hauteurOeil + correctionHauteurOeilInferieur; 

		}
		
		
		if (bord == eBordSoleil.etoile)
			correction -= 16.0;
		
		if (bord == eBordSoleil.sup)
			correction -= 32.0;
		
		correction = correction / 60.0;
		return correction;
	}

}
