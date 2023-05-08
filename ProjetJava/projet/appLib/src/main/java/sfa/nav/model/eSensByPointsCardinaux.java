package sfa.nav.model;

public enum eSensByPointsCardinaux {
	Nord("N"), Sud("S"), Est("E"), Ouest ("W"), Error("xx");
	final private String _Tag;
	
	private eSensByPointsCardinaux (String s) {_Tag = s;}
	
	static eSensByPointsCardinaux inverse(eSensByPointsCardinaux sens) {
		if (sens == Nord) return Sud;
		if (sens == Sud) return Nord;
		if (sens == Est) return Ouest;
		if (sens == Ouest) return Est;
		return Error;
	}

	public String getTag() { return _Tag;}
}
