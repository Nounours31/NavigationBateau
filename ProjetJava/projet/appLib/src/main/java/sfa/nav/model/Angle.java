package sfa.nav.model;

import java.text.DecimalFormat;

import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.ToStringOptions;
import sfa.nav.model.tools.ToStringOptions.eToStringMode;

public class Angle {
	private double _angleInDegre;
	private boolean _isOriente = false;

	protected Angle() {
		_internalSetInDegre(0);
	}

	public double asRadian() {
		return Angle.DegreToRadian(_angleInDegre);
	}

	public double asDegre() {
		return _angleInDegre;
	}

	protected void _internalSetInDegre(double d) {
		if (!_isOriente) { 
			while (d >= 360.0)
				d -= 360.0;
	
			while (d < 0.0)
				d += 360.0;
		} else {
			while (d >= 360.0)
				d -= 360.0;
	
			while (d <= -360.0)
				d += 360.0;			
		}
		_angleInDegre = d;
	}


	@Override
	public String toString() {
		ToStringOptions opts = new ToStringOptions(
				ToStringOptions.or(eToStringMode.full, eToStringMode.deg, eToStringMode.rad, eToStringMode.MinuteDecimale));
		return myToString(opts);
	}

	public String myToString(ToStringOptions opts) {
		double x = this.asDegre();
		String Signe = "";
		if (opts.isA(eToStringMode.Negatif)) {
			if (x < 0) Signe = "-";
		}
		x = Math.abs(x);
		
		double h = Math.floor(x);
		double m = Math.floor((x - h) * 60.0);
		double mDecimale = ((x - h) * 60.0);
		double s = ((((x - h) * 60.0) - m) * 60.0);

		DecimalFormat dfPosition = new DecimalFormat("000.000°");
		DecimalFormat dfSeconde = new DecimalFormat("00.00\"");
		DecimalFormat dfMinutDecimal = new DecimalFormat("00.00");
		DecimalFormat dfRad = new DecimalFormat("0.00 Rad");
		StringBuffer sb = new StringBuffer();
		
		String asDegre = null;
		String asDegreDecimal = null;
		String asMinuteDecimale = null;
		String asRadian = null;

		asDegreDecimal = dfPosition.format(x);
		asDegre = String.format("[%s%02d°%02d'%s]", Signe, (int) Math.floor(h), (int) Math.floor(m), dfSeconde.format(s));
		asMinuteDecimale = String.format("%s%02d°%s", Signe, (int) Math.floor(h), dfMinutDecimal.format(mDecimale));
		
		double asRad = this.asRadian();
		asRadian = String.format("[%s%s]", Signe, dfRad.format(asRad));
		
		
		if (opts.isA(eToStringMode.light)) {
			sb.append(Signe + asDegreDecimal);
		} else if (opts.isA(eToStringMode.full)) {
			sb.append(Signe + asDegreDecimal + "[" + asMinuteDecimale + "]" + asDegre  + asRadian);
		} else if (opts.isA(eToStringMode.canevas)) {
			sb.append(asMinuteDecimale );
		} else {
			sb.append("xxxxxxxxxxx");
		}
		return sb.toString();
	}
	
	public String toCanevas() {
		ToStringOptions opts = new ToStringOptions(
				ToStringOptions.or(eToStringMode.canevas, eToStringMode.deg, eToStringMode.MinuteDecimale, eToStringMode.Negatif));

		return myToString(opts);
	}


	public static double DegreToRadian(double asDegre) {
		return asDegre * Constantes.DEG2RAD;
	}

	public static double RadianToDegre(double asRadian) {
		return asRadian * Constantes.RAD2DEG;
	}

	public Angle plus(Angle dz) {
		return AngleFactory.fromDegre(this._angleInDegre + dz._angleInDegre);
	}

	public Angle multiplyByDouble(double coef) {
		return AngleFactory.fromDegre(this._angleInDegre * coef);
	}

	public void setOriente() {
		_isOriente = true;
	}

}
