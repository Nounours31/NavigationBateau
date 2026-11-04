package sfa.nav.astro.calculs;

public enum eCalculAstroConstantes {
    HeureMeusure("Heure (UT) ou la mesure est faite", eAstroContanteType.heure),
    AHvo("Angle Horaire du soleil a Greenwich (AHvo ou GHA)", eAstroContanteType.angle),
    
    HeureUT1("Heure (UT) de debut de l'interval", eAstroContanteType.heure),
    HeureUT2("Heure (UT) de fin d'interval", eAstroContanteType.heure),
    Declinaison1("Declinaison soleil(D) a l'heure du debut de l'interval", eAstroContanteType.declinaison),
    Declinaison2("Declinaison soleil(D) a l'heure de fin de l'interval", eAstroContanteType.declinaison),
    
    HeureUTRef("Heure (UT) de la declinaison de reference", eAstroContanteType.heure),
    DeclinaisonRef("Declinaison soleil(D) a l'heure de reference", eAstroContanteType.declinaison),
    PasDeDeclinaison("Vitesse de declinaison soleil horaire (en degre par heure)", eAstroContanteType.declinaison),

	
	Ho("Hauteur observee (Ho) - Angle", eAstroContanteType.angle),
	Ei("Erreur instrumentale (Ei) - Angle", eAstroContanteType.angle),
	CorrectionSolaire("Correction soleil(cor)", eAstroContanteType.angle),
	DeclinaisonSolaire("Declinaison soleil(D)", eAstroContanteType.angle), 
	Hv("Merdienne hv", eAstroContanteType.angle), 
	Hc ("Meridienne Hc", eAstroContanteType.angle)
	;

	public enum eAstroContanteType {
		angle, heure, declinaison, latitude, longitude;
	}
		
	public String getDisplay() {
		return _display + "["+_typeValeur.name()+"]";
	}

	final private String _display;
	final private eAstroContanteType _typeValeur;

	private eCalculAstroConstantes(String s, eAstroContanteType type) {
		this._display = s;
		this._typeValeur = type;
	}


}
