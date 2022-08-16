package sfa.voile.nav.astro.methodes;

public enum eCalculAstroConstantes {
	Methode_CalculDeclinaison_ParInterval(""),
	Methode_CalculDeclinaison_ParGradient(""),

    Type("Par interval ou par vitesse angulaire"),
    HeureMeusure("Heure de calcul de la Declinaiosn SOlaire (heure de la meusure)"),
    AHvo("Angle Horaire du soleil a Greenwich (AHvo ou GHA)"),
    HeureUT1("Heure UT debut interval Declinaison soleil D"),
    Declinaison1("Declinaison soleil(D) debut interval "),
    HeureUT2("Heure UT fin interval Declinaison soleil D"),
    Declinaison2("Declinaison soleil(D) fin interval"),
    HeureUTRef("Heure de reference calcul declinaison"),
    DeclinaisonRef("Declinaison soleil(D) a l'heure de reference"),
    PasDeDeclinaison("Declinaison soleil horaire (d en degre par heure)"),

	
	Ho("Hauteur observee (Ho)"),
	Ei("Erreur instrumentale (Ei)"),
	CorrectionSolaire("Correction soleil(cor)"),
	DeclinaisonSolaire("Declinaison soleil(D)"), 
	Hv("Merdienne hv");

	public String getDisplay() {
		return _display;
	}

	final private String _display;

	private eCalculAstroConstantes(String s) {
		this._display = s;
	}


}
