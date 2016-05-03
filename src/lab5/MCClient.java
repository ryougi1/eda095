package lab5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MCClient {
	private MulticastSocket ms;
	private DatagramSocket socket;
	private InetAddress serverAddress;
	private InetAddress ia;

	public MCClient() {
		try {
			ms = new MulticastSocket();
			ia = InetAddress.getByName("experiment.mcast.net");
			ms.joinGroup(ia);
			ms.setTimeToLive(5);
			socket = new DatagramSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void discover() {
		byte[] discoverMsg = "HELLO".getBytes();
		try {
			DatagramPacket dp = new DatagramPacket(discoverMsg, discoverMsg.length, ia, 4199);
			ms.send(dp);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		setUp();
	}

	public void setUp() {
		while (serverAddress == null) {
			try {
				//TODO flytta upp
				byte[] discoverResponse = new byte[65507];
				DatagramPacket rdp = new DatagramPacket(discoverResponse, discoverResponse.length);
				ms.receive(rdp);
				String responseMsg = new String(rdp.getData(), 0, rdp.getLength());
				serverAddress = InetAddress.getByName(responseMsg);
				String address = serverAddress.toString();
				System.out.println("Found server on: " + address);
				getDateTime();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void getDateTime() {
		while (true) {
			try {
				int ch;
				String s = new String();
				do {
					ch = System.in.read();
					if (ch != '\n') {
						s = s + (char) ch;
					}
				} while (ch != '\n');
				System.out.println("Sending message: " + s);
				byte[] msg = s.getBytes();
				DatagramPacket dp = new DatagramPacket(msg, msg.length, serverAddress, 30000);
				socket.send(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				byte[] data = new byte[65507];
				DatagramPacket rdp = new DatagramPacket(data, data.length);
				socket.receive(rdp);
				String response = new String(rdp.getData(), 0, rdp.getLength());
				System.out.println(response);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		MCClient c = new MCClient();
		c.discover();
	}
}
