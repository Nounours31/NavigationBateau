package sfa.nav.httpd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyHttpHandlerFactory {
	final static private Logger logger = LoggerFactory.getLogger(MyHttpHandlerFactory.class);

	static public enum eHttpHandlerType {
		OnFile, OnDir, OnVolatile;
	}

	static IMyHttpHandler createMyHttpHandler(eHttpHandlerType type) {
		switch (type) {
		case OnDir:
			logger.debug("Create dir handler");
			return new MyHttpHandlerOnDirectory();
		case OnFile:
			logger.debug("Create File handler");
			return new MyHttpHandlerOnFile();
		case OnVolatile:
			logger.debug("Create volatile handler");
			return new MyHttpHandlerOnVolatile();
		default:
			return null;
		}
	}
}
