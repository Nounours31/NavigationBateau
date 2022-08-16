/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.toolsUI.menus;

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
import sfa.voile.nav.astro.toolsUI.poubelle.MenuDeclainaisonSolaire;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author pierr
 */
public class MenuPrincipal {
	private static Logger _logger = LoggerFactory.getLogger(MenuPrincipal.class);
	private static MenuPrincipal_Dialog _dialogDialog = new MenuPrincipal_Dialog();
	

	public static void display() {
		while (true) {
        	eUINavAstroContante methode = MenuPrincipal._dialogDialog.dialogue();
			if (methode == null)
				continue;
			
			switch (methode) {
                case Sortir: return;
                case DroiteHauteur: 
                    break;
                case LatitudeDeLaMeridienne : 
                	MenuLatitudeMeridienne.display();
                    break;
                case DeclinaisonSolaire : 
                	MenuDeclainaisonSolaire.display();
                    break;
                default:
            }
        }
	}
}
