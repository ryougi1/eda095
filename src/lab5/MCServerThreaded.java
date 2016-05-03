package lab5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MCServerThreaded extends Thread {
	private MulticastSocket ms;
	private DatagramPacket rdp;

	public MCServerThreaded() {
		try {
			ms = new MulticastSocket(4099);
			InetAddress ia = InetAddress.getByName("experiment.mcast.net");
			ms.joinGroup(ia);
			byte[] rdata = new byte[65507];
			rdp = new DatagramPacket(rdata, rdata.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		DateTimeFormatter formatter;
		String output;
		try {
			while (true) {
				ms.receive(rdp);
				String msg = new String(rdp.getData(), 0, rdp.getLength());
				switch (msg) {
				case "HELLO":
					output = InetAddress.getLocalHost().toString();
					respond(output, rdp);
					break;
				case "DATETIME":
					formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
					output = LocalDateTime.now().format(formatter);
					respond(output, rdp);
					break;
				default:
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void respond(String response, DatagramPacket target) {
		try {
			byte[] data = response.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, target.getAddress(), target.getPort());
			ms.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		for (int i = 0; i < 3; i++) {
			MCServerThreaded s = new MCServerThreaded();
			s.start();	
		}
	}

}
