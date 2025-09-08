package maven;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClients {
	public static void test(long millis) throws Exception {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:80"))
				.build();
		int counter = 0;
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < millis) {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String body = response.body();
			counter += 1;
		}
		System.out.printf("Served %d requests in %d millis.\n", counter, millis);
	}
	
	public static void test(int requestCount) throws Exception {
		HttpClient client = HttpClient.newHttpClient();
		long start = System.currentTimeMillis();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:80"))
				.build();
		
		for (int i = 0; i < requestCount; i++) {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String body = response.body();
		}
		long end = System.currentTimeMillis();
		System.out.printf("Served %d requests in %d millis.\n", requestCount, end - start);
	}
	
	public static void main(String[] args) throws Exception {
		test(10_000L);
	}
}
