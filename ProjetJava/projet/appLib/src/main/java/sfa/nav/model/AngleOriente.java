package sfa.nav.model;

import sfa.nav.model.tools.ToStringOptions;
import sfa.nav.model.tools.ToStringOptions.eToStringMode;

public class AngleOriente extends Angle {

	protected AngleOriente() {
		_internalSetInDegre(0);
		super.setOriente();
	}
	
	@Override
	public String toString() {
		ToStringOptions opts = new ToStringOptions(
				ToStringOptions.or(eToStringMode.full, eToStringMode.deg, eToStringMode.rad, eToStringMode.Negatif));

		return this.myToString(opts);
	}

}
