package sfa.nav.odt.tools;

import java.util.ArrayList;
import java.util.List;

public class HandlerStringString {
	public String _s;
	public String _v;
	public HandlerStringString(String s, String v) {_s = s; _v = v;}
	
	static public List<HandlerStringString> fromStringStringArray (String[][] allInfos) {
		List<HandlerStringString> retour = new ArrayList<>();
		for (String[] s : allInfos) {
			if ((s != null) && (s.length == 2) && (s[0] != null) && (s[1] != null)) {
				HandlerStringString x = new HandlerStringString(s[0], s[1]);
				retour.add(x);
			}
		}
		return retour;
	}
}
