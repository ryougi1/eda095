package lab3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private ServerSocket server;
	private ArrayList<ClientThread> clients;
	
	public Server(int port) {
		try {
			server = new ServerSocket(port);
			clients = new ArrayList<ClientThread>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized void broadcast(String msg) {
		for(ClientThread ct: clients) {
			ct.writeMessage(msg);
		}
	}
	
	public void start() {
		Socket socket;
		try {
			while((socket = server.accept()) != null) {
				ClientThread t = new ClientThread(socket);
				clients.add(t);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class ClientThread extends Thread {
		Socket socket;
		ObjectInputStream sinput;
		ObjectOutputStream soutput;
		String username;
		
		ClientThread(Socket socket) {
			this.socket = socket;
			try {
				sinput = new ObjectInputStream(socket.getInputStream());
				soutput = new ObjectOutputStream(socket.getOutputStream());
				username = (String) sinput.readObject();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (ClassNotFoundException e) {
			}
		}
		
		private void writeMessage(String msg) {
			try {
				soutput.writeObject(msg);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		public void run () {
			while (true) {
				try {
					String msg = (String) sinput.readObject();
					broadcast(username + ": " + msg);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main (String[] args) {
		Server s = new Server(2626);
		s.start();
	}

}
