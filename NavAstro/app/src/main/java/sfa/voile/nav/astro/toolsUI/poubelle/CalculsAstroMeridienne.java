/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.toolsUI.poubelle;

import java.util.Hashtable;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sfa.voile.nav.astro.modele.Angle;
import sfa.voile.nav.astro.ui.tools.DataReaders;

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

    private Hashtable<eArgLatitudeMeridienne, Object> readStdInForLatitudeMeridienne() {
        Hashtable<eArgLatitudeMeridienne, Object> retour = new Hashtable<eArgLatitudeMeridienne, Object>();
        DataReaders _toolsUI = new DataReaders();

        for (eArgLatitudeMeridienne arg : eArgLatitudeMeridienne.values()) {
            System.out.println("Saisir: " + arg.getDisplay());
            Angle x = new Angle (_toolsUI.readDegreMinuteSeconde());
            retour.put(arg, x);
        }

        return retour;
    }

    public static void LatitudeMeridienne() {
        DataReaders _toolsUI = new DataReaders();
        _logger.debug("Calcul latitude meridienne");

        Hashtable<eArgLatitudeMeridienne, Object> methodeArgs = new CalculsAstroMeridienne().readStdInForLatitudeMeridienne();
        _logger.debug("Arg droite hauteur");

        Angle Ho = (Angle) methodeArgs.get(eArgLatitudeMeridienne.Ho);
        Angle Ei = (Angle) methodeArgs.get(eArgLatitudeMeridienne.Ei);
        Angle Hc = Ho.plus (Ei);

        Angle Hv = Hc.plus ((Angle) methodeArgs.get(eArgLatitudeMeridienne.CorrectionSolaire));
        double dz /* distanceZenitale */ = 90.0 - Hv.getVal();

        Angle D = (Angle) methodeArgs.get(eArgLatitudeMeridienne.DeclinaisonSolaire);

        Angle L = D.plus(dz);

        System.out.println("Res: " + L.toString());
    }

}
