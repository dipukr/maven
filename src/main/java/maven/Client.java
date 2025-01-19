package maven;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class Client {
	public static void main(String argv[]) throws Exception {
		Socket socket = new Socket("172.16.4.134", 6789);
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 
		DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());	
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.print("Enter message: ");
		String message = inFromUser.readLine(); 
		outToServer.writeBytes(message + '\n'); 
		message = inFromServer.readLine(); 
		System.out.println("from server: " + message); 
		socket.close();
	} 
} 