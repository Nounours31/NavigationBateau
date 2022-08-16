/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.toolsUI.menus;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.methodes.eCalculAstroConstantes;
import sfa.voile.nav.astro.modele.Angle;

public class MenuLatitudeMeridienne_Dialog extends NavAstroMenuItems  {
	private static Logger _logger = LoggerFactory.getLogger(MenuPrincipal.class);

	public MenuLatitudeMeridienne_Dialog () {
		super ();

		_Item = new ArrayList<NavAstroMenuItem>();
		_Item.add (new NavAstroMenuItem (eUINavAstroContante.Sortir, 1, "Sortir"));
		_Item.add (new NavAstroMenuItem (eUINavAstroContante.Meridienne, 2, "xxx interval"));
	}

	public void argsForMeridienne(HashMap<eCalculAstroConstantes, Object> args) {
        UtilitairesUI _toolsUI = new UtilitairesUI();
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
