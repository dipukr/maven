package maven;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient { 
	public static void main(String[] args) throws Exception {
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
}