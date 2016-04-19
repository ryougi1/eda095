package lab3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Test extends Application {
	static TextArea chattTextArea;
	
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Chatt");
		
		//Main scene
		GridPane grid2 = new GridPane();
		grid2.setPadding(new Insets(10, 10, 10, 10));
		grid2.setVgap(8);
		grid2.setHgap(10);
		
		Label chatLabel = new Label("Chatt: ");
		GridPane.setConstraints(chatLabel, 0, 0);
		chattTextArea = new TextArea();
		GridPane.setConstraints(chattTextArea, 0, 1);
		chattTextArea.setMaxSize(400, 400);
		
		Label messageLabel = new Label("Enter message: ");
		GridPane.setConstraints(messageLabel, 0, 3);
		TextArea messageInput = new TextArea();
		GridPane.setConstraints(messageInput, 0, 4);	
		messageInput.setMaxSize(400, 75);
		
		Button sendButton = new Button("Send");
		GridPane.setConstraints(sendButton, 1, 4);
		sendButton.setOnAction(e -> {
			chattTextArea.appendText("\n" + messageInput.getText());
		} );
		grid2.getChildren().addAll(chatLabel, chattTextArea, messageLabel, messageInput, sendButton);
		Scene mainScene = new Scene(grid2, 500, 600);
		
		//Login scene
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);

		Label nameLabel = new Label("Username: ");
		GridPane.setConstraints(nameLabel, 0, 0);
		TextField nameInput = new TextField("ryougi");
		GridPane.setConstraints(nameInput, 1, 0);	
		Button goButton = new Button("Go ");
		GridPane.setConstraints(goButton, 3, 0);
		goButton.setOnAction(e -> {
			System.out.println(nameInput.getText());
			primaryStage.setScene(mainScene);
		});
		grid.getChildren().addAll(nameLabel, nameInput, goButton);
		Scene loginScene = new Scene(grid, 300, 75);
		
		
		primaryStage.setScene(loginScene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
		chattTextArea.appendText("testing append");
	}
}
