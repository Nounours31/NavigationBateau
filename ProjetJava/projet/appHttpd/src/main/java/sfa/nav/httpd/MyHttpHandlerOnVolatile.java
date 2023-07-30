package sfa.nav.httpd;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;

import sfa.nav.httpd.MyHttpResponse.Status;
import sfa.nav.httpd.MyHttpResponse.eMimeTypes;
import sfa.nav.model.LatitudeFactory;
import sfa.nav.model.LongitudeFactory;
import sfa.nav.model.PointGeographique;
import sfa.nav.model.PointGeographiqueFactory;
import sfa.nav.model.tools.DataLoxodromieCapDistance;
import sfa.nav.nav.calculs.CalculsDeNavigation;

public class MyHttpHandlerOnVolatile extends AMyHttpHandler {
	private static final Logger logger = LoggerFactory.getLogger(MyHttpHandlerOnVolatile.class);

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
			logger.error("could not read source", e);
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
				logger.error("close failed", ignored);
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

	class lL {
		double latitude;
		double longitude;
	}
	class Pipo {
		lL depart;
		lL arrivee;
	}
	class Pipette {
		Pipo query;
	}

	class koko {
		Object data;
		String[] errors;
	}

	@Override
	public MyRequestInfo computeResponse(HttpExchange httpExchange, Map<String, String> requestParamValue,
			Map<String, List<String>> requestParamHeaders) throws IOException {
		
		MyRequestInfo ret = new MyRequestInfo();

		String uri = httpExchange.getRequestURI().toString();
		String post = httpExchange.getRequestMethod();
		if (uri.equals("/api/nav/ortho") && post.equals("POST")) {
			InputStream is = httpExchange.getRequestBody();
			byte[] message = is.readAllBytes();
			logger.debug("Api: {} - received: {}", uri, new String(message, StandardCharsets.UTF_8));
		
			Gson gson = new Gson();
			Pipette p = gson.fromJson(new String(message, StandardCharsets.UTF_8), Pipette.class);
			logger.debug("Pipette: {} - received: {}", p);
			
			CalculsDeNavigation cnv = new CalculsDeNavigation();
			PointGeographique pg1 = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(p.query.depart.latitude), LongitudeFactory.fromDegre(p.query.depart.longitude));
			PointGeographique pg2 = PointGeographiqueFactory.fromLatLong(LatitudeFactory.fromDegre(p.query.arrivee.latitude), LongitudeFactory.fromDegre(p.query.arrivee.longitude));
			DataLoxodromieCapDistance calculs = cnv.capLoxodromique(pg1, pg2);
			
			koko kk = new koko();
			kk.data = calculs;
			kk.errors = new String[] {};
			
			String s = gson.toJson(kk);
			logger.debug("retour: {}", s);
			
			ret.mimeType(eMimeTypes.json);
			ret.setContent(s);
			ret.status(Status.OK);
		}
		else {
			ret.mimeType(eMimeTypes.txt);
			ret.setContent("KO");
			ret.status(Status.INTERNAL_ERROR);
		}
		return ret;
	}

}
