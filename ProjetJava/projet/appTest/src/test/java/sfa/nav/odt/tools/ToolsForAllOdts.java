package sfa.nav.odt.tools;

import sfa.nav.model.Distance;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.nav.calculs.CalculsDeNavigation;

public class ToolsForAllOdts {

	public static Distance distanceEntreDeuxPointsProches(PointGeographique pg1, PointGeographique pg2) {
		CalculsDeNavigation ca = new CalculsDeNavigation();

		DataLoxodromieCapDistance Retour = ca.capLoxodromique(pg1, pg2);
		return Retour.distance();
	}
}
