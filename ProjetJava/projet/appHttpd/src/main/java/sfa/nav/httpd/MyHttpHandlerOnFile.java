package sfa.nav.httpd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;

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
			retour.mimeType(MediaType.TEXT_PLAIN);
			retour.status(Response.Status.FORBIDDEN.getStatusCode());
		} else {
			File f = new File(getRoot(), uri);

			if (f.exists() && f.canRead()) {
				retour.setContent(readSource(f));
				retour.mimeType(getMediaTypeFromFile(f));
				retour.status(Response.Status.OK.getStatusCode());
			} else {
				retour.setContent(
						String.format("KO cannot be read or not exist : Exist? %b Read? %b", f.exists(), f.canRead()));
				retour.mimeType(MediaType.TEXT_PLAIN);
				retour.status(Response.Status.NOT_FOUND.getStatusCode());
			}
		}
		return retour;
	}

	private String getMediaTypeFromFile(File f) {
		String retour = MediaType.TEXT_PLAIN; 
		try {
			Tika tika = new Tika(); 
			System.out.println(tika.detect(f));
			return tika.detect(f);
		}
		catch (Exception e) {
			logger.error("KO decoidage type fichier ", e);
		}
		return null;
	}
}
