/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.nav.astro.calculs;

import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Angle;
import sfa.nav.model.AngleFactory;

/**
 *
 * @author pierr
 */
public class CalculsAstroMeridienne {

    private static Logger _logger = LoggerFactory.getLogger(CalculsAstroMeridienne.class);

    private CalculsAstroMeridienne() {
    }


    public enum eArgLatitudeMeridienne {
        Ho("Hauteur observee (Ho)"),
        Ei("Erreur instrumentale (Ei)"),
        CorrectionSolaire("Correction soleil(cor)"),
        DeclinaisonSolaire("Declinaison soleil(D)");

        public String getDisplay() {
            return _display;
        }

        final private String _display;

        private eArgLatitudeMeridienne(String s) {
            this._display = s;
        }
    }

    private Hashtable<eArgLatitudeMeridienne, Object> readStdInForLatitudeMeridienne() throws NavException {
        Hashtable<eArgLatitudeMeridienne, Object> retour = new Hashtable<eArgLatitudeMeridienne, Object>();

        for (eArgLatitudeMeridienne arg : eArgLatitudeMeridienne.values()) {
            System.out.println("Saisir: " + arg.getDisplay());
            Angle x = AngleFactory.fromString(null);
            retour.put(arg, x);
        }

        return retour;
    }

    public static void LatitudeMeridienne() throws NavException {
        _logger.debug("Calcul latitude meridienne");

        Hashtable<eArgLatitudeMeridienne, Object> methodeArgs = new CalculsAstroMeridienne().readStdInForLatitudeMeridienne();
        _logger.debug("Arg droite hauteur");

        Angle Ho = (Angle) methodeArgs.get(eArgLatitudeMeridienne.Ho);
        Angle Ei = (Angle) methodeArgs.get(eArgLatitudeMeridienne.Ei);
        Angle Hc = Ho.plus (Ei);

        Angle Hv = Hc.plus ((Angle) methodeArgs.get(eArgLatitudeMeridienne.CorrectionSolaire));
        double dz /* distanceZenitale */ = 90.0 - Hv.asDegre();
        Angle distanceZenitale = AngleFactory.fromDegre(dz);

        Angle D = (Angle) methodeArgs.get(eArgLatitudeMeridienne.DeclinaisonSolaire);

        Angle L = D.plus(distanceZenitale);

        System.out.println("Res: " + L.toString());
    }

}
