package maven;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import javax.imageio.ImageIO;

public class Demos {
	public void serverUDP() throws Exception {
		try (DatagramSocket serverSocket = new DatagramSocket(9876)) {
			byte[] receiveData = new byte[1024];
			byte[] sendData  = new byte[1024];
			while (true) { 
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData());	
				InetAddress ipaddr = receivePacket.getAddress();
				int port = receivePacket.getPort();
				String capitalizedSentence = sentence.toUpperCase();
				sendData = capitalizedSentence.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipaddr, port);
				serverSocket.send(sendPacket);
			}
		}
	}
	
	public void clientUDP() throws Exception {
		InputStreamReader streamReader = new InputStreamReader(System.in);
		BufferedReader inFromUser = new BufferedReader(streamReader);
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress ipaddr = InetAddress.getByName("hostname");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		String sentence = inFromUser.readLine();
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipaddr, 9876);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String modifiedSentence = new String(receivePacket.getData());
		System.out.println("from server: " + modifiedSentence);
		clientSocket.close();
	}
	
	public void client() throws Exception {
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
	
	public void loader(URL url) throws Exception {
		InputStream inputStream = url.openStream();
		String file = url.getFile().substring(1);
		BufferedInputStream in = new BufferedInputStream(inputStream);
		FileOutputStream fos = new FileOutputStream(file);
		final int BUF_SZ = 1 << 12;
		byte[] buffer = new byte[BUF_SZ];
		while (true) {
			int count = in.read(buffer, 0, BUF_SZ);
			if (count == -1) break;
			fos.write(buffer, 0, count);
		}
		fos.close();
	}
	
	public void convolution(File file) throws Exception {
		BufferedImage input = ImageIO.read(file);
		float[] matrix = {
			1f/273, 4f/273, 7f/273,  4f/273, 1f/273,
			4f/273, 16f/273, 26f/273, 16f/273, 4f/273,
			7f/273, 26f/273, 41f/273, 26f/273, 7f/273,
			4f/273, 16f/273, 26f/273, 16f/273, 4f/273,
			1f/273, 4f/273, 7f/273, 4f/273, 1f/273
		};

		Kernel kernel = new Kernel(5, 5, matrix); // Gaussian kernel
		ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

		BufferedImage blurred = op.filter(input, null);
		ImageIO.write(blurred, "jpg", new File("output.jpg"));
	}
	
	
}
