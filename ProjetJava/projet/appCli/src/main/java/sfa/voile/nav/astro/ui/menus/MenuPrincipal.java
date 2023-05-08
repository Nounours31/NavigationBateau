/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.menus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavAstroError;

/**
 *
 * @author pierr
 */
public class MenuPrincipal extends MenuAdapteur {
	private static Logger _logger = LoggerFactory.getLogger(MenuPrincipal.class);

	public MenuPrincipal() {
		_Item = new NavAstroMenuItem[] {
				new NavAstroMenuItem (eUINavAstroAllMenuItems.Sortir, 1),
				new NavAstroMenuItem (eUINavAstroAllMenuItems.DeclinaisonSolaire, 2),
				new NavAstroMenuItem (eUINavAstroAllMenuItems.LatitudeDeLaMeridienne, 3),
				new NavAstroMenuItem (eUINavAstroAllMenuItems.DroiteHauteur, 4)
		};
	}

	@Override
	public boolean affichageItemsDuMenu() {
		boolean retour = false;
		try {
			NavAstroError err = new NavAstroError();
			while (retour == false) {
				displayMenu();
	        	eUINavAstroAllMenuItems methode = getUserChoiceAndValidedIt(err);
				if (methode == null)
					continue;
				
				switch (methode) {
	                case Sortir: 
	                	retour = true;
	                	break;
	                case DeclinaisonSolaire : 
	                	(new MenuDeclinaisonSolaire()).affichageItemsDuMenu();
	                    break;
	                case LatitudeDeLaMeridienne : 
	                	(new MenuMeridienne()).affichageItemsDuMenu();
	                    break;
	                default:
	                	displayErreur (err);
	                	break;
	            }
			}
		}
		catch (Exception e) {}
		return retour;
	}
}
