package sfa.nav.lib.tools;

public enum eToStringMode {
	deg  (0x01),
	rad  (0x02),
	light(0x10),
	full (0x20);

	final private int _val;
	
	eToStringMode(int val) {
		_val = val;
	}


	public static boolean isA(int y, eToStringMode x) {
		return ((x._val & y) != 0);
	}

	public static int or(eToStringMode ...x) {
		int retour = 0;
		for (eToStringMode i : x)
			retour |= i._val;
		return retour;
	}

	public static int and(eToStringMode ...x) {
		int retour = 0;
		for (eToStringMode i : x)
			retour &= i._val;
		return retour;
	}
}
