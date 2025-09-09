package maven;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	private int port;

	public Server(int port) {
		this.port = port;
	}

	public void start() {
		ExecutorService executorService = Executors.newFixedThreadPool(16);
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				System.out.printf("Listening at port %d.\n", 6789);
				Socket clientSocket = serverSocket.accept();
				executorService.execute(() -> handle(clientSocket));
			}
		} catch (Exception e) {
			System.out.printf("ERROR: %s.\n", e.getMessage());
			System.exit(0);
		}
	}
	
	public void handle(Socket socket) {
		try {
			InputStream inputStream = socket.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			OutputStream outputStream = socket.getOutputStream();
			DataOutputStream writer = new DataOutputStream(outputStream);
			String line = reader.readLine();
			writer.write(line.toUpperCase().getBytes());
		} catch (Exception e) {
			System.out.printf("ERROR: %s\n", e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(6789);
		server.start();
	}
}