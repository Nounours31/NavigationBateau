package sfa.voile.nav.astro.ui.menus;

public enum eUINavAstroAllMenuItems {
	Unconnu("Mauvaix choix...."), 
	Sortir("Sortir du menu"), 
	LatitudeDeLaMeridienne("Calcul de la latitude de la meridienne"), 
	DeclinaisonSolaire("Calcul de la declinaison du soleil"), 
	DroiteHauteur("Calcul de la droite de hauteur"), 
	Meridienne("Calcul de la meridienne"), 
	DeclainaisonSolaireParInterval("Calcul de la declinaison solaire par interval"),
	DeclainaisonSolaireParGradiant("Calcul de la declinaison solaire par gradiant"),
	;
	
	private final String _desc;
	private eUINavAstroAllMenuItems(String d) {_desc = d;}
	
	String getDescriptif() {
		return _desc;
	}
}
