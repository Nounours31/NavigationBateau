/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.ui.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.methodes.CalculsAstro;
import sfa.voile.nav.astro.methodes.eCalculAstroConstantes;
import sfa.voile.nav.astro.modele.Angle;
import sfa.voile.nav.astro.modele.Declinaison;
import sfa.voile.nav.astro.modele.Heure;
import sfa.voile.nav.astro.tools.NavAstroError;
import sfa.voile.nav.astro.ui.dialogues.DialogueForCalculDeclinaisonParInterval;
import sfa.voile.nav.astro.ui.dialogues.DialogueForMeridienne;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author pierr
 */
public class MenuMeridienne extends MenuAdapteur {
	private static Logger _logger = LoggerFactory.getLogger(MenuMeridienne.class);

	public MenuMeridienne() {
		_Item = new NavAstroMenuItem[] {
				new NavAstroMenuItem (eUINavAstroAllMenuItems.Sortir, 1),
				new NavAstroMenuItem (eUINavAstroAllMenuItems.Meridienne, 2),		
				new NavAstroMenuItem (eUINavAstroAllMenuItems.LatitudeDeLaMeridienne, 3)	
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
                case LatitudeDeLaMeridienne: 
                	DialogueForMeridienne x = new DialogueForMeridienne();
                	_args = new HashMap<eCalculAstroConstantes, Object>();
					x.demandeDesArguments(_args, methode);
					
					Angle Ho = getAngleFromArgsList (eCalculAstroConstantes.Ho);
					Angle Ei = getAngleFromArgsList (eCalculAstroConstantes.Ei);
					Angle Hc = getAngleFromArgsList (eCalculAstroConstantes.Hc);
					Angle Hv = getAngleFromArgsList (eCalculAstroConstantes.Hv); 
					Declinaison D = getDeclinaisonFromArgsList(eCalculAstroConstantes.DeclinaisonSolaire);
					_calculAstro.LatitudeMeridienne(Ho, Ei, Hc, Hv, D);

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
