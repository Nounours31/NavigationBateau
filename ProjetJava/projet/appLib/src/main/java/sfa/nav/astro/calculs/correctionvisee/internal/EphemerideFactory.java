package sfa.nav.astro.calculs.correctionvisee.internal;

import sfa.nav.astro.calculs.Ephemerides.EphemeridesType;

public class EphemerideFactory {

	public static Ephemeride getEphereride (EphemeridesType ephererideType) {
		switch (ephererideType) {
			case ParGradiant:
				return new EphemerideParGradiant();
			case parInterval: 
				return new EphemerideParInterval ();
			default: return null;
		}
	}
}
