package maven;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class WebServer {
	public static void main(String argv[]) throws Exception {
		ServerSocket listenSocket = new ServerSocket(6789);
		Socket connectionSocket = listenSocket.accept();
		InputStream inputStream = connectionSocket.getInputStream();
		OutputStream outputStream = connectionSocket.getOutputStream();
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(inputStream));
		DataOutputStream outToClient = new DataOutputStream(outputStream);
		String requestMessageLine = inFromClient.readLine();
		StringTokenizer tokenizer = new StringTokenizer(requestMessageLine);
		if (tokenizer.nextToken().equals("GET")) {
			String fileName = tokenizer.nextToken();
			if (fileName.startsWith("/") == true)
				fileName = fileName.substring(1);
			File file = new File(fileName);
			int numOfBytes = (int) file.length();
			FileInputStream inFile  = new FileInputStream(fileName);
			byte[] fileInBytes = new byte[numOfBytes];
			inFile.read(fileInBytes);
			outToClient.writeBytes("HTTP/1.0 200 Document Follows\r\n");
			if (fileName.endsWith(".jpg"))
				outToClient.writeBytes("Content-Type: image/jpeg\r\n");
			if (fileName.endsWith(".gif"))
				outToClient.writeBytes("Content-Type: image/gif\r\n");
			outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
			outToClient.writeBytes("\r\n");
			outToClient.write(fileInBytes, 0, numOfBytes);
			connectionSocket.close();
		} else System.out.println("Bad Request Message");
	}
}
