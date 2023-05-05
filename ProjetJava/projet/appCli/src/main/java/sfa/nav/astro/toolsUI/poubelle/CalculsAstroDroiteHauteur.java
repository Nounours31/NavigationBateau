/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.nav.astro.toolsUI.poubelle;

import java.util.Hashtable;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.lib.tools.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;

/**
 *
 * @author pierr
 */
public class CalculsAstroDroiteHauteur extends CalculsAstro {

    private static Logger _logger = LoggerFactory.getLogger(CalculsAstroDroiteHauteur.class);

    private  CalculsAstroDroiteHauteur() {
    }

        public enum eArgDroiteHauteur {
        Ho("Hauteur observee (Ho)"),
        Ei("Erreur instrumentale (Ei)"),
        CorrectionSolaire("Correction soleil(cor)"),
        DeclinaisonSolaire("Declinaison soleil(D)");

        public String getDisplay() {
            return _display;
        }

        final private String _display;

        private eArgDroiteHauteur(String s) {
            this._display = s;
        }
    }
    private Map<eArgDroiteHauteur, Object> ArgForDroiteHauteur() throws NavException {
        Map<eArgDroiteHauteur, Object> retour = new Hashtable<eArgDroiteHauteur, Object>();

        for (eArgDroiteHauteur arg : eArgDroiteHauteur.values()) {
            System.out.println("Saisir: " + arg.getDisplay());
            Angle x = AngleFactory.fromString(null);
            retour.put(arg, x);
        }

        return retour;
    }

    public static void droiteHauteur() throws NavException {
        Map<eArgDroiteHauteur, Object> methodeArgs = new CalculsAstroDroiteHauteur().ArgForDroiteHauteur();
        _logger.debug("Arg droite hauteur");
    }

}
