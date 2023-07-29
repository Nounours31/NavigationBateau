package sfa.nav.httpd;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public interface IMyHttpHandler extends HttpHandler {
	MyRequestInfo computeResponse(HttpExchange httpExchange, Map<String, String> requestParamValue,
			Map<String, List<String>> requestParamHeaders) throws IOException;

	void handle(HttpExchange httpExchange) throws IOException;
}
