package sfa.nav.httpd;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.WebServerPlugin;
import fi.iki.elonen.router.RouterNanoHTTPD.GeneralHandler;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;

public class NavUIHandler extends GeneralHandler {

	private static File homeDir = new File("E:\\WorkSpaces\\WS\\GitHub\\Navigation\\1.0.0\\Eclipse\\ProjetUI\\V1.0.0");

	protected Response getForbiddenResponse(String s) {
		return NanoHTTPD.newFixedLengthResponse(Response.Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: " + s);
	}

	protected Response getInternalErrorResponse(String s) {
		return NanoHTTPD.newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT,
				"INTERNAL ERROR: " + s);
	}

	protected Response getNotFoundResponse() {
		return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT,
				"Error 404, file not found.");
	}

	private String encodeUri(String uri) {
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

	protected String listDirectory(String uri, File f) {
		String heading = "Directory " + uri;
		StringBuilder msg = new StringBuilder("<html><head><title>" + heading + "</title><style><!--\n"
				+ "span.dirname { font-weight: bold; }\n" + "span.filesize { font-size: 75%; }\n" + "// -->\n"
				+ "</style>" + "</head><body><h1>" + heading + "</h1>");

		String up = null;
		if (uri.length() > 1) {
			String u = uri.substring(0, uri.length() - 1);
			int slash = u.lastIndexOf('/');
			if (slash >= 0 && slash < u.length()) {
				up = uri.substring(0, slash + 1);
			}
		}

		List<String> files = Arrays.asList(f.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir, name).isFile();
			}
		}));

		Collections.sort(files);
		List<String> directories = Arrays.asList(f.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir, name).isDirectory();
			}
		}));

		Collections.sort(directories);
		if (up != null || directories.size() + files.size() > 0) {
			msg.append("<ul>");
			if (up != null || directories.size() > 0) {
				msg.append("<section class=\"directories\">");
				if (up != null) {
					msg.append("<li><a rel=\"directory\" href=\"").append(up)
							.append("\"><span class=\"dirname\">..</span></a></b></li>");
				}
				for (String directory : directories) {
					String dir = directory + "/";
					msg.append("<li><a rel=\"directory\" href=\"").append(encodeUri(uri + dir))
							.append("\"><span class=\"dirname\">").append(dir).append("</span></a></b></li>");
				}
				msg.append("</section>");
			}
			if (files.size() > 0) {
				msg.append("<section class=\"files\">");
				for (String file : files) {
					msg.append("<li><a href=\"").append(encodeUri(uri + file)).append("\"><span class=\"filename\">")
							.append(file).append("</span></a>");
					File curFile = new File(f, file);
					long len = curFile.length();
					msg.append("&nbsp;<span class=\"filesize\">(");
					if (len < 1024) {
						msg.append(len).append(" bytes");
					} else if (len < 1024 * 1024) {
						msg.append(len / 1024).append(".").append(len % 1024 / 10 % 100).append(" KB");
					} else {
						msg.append(len / (1024 * 1024)).append(".").append(len % (1024 * 1024) / 10000 % 100)
								.append(" MB");
					}
					msg.append(")</span></li>");
				}
				msg.append("</section>");
			}
			msg.append("</ul>");
		}
		msg.append("</body></html>");
		return msg.toString();
	}

	@Override
	public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
		Map<String, String> headers = session.getHeaders();
		String uri = uriResource.getUri();

		// Remove URL arguments
		uri = uri.trim().replace(File.separatorChar, '/');
		if (uri.indexOf('?') >= 0) {
			uri = uri.substring(0, uri.indexOf('?'));
		}

		// Prohibit getting out of current directory
		if (uri.contains("../")) {
			return getForbiddenResponse("Won't serve ../ for security reasons.");
		}

		// Browsers get confused without '/' after the directory, send a
		// redirect.
		File f = new File(homeDir, uri);

		if (f.isDirectory()) {
			// First look for index files (index.html, index.htm, etc) and if
			// none found, list the directory if readable.
			String indexFile = null;
			if (indexFile == null) {
				if (f.canRead()) {
					// No index file, list the directory if it is readable
					return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML,
							listDirectory(uri, f));
				} else {
					return getForbiddenResponse("No directory listing.");
				}
			}
		}

		String mimeTypeForFile = getMimeType();
		WebServerPlugin plugin = new MarkdownWebServerPlugin();
		Response response = null;
		if (plugin != null && plugin.canServeUri(uri, homeDir)) {
			response = plugin.serveFile(uri, headers, session, f, mimeTypeForFile);
		}
		return response != null ? response : getNotFoundResponse();
	}

	@Override
	public String getMimeType() {
		return NanoHTTPD.MIME_HTML;
	}

	@Override
	public Response.IStatus getStatus() {
		return Response.Status.OK;
	}
}