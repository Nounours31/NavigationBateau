package sfa.voile.nav.astro.ui.dialogues;

import java.util.HashMap;

import sfa.voile.nav.astro.methodes.eCalculAstroConstantes;
import sfa.voile.nav.astro.ui.menus.eUINavAstroAllMenuItems;

public abstract class DialogueAdapteur {
	
	public abstract void demandeDesArguments(HashMap<eCalculAstroConstantes, Object> args, eUINavAstroAllMenuItems iCas);
}
