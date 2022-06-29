package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ClientControle extends Application {
	
	
	PrintWriter pw ;
	static int num ;
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Client");
		BorderPane borderPane = new BorderPane();
		Label labelHost = new Label("Host :");
		TextField textFieldHost = new TextField("localhost");
		Label labelPort = new Label("Port :");
		TextField textFieldPort = new TextField("1234");
		Button buttonConnecter = new Button("Connecter");
		HBox hbox = new HBox();
		hbox.getChildren().addAll(labelHost,textFieldHost,labelPort,textFieldPort,buttonConnecter);
		hbox.setSpacing(20);
		hbox.setPadding(new Insets(20));
		hbox.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
		borderPane.setTop(hbox);
		borderPane.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
		ObservableList<String> listModel = FXCollections.observableArrayList();
		ListView<String> listView = new ListView<>(listModel);
		VBox vbox = new VBox();
		vbox.getChildren().add(listView);
		borderPane.setCenter(vbox);
		
		Label lableCommande=new Label("Commande :");
		TextField textFieldCommande = new TextField();
		textFieldCommande.setPrefSize(400, 25);
		Button buttonRun = new Button("Run");
		HBox hbox2 = new HBox();
		hbox2.getChildren().addAll(lableCommande,textFieldCommande,buttonRun);
		hbox2.setSpacing(20);
		hbox2.setPadding(new Insets(20));
		borderPane.setBottom(hbox2);
		
		Scene scene = new  Scene(borderPane,600,300);
		primaryStage.setScene(scene);
		primaryStage.show();
		buttonRun.setOnAction((evt)->{
			String cmd = textFieldCommande.getText();
			textFieldCommande.setText("");
			pw.println(cmd);
			
		});
		
		buttonConnecter.setOnAction((evt)->{
			String host = textFieldHost.getText();
			int port = Integer.parseInt(textFieldPort.getText());
			try {
				Socket socket = new Socket(host,port);
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				pw = new  PrintWriter(socket.getOutputStream(),true);
				new Thread(()->{
					try {
						int nb = 3;
						System.out.println("J'envoie le nombre "+nb+" au serveur ");
						os.write(nb);
						ClientChat.num = is.read();
						while(true) {
						String response = br.readLine();
						Platform.runLater(()->{
							listModel.add(response);
						});
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}).start();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}