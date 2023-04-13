package sfa.nav;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class starter {
	private static Logger logger = LoggerFactory.getLogger(starter.class);

	public static void main(String[] args) {
		logger.debug("Start myUI ...");
		MyUI ui = new MyUI();
		ui.startUI();
		logger.debug("End myUI ...! bye");
	}
}
