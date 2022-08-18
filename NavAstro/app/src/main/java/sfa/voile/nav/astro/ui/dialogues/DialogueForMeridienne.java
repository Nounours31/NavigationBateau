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
import sfa.voile.nav.astro.ui.menus.MenuPrincipal;
import sfa.voile.nav.astro.ui.menus.eUINavAstroAllMenuItems;
import sfa.voile.nav.astro.ui.tools.DataReaders;

public class DialogueForMeridienne extends DialogueAdapteur  {
	private static Logger _logger = LoggerFactory.getLogger(MenuPrincipal.class);

	public DialogueForMeridienne () {
		super ();
	}

	
	@Override
	public void demandeDesArguments(HashMap<eCalculAstroConstantes, Object> args, eUINavAstroAllMenuItems iCas) {
        DataReaders _toolsUI = new DataReaders();
        _logger.debug("Calcul latitude meridienne");

        eCalculAstroConstantes arg = eCalculAstroConstantes.Ho;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        Angle x = new Angle (_toolsUI.readDegreMinuteSeconde());
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);
        
        arg = eCalculAstroConstantes.Ei;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        x = new Angle (_toolsUI.readDegreMinuteSeconde());
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);

        arg = eCalculAstroConstantes.Hv;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        x = new Angle (_toolsUI.readDegreMinuteSeconde());
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);

        arg = eCalculAstroConstantes.CorrectionSolaire;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        x = new Angle (_toolsUI.readDegreMinuteSeconde());
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);

        arg = eCalculAstroConstantes.DeclinaisonSolaire;
        _toolsUI.println("Saisir: " + arg.getDisplay());
        x = new Angle (_toolsUI.readDegreMinuteSeconde());
        System.out.println(String.format("\t%s: %s", arg.name(), x.toString()));
        args.put(arg, x);
	}
}
