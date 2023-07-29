package sfa.nav.httpd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

//curl -X PUT http://localhost:8001/toto 
//curl -X GET http://localhost:8001/toto 
//curl -X POST http://localhost:8001/toto -H "Content-Type: application/json" -d "{\"login\":\"my_login\",\"password\":\"my_password\"}"
//curl -X POST http://localhost:8001/toto -H "Content-Type: application/json" -H "Accept: application/json"  -d "{\"login\":\"my_login\",\"password\":\"my_password\"}"

public abstract class AMyHttpHandler implements IMyHttpHandler {
	final static private Logger logger = LoggerFactory.getLogger(AMyHttpHandler.class);
	final static private String ERROR = "Erreur";

	protected static String homeDir = "E:\\WorkSpaces\\WS\\GitHub\\Navigation\\1.0.0\\Eclipse\\ProjetUI\\V1.0.0";

	static private enum eHeaderKeys {
		Accept("Accept"), Host("Host"), UserAgent("User-agent"), ContentType("Content-type"),
		ContentLength("Content-length"), Unknown("");

		private final String name;

		private eHeaderKeys(String s) {
			name = s;
		}

		public boolean is(String s) {
			return this.name.equals(s);
		}

		public eHeaderKeys parseFromString(String s) {
			for (eHeaderKeys i : eHeaderKeys.values()) {
				if (i.is(s))
					return i;
			}
			return Unknown;
		}
	}

	protected String getURLWithoutOpts(String fullURI) {
		if (fullURI == null)
			return "";

		if (fullURI.contains("?"))
			return fullURI.split("\\?")[0];

		return fullURI;
	}

	protected String getRootPath() {
		return homeDir;
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		Map<String, String> requestParamValue = null;
		requestParamValue = parseParams(httpExchange);

		Map<String, List<String>> requestHeaderValue = null;
		requestHeaderValue = parseHeaders(httpExchange);

		handleResponse(httpExchange, requestParamValue, requestHeaderValue);
	}

	protected Map<String, List<String>> parseHeaders(HttpExchange httpExchange) {
		Map<String, List<String>> requestHeaderValue = new HashMap<>();
		Headers headers = httpExchange.getRequestHeaders();
		for (String oneheader : headers.keySet()) {
			requestHeaderValue.put(oneheader, headers.get(oneheader));
		}
		return requestHeaderValue;
	}

	protected Map<String, String> parseParams(HttpExchange httpExchange) {
		Map<String, String> requestParamValue;
		if ("GET".equals(httpExchange.getRequestMethod())) {
			requestParamValue = parseParamForGetRequest(httpExchange);
		} else if ("POST".equals(httpExchange.getRequestMethod())) {
			requestParamValue = parseParamForPostRequest(httpExchange);
		} else {
			requestParamValue = new HashMap<>();
			requestParamValue.put(ERROR, String.format("RequestMethod non prevue %s", httpExchange.getRequestMethod()));
		}
		return requestParamValue;
	}

	private Map<String, String> parseParamForGetRequest(HttpExchange httpExchange) {
		String fullURI = httpExchange.getRequestURI().toString();
		HashMap<String, String> retour = new HashMap<>();

		if (fullURI.contains("?")) {
			String opt = fullURI.split("\\?")[1];

			String[] allOpts = null;
			if (opt.contains("&")) {
				allOpts = opt.split("&");
			} else {
				allOpts = new String[] { opt };
			}

			for (String s : allOpts) {
				if (s.contains("=")) {
					String[] keyval = s.split("=");
					retour.put(keyval[0], keyval[1]);
				} else
					retour.put(s, "");
			}
		}
		retour.put("Methode", "GET");
		return retour;
	}

