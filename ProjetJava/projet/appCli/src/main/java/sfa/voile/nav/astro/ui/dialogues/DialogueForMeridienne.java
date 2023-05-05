/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.dialogues;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.eCalculAstroConstantes;
import sfa.nav.lib.tools.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.voile.nav.astro.ui.menus.eUINavAstroAllMenuItems;

public class DialogueForMeridienne extends DialogueAdapteur  {
	private static Logger _logger = LoggerFactory.getLogger(DialogueForMeridienne.class);

	public DialogueForMeridienne () {
		super ();
	}

	
	@Override
	public void demandeDesArguments(HashMap<eCalculAstroConstantes, Object> args, eUINavAstroAllMenuItems iCas) throws NavException {
        _logger.debug("Calcul latitude meridienne");

        eCalculAstroConstantes arg = eCalculAstroConstantes.Ho;
        Angle x = AngleFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);
        
        arg = eCalculAstroConstantes.Ei;
        x = AngleFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);

        arg = eCalculAstroConstantes.Hv;
        x = AngleFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);

        arg = eCalculAstroConstantes.CorrectionSolaire;
        x = AngleFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);

        arg = eCalculAstroConstantes.DeclinaisonSolaire;
        x = AngleFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);
        
        dumpArgs(args, iCas);
	}
}
