package sfa.nav.httpd;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.DefaultHandler;

public class UserHandler extends DefaultHandler {
	@Override
	public String getText() {
		return "UserA, UserB, UserC";
	}

	@Override
	public String getMimeType() {
		return NanoHTTPD.MIME_PLAINTEXT;
	}

	@Override
	public Response.IStatus getStatus() {
		return Response.Status.OK;
	}
}