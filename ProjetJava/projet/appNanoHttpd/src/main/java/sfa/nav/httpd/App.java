package sfa.nav.httpd;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

public class App extends RouterNanoHTTPD {
	public App() throws IOException {
		super(8080);
		addMappings();
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
	}

	@Override
	public void addMappings() {
		addRoute("/", NavUIHandler.class); // curl 'http://localhost:8080'
		addRoute("/index.html", NavUIHandler.class); // curl 'http://localhost:8080'
		addRoute("/users", UserHandler.class); // curl -X POST 'http://localhost:8080/users'
		addRoute("/stores", StoreHandler.class); // curl 'http://localhost:8080/stores/123'
	}

	public static void main(String[] args) throws IOException {
		new App();
	}
}
