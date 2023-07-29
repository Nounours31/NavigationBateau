package sfa.nav.httpd;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;

import sfa.nav.httpd.MyHttpResponse.eMimeTypes;

public class MyHttpHandlerOnDirectory extends AMyHttpHandler {
	final static private Logger logger = LoggerFactory.getLogger(MyHttpHandlerOnDirectory.class);

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
					msg.append("<li><a rel=\"directory\" href=\"").append(this.encodeUri(uri + dir))
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

			if (f.isDirectory()) {
				String indexFile = null;
				if (indexFile == null) {
					if (f.canRead()) {
						retour.setContent(listDirectory(uri, f));
						retour.mimeType(eMimeTypes.html);
						retour.status(MyHttpResponse.Status.OK);
					} else {
						retour.setContent(String.format("Won't serve %s no read access", f.getAbsolutePath()));
						retour.mimeType(eMimeTypes.txt);
						retour.status(MyHttpResponse.Status.FORBIDDEN);
					}
				}
			}
		}
		return retour;
	}

}