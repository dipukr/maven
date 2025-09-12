package maven;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

	private String host;
	private int port;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public String connectSend(String message) {
		String response = null;
		try {
			Socket serverSocket = new Socket(host, port);
			InputStream inputStream = serverSocket.getInputStream();
			OutputStream outputStream = serverSocket.getOutputStream();
			InputStreamReader streamReader = new InputStreamReader(inputStream);
			BufferedReader fromServer = new BufferedReader(streamReader);
			DataOutputStream toServer = new DataOutputStream(outputStream);
			toServer.writeBytes(message + '\n');
			response = fromServer.readLine();
			serverSocket.close();
		} catch (Exception e) {
			System.out.printf("ERROR: %s.\n", e.getMessage());
		}
		return response;
	}
} 
