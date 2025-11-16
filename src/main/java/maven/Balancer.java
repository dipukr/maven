package maven;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Balancer {

	public static final int ROUND_ROBIN = 1;
	public static final int WT_ROUND_ROBIN = 2;
	public static final int IP_HASH = 3;
	public static final int LEAST_CONN = 4;
	public static final int WT_LEAST_CONN = 5;
	public static final int LEAST_RESP_TIME = 6;
	public static final int RESOURCE = 7;
	
	record Node(String host, int port) {}

	private FileLogger logger = FileLogger.getLogger("/tmp/load_balancer.log");
	private ExecutorService pool = Executors.newFixedThreadPool(20);
	private List<Node> backends = new CopyOnWriteArrayList<>();
	private AtomicInteger counter = new AtomicInteger(0);
	private ServerSocket serverSocket;
	private boolean running = false;
	private int port;

	public Balancer(int port) {
		this.port = port;
	}

	public void start() throws IOException {
		if (backends.isEmpty())
			throw new IllegalStateException("Configuration error.");
		this.serverSocket = new ServerSocket(port);
		this.running = true;
		logger.info("Load balancer started on port %d.", port);
		while (running) {
			Socket clientSocket = serverSocket.accept();
			pool.submit(() -> handleClient(clientSocket));
		}
	}

	public void stop() throws IOException {
		running = false;
		if (serverSocket != null)
			serverSocket.close();
		pool.shutdownNow();
		logger.info("Server stopped.");
	}

	public void addServer(Node server) {
		backends.add(server);
	}

	public void removeServer(Node server) {
		backends.remove(server);
	}

	public void handleClient(Socket client) {
		int index = counter.getAndIncrement() % backends.size();
		Node backend = backends.get(index);
		try (Socket backendSocket = new Socket(backend.host(), backend.port());
				InputStream clientIn = client.getInputStream();
				OutputStream clientOut = client.getOutputStream();
				InputStream backendIn = backendSocket.getInputStream();
				OutputStream backendOut = backendSocket.getOutputStream()) {
			Thread a = new Thread(() -> forward(clientIn, backendOut)); // Forward client request → backend
			Thread b = new Thread(() -> forward(backendIn, clientOut)); // Forward backend response → client
			a.start();
			b.start();
			a.join();
			b.join();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	public void forward(InputStream inStream, OutputStream outStram) {
		try {
			byte[] buffer = new byte[8192];
			int read = 0;
			while ((read = inStream.read(buffer)) != -1) {
				outStram.write(buffer, 0, read);
				outStram.flush();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
