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
			case soleilBordInf:
			case soleilBordSup:
			case venus: 
				if (!byTable)
					return new CorrectionDeViseeSoleilParCalcul (err, visee);
				return new CorrectionDeViseeSoleilParTable (err, visee);
			case luneBordInf:
			case luneBordSup: 
				if (!byTable)
					return new CorrectionDeViseeLuneParCalcul (err, visee);
				return new CorrectionDeViseeLuneParTable (err, visee);
			default: return null;
		}
	}
}
