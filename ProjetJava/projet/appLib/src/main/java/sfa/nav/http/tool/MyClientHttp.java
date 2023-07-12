package sfa.nav.http.tool;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyClientHttp {

	public HttpClient getClient() {
		HttpClient client = HttpClient.newBuilder()
				.version(Version.HTTP_2)
				.followRedirects(Redirect.NORMAL)
				.build();
		return client;
	}

	public HttpRequest getRequest(URI uri, Map<String, String> headers) throws URISyntaxException {
		Builder httpRequestBuilder = HttpRequest.newBuilder()
				.uri(uri)
				.GET()
				.timeout(Duration.ofSeconds(10));
		
		for (String oneKey : headers.keySet()) {
			httpRequestBuilder = httpRequestBuilder.header(oneKey, headers.get(oneKey));			
		}
		HttpRequest request = httpRequestBuilder.build();
		return request;
	}


	// "https://reqbin.com/echo/post/json"
	public String Get(String url, Map<String, String> headers) {
		try {
			HttpClient client = getClient();
			Builder httpRequestBuilder =  HttpRequest.newBuilder()
					.uri(URI.create(url))
					.GET()
					.timeout(Duration.ofSeconds(10));
			
			if (!headers.containsKey("Accept"))
				headers.put("Accept", "application/json");
			
			for (String oneKey : headers.keySet()) {
				httpRequestBuilder = httpRequestBuilder.header(oneKey, headers.get(oneKey));			
			}
			
			HttpRequest request = httpRequestBuilder.build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			System.out.println(response.body());
			return response.body();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void Post(String url, Map<String, String> headers) {

		try {
			String requestBody = prepareRequest();
			HttpClient client = getClient();
			Builder httpRequestBuilder =  HttpRequest.newBuilder()
					.uri(URI.create(url))
					.POST(HttpRequest.BodyPublishers.ofString(requestBody))
					.timeout(Duration.ofSeconds(10));
			
			headers.put("Accept", "application/json");
			for (String oneKey : headers.keySet()) {
				httpRequestBuilder = httpRequestBuilder.header(oneKey, headers.get(oneKey));			
			}
			
			HttpRequest request = httpRequestBuilder.build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			System.out.println(response.body());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String prepareRequest() {
		final GsonBuilder builder = new GsonBuilder();
	    final Gson gson = builder.create();
	    
	    HashMap<String, String> values = new HashMap<>() {
			{
				put("Id", "12345");
				put("Customer", "Roger Moose");
				put("Quantity", "3");
				put("Price","167.35");
			}
		};

		String requestBody = gson.toJson(values);
		return requestBody;
	}
}
