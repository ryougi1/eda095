package lab5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeServerUDP {
	private DatagramSocket socket;
	private DatagramPacket dp;

	public TimeServerUDP(int port) {
		try {
			socket = new DatagramSocket(port);
			byte[] data = new byte[65507];
			dp = new DatagramPacket(data, data.length);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	//Listen on socket, forever. Process message.
	public void run() throws IOException {
		DateTimeFormatter formatter;
		String output;
		while (true) {
			socket.receive(dp);
			String command = new String(dp.getData(), 0, dp.getLength());
			switch (command) {
			case "DATE":
				formatter = DateTimeFormatter.ofPattern("d MMM uuuu", Locale.FRENCH);
				output = LocalDateTime.now().format(formatter);
				respond(output, dp);
				break;
			case "TIME":
				formatter = DateTimeFormatter.ofPattern("HH:mm");
				output = LocalDateTime.now().format(formatter);
				respond(output, dp);
				break;
			default:
				respond("INVALID INPUT", dp);
			}
				
		}
	}

	//Create response packet and send it
	private void respond(String response, DatagramPacket target) {
		try {
			byte[] data = response.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, target.getAddress(), target.getPort());
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TimeServerUDP ts = new TimeServerUDP(30000);
		try {
			ts.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
