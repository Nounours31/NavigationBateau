/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.toolsUI.poubelle;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.ui.tools.DataReaders;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author pierr
 */
public class MenuPOurPieceDetachee {
	private static Logger _logger = LoggerFactory.getLogger(MenuPOurPieceDetachee.class);

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

	public enum eChoixMethodeSoleil  {
		TheEnd(0, "Sortir"),
		PartGradiant(1, "Par le gradiant (Position initiale et vitesse de variation)"),
		ParInterval(2, "Par interval (regle de trois sur un interval de valeur");

		private static eChoixMethodeSoleil[] valuesSorted() {
			eChoixMethodeSoleil[] a = eChoixMethodeSoleil.values();
			Arrays.sort(a, Comparator.comparingInt(v -> v.getCode()));
			return a;
		}

		private final int _code;
		private final String _nom;

		private eChoixMethodeSoleil(int i, String s) {
			_code = i;
			_nom = s;
		}

		public int getCode() {
			return _code;
		}

		public String getNom() {
			return _nom;
		}
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

	public eChoixMethodeSoleil choixMethodeSoleil() {
		System.out.println("Saisir la methode :");
		for (eChoixMethodeSoleil uneMethode : eChoixMethodeSoleil.valuesSorted()) {
			System.out.println(String.format("\t%1d. %s", uneMethode.getCode(), uneMethode.getNom()));
		}
		while (true) {
			System.out.print("Choix :");
			try {
				Scanner in = new Scanner(System.in);
				String s = in.nextLine();
				_logger.debug("Saisie ==>{}<==", s);
				final int iChoix = Integer.parseInt(s);

				return Arrays.asList(eChoixMethodeSoleil.values())
						.stream()
						.filter(v -> v.getCode() == iChoix)
						.findFirst().get();
			}
			catch (Exception e) {
				_logger.error("Err: {}", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public MenuPOurPieceDetachee() {
	}
}
