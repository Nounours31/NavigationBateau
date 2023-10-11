package sfa.nav.astro.calculs.correctionvisee.internal;

public class CorrectionDeViseeTablesDeNavigation {

	//		janvier, fevrier, ...., decembre
	final public static double[] Soleil_DeuxiemeCorrection_BordInferieur_parmoisEnMinuteArc = {
			+00.3, +00.2, +00.1, +00.0, -00.2, -00.2, -00.2, -00.2, -00.1, +00.1, +00.2, +00.3
	};

	//		janvier, fevrier, ...., decembre
	final public static double[] Soleil_TroisiemeCorrection_BordSuperieur_parmoisEnMinuteArc = {
			-32.3, -32.2, -32.1, -32.0, -31.8, -31.8, -31.8, -31.8, -31.9, -32.1, -32.2, -32.3
	};

	final public static double[] Lune_PI_HP_HorizontaleParallaxeEnMinuteARc = {			
			54.0,	55.0,	55.5,	56.0,	56.5, 	57.0,	57.5,	58.0,	58.5,	59.0,	59.5,	60.0,	61.0};
	
	final public static double[] Lune_Diametre_EnMinuteDeArc = {			 			
			29.4,	30.0,	30.3,	30.6,	30.8,	31.1,	31.4,	31.7,	32.0,	32.2,	32.5,	32.8,	33.3};
}
