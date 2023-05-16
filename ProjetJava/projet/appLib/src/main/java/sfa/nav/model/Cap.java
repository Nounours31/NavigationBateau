package sfa.nav.model;

import sfa.nav.model.tools.ToStringOptions;

public class Cap extends Angle {
	protected Cap() {
		super ();
	}	
	
	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public String myToString(ToStringOptions opts) {
		return super.myToString(opts);
	}

	public boolean equalsInDegre(Cap capCas1, double precisionCap) {
		if (Math.abs(capCas1.asDegre() - this.asDegre()) < precisionCap)
			return true;
		return false;
	}
}
