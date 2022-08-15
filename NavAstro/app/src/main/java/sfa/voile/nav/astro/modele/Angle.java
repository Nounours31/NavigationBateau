/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.voile.nav.astro.modele;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sfa.voile.nav.astro.tools.Utiitaires;

/**
 *
 * @author pierr
 */
public class Angle {

    private static Logger _logger = LoggerFactory.getLogger(Angle.class);
    protected double _val = 0.0;

    public Angle(double v) {
        _val = v;
    }

    public String toDegreMinuteSeconde() {
        Locale loc = Locale.getDefault();
        NumberFormat nf = NumberFormat.getNumberInstance(loc);
        DecimalFormat df_double = (DecimalFormat) nf;
        df_double.applyPattern("00.00");

        double x = _val;
        StringBuffer sb = new StringBuffer();
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

        sb.append(df_double.format(x));
        sb.append("\"");

        return sb.toString();
    }

    public String toDegreMinuteDecimale() {
        Locale loc = Locale.getDefault();
        NumberFormat nf = NumberFormat.getNumberInstance(loc);
        DecimalFormat df_double = (DecimalFormat) nf;
        df_double.applyPattern("00.00");

        double x = _val;
        StringBuffer sb = new StringBuffer();
        if (x < 0) {
            sb.append("-");
        }
        x = Math.abs(x);

        sb.append(String.format("%02d", (int) Math.floor(x)));
        sb.append("°");
        x -= Math.floor(x);
        x *= 60.0;

        sb.append(df_double.format(x));
        sb.append("'");

        return sb.toString();
    }

    public double getVal() {
        return _val;
    }

    public void setVal(double x) {
    	_val = x;
	}


    public Angle plus(double dz) {
        return new Angle(this._val + dz);
    }

    public Angle plus(Angle Ei) {
        return new Angle(this._val + Ei._val);
    }

    @Override
    public String toString() {
        return "Angle: " + this.toDegreMinuteSeconde() + "["+this.toDegreMinuteDecimale()+"]"+ "[" + _val + "]";
    }

    public double multiply(double taux) {
        return _val * taux;
    }

}
