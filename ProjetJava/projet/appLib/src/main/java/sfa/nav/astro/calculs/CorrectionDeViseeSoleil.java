package sfa.nav.astro.calculs;

import sfa.nav.astro.calculs.internal.CorrectionDeViseeSoleilParTable;
import sfa.nav.model.Angle;
import sfa.nav.model.NavDateHeure;

public class CorrectionDeViseeSoleil extends CorrectionDeVisee { 

	private final  boolean isParCalcul;

	
	public CorrectionDeViseeSoleil(ErreurSextan errsextan) {
		super (errsextan);
		isParCalcul = false;
	}


	public double correctionTotale_EnDegre (Angle hauteurInstrumentale_Hi, double hauteurOeil, NavDateHeure heureObservation, eTypeVisee type) {
		if (!isParCalcul)
			return new CorrectionDeViseeSoleilParTable(getErr()).correctionTotale_EnDegre(hauteurInstrumentale_Hi, hauteurOeil, heureObservation, type);
		return 0.0;
	}


	public double correctionSemiDiametre_EnMinuteArc(NavDateHeure heureObservation, eTypeVisee typeVisee) {
		if (!isParCalcul)
			return new CorrectionDeViseeSoleilParTable(getErr()).correctionSemiDiametre_EnMinuteArc(heureObservation, typeVisee);
		return 0.0;
	}



	public double correctionHoeilDipRefractionParallaxeDemiDiametre_EnMinuteArc(Angle hauteurInstrumentale_Hi, double hauteurOeil) {
		if (!isParCalcul)
			return new CorrectionDeViseeSoleilParTable(getErr()).correctionHoeilDipRefractionParallaxeDemiDiametre_EnMinuteArc(hauteurInstrumentale_Hi, hauteurOeil);
		return 0.0;
	}

}
