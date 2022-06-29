package client;

import com.sun.jndi.url.corbaname.corbanameURLContextFactory;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Preparer extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Paneau de configuration");
		BorderPane borderPane = new BorderPane();
		Button buttonChat = new Button("Chat");
		buttonChat.setPrefWidth(600);
		Button buttonFileTransfert = new Button("File Transfert");
		buttonFileTransfert.setPrefWidth(600);
		Button buttonControlServer = new Button("Controle");
		buttonControlServer.setPrefWidth(600);
		VBox vbox = new VBox();
		vbox.getChildren().addAll(buttonChat,buttonFileTransfert,buttonControlServer);
		vbox.setSpacing(20);
		vbox.setPadding(new Insets(20));
		vbox.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));
		borderPane.setCenter(vbox);
		borderPane.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));
		Scene scene = new  Scene(borderPane,600,150);
		primaryStage.setScene(scene);
		primaryStage.show();
		buttonChat.setOnAction((evt)->{
			ClientChat  c = new ClientChat();
			Stage primaStage = new Stage();
			try {
				c.start(primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Chat");

		});
		buttonFileTransfert.setOnAction((evt)->{
			ClientFileTransfert  c = new ClientFileTransfert();
			Stage primaStage = new Stage();
			try {
				c.start(primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("File Transfert");

		});
		buttonControlServer.setOnAction((evt)->{
			ClientControle  c = new ClientControle();
			Stage primaStage = new Stage();
			try {
				c.start(primaryStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("File Transfert");

		});
		
		
		
		
		
	}
	
	

}
