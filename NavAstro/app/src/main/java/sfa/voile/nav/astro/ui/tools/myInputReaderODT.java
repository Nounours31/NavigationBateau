package sfa.voile.nav.astro.ui.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.aaaa.Repository;

public class myInputReaderODT implements myInputReaderItf {
	private static Logger _logger = LoggerFactory.getLogger(myInputReader.class);
	private File odtRespponse = null;
	private InputStreamReader ir = null;
	
	public myInputReaderODT()  {
		File repo = Repository.getRepository();
		if (repo.exists() && repo.isDirectory()) {
			odtRespponse = new File (repo, "ODTScenario_ScenarioAsTxt.txt");
			try {
				ir = new InputStreamReader(new FileInputStream(odtRespponse), StandardCharsets.UTF_8);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
    public String readInput () {
		// Scanner in = new Scanner(System.in);
		// String s = in.nextLine();
    	
    	Scanner console = null;
		try {
			console = new Scanner(ir);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	String s = console.nextLine();

    	_logger.debug("Saisie ==>{}<==", s);
    	return s;
	}

}
