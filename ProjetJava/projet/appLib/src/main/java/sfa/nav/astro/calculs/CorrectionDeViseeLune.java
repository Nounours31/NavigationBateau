package sfa.nav.astro.calculs;

import sfa.nav.astro.calculs.internal.CorrectionDeViseeLuneParTable;
import sfa.nav.model.Angle;

public class CorrectionDeViseeLune extends CorrectionDeVisee {
	public class correctionLunaireDeViseeHandler {
		public double parallaxe;
		public double visee;
	}

	private final  boolean isParCalcul;
	public CorrectionDeViseeLune(ErreurSextan sextanErr) {
		super (sextanErr);
		isParCalcul = false;
	}


	public double correctionTotaleEnDegreLune (Angle hauteurApparente_Ha, double hauteurOeil, eTypeVisee type, double indiceRefraction_PI) {
		if (!isParCalcul)
			return new CorrectionDeViseeLuneParTable(getErr()).correctionTotaleEnDegreLune(hauteurApparente_Ha, hauteurOeil, type, indiceRefraction_PI);
		return 0.0;
	}


	public correctionLunaireDeViseeHandler correctionHoeilDipRefractionParallaxeDemiDiametre_EnMinuteArc(Angle hauteurApparente_Ha, double indiceRefraction_PI, eTypeVisee type) {
		if (!isParCalcul)
			return new CorrectionDeViseeLuneParTable(getErr()).correctionHoeilDipRefractionParallaxeDemiDiametre_EnMinuteArc(hauteurApparente_Ha, indiceRefraction_PI, type);					
		return null;
	}
	
}
