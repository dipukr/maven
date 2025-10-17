package maven;

import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AuthServer {
	
	private HttpServer server;
	private int port;
	
	public AuthServer(int port) {
		this.port = port;
	}
	
	public void start() {
		try {
			var socketAddress = new InetSocketAddress(port);
			server = HttpServer.create(socketAddress, 0);
		} catch (IOException e) {
			System.out.println("ERROR: " + e.getMessage());
			System.exit(0);
		}
		server.createContext("/register", new RegisterHandler());
		server.createContext("/login", new LoginHandler());
		server.setExecutor(null);
		server.start();
		System.out.println("Server started on port: " + port);
	}

	public class RegisterHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			if (!exchange.getRequestMethod().equals("POST")) {
				exchange.sendResponseHeaders(405, -1);
				return;
			}
			String body = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
			JSONObject json = new JSONObject(body);
			String username = json.getString("username");
			String password = json.getString("password");

			AuthService authService = AuthService.getInstance();
			boolean success = authService.register(username, password);

			String response = success ? "User registered successfully!\n" : "Registration failed!\n";
			exchange.sendResponseHeaders(success ? 200 : 400, response.getBytes().length);
			try (OutputStream outputStream = exchange.getResponseBody()) {
				outputStream.write(response.getBytes());
			}
		}
	}

	public class LoginHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
				exchange.sendResponseHeaders(405, -1);
				return;
			}
			String body = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
			JSONObject json = new JSONObject(body);
			String username = json.getString("username");
			String password = json.getString("password");

			AuthService authService = AuthService.getInstance();
			String token = authService.login(username, password);

			if (token != null) {
				exchange.getResponseHeaders().add("Set-Cookie", "jwt=" + token);
				String response = "Login successful!\n";
				exchange.sendResponseHeaders(200, response.getBytes().length);
				try (OutputStream os = exchange.getResponseBody()) {
					os.write(response.getBytes());
				}
			} else {
				String response = "Invalid username/password!\n";
				exchange.sendResponseHeaders(401, response.getBytes().length);
				try (OutputStream os = exchange.getResponseBody()) {
					os.write(response.getBytes());
				}
			}
		}
	}
}
