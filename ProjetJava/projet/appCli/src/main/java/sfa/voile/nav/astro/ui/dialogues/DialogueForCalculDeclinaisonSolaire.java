/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.dialogues;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.eCalculAstroConstantes;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;
import sfa.nav.model.Declinaison;
import sfa.nav.model.DeclinaisonFactory;
import sfa.nav.model.Heure;
import sfa.nav.model.HeureFactory;
import sfa.voile.nav.astro.ui.menus.MenuPrincipal;
import sfa.voile.nav.astro.ui.menus.eUINavAstroAllMenuItems;

public class DialogueForCalculDeclinaisonSolaire extends DialogueAdapteur  {
	private static Logger _logger = LoggerFactory.getLogger(DialogueForCalculDeclinaisonSolaire.class);

	public DialogueForCalculDeclinaisonSolaire () {
		super ();
	}

	@Override
	public void demandeDesArguments(HashMap<eCalculAstroConstantes, Object> args, eUINavAstroAllMenuItems iCas) throws NavException {
		if (iCas == eUINavAstroAllMenuItems.DeclainaisonSolaireParGradiant) 
			this.parGradiant(args);
		
		else if (iCas == eUINavAstroAllMenuItems.DeclainaisonSolaireParInterval)
			this.parInterval(args);
		
		else
			throw new NavException("Methode non implementee: " + iCas.name());		
        dumpArgs(args, iCas);
	}
	
	private void parGradiant(HashMap<eCalculAstroConstantes, Object> args) throws NavException {
        _logger.debug("Calcul latitude meridienne");

        eCalculAstroConstantes arg = eCalculAstroConstantes.HeureUTRef;
        Heure x = HeureFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);
        
        arg = eCalculAstroConstantes.DeclinaisonRef;
        Declinaison D = (Declinaison) DeclinaisonFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), D.toString()));
        args.put(arg, x);

        arg = eCalculAstroConstantes.PasDeDeclinaison;
        Angle a = AngleFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), a.toString()));
        args.put(arg, a);

        arg = eCalculAstroConstantes.HeureMeusure;
        x = HeureFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);
	}
	
	private void parInterval(HashMap<eCalculAstroConstantes, Object> args) throws NavException {
        _logger.debug("Calcul latitude meridienne");

        eCalculAstroConstantes arg = eCalculAstroConstantes.HeureUT1;
        Heure h = HeureFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), h.toString()));
        args.put(arg, h);
        
        arg = eCalculAstroConstantes.Declinaison1;
        Declinaison D = (Declinaison) DeclinaisonFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), D.toString()));
        args.put(arg, D);

        arg = eCalculAstroConstantes.HeureUT2;
        h = HeureFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), h.toString()));
        args.put(arg, h);
        
        arg = eCalculAstroConstantes.Declinaison2;
        D = (Declinaison) DeclinaisonFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), D.toString()));
        args.put(arg, D);

        arg = eCalculAstroConstantes.HeureMeusure;
        h = HeureFactory.fromString(null);
        System.out.println(String.format("\t%s: %s", arg.name(), h.toString()));
        args.put(arg, h);
	}
}
