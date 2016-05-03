package lab5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MCServerWithListener {
	private DatagramSocket socket;
	private DatagramPacket rdp;
	private MCServerOffer listener;

	public MCServerWithListener() {
		try {
			socket = new DatagramSocket(30000);
			listener = new MCServerOffer();
			listener.start();
			byte[] rdata = new byte[65507];
			rdp = new DatagramPacket(rdata, rdata.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		DateTimeFormatter formatter;
		String output;
		while (true) {
			try {
				socket.receive(rdp);
				String msg = new String(rdp.getData(), 0, rdp.getLength());
				switch (msg) {
				case "DATE":
					formatter = DateTimeFormatter.ofPattern("d MMM uuuu", Locale.FRENCH);
					output = LocalDateTime.now().format(formatter);
					respond(output, rdp);
					break;
				case "TIME":
					formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
					output = LocalDateTime.now().format(formatter);
					respond(output, rdp);
					break;
				default:

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void respond(String response, DatagramPacket target) {
		try {
			byte[] data = response.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, target.getAddress(), target.getPort());
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class MCServerOffer extends Thread {
		private MulticastSocket ms;
		private DatagramPacket mcrdp;

		public MCServerOffer() {
			try {
				ms = new MulticastSocket(4199);
				InetAddress ia = InetAddress.getByName("experiment.mcast.net");
				ms.joinGroup(ia);
				byte[] rdata = new byte[65507];
				mcrdp = new DatagramPacket(rdata, rdata.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (true) {
				try {
					ms.receive(mcrdp);
					String msg = new String(mcrdp.getData(), 0, mcrdp.getLength());
					if (msg.equals("HELLO")) {
						byte[] data = InetAddress.getLocalHost().getHostName().getBytes();
						DatagramPacket response = new DatagramPacket(data, data.length, mcrdp.getAddress(),
								mcrdp.getPort());
						ms.send(response);
					}
				} catch (IOException e) {
					e.printStackTrace();

				}
			}

		}
	}

	public static void main(String args[]) {

		MCServerWithListener s = new MCServerWithListener();
		s.run();

	}

}
