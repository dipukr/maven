package maven;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {
	public static void main(String[] args) throws Exception {
		var server = new Socket("127.0.0.1", 6789);
		var inputStream = server.getInputStream();
		var outputStream = server.getOutputStream();
		var streamReader = new InputStreamReader(inputStream);
		var fromServer = new BufferedReader(streamReader);
		var toServer = new DataOutputStream(outputStream);
		toServer.writeBytes("hello" + '\n');
		System.out.println(fromServer);
		server.close();
	}
	private String host;
	private int port;

	public TCPClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public String connectSend(String message) {
		try (var server = new Socket(host, port)) {
			var inputStream = server.getInputStream();
			var outputStream = server.getOutputStream();
			var streamReader = new InputStreamReader(inputStream);
			var fromServer = new BufferedReader(streamReader);
			var toServer = new DataOutputStream(outputStream);
			toServer.writeBytes(message + '\n');
			return fromServer.readLine();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return e.getMessage();
		}
	}
} 
