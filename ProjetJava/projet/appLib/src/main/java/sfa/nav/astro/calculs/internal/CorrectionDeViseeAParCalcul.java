package sfa.nav.astro.calculs.internal;

import sfa.nav.astro.calculs.CorrectionDeViseeLune;
import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;

public class CorrectionDeViseeAParCalcul extends CorrectionDeViseeLune{

	public CorrectionDeViseeAParCalcul (ErreurSextan _err) {
		super (_err);
	}
	
	
	private double correctionEnMinute_DIP_DeppressionOeil (double hauteurOeil) {
		return 0.0293 * Math.sqrt(hauteurOeil) * 60.0;
	}


	private double correctionEnMinute_Refraction (Angle hauteurApparente_Ha, double Pression_hPa, double Temperature_Celcius) {
		double R0 = 1 / Math.tan((Math.PI / 180.0) * 
				hauteurApparente_Ha.asDegre() + (7.31 / (hauteurApparente_Ha.asDegre() + 4.4)));
		double f = 0.28 * Pression_hPa / (273 + Temperature_Celcius);
		
		return f * R0;
	}

	private double correctionEnMinute_parallaxe (Angle hauteurApparente_Ha, double HP_minute) {
		return HP_minute * Math.cos (hauteurApparente_Ha.asRadian());
	}

	private int correctionEnMinute_DemiDiametre(NavDateHeure date, eTypeVisee visee, double HP) {
		int iMois = date.getMois();
		
		// lune
		double diametreLune = 0.0;
		if ((visee == eTypeVisee.luneBordInf) || (visee == eTypeVisee.luneBordSup)) {
			for (int i = 0; i < Lune_HP_EnMinuteARc.length; i++) {
				if (Lune_HP_EnMinuteARc[i] >= HP) {
					diametreLune = this.Interpolation(Lune_HP_EnMinuteARc[i-1], Lune_Diametre_SelonHP_EnMinuteDeArc[i -1], Lune_HP_EnMinuteARc[i], Lune_Diametre_SelonHP_EnMinuteDeArc[i], HP); 
				}
			}
		}
		
		double venus = 0.0;
		if (visee == eTypeVisee.venus) {
			venus = 0.1;
		}

		double mars = 0.0;
		if (visee == eTypeVisee.mars) {
			mars = 0.1;
		}

		double retour = 0.0;
		switch (visee) {
			case etoile: retour = 0.0; break;
			case venus: retour = venus; break;
			case mars: retour = mars; break;
			case soleilBordInf: retour = (Soleil_BordSuperieur_parmoisEnMinuteArc[iMois] - Soleil_BordSuperieur_parmoisEnMinuteArc[iMois]) / 2.0; break;
			case soleilBordSup: retour = (-1)*(Soleil_BordSuperieur_parmoisEnMinuteArc[iMois] - Soleil_BordSuperieur_parmoisEnMinuteArc[iMois]) / 2.0; break;
			case luneBordInf: retour = diametreLune / 2.0; break;
			case luneBordSup: retour = (-1) * diametreLune / 2.0; break;
			default: retour = 0.0;
		}
		return 0;
	}

	public double correctionEnMinute_altitude (Angle hauteurApparente_Ha, double HP, NavDateHeure date, eTypeVisee visee) {
		return correctionEnMinute_altitude(hauteurApparente_Ha, 1010, 10, HP, date, visee);
	}

	
	public double correctionEnMinute_altitude (Angle hauteurApparente_Ha, double Pression_hPa, double Temperature_Celcius, double HP, NavDateHeure date, eTypeVisee visee) {
		return (-1) * correctionEnMinute_Refraction(hauteurApparente_Ha, Pression_hPa, Temperature_Celcius) + 
				correctionEnMinute_parallaxe(hauteurApparente_Ha, HP) + 
				correctionEnMinute_DemiDiametre(date, visee, HP);
	}




	public double Ho_AvecParallaxeHorizontale (Angle hauteurApparente_Ha, double Pression_hPa, double Temperature_Celcius, double HP_minute, NavDateHeure date, eTypeVisee visee) {
		return hauteurApparente_Ha.asDegre() + correctionEnMinute_altitude(hauteurApparente_Ha, Pression_hPa, Temperature_Celcius, HP_minute, date, visee) / 60.0;
	}

	public double Ho_SansParallaxeHorizontale (Angle hauteurApparente_Ha, double Pression_hPa, double Temperature_Celcius, NavDateHeure date, eTypeVisee visee) {
		return hauteurApparente_Ha.asDegre() + correctionEnMinute_altitude(hauteurApparente_Ha, Pression_hPa, Temperature_Celcius, 0.0, date, visee) / 60.0;
	}

	public double Ha (Angle hauteurVisee_Hi, double h_enMetre) {
		return hauteurVisee_Hi.asDegre() + correctionEnMinuteArcPourLeSextan()/60.0 - correctionEnMinute_DIP_DeppressionOeil(h_enMetre) / 60.0;
	}
	
	
	//		janvier, fevrier, ...., decembre
	final private static double[] Soleil_BordInferieur_parmoisEnMinuteArc = {
			+00.3, +00.2, +00.1, +00.0, -00.2, -00.2, -00.2, -00.2, -00.1, +00.1, +00.2, +00.3
	};

	//		janvier, fevrier, ...., decembre
	final private static double[] Soleil_BordSuperieur_parmoisEnMinuteArc = {
			-32.3, -32.2, -32.1, -32.0, -31.8, -31.8, -31.8, -31.8, -31.9, -32.1, -32.2, -32.3
	};

	final public static double[] Lune_HP_EnMinuteARc = {	/* min */	00.0, 54.0,	55.0,	55.5,	56.0,	56.5, 	57.0,	57.5,	58.0,	58.5,	59.0,	59.5,	60.0,	61.0, /* max */90.0};
	final public static double[] Lune_Diametre_SelonHP_EnMinuteDeArc = {29.4, 29.4,	30.0,	30.3,	30.6,	30.8,	31.1,	31.4,	31.7,	32.0,	32.2,	32.5,	32.8,	33.3,          33.3};

}
