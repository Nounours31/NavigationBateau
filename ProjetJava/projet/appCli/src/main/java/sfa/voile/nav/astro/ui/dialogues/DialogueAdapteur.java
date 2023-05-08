package sfa.voile.nav.astro.ui.dialogues;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.eCalculAstroConstantes;
import sfa.nav.infra.tools.error.NavException;
import sfa.voile.nav.astro.ui.menus.eUINavAstroAllMenuItems;


public abstract class DialogueAdapteur {
	private static Logger _logger = LoggerFactory.getLogger(DialogueAdapteur.class);

	public abstract void demandeDesArguments(HashMap<eCalculAstroConstantes, Object> args, eUINavAstroAllMenuItems iCas) throws NavException;
	
    protected void dumpArgs(HashMap<eCalculAstroConstantes, Object> args, eUINavAstroAllMenuItems iCas) {
    	_logger.debug("Dump arguments saisis - cas : {}", iCas);
    	for (eCalculAstroConstantes key : args.keySet()) {
        	_logger.debug("\t - {} : ->{}<-", key.name(), args.get(key));			
		}
    }    
}
