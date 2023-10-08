package sfa.nav.astro.calculs.internal;

import sfa.nav.astro.calculs.ErreurSextan;

public abstract class CorrectionDeViseeSoleil extends CorrectionDeVisee { 

	protected CorrectionDeViseeSoleil (ErreurSextan _err, eTypeVisee _visee) {
		super (_err, _visee);
	}

}
