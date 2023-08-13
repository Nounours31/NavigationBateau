package sfa.nav.httpd;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import sfa.nav.httpd.ws.infra.WSContratForWSResponse;

public class MyRequestInfo {
	private static final Logger logger = LoggerFactory.getLogger(MyRequestInfo.class);

	private byte[] content;
	private String mime;
	private int status;

	public MyRequestInfo() {
		content = new byte[] {};
		mime = "";
		status = 500;
	}

	public long length() {
		if (content == null)
			return 0;
		return content.length;
	}

	public byte[] getBytes() {
		return content;
	}

	public void mimeType(String mimeHtml) {
		mime = mimeHtml;
	}
	public String mimeType() {
		return mime;
	}

	public void status(int s) {
		status = s;
	}
	public int status() {
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

	public void setContent(JsonObject data) {
		this.setContent(new Gson().toJson(data));
		
	}

	public void setContent(WSContratForWSResponse wsResponse) {
		this.setContent(new Gson().toJson(wsResponse, WSContratForWSResponse.class));
	}


}
