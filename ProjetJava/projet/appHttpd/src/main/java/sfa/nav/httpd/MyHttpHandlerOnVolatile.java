package sfa.nav.httpd;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;

import sfa.nav.httpd.MyHttpResponse.Status;

public class MyHttpHandlerOnVolatile extends AMyHttpHandler {

	/**
	 * logger to log to.
	 */
	private static final Logger LOG = Logger.getLogger(MyHttpHandlerOnVolatile.class.getName());

	public MyHttpHandlerOnVolatile() {
		super();
	}

	public boolean canServeUri(String uri, File rootDir) {
		File f = new File(rootDir, uri);
		return f.exists();
	}

	public void initialize(Map<String, String> commandLineOptions) {
	}

	private String readSource(File file) {
		FileReader fileReader = null;
		BufferedReader reader = null;
		try {
			fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
			String line = null;
			StringBuilder sb = new StringBuilder();
			do {
				line = reader.readLine();
				if (line != null) {
					sb.append(line).append("\n");
				}
			} while (line != null);
			reader.close();
			return sb.toString();
		} catch (Exception e) {
			MyHttpHandlerOnVolatile.LOG.log(Level.SEVERE, "could not read source", e);
			return null;
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ignored) {
				MyHttpHandlerOnVolatile.LOG.log(Level.FINEST, "close failed", ignored);
			}
		}
	}

	public MyHttpResponse serveFile(String uri, Map<String, String> headers, File file, String mimeType) {

		String markdownSource = readSource(file);
		byte[] bytes = null;
		try {
			bytes = markdownSource.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			markdownSource = null;
		}
		if ((markdownSource == null) || (bytes == null))
			return null;
		return MyHttpResponse.newFixedLengthResponse(Status.OK, MyHttpResponse.MIME_HTML,
				new ByteArrayInputStream(bytes), bytes.length);

	}

	@Override
	public MyRequestInfo computeResponse(HttpExchange httpExchange, Map<String, String> requestParamValue,
			Map<String, List<String>> requestParamHeaders) throws IOException {
		return null;
	}

}
