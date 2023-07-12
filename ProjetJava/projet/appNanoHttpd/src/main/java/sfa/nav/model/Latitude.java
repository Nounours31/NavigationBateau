package sfa.nav.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.model.tools.ToStringOptions;
import sfa.nav.model.tools.ePointsCardinaux;

// -----------------------------------------
// La latitude va de 0 -> +90° (N)  et 0 -> -90° (S) 
// ou [0;90] U [270;360]
// -----------------------------------------

public class Latitude extends Angle {
	private static Logger logger = LoggerFactory.getLogger(Latitude.class);

	protected Latitude() {
		super();
	}

	@Override
	public double asRadian() {
		return Angle.DegreToRadian(asDegre());
	}

	@Override
	public double asDegre() {
		if (getSens() == ePointsCardinaux.Nord) {
			return super.asDegre();
		} else if (getSens() == ePointsCardinaux.Sud) {
			return super.asDegre() - 360.0;
		}
		logger.error("Latitude invalide");
		return 0.0;
	}

	@Override
	public String toString() {
		Angle a = AngleFactory.fromDegre(Math.abs(this.asDegre()));
		return "lat:" + a.toString() + " " + getSens().getTag();
	}

	public String myToString(ToStringOptions opts) {
		Angle a = AngleFactory.fromDegre(Math.abs(this.asDegre()));
		return "lat:" + a.myToString(opts) + " " + getSens().getTag();
	}

	public ePointsCardinaux getSens() {
		if (super.asDegre() <= 90.0)
			return ePointsCardinaux.Nord;
		if (super.asDegre() >= 270.0)
			return ePointsCardinaux.Sud;
		return ePointsCardinaux.Error;
	}

	public void inverseSens() {
		set(asDegre() * (-1.0));
	}

	public static boolean isValideAngleInDegre(double x) {
		Angle a = AngleFactory.fromDegre(x);
		if (a.asDegre() <= 90.0)
			return true;
		if (a.asDegre() >= 270.0)
			return true;
		return false;
	}
}
