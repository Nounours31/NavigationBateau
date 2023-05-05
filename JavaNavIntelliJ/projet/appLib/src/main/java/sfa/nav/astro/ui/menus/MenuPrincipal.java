/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.methodes.CalculsAstro;
import sfa.voile.nav.astro.methodes.eCalculAstroConstantes;
import sfa.voile.nav.astro.tools.NavAstroError;

import java.nio.charset.StandardCharsets;

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
