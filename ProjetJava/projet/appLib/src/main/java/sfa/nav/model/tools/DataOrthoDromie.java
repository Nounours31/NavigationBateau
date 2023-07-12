package sfa.nav.model.tools;

import sfa.nav.model.Cap;
import sfa.nav.model.PointGeographique;

public class DataOrthoDromie extends DataLoxodromieCapDistance {
	private PointGeographique _vertex;
	private Cap _routefondInitiale;
		
	public PointGeographique vertex() {
		return _vertex;
	}
	public void vertex(PointGeographique v) {
		_vertex = v;
	}
	public Cap Rfinitiale() {
		return _routefondInitiale;
	}
	public void Rfinitiale(Cap v) {
		_routefondInitiale = v;
	}

	@Override
	public String toString() {
		return "C:"+ cap().toString()+" - D:"+ distance().toString() + " - P:" + vertex().toString()+ " - RF initiale:" + Rfinitiale().toString();
	}
}
