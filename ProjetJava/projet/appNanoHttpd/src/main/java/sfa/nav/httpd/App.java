package sfa.nav.httpd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import fi.iki.elonen.NanoFileUpload;
import fi.iki.elonen.NanoHTTPD;

public class App extends RouterNanoHTTPD {
	public App() throws IOException {
		super(8080);
		addMappings();
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
	}


    @Override
    public void addMappings() {
        addRoute("/", IndexHandler.class); // curl 'http://localhost:8080' 
		addRoute("/users", UserHandler.class); // curl -X POST 'http://localhost:8080/users' 
		addRoute("/stores", StoreHandler.class); // curl 'http://localhost:8080/stores/123' 
    }

	@Override
	public Response serve(IHTTPSession session) {
		Response response = null;

		if (session.getMethod() == Method.GET) {
			String itemIdRequestParameter = session.getParameters().get("itemId").get(0);
			response = newFixedLengthResponse("Requested itemId = " + itemIdRequestParameter);
		} else if (session.getMethod() == Method.POST) {
			try {
				session.parseBody(new HashMap<>());
				String requestBody = session.getQueryParameterString();
				response = newFixedLengthResponse("Request body = " + requestBody);
			} catch (IOException | ResponseException e) {
				// handle
			}
		} else if (session.getMethod() == Method.PUT) {
			try {
				List<FileItem> files = new NanoFileUpload(new DiskFileItemFactory()).parseRequest(session);
				int uploadedCount = 0;
				for (FileItem file : files) {
					try {
						String fileName = file.getName();
						byte[] fileContent = file.get();
						Files.write(Paths.get(fileName), fileContent);
						uploadedCount++;
					} catch (Exception exception) {
						// handle
					}
				}
				return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT,
						"Uploaded files " + uploadedCount + " out of " + files.size());
			} catch (FileUploadException e) {
				throw new IllegalArgumentException("Could not handle files from API request", e);
			}
		} else {
			response = newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
					"The requested resource does not exist");
		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		return response;

	}

	public static void main(String[] args) throws IOException {
		new App();
	}
}
