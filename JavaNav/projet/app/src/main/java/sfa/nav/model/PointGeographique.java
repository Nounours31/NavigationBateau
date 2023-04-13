package sfa.nav.model;

public class PointGeographique {
	private Latitude _latitude;
	private Longitude _longitude;
	
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
		return "PointGeographique [" + _latitude + ", " + _longitude + "]";
	}
	
	
}
