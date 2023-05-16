package sfa.nav.model.tools;

public enum ePointsCardinaux {
	Nord("N"), Sud("S"), Est("E"), Ouest ("W"), Error("xx"),
	N("N"), S("S"), E("E"), W ("W");
	
	final private String _Tag;
	
	private ePointsCardinaux (String s) {_Tag = s;}
	
	static ePointsCardinaux inverse(ePointsCardinaux sens) {
		if (sens == Nord) return Sud;
		if (sens == Sud) return Nord;
		if (sens == Est) return Ouest;
		if (sens == Ouest) return Est;

		if (sens == N) return S;
		if (sens == S) return N;
		if (sens == E) return W;
		if (sens == W) return E;
		return Error;
	}
	
	public ePointsCardinaux sensInverse() {
		return ePointsCardinaux.inverse(this);
	}

	public String getTag() { return _Tag;}
}
