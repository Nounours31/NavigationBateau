package sfa.nav.model.tools;

public class ToStringOptions {
	public enum eToStringMode {
		empty(0),
		deg  (1),
		rad  (1 << 1),
		light(1 << 2),
		full (1 << 3), 
		MilleNautique(1 << 4),
		KM(1 << 5);

		final private int _val;
		private eToStringMode(int val) {
			_val = val;
		}
		
		public int getVal() { return _val; }
	}
	
	
	private int _mode = eToStringMode.empty.getVal();
	
	public ToStringOptions () {
		_mode = eToStringMode.empty._val;
	}

	public ToStringOptions (int i) {
		_mode = i;
	}

	public ToStringOptions (eToStringMode i) {
		_mode = i.getVal();
	}

	public boolean isA(eToStringMode x) {
		return ((_mode & x.getVal()) != 0);
	}

	public static int or(eToStringMode ...x) {
		int retour = 0;
		for (eToStringMode i : x)
			retour |= i.getVal();
		return retour;
	}

	public static int and(eToStringMode ...x) {
		int retour = 0;
		for (eToStringMode i : x)
			retour &= i.getVal();
		return retour;
	}
}
