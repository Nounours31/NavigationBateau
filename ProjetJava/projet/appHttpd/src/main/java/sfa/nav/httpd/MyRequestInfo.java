package sfa.nav.httpd;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.httpd.MyHttpResponse.Status;
import sfa.nav.httpd.MyHttpResponse.eMimeTypes;

public class MyRequestInfo {
	private static final Logger logger = LoggerFactory.getLogger(MyRequestInfo.class);

	private byte[] content;
	private eMimeTypes mime;
	private Status status;

	public MyRequestInfo() {
		content = new byte[] {};
		mime = eMimeTypes.aa;
		status = Status.INTERNAL_ERROR;
	}

	public long length() {
		if (content == null)
			return 0;
		return content.length;
	}

	public byte[] getBytes() {
		return content;
	}

	public void mimeType(eMimeTypes mimeHtml) {
		mime = mimeHtml;
	}
	public eMimeTypes mimeType() {
		return mime;
	}

	public void status(Status s) {
		status = s;
	}
	public Status status() {
		return status;
	}

	public void setContent(String s) {
		content = s.getBytes(StandardCharsets.UTF_8);
	}
	public void setContent(byte[] s) {
		content = s;
	}

	@Override
	public String toString() {
		return "MyRequestInfo [contentlength" + length() + ", mime=" + mimeType() + ", status=" + status() + "]";
	}


}
