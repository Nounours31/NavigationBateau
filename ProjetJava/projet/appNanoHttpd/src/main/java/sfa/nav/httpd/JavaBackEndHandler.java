package sfa.nav.httpd;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD.GeneralHandler;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;

public class JavaBackEndHandler extends GeneralHandler {
	@Override
	public Response get(
			// curl 'http://localhost:8080/stores/123'
			UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
		return RouterNanoHTTPD.newFixedLengthResponse("Retrieving store for id = " + urlParams.get("storeId"));
	}
}