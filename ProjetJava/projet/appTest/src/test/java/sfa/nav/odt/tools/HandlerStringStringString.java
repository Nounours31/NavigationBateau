package sfa.nav.odt.tools;

import java.util.ArrayList;
import java.util.List;

public class HandlerStringStringString {
	public String _u;
	public String _v;
	public String _w;
	public HandlerStringStringString(String u, String v, String w) {_u = u; _v = v; _w = w;}
	
	static public List<HandlerStringStringString> fromStringStringArray (String[][] allInfos) {
		List<HandlerStringStringString> retour = new ArrayList<>();
		for (String[] s : allInfos) {
			if ((s != null) && (s.length == 3) && (s[0] != null) && (s[1] != null) && (s[2] != null)) {
				HandlerStringStringString x = new HandlerStringStringString(s[0], s[1], s[2]);
				retour.add(x);
			}
		}
		return retour;
	}
}
