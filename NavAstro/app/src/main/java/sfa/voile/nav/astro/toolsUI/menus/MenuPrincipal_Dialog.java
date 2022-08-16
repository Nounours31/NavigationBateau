/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.toolsUI.menus;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuPrincipal_Dialog extends NavAstroMenuItems  {
	private static Logger _logger = LoggerFactory.getLogger(MenuPrincipal.class);

	public MenuPrincipal_Dialog () {
		super ();

		_Item = new ArrayList<NavAstroMenuItem>();
		_Item.add (new NavAstroMenuItem (eUINavAstroContante.Sortir, 1, "Sortir"));
		_Item.add (new NavAstroMenuItem (eUINavAstroContante.LatitudeDeLaMeridienne, 2, "Latitude de la Meridienne"));
		_Item.add (new NavAstroMenuItem (eUINavAstroContante.DeclinaisonSolaire, 3, "Declinaison solaire"));
		_Item.add (new NavAstroMenuItem (eUINavAstroContante.DroiteHauteur, 4, "Droite De Hauteur"));
	}	
}
