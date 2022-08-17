/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.toolsUI.poubelle;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.methodes.eCalculAstroConstantes;
import sfa.voile.nav.astro.ui.tools.DataReaders;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author pierr
 */
public class MenuDeclainaisonSolaire {
	private static Logger _logger = LoggerFactory.getLogger(MenuDeclainaisonSolaire.class);

	public enum eChoixMethodePrincipale  {
		TheEnd(0, "Sortir"),
		LatitudeMeridienne(2, "Latitude de la Meridienne"),
		DeclinaisonSolaire(3, "Declinaison solaire"),
		DroiteDeHauteur(1, "Droite De Hauteur");

		private static eChoixMethodePrincipale[] valuesSorted() {
			eChoixMethodePrincipale[] a = eChoixMethodePrincipale.values();
			Arrays.sort(a, Comparator.comparingInt(v -> v.getCode()));
			return a;
		}

		private final int _code;
		private final String _nom;

		private eChoixMethodePrincipale(int i, String s) {
			_code = i;
			_nom = s;
		}

		public int getCode() {
			return _code;
		}

		public String getNom() {
			return _nom;
		}

			static public void displayMenu() {
			for (eChoixMethodePrincipale uneMethode : eChoixMethodePrincipale.valuesSorted()) {
				System.out.println(String.format("\t%1d. %s", uneMethode.getCode(), uneMethode.getNom()));
			}
		}
		
		static public eChoixMethodePrincipale readAndCheck(String input) {
			System.out.print("Choix :");
			try {
				final int iChoix = Integer.parseInt(input);
				return Arrays.asList(eChoixMethodePrincipale.values())
						.stream()
						.filter(v -> v.getCode() == iChoix)
						.findFirst().get();
			}
			catch (Exception e) {
				_logger.error("Err: {}", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
	}


	public static void display() {
		
	}
		public eChoixMethodePrincipale choixMethode() {
		eChoixMethodePrincipale retour = null;
		System.out.println("Saisir le point :");
		eChoixMethodePrincipale.displayMenu();
		
		while (true) {
			try {
				System.out.print("Choix :");
				String input = DataReaders.readInput();
				retour = eChoixMethodePrincipale.readAndCheck(input);
				if (retour != null)
					break;
			}
			catch (Exception e) {}
		}
		return retour;
	}


	public MenuDeclainaisonSolaire() {
	}


	public static Hashtable<eCalculAstroConstantes, Object> getArgs() {
		// TODO Auto-generated method stub
		return null;
	}
}
