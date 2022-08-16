/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.toolsUI.menus;

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

import java.nio.charset.StandardCharsets;

/**
 *
 * @author pierr
 */
public class MenuLatitudeMeridienne {
	private static Logger _logger = LoggerFactory.getLogger(MenuLatitudeMeridienne.class);

	private static  MenuLatitudeMeridienne_Dialog _dialogDialog = new MenuLatitudeMeridienne_Dialog();


	public MenuLatitudeMeridienne() {
	}


	public static Hashtable<eCalculAstroConstantes, Object> getArgs() {
		// TODO Auto-generated method stub
		return null;
	}
	

	

	public static void display() {
		while (true) {
        	eUINavAstroContante methode = _dialogDialog.dialogue();
			if (methode == null)
				continue;
			
			switch (methode) {
                case Sortir: return;
                case Meridienne: 
                	HashMap<eCalculAstroConstantes, Object> args = new HashMap<eCalculAstroConstantes, Object>();
                	_dialogDialog.argsForMeridienne(args);
                	CalculsAstro.LatitudeMeridienne (args);
                    break;
                default:
            }
        }
	}

}
