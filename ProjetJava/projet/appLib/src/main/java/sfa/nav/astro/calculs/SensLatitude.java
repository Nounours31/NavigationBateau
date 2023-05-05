package sfa.nav.astro.calculs;

public enum SensLatitude {
	Nord, Sud, Error;

	static SensLatitude inverse(SensLatitude sens) {
		if (sens == Nord) return Sud;
		if (sens == Sud) return Nord;
		return Error;
	}
}
