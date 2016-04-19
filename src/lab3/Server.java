package lab3;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private ServerSocket server;
	private static ArrayList<Socket> clients;
	private ExecutorService es;
	
	public Server(int port) throws IOException {
		server = new ServerSocket(port);
		clients = new ArrayList<Socket>();
	}
	
	public synchronized boolean addConnection(Socket s ) {
		if(clients.contains(s)) {
			return false;
		}
		return clients.add(s);
	}
	
	public static synchronized void broadcast(byte[] buff, int len) {
		for(Socket s: clients) {
			try {
				OutputStream os = s.getOutputStream();
				os.write(buff, 0, len);
				os.flush();
			} catch (IOException e) {
				clients.remove(s);
				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		Socket socket;
		es = Executors.newFixedThreadPool(10);
		try {
			while((socket = server.accept()) != null) {
				addConnection(socket);
				es.submit(new ServerThread(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
