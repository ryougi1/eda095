package lab5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientUDP {

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Syntax: java clientUDP <destination> <port> <message>");
			System.exit(1);
		}

		// Create a DatagramSocket on any free port
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Could not create socket!");
			System.exit(1);
		}

		// Create a DatagramPacket to send
		byte[] sdata = args[2].getBytes();
		InetAddress dest = null;
		int port = Integer.parseInt(args[1]);
		try {
			dest = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + args[0]);
			System.exit(1);
		}
		DatagramPacket sdp = new DatagramPacket(sdata, sdata.length, dest, port);

		// Send the datagram
		try {
			socket.send(sdp);
		} catch (IOException e) {
			System.out.println("An IOException occured: " + e);
			System.exit(1);
		}

		// Receive response, print message
		byte[] rdata = new byte[65507];
		DatagramPacket rdp = new DatagramPacket(rdata, rdata.length);
		try {
			socket.receive(rdp);
		} catch (IOException e) {
			System.out.println("An IOException occured: " + e);
			System.exit(1);
		}
		String message = new String(rdp.getData(), 0, rdp.getLength());
		System.out.println(message);
		socket.close();
	}
}
