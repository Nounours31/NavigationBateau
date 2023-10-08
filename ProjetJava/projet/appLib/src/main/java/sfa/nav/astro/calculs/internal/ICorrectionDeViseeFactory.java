package sfa.nav.astro.calculs.internal;

import sfa.nav.astro.calculs.ErreurSextan;
import sfa.nav.astro.calculs.ICorrectionDeVisee;
import sfa.nav.astro.calculs.internal.CorrectionDeVisee.eTypeVisee;

public class ICorrectionDeViseeFactory {

	public static ICorrectionDeVisee getCorrectionVisse (eTypeVisee visee, boolean byTable, ErreurSextan err) {
		switch (visee) {
			case etoile:
			case mars:
			case planete:
			case soleilBordInf:
			case soleilBordSup:
			case venus: return new CorrectionDeViseeSoleilParTable (err, visee);
			case luneBordInf:
			case luneBordSup: return new CorrectionDeViseeLuneParTable (err, visee);
			default: return null;
		}
	}
}
