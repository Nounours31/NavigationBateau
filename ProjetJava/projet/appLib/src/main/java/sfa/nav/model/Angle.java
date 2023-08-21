package sfa.nav.model;

import java.text.DecimalFormat;

import sfa.nav.model.tools.Constantes;
import sfa.nav.model.tools.ToStringOptions;
import sfa.nav.model.tools.ToStringOptions.eToStringMode;

public class Angle {
	private double _angleInDegre;
	private boolean _isOriente = false;

	protected Angle() {
		set(0);
	}

	public double asRadian() {
		return Angle.DegreToRadian(_angleInDegre);
	}

	public double asDegre() {
		return _angleInDegre;
	}

	protected void set(double d) {
		if (!_isOriente) { 
			while (d >= 360.0)
				d -= 360.0;
	
			while (d < 0.0)
				d += 360.0;
		}	
		_angleInDegre = d;
	}


	@Override
	public String toString() {
		ToStringOptions opts = new ToStringOptions(
				ToStringOptions.or(eToStringMode.full, eToStringMode.deg, eToStringMode.rad));
		return myToString(opts);
	}

	public String myToString(ToStringOptions opts) {
		double x = this.asDegre();
		double h = Math.floor(x);
		double m = Math.floor((x - h) * 60.0);
		double s = ((((x - h) * 60.0) - m) * 60.0);

		DecimalFormat dfPosition = new DecimalFormat("000.000°");
		DecimalFormat dfSeconde = new DecimalFormat("00.00\"");
		DecimalFormat dfRad = new DecimalFormat("0.00 Rad");
		StringBuffer sb = new StringBuffer();

		if (opts.isA(eToStringMode.light)) {
			sb.append(dfPosition.format(x));
		} else if (opts.isA(eToStringMode.full)) {
			sb.append(dfPosition.format(x));

			if (opts.isA(eToStringMode.deg))
				sb.append(
						String.format("[%02d°%02d'%s]", (int) Math.floor(h), (int) Math.floor(m), dfSeconde.format(s)));

			if (opts.isA(eToStringMode.rad)) {
				double asRad = this.asRadian();
				sb.append(String.format("[%s]", dfRad.format(asRad)));
			}
		} else {
			sb.append("xxxxxxxxxxx");
		}
		return sb.toString();
	}

	public static double DegreToRadian(double asDegre) {
		return asDegre * Constantes.DEG2RAD;
	}

	public static double RadianToDegre(double asRadian) {
		return asRadian * Constantes.RAD2DEG;
	}

	public Angle plus(Angle dz) {
		this.set(this._angleInDegre + dz._angleInDegre);
		return this;
	}

	public Angle multiplyByDouble(double coef) {
		this.set(this._angleInDegre * coef);
		return this;
	}

	public void setOriente() {
		_isOriente = true;
	}

}
