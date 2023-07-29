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
			return new MyHttpHandlerOnDirectory();
		case OnFile:
			return new MyHttpHandlerOnFile();
		case OnVolatile:
			return new MyHttpHandlerOnVolatile();
		default:
			return null;
		}
	}
}
