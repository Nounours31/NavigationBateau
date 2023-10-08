package sfa.nav.astro.calculs.internal;

import sfa.nav.astro.calculs.ErreurSextan;

public abstract class CorrectionDeViseeLune extends CorrectionDeVisee {


	protected CorrectionDeViseeLune (ErreurSextan _err, eTypeVisee _visee) {
		super (_err, _visee);
	}
	
}
