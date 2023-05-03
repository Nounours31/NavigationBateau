package sfa.nav.lib.model;

import sfa.nav.lib.tools.NavException;
import sfa.nav.lib.tools.eToStringMode;

public class PointGeographique {
	private Latitude _latitude = null;;
	private Longitude _longitude = null;
	
	public PointGeographique (Latitude lat, Longitude Longi) {
		try {
			_latitude = Latitude.fromDegre(lat.asDegre());
			_longitude = Longitude.fromDegre(Longi.asDegre());
		}
		catch (NavException e) {
			
		}
	}
	
	public Latitude latitude() {
		return _latitude;
	}
	public void latitude(Latitude _latitude) {
		this._latitude = _latitude;
	}
	public Longitude longitude() {
		return _longitude;
	}
	public void longitude(Longitude _longitude) {
		this._longitude = _longitude;
	}
	
	@Override
	public String toString() {
		return "PointGeographique [l:" + _latitude.myToString(eToStringMode.or(eToStringMode.light)) + ", G:" + _longitude.myToString(eToStringMode.or(eToStringMode.light)) + "]";
	}
	
	
}
