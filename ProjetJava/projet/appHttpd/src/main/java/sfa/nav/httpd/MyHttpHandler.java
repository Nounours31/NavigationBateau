package sfa.nav.httpd;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;

import sfa.nav.httpd.MyHttpHandlerFactory.eHttpHandlerType;

//curl -X PUT http://localhost:8001/toto 
//curl -X GET http://localhost:8001/toto 
//curl -X POST http://localhost:8001/toto -H "Content-Type: application/json" -d "{\"login\":\"my_login\",\"password\":\"my_password\"}"
//curl -X POST http://localhost:8001/toto -H "Content-Type: application/json" -H "Accept: application/json"  -d "{\"login\":\"my_login\",\"password\":\"my_password\"}"

public class MyHttpHandler extends AMyHttpHandler implements IMyHttpHandler {
	final static private Logger logger = LoggerFactory.getLogger(MyHttpHandler.class);

	public MyHttpHandler(String rootServer) {
		super();
		setRoot(rootServer);
	}
	
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String fullURI = httpExchange.getRequestURI().toString(); // Ex: /toto?a=b
		logger.debug(fullURI);

		eHttpHandlerType whatToCreate = eHttpHandlerType.OnVolatile;
		File fileToServe = new File(this.getRoot(), this.getURLWithoutOpts(fullURI));
		if (fileToServe.exists() && fileToServe.isFile())
			whatToCreate = eHttpHandlerType.OnFile;

		if (fileToServe.exists() && fileToServe.isDirectory())
			whatToCreate = eHttpHandlerType.OnDir;

		IMyHttpHandler s = MyHttpHandlerFactory.createMyHttpHandler(whatToCreate);
		s.handle(httpExchange);
		return;
	}

	@Override
	public MyRequestInfo computeResponse(HttpExchange httpExchange, Map<String, String> requestParamValue,
			Map<String, List<String>> requestParamHeaders) throws IOException {
		return null;
	}
}
