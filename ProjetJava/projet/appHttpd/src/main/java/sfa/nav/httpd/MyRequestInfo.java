package sfa.nav.httpd;

import java.io.UnsupportedEncodingException;

import sfa.nav.httpd.MyHttpResponse.Status;

public class MyRequestInfo {
	private String content;
	private String mime;
	private Status status;

	public MyRequestInfo() {
		content = "";
		mime = MyHttpResponse.MIME_PLAINTEXT;
		status = Status.INTERNAL_ERROR;
	}

	public long length() {
		try {
			return content.getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public byte[] getBytes() {
		try {
			return content.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new byte[] {};
		}
	}

	public void setMimeType(String mimeHtml) {
		mime = mimeHtml;
	}

	public void setStatus(Status s) {
		status = s;
	}

	public void setContent(String s) {
		content = s;
	}

}
