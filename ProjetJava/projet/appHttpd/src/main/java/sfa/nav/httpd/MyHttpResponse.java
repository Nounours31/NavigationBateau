package sfa.nav.httpd;

import java.io.ByteArrayInputStream;

public class MyHttpResponse {

	public static enum Status {
		FORBIDDEN, INTERNAL_ERROR, NOT_FOUND, OK
	}

	public static final String MIME_PLAINTEXT = null;
	public static final String MIME_HTML = null;

	public static MyHttpResponse newFixedLengthResponse(Status forbidden, String mimePlaintext, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static MyHttpResponse newFixedLengthResponse(Status ok, String mimeHtml, ByteArrayInputStream byteArrayInputStream,
			int length) {
		// TODO Auto-generated method stub
		return null;
	}

}