	private Map<String, String> parseParamForPostRequest(HttpExchange httpExchange) {
		HashMap<String, String> retour = new HashMap<>();
		Headers headers = httpExchange.getRequestHeaders();
		boolean isJson = false;
		for (String key : headers.keySet()) {
			if (eHeaderKeys.ContentType.is(key)) {
				List<String> ContentType = headers.get(key);
				if (!ContentType.equals("application/json")) {
					retour.put(ERROR, String.format("ContentType non prevue %s", ContentType));
					break;
				} else {
					isJson = true;
				}
			}
		}

		if (!isJson)
			retour.put(ERROR, "No ContentType ");
		else {
			InputStream is = httpExchange.getRequestBody();
			try {
				retour.put("json", new String(is.readAllBytes(), Charset.forName("UTF8")));
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			retour.put("Methode", "POST");
		}
		return retour;
	}

	private void handleResponse(HttpExchange httpExchange, Map<String, String> requestParamValue,
			Map<String, List<String>> requestParamHeaders) throws IOException {
		OutputStream outputStream = httpExchange.getResponseBody();

		// overloaded
		MyRequestInfo requestInfo = this.computeResponse(httpExchange, requestParamValue, requestParamHeaders);

		// this line is a must
		httpExchange.sendResponseHeaders(200, requestInfo.length());
		outputStream.write(requestInfo.getBytes());
		outputStream.flush();
		outputStream.close();
	}

	private String getBody(HttpExchange httpExchange, Map<String, String> requestParamValue) {
		Headers headers = httpExchange.getRequestHeaders();

		boolean isJson = false;
		for (String key : headers.keySet()) {
			if (eHeaderKeys.Accept.is(key)) {
				List<String> Accept = headers.get(key);
				if (!Accept.equals("application/json")) {
					requestParamValue.put(ERROR, String.format("Accept non prevue %s", Accept));
					break;
				} else {
					isJson = true;
				}
			}
		}

		if (requestParamValue.containsKey(ERROR))
			return errorMessage(requestParamValue, isJson);

		return okMessage(requestParamValue, isJson);
	}

	private String okMessage(Map<String, String> requestParamValue, boolean isJson) {
		if (isJson) {
			Gson gson = new Gson();
			return gson.toJson(requestParamValue);
		} else {
			StringBuilder htmlBuilder = new StringBuilder();
			htmlBuilder.append("<html>").append("<body>").append("<h1>").append("Hello ").append(requestParamValue)
					.append("</h1>").append("</body>").append("</html>");

			return htmlBuilder.toString();
		}
	}

	private String errorMessage(Map<String, String> requestParamValue, boolean isJson) {
		if (isJson) {
			Gson gson = new Gson();
			return gson.toJson(requestParamValue);
		} else {
			StringBuilder htmlBuilder = new StringBuilder();
			htmlBuilder.append("<html>").append("<body>").append("<h1>").append("Error ").append(requestParamValue)
					.append("</h1>").append("</body>").append("</html>");

			return htmlBuilder.toString();
		}
	}

	protected MyHttpResponse getForbiddenResponse(String s) {
		return MyHttpResponse.newFixedLengthResponse(MyHttpResponse.Status.FORBIDDEN, MyHttpResponse.MIME_PLAINTEXT,
				"FORBIDDEN: " + s);
	}

	protected MyHttpResponse getInternalErrorResponse(String s) {
		return MyHttpResponse.newFixedLengthResponse(MyHttpResponse.Status.INTERNAL_ERROR,
				MyHttpResponse.MIME_PLAINTEXT, "INTERNAL ERROR: " + s);
	}

	protected MyHttpResponse getNotFoundResponse() {
		return MyHttpResponse.newFixedLengthResponse(MyHttpResponse.Status.NOT_FOUND, MyHttpResponse.MIME_PLAINTEXT,
				"Error 404, file not found.");
	}

	protected String encodeUri(String uri) {
		String newUri = "";
		StringTokenizer st = new StringTokenizer(uri, "/ ", true);
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			if ("/".equals(tok)) {
				newUri += "/";
			} else if (" ".equals(tok)) {
				newUri += "%20";
			} else {
				try {
					newUri += URLEncoder.encode(tok, "UTF-8");
				} catch (UnsupportedEncodingException ignored) {
				}
			}
		}
		return newUri;
	}
}
