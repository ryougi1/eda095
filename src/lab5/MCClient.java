package lab5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MCClient {
	private MulticastSocket ms;
	private DatagramPacket rdp;
	private InetAddress ia;
	private InetAddress serverAddress;

	public MCClient() {
		try {
			ms = new MulticastSocket();
			ms.setTimeToLive(1);
			ia = InetAddress.getByName("experiment.mcast.net");

			byte[] rdata = new byte[65507];
			rdp = new DatagramPacket(rdata, rdata.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void discover() {
		byte[] discoverMsg = "HELLO".getBytes();
		DatagramPacket dp = new DatagramPacket(discoverMsg, discoverMsg.length, ia, 4099);
		try {
			ms.send(dp);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			ms.receive(rdp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverAddress = rdp.getAddress();
		String address = new String(rdp.getData(), 0, rdp.getLength());
		System.out.println("Found server on: " + address);
	}

	public void getDateTime() {
		try {
			byte[] msg = "DATETIME".getBytes();
			DatagramPacket dp = new DatagramPacket(msg, msg.length, serverAddress, 4099);
			ms.send(dp);

			ms.receive(rdp);
			String dateTime = new String(rdp.getData(), 0, rdp.getLength());
			System.out.println(dateTime);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ms.close();
	}

	public static void main(String args[]) {
		MCClient c = new MCClient();
		c.discover();
		c.getDateTime();
	}
}
