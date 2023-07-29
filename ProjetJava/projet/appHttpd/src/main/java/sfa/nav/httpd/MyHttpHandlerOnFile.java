package sfa.nav.httpd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;

public class MyHttpHandlerOnFile extends AMyHttpHandler {

	/**
	 * logger to log to.
	 */
	private static final Logger LOG = Logger.getLogger(MyHttpHandlerOnFile.class.getName());

	public MyHttpHandlerOnFile() {
		super();
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
			MyHttpHandlerOnFile.LOG.log(Level.SEVERE, "could not read source", e);
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
				MyHttpHandlerOnFile.LOG.log(Level.FINEST, "close failed", ignored);
			}
		}
	}

	@Override
	public MyRequestInfo computeResponse(HttpExchange httpExchange, Map<String, String> requestParamValue,
			Map<String, List<String>> requestParamHeaders) throws IOException {

		MyRequestInfo retour = new MyRequestInfo();

		String uri = httpExchange.getRequestURI().getPath();

		// Remove URL arguments
		uri = uri.trim().replace(File.separatorChar, '/');
		if (uri.indexOf('?') >= 0) {
			uri = uri.substring(0, uri.indexOf('?'));
		}

		// Prohibit getting out of current directory
		if (uri.contains("../")) {
			retour.setContent("Won't serve ../ for security reasons.");
			retour.setMimeType(MyHttpResponse.MIME_PLAINTEXT);
			retour.setStatus(MyHttpResponse.Status.FORBIDDEN);
		} else {
			File f = new File(homeDir, uri);

			if (f.exists() && f.canRead()) {
				retour.setContent(readSource(f));
				retour.setMimeType(MyHttpResponse.MIME_HTML);
				retour.setStatus(MyHttpResponse.Status.OK);
			} else {
				retour.setContent(
						String.format("KO cannot be read or not exist : Exist? %b Read? %b", f.exists(), f.canRead()));
				retour.setMimeType(MyHttpResponse.MIME_PLAINTEXT);
				retour.setStatus(MyHttpResponse.Status.NOT_FOUND);
			}
		}
		return retour;
	}
}
