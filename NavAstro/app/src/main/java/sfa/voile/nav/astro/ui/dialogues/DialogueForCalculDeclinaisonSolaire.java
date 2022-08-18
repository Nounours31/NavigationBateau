/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.dialogues;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.methodes.eCalculAstroConstantes;
import sfa.voile.nav.astro.modele.Angle;
import sfa.voile.nav.astro.modele.Declinaison;
import sfa.voile.nav.astro.modele.Heure;
import sfa.voile.nav.astro.tools.NavAstroException;
import sfa.voile.nav.astro.ui.menus.MenuPrincipal;
import sfa.voile.nav.astro.ui.menus.eUINavAstroAllMenuItems;
import sfa.voile.nav.astro.ui.tools.DataReaders;

public class DialogueForCalculDeclinaisonSolaire extends DialogueAdapteur  {
	private static Logger _logger = LoggerFactory.getLogger(DialogueForCalculDeclinaisonSolaire.class);

	public DialogueForCalculDeclinaisonSolaire () {
		super ();
	}

	@Override
	public void demandeDesArguments(HashMap<eCalculAstroConstantes, Object> args, eUINavAstroAllMenuItems iCas) throws NavAstroException {
		if (iCas == eUINavAstroAllMenuItems.DeclainaisonSolaireParGradiant) 
			this.parGradiant(args);
		
		else if (iCas == eUINavAstroAllMenuItems.DeclainaisonSolaireParInterval)
			this.parInterval(args);
		
		else
			throw new NavAstroException("Methode non implementee: " + iCas.name());		
        dumpArgs(args, iCas);
	}
	
	private void parGradiant(HashMap<eCalculAstroConstantes, Object> args) {
        DataReaders _toolsUI = new DataReaders();
        _logger.debug("Calcul latitude meridienne");

        eCalculAstroConstantes arg = eCalculAstroConstantes.HeureUTRef;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        Heure x = new Heure (_toolsUI.readHeure());
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);
        
        arg = eCalculAstroConstantes.DeclinaisonRef;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        Declinaison D = new Declinaison (_toolsUI.readDeclinaison());
        System.out.println(String.format("\t%s: %s", arg.name(), D.toString()));
        args.put(arg, x);

        arg = eCalculAstroConstantes.PasDeDeclinaison;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        Angle a = new Angle (_toolsUI.readAngle());
        System.out.println(String.format("\t%s: %s", arg.name(), a.toString()));
        args.put(arg, a);

        arg = eCalculAstroConstantes.HeureMeusure;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        x = new Heure (_toolsUI.readHeure());
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);
	}
	
	private void parInterval(HashMap<eCalculAstroConstantes, Object> args) {
        DataReaders _toolsUI = new DataReaders();
        _logger.debug("Calcul latitude meridienne");

        eCalculAstroConstantes arg = eCalculAstroConstantes.HeureUT1;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        Heure h = new Heure (_toolsUI.readHeure());
        System.out.println(String.format("\t%s: %s", arg.name(), h.toString()));
        args.put(arg, h);
        
        arg = eCalculAstroConstantes.Declinaison1;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        Declinaison D = new Declinaison (_toolsUI.readDeclinaison());
        System.out.println(String.format("\t%s: %s", arg.name(), D.toString()));
        args.put(arg, D);

        arg = eCalculAstroConstantes.HeureUT2;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        h = new Heure (_toolsUI.readHeure());
        System.out.println(String.format("\t%s: %s", arg.name(), h.toString()));
        args.put(arg, h);
        
        arg = eCalculAstroConstantes.Declinaison2;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        D = new Declinaison (_toolsUI.readDeclinaison());
        System.out.println(String.format("\t%s: %s", arg.name(), D.toString()));
        args.put(arg, D);

        arg = eCalculAstroConstantes.HeureMeusure;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        h = new Heure (_toolsUI.readHeure());
        System.out.println(String.format("\t%s: %s", arg.name(), h.toString()));
        args.put(arg, h);
	}
}
