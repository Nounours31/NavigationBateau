package sfa.voile.nav.astro.ui.tools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.aaaa.Repository;

public class myInputReaderODT implements myInputReaderItf {
	private static Logger _logger = LoggerFactory.getLogger(myInputReader.class);
	private File odtRespponse = null;
	
	static private Scanner console = null; 
	
	public myInputReaderODT()  {
		if (console != null)
			return;
		
		File repo = Repository.getRepository();
		if (repo.exists() && repo.isDirectory()) {
			odtRespponse = new File (repo, "ODTScenario_ScenarioAsTxt.txt");
			try {
				String contenu = Files.readString(odtRespponse.toPath(), StandardCharsets.UTF_8);
				InputStreamReader ir = new InputStreamReader(new ByteArrayInputStream( contenu.getBytes( StandardCharsets.UTF_8)));
				console = new Scanner(ir);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
    public String readInput () {
		// Scanner in = new Scanner(System.in);
		// String s = in.nextLine();
    	
		String s = null;
		try {
	    	s = console.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

    	_logger.debug("Saisie ==>{}<==", s);
    	return s;
	}

}
