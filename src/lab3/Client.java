package lab3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client{
	private Socket socket;
	private String username;
	private ClientGUI gui;
	private ObjectInputStream sinput;
	private ObjectOutputStream soutput;
	

	public Client(String username, ClientGUI gui) {
		this.username = username;
		this.gui = gui;
		socket = null;
	}
	
	public void setUp() {
		try {
			socket = new Socket("localhost", 2626);
			System.out.println("socket ok");
			System.out.println(socket.getInetAddress().getHostAddress() + ": " + socket.getPort());
		} catch (Exception e) {
			e.printStackTrace();
			gui.append("\n Error connecting to server");
		}
		try {
			sinput = new ObjectInputStream(socket.getInputStream());
			soutput = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("input output stream ok");
		} catch (IOException ioe) {
			gui.append("\n Error creating input/output streams");
			ioe.printStackTrace();
		}
		new ListenFromServer().start();
		try {
			soutput.writeObject(username);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void sendMessage(String msg) {
		try {
			soutput.writeObject(msg);
		} catch (IOException ioe) {
			gui.append("\n Error sending message");
			ioe.printStackTrace();
		}
	}
	
	class ListenFromServer extends Thread {
		 public void run() {
			while (true) {
				try {
					String msg = (String) sinput.readObject();
					gui.append(msg);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
}
