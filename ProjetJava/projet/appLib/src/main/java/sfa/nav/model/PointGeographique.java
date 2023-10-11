package sfa.nav.model;

import sfa.nav.model.tools.ToStringOptions;
import sfa.nav.model.tools.ToStringOptions.eToStringMode;

public class PointGeographique {
	private Latitude _latitude = null;;
	private Longitude _longitude = null;

	protected PointGeographique() {
	}

	public Latitude latitude() {
		return _latitude;
	}

	public void latitude(Latitude _latitude) {
		this._latitude = LatitudeFactory.fromDegre(_latitude.asDegre());
	}

	public Longitude longitude() {
		return _longitude;
	}

	public void longitude(Longitude _longitude) {
		this._longitude = LongitudeFactory.fromDegre(_longitude.asDegre());
	}

	@Override
	public String toString() {
		ToStringOptions opts = new ToStringOptions(
				ToStringOptions.or(eToStringMode.canevas, eToStringMode.MinuteDecimale));
		
		// ToStringOptions opts = new ToStringOptions(eToStringMode.light);
		return "PtGeo [" + _latitude.myToString(opts) + ", " + _longitude.myToString(opts) + "]";
	}

	public String toCanevas() {
		ToStringOptions opts = new ToStringOptions(eToStringMode.canevas);
		return "PtGeo [" + _latitude.myToString(opts) + ", " + _longitude.myToString(opts) + "]";
	}
}
