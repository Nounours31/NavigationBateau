package sfa.nav.astro.calculs.correctionvisee.internal;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.ICorrectionDeVisee;
import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;

public class ICorrectionDeViseeFactory {

	public static ICorrectionDeVisee getCorrectionVisse (eTypeVisee visee, boolean byTable, ErreurSextan err) {
		byTable = false;
		switch (visee) {
			case etoile:
			case mars:
			case planete:
			case venus: 
				return new CorrectionDeViseePlanete (err, visee);
			case soleilBordInf:
			case soleilBordSup:
				return new CorrectionDeViseeSoleil (err, visee);
			case luneBordInf:
			case luneBordSup: 
				return new CorrectionDeViseeLune (err, visee);
			default: return null;
		}
	}
}
