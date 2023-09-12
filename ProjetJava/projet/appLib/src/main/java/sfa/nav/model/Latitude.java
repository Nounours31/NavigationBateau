package sfa.nav.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.model.tools.ToStringOptions;
import sfa.nav.model.tools.ePointsCardinaux;
import sfa.nav.model.tools.ToStringOptions.eToStringMode;

// -----------------------------------------
// La latitude va de 0 -> +90° (N)  et 0 -> -90° (S) 
// ou [0;90] U [270;360]
// -----------------------------------------

public class Latitude extends Angle {
	private static Logger logger = LoggerFactory.getLogger(Latitude.class);

	protected Latitude() {
		super();
		super.setOriente();
	}

	@Override
	public double asRadian() {
		return Angle.DegreToRadian(asDegre());
	}

	@Override
	public double asDegre() {
		return super.asDegre();
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

	
	public String toCanevas() {
		ToStringOptions opts = new ToStringOptions(
				ToStringOptions.or(eToStringMode.canevas, eToStringMode.MinuteDecimale, eToStringMode.Negatif));

		
		Angle a = AngleFactory.fromDegre(Math.abs(this.asDegre()));
		return "[" + a.myToString(opts) + " " + getSens().getTag() + "]";
	}

	public ePointsCardinaux getSens() {
		if ((super.asDegre() >= 0.0) && (super.asDegre() <= 90.0))
			return ePointsCardinaux.Nord;
		if ((super.asDegre() < 0.0) && (super.asDegre() >= -90.0))
			return ePointsCardinaux.Sud;
		return ePointsCardinaux.Error;
	}


	public static boolean isValideAngleInDegre(double x) {
		if (Math.abs(x) <= 90.0)
			return true;
		return false;
	}
	
	public Latitude plusDegre(double variationEnDegre) {
		double inDegre = this.asDegre();
		inDegre += variationEnDegre;
		
		return LatitudeFactory.fromDegre(inDegre);		
	}

}
