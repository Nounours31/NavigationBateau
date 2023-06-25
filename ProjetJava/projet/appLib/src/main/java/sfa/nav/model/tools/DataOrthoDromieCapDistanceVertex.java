package sfa.nav.model.tools;

import sfa.nav.model.PointGeographique;

public class DataOrthoDromieCapDistanceVertex extends DataLoxodromieCapDistance {
	private PointGeographique _vertex;
		
	public PointGeographique vertex() {
		return _vertex;
	}
	public void vertex(PointGeographique v) {
		_vertex = v;
	}

	@Override
	public String toString() {
		return "C:"+ cap().toString()+" - D:"+ distance().toString() + " - P:" + vertex().toString();
	}
}
