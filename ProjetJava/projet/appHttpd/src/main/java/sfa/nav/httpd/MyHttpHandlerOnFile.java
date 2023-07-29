package sfa.nav.httpd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.sun.net.httpserver.HttpExchange;

import sfa.nav.httpd.MyHttpResponse.eMimeTypes;

public class MyHttpHandlerOnFile extends AMyHttpHandler {
	final static private Logger logger = LoggerFactory.getLogger(MyHttpHandlerOnDirectory.class);

	public MyHttpHandlerOnFile() {
		super();
	}

	public void initialize(Map<String, String> commandLineOptions) {
	}

	private byte[] readSource(File file) {
		FileReader fileReader = null;
		BufferedReader reader = null;
		try {
			byte[] retour = Files.readAllBytes(file.toPath());
			return retour;
		} catch (IOException e) {
			logger.error("could not read source", e);
		} 
		return null;
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
			retour.mimeType(eMimeTypes.txt);
			retour.status(MyHttpResponse.Status.FORBIDDEN);
		} else {
			File f = new File(homeDir, uri);

			if (f.exists() && f.canRead()) {
				retour.setContent(readSource(f));
				retour.mimeType(eMimeTypes.getTypeFromFile(f));
				retour.status(MyHttpResponse.Status.OK);
			} else {
				retour.setContent(
						String.format("KO cannot be read or not exist : Exist? %b Read? %b", f.exists(), f.canRead()));
				retour.mimeType(eMimeTypes.txt);
				retour.status(MyHttpResponse.Status.NOT_FOUND);
			}
		}
		return retour;
	}
}
