package lab3;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread implements Runnable {
	private Socket socket;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		InputStream is = null;
		try {
			is = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		byte[] buffer = new byte[1024 * 16];
		while (true) {
			int read = 0;
			try {
				read = is.read(buffer);
				Server.broadcast(buffer, read);
			} catch (SocketException se) {
				System.out.println(socket.getInetAddress().getHostName() + " has disconnected.");	
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

		}
	}

}
