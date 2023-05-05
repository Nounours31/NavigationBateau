package sfa.voile.nav.astro.ui.tools;

public class myInputReaderFactory {
	static public boolean _isODTMode = false;
	
	static public myInputReaderItf getMyInputReader() {
		if (_isODTMode)
			return new myInputReaderODT();
		return new myInputReader();
	}
}
