package sfa.nav.astro.calculs.correctionvisee.internal;

import sfa.nav.model.AngleFactory;

public class CorrectionDeViseeTablesDeNavigation {
	final public static double[] Soleil_hauteurOeilEnMetre = 			{ 	00.0, 	02.0, 	04.0, 	06.0, 	08.0, 	10.0, 	12.0};		
	final public static double[][] Soleil_PremiereCorrection_RefractionDepressionParallaxe_Demidiametre_EnMinuteDeArc = {
			// 												Hi, 	0m, 	2m, 	4m, 	6m, 	8m		10m		12m		
			{	AngleFactory.fromStringSafe("06°00").asDegre(),		07.5,	05.0,	04.0,	03.2,	02.5,	02.0, 	01.5},
			
			{	AngleFactory.fromStringSafe("07°00").asDegre(), 	08.7,	06.2,	05.1, 	04.3,	03.6,	03.0,	02.5},
			{	AngleFactory.fromStringSafe("07°20").asDegre(), 	09.0,	06.5,	05.4, 	04.6, 	04.0,	03.4,	02.8},
			{	AngleFactory.fromStringSafe("07°40").asDegre(), 	09.3,	06.8,	05.7,	04.9,	04.2,	03.6,	03.1},
			
			{	AngleFactory.fromStringSafe("08°00").asDegre(),		09.6,	07.1,	06.0,	05.2,	04.5,	03.9,	03.4},
			{	AngleFactory.fromStringSafe("08°20").asDegre(),		09.8,	07.3,	06.3,	05.4,	04.8,	04.2,	03.6},
			{	AngleFactory.fromStringSafe("08°40").asDegre(),		10.1,	07.5,	06.5,	05.7,	05.0,	04.4,	03.9},
			
			{	AngleFactory.fromStringSafe("09°00").asDegre(), 	10.3,	07.7,	06.7,	05.9,	05.2,	04.6,	04.1},
			{	AngleFactory.fromStringSafe("09°20").asDegre(), 	10.5,	07.9,	06.9,	06.1,	05.4,	04.8,	04.3},
			{	AngleFactory.fromStringSafe("09°40").asDegre(), 	10.7,	08.1,	07.1,	06.3,	05.6,	05.0,	04.5},
			
			{	AngleFactory.fromStringSafe("10°00").asDegre(), 	10.8,	08.3,	07.3,	06.5,	05.8,	05.2,	04.7},
			{	AngleFactory.fromStringSafe("10°20").asDegre(), 	11.0,	08.5,	07.4,	06.6,	05.9,	05.3,	04.8},
			{	AngleFactory.fromStringSafe("10°40").asDegre(), 	11.2,	08.6,	07.6,	06.8,	06.1,	05.5,	05.0},
			
			{	AngleFactory.fromStringSafe("11°00").asDegre(), 	11.3,	08.8,	07.7,	06.9,	06.3,	05.7,	05.1},
			{	AngleFactory.fromStringSafe("11°30").asDegre(), 	11.5,	09.0,	07.9,	07.1,	06.5,	05.9,	05.3},
			
			{	AngleFactory.fromStringSafe("12°00").asDegre(), 	11.7,	09.2,	08.1,	07.3,	06.7,	06.1,	05.5},
			{	AngleFactory.fromStringSafe("12°30").asDegre(), 	11.9,	09.4,	08.3,	07.5,	06.8,	06.2,	05.7},

			{	AngleFactory.fromStringSafe("13°00").asDegre(), 	12.0,	09.5,	08.5,	07.7,	07.0,	06.4,	05.9},
			{	AngleFactory.fromStringSafe("13°30").asDegre(), 	12.2,	09.7,	08.6,	07.8,	07.1,	06.5,	06.0},

			{	AngleFactory.fromStringSafe("14°00").asDegre(),		12.3,	09.8,	08.8,	08.0,	07.3,	06.7,	06.2},
			{	AngleFactory.fromStringSafe("15°00").asDegre(),		12.6,	10.1,	09.0,	08.2,	07.5,	06.9,	06.4},
			{	AngleFactory.fromStringSafe("16°00").asDegre(),		12.8,	10.3,	09.3,	08.5,	07.8,	07.2,	06.7},
			{	AngleFactory.fromStringSafe("17°00").asDegre(),		13.0,	10.5,	09.5,	08.7,	08.0,	07.4,	06.9},
			{	AngleFactory.fromStringSafe("18°00").asDegre(),		13.2,	10.7,	09.6,	08.8,	08.2,	07.6,	07.1},
			{	AngleFactory.fromStringSafe("19°00").asDegre(),		13.4,	10.8,	09.8,	09.0,	08.3,	07.7,	07.2},

			{	AngleFactory.fromStringSafe("20°00").asDegre(),		13.5,	11.0,	09.9,	09.1,	08.5,	07.9,	07.4},
			{	AngleFactory.fromStringSafe("22°00").asDegre(),		13.8,	11.3,	10.2,	09.4,	08.7,	08.1,	07.6},
			{	AngleFactory.fromStringSafe("24°00").asDegre(),		14.0,	11.5,	10.4,	09.6,	08.9,	08.3,	07.8},
			{	AngleFactory.fromStringSafe("26°00").asDegre(),		14.2,	11.7,	10.6,	09.8,	09.1,	08.5,	08.0},
			{	AngleFactory.fromStringSafe("28°00").asDegre(),		14.3,	11.8,	10.8,	10.0,	09.3,	08.7,	08.2},

			{	AngleFactory.fromStringSafe("30°00").asDegre(),		14.5,	12.0,	10.9,	10.1,	09.4,	08.8,	08.3},
			{	AngleFactory.fromStringSafe("32°00").asDegre(),		14.6,	12.1,	11.0,	10.2,	09.6,	09.0,	08.5},
			{	AngleFactory.fromStringSafe("34°00").asDegre(),		14.7,	12.2,	11.1,	10.3,	09.7,	09.1,	08.6},
			{	AngleFactory.fromStringSafe("36°00").asDegre(),		14.8,	12.3,	11.2,	10.4,	09.8,	09.2,	08.7},
			{	AngleFactory.fromStringSafe("38°00").asDegre(),		14.9,	12.4,	11.3,	10.5,	09.9,	09.3,	08.8},

			{	AngleFactory.fromStringSafe("40°00").asDegre(), 	15.0,	12.5,	11.4,	10.6,	10.0,	09.4,	08.8},
			{	AngleFactory.fromStringSafe("45°00").asDegre(), 	15.1,	12.6,	11.6,	10.8,	10.1,	09.5,	09.0},

			{	AngleFactory.fromStringSafe("50°00").asDegre(), 	15.3,	12.8,	11.7,	10.9,	10.3,	09.7,	09.2},
			{	AngleFactory.fromStringSafe("55°00").asDegre(), 	15.4,	12.9,	11.9,	11.1,	10.4,	09.8,	09.3},

			{	AngleFactory.fromStringSafe("60°00").asDegre(), 	15.5,	13.0,	12.0,	11.2,	10.5,	09.9,	09.4},
			{	AngleFactory.fromStringSafe("70°00").asDegre(), 	15.7,	13.2,	12.2,	11.4,	10.7,	10.1,	09.6},
			{	AngleFactory.fromStringSafe("80°00").asDegre(), 	15.9,	13.4,	12.3,	11.5,	10.9,	10.3,	09.7},
			{	AngleFactory.fromStringSafe("90°00").asDegre(),		16.0,	13.5,	12.5,	11.7,	11.0,	10.4,	09.9},
	};

