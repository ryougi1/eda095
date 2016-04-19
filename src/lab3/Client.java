package lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Client {
	private Socket socket;
//	static TextArea ta;

//	public void start(Stage primaryStage) throws Exception {
//		primaryStage.setTitle("Chatt");
//
//		// Main scene
//		GridPane grid2 = new GridPane();
//		grid2.setPadding(new Insets(10, 10, 10, 10));
//		grid2.setVgap(8);
//		grid2.setHgap(10);
//
//		Label chatLabel = new Label("Chatt: ");
//		GridPane.setConstraints(chatLabel, 0, 0);
//		ta = new TextArea();
//		GridPane.setConstraints(ta, 0, 1);
//		ta.setPrefSize(400, 400);
//
//		Label messageLabel = new Label("Enter message: ");
//		GridPane.setConstraints(messageLabel, 0, 3);
//		TextArea messageInput = new TextArea();
//		GridPane.setConstraints(messageInput, 0, 4);
//		messageInput.setPrefSize(400, 75);
//
//		Button sendButton = new Button("Send");
//		GridPane.setConstraints(sendButton, 1, 4);
//		sendButton.setOnAction(e -> {
////			chattTextArea.appendText("\n" + messageInput.getText());
//			messageInput.clear();
//			
//		});
//		grid2.getChildren().addAll(chatLabel, ta, messageLabel, messageInput, sendButton);
//		Scene mainScene = new Scene(grid2, 500, 400);
//
//		//Login scene
//		GridPane grid = new GridPane();
//		grid.setPadding(new Insets(10, 10, 10, 10));
//		grid.setVgap(8);
//		grid.setHgap(10);
//
//		Label nameLabel = new Label("Username: ");
//		GridPane.setConstraints(nameLabel, 0, 0);
//		TextField nameInput = new TextField("ryougi");
//		GridPane.setConstraints(nameInput, 1, 0);	
//		Button goButton = new Button("Go ");
//		GridPane.setConstraints(goButton, 3, 0);
//		goButton.setOnAction(e -> {
//			String user = nameInput.getText();
//			primaryStage.setScene(mainScene);
//		});
//		grid.getChildren().addAll(nameLabel, nameInput, goButton);
//		Scene loginScene = new Scene(grid, 300, 75);
//		
//		primaryStage.setScene(loginScene);
//		primaryStage.show();
//	}

	public static void main (String[] args) {
		Client client = new Client();
		client.doIt();
	}
	
	public void doIt() {
//		launch(args);
		try {
			socket = new Socket("localhost", 2650);
			ExecutorService es = Executors.newFixedThreadPool(2);

			es.submit(new Runnable() {
				public void run() {
					try {
						InputStream is = socket.getInputStream();
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						String line;
						while ((line = br.readLine()) != null) {
							System.out.println(line);
						}
					} catch (SocketException se) {
						System.out.println(socket.getInetAddress().getHostName() + " has disconnected.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			es.submit(new Runnable() {
				public void run() {
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					String line;
					OutputStream os;
					try {
						os = socket.getOutputStream();
						while ((line = br.readLine()) != null) {
							os.write((line + '\n').getBytes());
							os.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
