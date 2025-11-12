package maven;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPServer {

	private int port = 8780;
	private boolean running = false;
	private ServerSocket serverSocket;
	private Router router = new Router();
	private ExecutorService pool = Executors.newFixedThreadPool(20);
	private FileLogger logger = FileLogger.getLogger("/tmp/http_server.log");
	
	public HTTPServer(int port) {
		this.port = port;
	}

	public void register(String method, String path, Handler handler) {
		router.register(method, path, handler);
	}

	public void start() throws IOException {
		serverSocket = new ServerSocket(port);
		running = true;
		logger.info("HTTP server started on port: " + port);
		while (running) {
			try {
				Socket clientSocket = serverSocket.accept();
				pool.submit(() -> handle(clientSocket));
			} catch (IOException ex) {
				if (running)
					logger.error(ex.getMessage());
			}
		}
	}

	public void stop() throws IOException {
		running = false;
		if (serverSocket != null)
			serverSocket.close();
		pool.shutdownNow();
		logger.info("Server stopped.");
	}

	public void handle(Socket client) {
		try (InputStream in = client.getInputStream();
				OutputStream out = client.getOutputStream();
				var streamReader = new InputStreamReader(in, UTF_8);
				var streamWriter = new OutputStreamWriter(out, UTF_8);
				BufferedReader reader = new BufferedReader(streamReader);
				BufferedWriter writer = new BufferedWriter(streamWriter)) {
			String requestLine = reader.readLine();
			if (requestLine == null || requestLine.isEmpty()) return;
			String[] parts = requestLine.split(" ");
			if (parts.length < 3) {
				sendBadResponse(writer, 400, "Bad Request", "Malformed request line");
				return;
			}
			String method = parts[0];
			String fullUrl = parts[1];
			Map<String, String> headers = new HashMap<>();
			String line;
			while ((line = reader.readLine()) != null && !line.isEmpty()) {
				int idx = line.indexOf(':');
				if (idx >= 0) {
					String k = line.substring(0, idx).trim().toLowerCase(Locale.ROOT);
					String v = line.substring(idx + 1).trim();
					headers.put(k, v);
				}
			}
			int contentLength = 0;
			if (headers.containsKey("content-length")) {
				try {
					contentLength = Integer.parseInt(headers.get("content-length"));
				} catch (NumberFormatException ex) {
					logger.error(ex.getMessage());
				}
			}
			char[] bodyChars = new char[contentLength];
			String body = "";
			if (contentLength > 0) {
				int read = 0;
				while (read < contentLength) {
					int r = reader.read(bodyChars, read, contentLength - read);
					if (r == -1) break;
					read += r;
				}
				body = new String(bodyChars, 0, Math.max(0, Math.min(read, contentLength)));
			}
			String pathOnly = fullUrl;
			String query = "";
			int idx = fullUrl.indexOf('?');
			if (idx >= 0) {
				pathOnly = fullUrl.substring(0, idx);
				query = fullUrl.substring(idx + 1);
			}
			Map<String, String> queryParams = parseQueryParams(query);
			Request request = Request.builder()
					.method(method)
					.url(fullUrl)
					.headers(headers)
					.queryParams(queryParams)
					.body(body)
					.build();

			Optional<Handler> handlerOpt = router.find(method, pathOnly);
			Response response;
			if (handlerOpt.isEmpty()) {
				response = Response.builder()
								.statusCode(404)
								.reason("Not Found")
								.header("Content-Type", "text/plain; charset=utf-8")
								.body("No handler for " + method + " " + pathOnly)
								.build();
			} else {
				try {
					response = handlerOpt.get().handle(request);
				} catch (Exception ex) {
					ex.printStackTrace();
					response = Response.builder()
									.statusCode(500)
									.reason("Internal Server Error")
									.header("Content-Type", "text/plain; charset=utf-8")
									.body("Handler error: " + ex.getMessage())
									.build();
				}
			}
			if (!response.headers().containsKey("Content-Length")) {
				response.headers().put("Content-Length",
						String.valueOf(response.body() == null ? 0 : 
							response.body().getBytes(UTF_8).length));
			}
			if (!response.headers().containsKey("Connection"))
				response.headers().put("Connection", "close");
			writer.write("HTTP/1.1 " + response.statusCode() + " " + response.reason() + "\r\n");
			for (var header: response.headers().entrySet())
				writer.write(header.getKey() + ": " + header.getValue() + "\r\n");
			writer.write("\r\n");
			if (response.body() != null && !response.body().isEmpty())
				writer.write(response.body());
			writer.flush();
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
	}

	public void sendBadResponse(BufferedWriter writer, int statusCode, String reason, String body) {
		try {
			writer.write("HTTP/1.1 " + statusCode + " " + reason + "\r\n");
			writer.write("Content-Type: text/plain; charset=utf-8\r\n");
	        writer.write("Content-Length: " + body.getBytes(UTF_8).length + "\r\n");
	        writer.write("Connection: close\r\n");
	        writer.write("\r\n");
	        writer.write(body);
	        writer.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public Map<String, String> parseQueryParams(String query) {
		var queryParams = new HashMap<String, String>();
		if (query == null || query.isEmpty())
			return queryParams;
		String[] pairs = query.split("&");
		for (String pair: pairs) {
			int idx = pair.indexOf('=');
			try {
				if (idx >= 0) {
					String k = URLDecoder.decode(pair.substring(0, idx), UTF_8);
					String v = URLDecoder.decode(pair.substring(idx + 1), UTF_8);
					queryParams.put(k, v);
				} else queryParams.put(URLDecoder.decode(pair, UTF_8), "");
			} catch (IllegalArgumentException e) {
				logger.error(e.getMessage());
			}
		}
		return queryParams;
	}
}