	//		janvier, fevrier, ...., decembre
	final public static double[] Soleil_DeuxiemeCorrection_BordInferieur_parmoisEnMinuteArc = {
			+00.3, +00.2, +00.1, +00.0, -00.2, -00.2, -00.2, -00.2, -00.1, +00.1, +00.2, +00.3
	};

	//		janvier, fevrier, ...., decembre
	final public static double[] Soleil_TroisiemeCorrection_BordSuperieur_parmoisEnMinuteArc = {
			-32.3, -32.2, -32.1, -32.0, -31.8, -31.8, -31.8, -31.8, -31.9, -32.1, -32.2, -32.3
	};

	final public static double[] DIP_HauteurOeilEnMetre = 	{ 	00.0, 	02.0, 	04.0, 	06.0, 	08.0, 	10.0, 	12.0};			
	final public static double[] DIP_CorrectionDepression_EnMinuteDeArc = {
			//	 													0m, 	2m, 	4m, 	6m, 	8m		10m		12m		
																	00.0,	-2.5,	-3.5,	-4.3,	-5.0,	-5.6,	-6.1			
	};
	final public static double[] Lune_PI_HP_HorizontaleParallaxeEnMinuteARc = {			54.0,	55.0,	55.5,	56.0,	56.5, 	57.0,	57.5,	58.0,	58.5,	59.0,	59.5,	60.0,	61.0};
	final public static double[] Lune_Diametre_EnMinuteDeArc = {			 			29.4,	30.0,	30.3,	30.6,	30.8,	31.1,	31.4,	31.7,	32.0,	32.2,	32.5,	32.8,	33.3};
	final public static double[][] Lune_SecondeCorrection_RefractionParallaxe_Demidiametre_EnMinuteDeArc = {
				{	AngleFactory.fromStringSafe("05°00").asDegre(),		59.2,	59.9,	60.6,	61.2,	61.8,	62.4,	63.1,	63.7,	64.4,	65.0,	65.6,	66.2,	67.4},
				{	AngleFactory.fromStringSafe("05°30").asDegre(),		59.6,	60.9,	61.5,	62.1,	62.8,	63.4,	64.1,	64.7,	65.4,	66.0,	66.7,	67.3,	68.5},
	
				{	AngleFactory.fromStringSafe("06°00").asDegre(),		60.2,	61.4,	62.1,	62.7,	63.4,	64.0,	64.7,	65.3,	65.9,	66.5,	67.2,	67.8,	69.1},
				{	AngleFactory.fromStringSafe("06°30").asDegre(),		60.7,	61.9,	62.5,	63.2,	63.8,	64.5,	65.1,	65.8,	66.4,	67.0,	67.6,	68.3,	69.6},
	
				{	AngleFactory.fromStringSafe("07°00").asDegre(),		61.1,	62.4,	63.0,	63.6,	64.2,	64.9,	65.5,	66.2,	66.8,	67.4,	68.0,	68.7,	70.0},
				{	AngleFactory.fromStringSafe("07°30").asDegre(),		61.5,	62.8,	63.4,	64.0,	64.6,	65.3,	65.9,	66.5,	67.1,	67.8,	68.4,	69.1,	70.4},
	
				{	AngleFactory.fromStringSafe("08°00").asDegre(),		61.8,	63.1,	63.7,	64.3,	64.9,	65.6,	66.2,	66.8,	67.5,	68.1,	68.7,	69.4,	70.7},
				{	AngleFactory.fromStringSafe("08°30").asDegre(),		62.1,	63.3,	63.9,	64.6,	65.2,	65.9,	66.5,	67.1,	67.8,	68.4,	69.0,	69.7,	70.9},
				{	AngleFactory.fromStringSafe("09°00").asDegre(),		62.3,	63.6,	64.2,	64.8,	65.4,	66.1,	66.7,	67.4,	68.0,	68.6,	69.2,	69.9,	71.1},
				
				{	AngleFactory.fromStringSafe("10°00").asDegre(),		62.7,	64.0,	64.6,	65.2,	65.8,	66.5,	67.1,	67.7,	68.3,	69.0,	69.6,	70.3,	71.5},
				{	AngleFactory.fromStringSafe("11°00").asDegre(),		63.0,	64.2,	64.8,	65.5,	66.1,	66.7,	67.3,	68.0,	68.6,	69.3,	69.9,	70.5,	71.8},
				{	AngleFactory.fromStringSafe("12°00").asDegre(),		63.2,	64.4,	65.0,	65.7,	66.3,	66.9,	67.5,	68.2,	68.8,	69.5,	70.1,	70.7, 	72.0},
				{	AngleFactory.fromStringSafe("13°00").asDegre(),		63.3,	64.6,	65.2,	65.8,	66.4,	67.0,	67.6,	68.3,	68.9,	69.6,	70.2,	70.8,	72.1},
				{	AngleFactory.fromStringSafe("14°00").asDegre(),		63.4,	64.6,	65.2,	65.9,	66.5,	67.1,	67.7,	68.4,	69.0,	69.6,	70.2,	70.9,	72.1},
				{	AngleFactory.fromStringSafe("15°00").asDegre(),		63.4,	64.6,	65.2,	65.9,	66.5,	67.1,	67.7,	68.4,	69.0,	69.6,	70.2,	70.9,	72.1},
				{	AngleFactory.fromStringSafe("16°00").asDegre(),		63.4,	64.6,	65.2,	65.8,	66.5,	67.1,	67.7,	68.3,	68.9,	69.6,	70.2,	70.8,	72.0},
				{	AngleFactory.fromStringSafe("17°00").asDegre(),		63.3,	64.5,	65.2,	65.8,	66.4,	67.0,	67.6,	68.2,	68.8,	69.5,	70.1,	70.7,	71.9},
				{	AngleFactory.fromStringSafe("18°00").asDegre(),		63.2,	64.4,	65.0,	65.6,	66.2,	66.9,	67.5,	68.1,	68.7,	69.3,	69.9,	70.6,	71.8},
				{	AngleFactory.fromStringSafe("19°00").asDegre(),		63.1,	64.3,	64.9,	65.5,	66.1,	66.7,	67.3,	67.9,	68.5,	69.2,	69.8,	70.4,	71.6},
	
				{	AngleFactory.fromStringSafe("20°00").asDegre(),		62.9,	64.1,	64.7,	65.3,	65.9,	66.5,	67.1,	67.7,	68.3,	69.0,	69.6,	70.2,	71.4},
				{	AngleFactory.fromStringSafe("21°00").asDegre(),		62.7,	63.9,	64.5,	65.1,	65.7,	66.3,	66.9,	67.5,	68.1,	68.7,	69.3,	70.0,	71.2},
				{	AngleFactory.fromStringSafe("22°00").asDegre(),		62.5,	63.7,	64.3,	64.9,	65.5,	66.1,	66.7,	67.3,	67.9,	68.5,	69.1,	69.7,	70.9},
				{	AngleFactory.fromStringSafe("23°00").asDegre(),		62.2,	63.4,	64.0,	64.6,	65.2,	65.9,	66.5,	67.0,	67.6,	68.2,	68.8,	69.4,	70.6},
				{	AngleFactory.fromStringSafe("24°00").asDegre(),		62.0,	63.1,	63.7,	64.3,	64.9,	65.5,	66.1,	66.7,	67.3,	67.9,	68.5,	69.1,	70.3},
				{	AngleFactory.fromStringSafe("25°00").asDegre(),		61.7,	62.8,	63.4,	64.0,	64.6,	65.2,	65.8,	66.4,	67.0,	67.6,	68.2,	68.8,	69.9},
				{	AngleFactory.fromStringSafe("26°00").asDegre(),		61.3,	62.5,	63.1,	63.7,	64.3,	64.9,	65.5,	66.0,	66.6,	67.2,	67.8,	68.4,	69.6},
				{	AngleFactory.fromStringSafe("27°00").asDegre(),		61.0,	62.2,	62.8,	63.3,	63.9,	64.5,	65.1,	65.7,	66.3,	66.8,	67.4,	68.0,	69.2},
				{	AngleFactory.fromStringSafe("28°00").asDegre(),		60.7,	61.8,	62.4,	63.0,	63.6,	64.1,	64.7,	65.3,	65.9,	66.4,	67.0,	67.6,	68.8},
				{	AngleFactory.fromStringSafe("29°00").asDegre(),		60.3,	61.4,	62.0,	62.6,	63.2,	63.7,	64.3,	64.9,	65.5,	66.0,	66.6,	67.2,	68.4},
	
				{	AngleFactory.fromStringSafe("30°00").asDegre(),		59.9,	61.0,	61.6,	62.2,	62.8,	63.3,	63.9,	64.4,	65.0,	65.6,	66.2,	66.7,	67.9},
				{	AngleFactory.fromStringSafe("31°00").asDegre(),		59.5,	60.6,	61.2,	61.7,	62.3,	62.9,	63.5,	64.0,	64.6,	65.1,	65.7,	66.3,	67.4},
				{	AngleFactory.fromStringSafe("32°00").asDegre(),		59.0,	60.2,	60.8,	61.3,	61.9,	62.4,	63.0,	63.5,	64.1,	64.7,	65.3,	65.8,	66.9},
				{	AngleFactory.fromStringSafe("33°00").asDegre(),		58.6,	59.7,	60.3,	60.8,	61.4,	61.9,	62.5,	63.1,	63.6,	64.2,	64.8,	65.3,	66.4},
				{	AngleFactory.fromStringSafe("34°00").asDegre(),		58.1,	59.2,	59.8,	60.3,	60.9,	61.4,	62.0,	62.5,	63.1,	63.6,	64.2,	64.8,	65.9},
				{	AngleFactory.fromStringSafe("35°00").asDegre(),		57.7,	58.7,	59.3,	59.8,	60.4,	60.9,	61.5,	62.0,	62.6,	63.1,	63.7,	64.2,	65.3},
				{	AngleFactory.fromStringSafe("36°00").asDegre(),		57.2,	58.2,	58.8,	59.3,	59.9,	60.4,	61.0,	61.5,	62.1,	62.6,	63.2,	63.7,	64.7},
				{	AngleFactory.fromStringSafe("37°00").asDegre(),		56.7,	57.7,	58.3,	58.8,	59.4,	59.9,	60.4,	60.9,	61.5,	62.0,	62.6,	63.1,	64.2},
				{	AngleFactory.fromStringSafe("38°00").asDegre(),		56.1,	57.2,	57.8,	58.2,	58.8,	59.3,	59.9,	60.4,	61.0,	61.4,	62.0,	62.5,	63.6},
				{	AngleFactory.fromStringSafe("39°00").asDegre(),		55.6,	56.6,	57.2,	57.7,	58.3,	58.7,	59.3,	59.8,	60.4,	60.8,	61.4,	61.9,	62.9},
	
				{	AngleFactory.fromStringSafe("40°00").asDegre(),		55.0,	56.1,	56.7,	57.1,	57.7,	58.1,	58.7,	59.2,	59.8,	60.2,	60.8,	61.3,	62.3},
				{	AngleFactory.fromStringSafe("41°00").asDegre(),		54.4,	55.5,	56.1,	56.5,	57.1,	57.5,	58.1,	58.6,	59.2,	59.6,	60.1,	60.6,	61.6},
				{	AngleFactory.fromStringSafe("42°00").asDegre(),		53.9,	54.9,	55.5,	55.9,	56.5,	56.9,	57.5,	57.9,	58.5,	59.0,	59.5,	60.0,	61.0},
				{	AngleFactory.fromStringSafe("43°00").asDegre(),		53.3,	54.3,	54.9,	55.3,	55.9,	56.3,	56.8,	57.3,	57.8,	58.3,	58.8,	59.3,	60.3},
				{	AngleFactory.fromStringSafe("44°00").asDegre(),		52.7,	53.7,	54.3,	54.7,	55.2,	55.6,	56.1,	56.6,	57.1,	57.6,	58.1,	58.6,	59.6},
				{	AngleFactory.fromStringSafe("45°00").asDegre(),		52.0,	53.0,	53.5,	54.0,	54.5,	55.0,	55.5,	56.0,	56.4,	56.9,	57.4,	57.9,	58.9},
	};
}
