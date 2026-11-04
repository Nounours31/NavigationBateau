package sfa.nav.httpd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpServer;

public class App {
	final private static Logger logger = LoggerFactory.getLogger(App.class);

	public App(String rootPath) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
		server.createContext("/", new MyHttpHandler(rootPath));

		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		server.setExecutor(threadPoolExecutor);

		server.start();
		logger.info(" Server started on port 8001");
	}

	public static void main(String[] args) throws IOException {
		String root = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-root"))
				root = args[++i];
		} 
		if (root == null) {
			logger.error("Il faut le root du site internet ...");
			return;
		}
		new App(root);
	}
}
