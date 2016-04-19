package lab3;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientList {
	private ArrayList<Socket> connections;
	
	public ClientList() {
		connections = new ArrayList<Socket>();
	}

	public synchronized boolean addConnection(Socket s ) {
		if(connections.contains(s)) {
			return false;
		}
		return connections.add(s);
	}
	
	public synchronized void broadcast(byte[] buff, int len) {
		for(Socket s: connections) {
			try {
				OutputStream os = s.getOutputStream();
				os.write(buff, 0, len);
				os.flush();
			} catch (IOException e) {
				connections.remove(s);
				e.printStackTrace();
			}
		}
	}

}
