/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.modele;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.tools.Utiitaires;

/**
 *
 * @author pierr
 */
public class Heure {

    private static Logger _logger = LoggerFactory.getLogger(Heure.class);
    private double _val = 0.0;

    public Heure(double v) {
        _val = v;
    }


    public String toHeureMinuteSeconde() {
        double x = _val;
        StringBuffer sb = new StringBuffer(x + " <==> ");
        if (x < 0) {
            sb.append("-");
        }
        x = Math.abs(x);

        sb.append(String.format("%02d", (int) Math.floor(x)));
        sb.append("°");
        x -= Math.floor(x);
        x *= 60.0;

        sb.append(String.format("%02d", (int) Math.floor(x)));
        sb.append("'");
        x -= Math.floor(x);
        x *= 60.0;

        sb.append(String.format("%02.02f", Math.floor(x)));
        sb.append("\"");

        return sb.toString();
    }

    public double getVal() {
        return _val;
    }

    public Heure plus(double dz) {
        return new Heure(this._val + dz);
    }

    public Heure plus(Heure Ei) {
        return new Heure(this._val + Ei._val);
    }

    @Override
    public String toString() {
        return "Heure: " + this.toHeureMinuteSeconde() + "[" + _val + "]";
    }

    public boolean apres(Heure H) {
        return (_val > H._val);
    }

    public boolean avant(Heure H) {
        return !apres(H);
    }

    public double moins(Heure H) {
        return _val - H._val;
    }

}
