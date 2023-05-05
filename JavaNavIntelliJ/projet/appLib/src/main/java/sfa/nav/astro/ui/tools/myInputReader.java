package sfa.voile.nav.astro.ui.tools;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class myInputReader implements myInputReaderItf {
	private static Logger _logger = LoggerFactory.getLogger(myInputReader.class);
	
	public myInputReader() {}
	
	@Override
    public String readInput () {
    	
		// Scanner in = new Scanner(System.in);
		// String s = in.nextLine();
    	
    	Scanner console = null;
		try {
			console = new Scanner(new InputStreamReader(System.in, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	String s = console.nextLine();

    	_logger.debug("Saisie ==>{}<==", s);
    	return s;
    }

}
