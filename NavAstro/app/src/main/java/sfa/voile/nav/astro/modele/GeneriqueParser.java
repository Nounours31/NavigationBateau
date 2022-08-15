package sfa.voile.nav.astro.modele;

import java.util.regex.Matcher;

public abstract class GeneriqueParser  {
    
    protected static GeneriqueDataFormat[] _allRegex = null;    
    public static GeneriqueDataFormat[] getAllAvailableFormat() {
		return _allRegex;
	}
 
	protected boolean parseDouble(String s1, HandleDouble d) {
		double v = 0.0;
		boolean rc = false;
		String s =  new String(s1);

		try {
			v = Double.parseDouble(s);
			rc = true;
		}
		catch (NumberFormatException e1) {
			s.replaceAll("\\.", ",");
			try {
				v = Double.parseDouble(s);
				rc = true;
			}
			catch (NumberFormatException e2) {
				s.replaceAll(",", "\\.");
				try {
					v = Double.parseDouble(s);
					rc = true;
				}
				catch (NumberFormatException e3) {					
				}
			}
		}
		d.d(v);
		return rc;
	}
}
