package sfa.voile.nav.astro.test.ui;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import sfa.voile.nav.astro.aaaa.Repository;
import sfa.voile.nav.astro.ui.tools.myInputReaderFactory;

public class ScenarioAxTxt {
	static public void setODTMode() {
		myInputReaderFactory._isODTMode = true;
	}

	static public void cleanODTScenario() {
		File repo = Repository.getRepository();
		FilenameFilter scenarioFileFilter = (dir, fileName) -> {
			  return (fileName.toLowerCase().startsWith("ODTScenario_") && fileName.toLowerCase().endsWith(".txt")); 
			};
		File[] allODTSCenard = repo.listFiles(scenarioFileFilter);
		for (File file : allODTSCenard) {
			file.delete();
		}
	}

	public static void addScenario(String cas) {
		cleanODTScenario();
		String scenario = "";
		switch (cas) {
		case "test1":
			scenario = getScenario1Input();
			break;
		case "test2":
			scenario = getScenario2Input();
			break;
		case "test3":
			scenario = getScenario3Input();
			break;
		case "test4":
			scenario = getScenario4Input();
			break;
		case "test5":
			scenario = getScenario5Input();
			break;
		case "test6":
			scenario = getScenario6Input();
			break;

		default:
			break;
		}
		
		File x = new File(Repository.getRepository(), "ODTScenario_ScenarioAsTxt.txt");
		try {
			
			FileWriter fw = new FileWriter(x, StandardCharsets.UTF_8, false);
			fw.append(scenario);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private static String getScenario1Input() {
		return "1\n";
	}

	private static String getScenario2Input() {
		return "2\n1\n1\n";
	}

	private static String getScenario3Input() {
		return "3\n1\n1\n";
	}
	
	private static String getScenario4Input() {
		return "4\n1\n1\n";
	}

	private static String getScenario5Input() {
		return "5\n1\n1\n";
	}

	private static String getScenario6Input() {
		return "6\n1\n1\n";
	}

}
