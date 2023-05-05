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
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.voile.nav.astro.aaaa.Repository;

public class myInputReaderODT implements myInputReaderItf {
	private static Logger _logger = LoggerFactory.getLogger(myInputReader.class);
	
	static private Scanner console = null; 
	static private FileTime timeStamp = null; 
	
	public myInputReaderODT()  {
		File repo = Repository.getRepository();
		File odtRespponse = new File (repo, "ODTScenario_ScenarioAsTxt.txt");

		if ((console != null) && (!hasChanged(odtRespponse)))
			return;
		
		if (repo.exists() && repo.isDirectory()) {
			try {
				String contenu = Files.readString(odtRespponse.toPath(), StandardCharsets.UTF_8);
				InputStreamReader ir = new InputStreamReader(new ByteArrayInputStream( contenu.getBytes( StandardCharsets.UTF_8)));
				console = new Scanner(ir);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean hasChanged(File odtRespponse) {
		BasicFileAttributes attr;
		try {
			attr = Files.readAttributes(odtRespponse.toPath(), BasicFileAttributes.class);
		    FileTime fileTimeModif = attr.lastModifiedTime();
		    FileTime fileTimeCreate = attr.lastModifiedTime();
		    
		    FileTime fileTimePlusRecent = fileTimeModif;
		    int PlusRecent =  fileTimeModif.compareTo(fileTimeCreate); // < 0 if this fileTimeModif < fileTimeCreate
		    if (PlusRecent > 0)
		    	fileTimePlusRecent = fileTimeCreate;
		    
		    if (timeStamp == null) {
		    	timeStamp = fileTimePlusRecent;
		    	return true;
		    }
		    else {
		    	if (timeStamp.compareTo(fileTimePlusRecent) == 0) return false;
		    	if (timeStamp.compareTo(fileTimePlusRecent) < 0) {
		    		timeStamp = fileTimePlusRecent;
		    		return true;
		    	}
		    	return false;		    	
		    }
		} catch (IOException e) {
			e.printStackTrace();
			return true;
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
