package sfa.nav.httpd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpServer;

public class App {
	Logger logger = LoggerFactory.getLogger(App.class);

	public App() throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
		server.createContext("/", new MyHttpHandler());

		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		server.setExecutor(threadPoolExecutor);

		server.start();
		logger.info(" Server started on port 8001");
	}

	public static void main(String[] args) throws IOException {
		new App();
	}
}
