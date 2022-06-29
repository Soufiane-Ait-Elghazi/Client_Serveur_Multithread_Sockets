package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ClientFileTransfert extends Application {
	
	static Socket socket = new Socket();
	private String filepath ;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Client File Transfert");
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
		hbox.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, null, null)));
		borderPane.setTop(hbox);
		
		
		Label labelFile = new Label("File :");
		TextField textFieldFile = new TextField("");
		textFieldFile.setPrefSize(300, 25);
		Button SendFile= new Button("Send");
		Button ChooseFile= new Button("Choose");
		HBox hbox1 = new HBox();
		hbox1.getChildren().addAll(labelFile,textFieldFile,ChooseFile,SendFile);
		hbox1.setSpacing(20);
		hbox1.setPadding(new Insets(20));
		hbox1.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, null, null)));
		borderPane.setCenter(hbox1);
	   ChooseFile.setOnAction((evt)->{
		  FileChooser fileChooser = new FileChooser();
			 fileChooser.setTitle("Open Resource File");
			 fileChooser.getExtensionFilters().addAll(
			         new ExtensionFilter("Text Files", "*.txt"),
			         new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
			         new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
			         new ExtensionFilter("All Files", "*.*"));
			 File selectedFile = fileChooser.showOpenDialog(primaryStage);
			 if (selectedFile != null) {
			   textFieldFile.setText(selectedFile.getAbsolutePath());
			 }
			
		  
	  });
		Scene scene = new  Scene(borderPane,600,200);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		SendFile.setOnAction((evt)->{
					try {
						    sendFilepath(textFieldFile.getText());
						    int i;
						    FileInputStream fis = new FileInputStream (textFieldFile.getText());
						    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
						    System.out.println(socket);
							while ((i = fis.read()) > -1)
							        dos.write(i);
					} catch (IOException e) {
						e.printStackTrace();
					}
		});

		buttonConnecter.setOnAction((evt)->{
			String host = textFieldHost.getText();
			int port = Integer.parseInt(textFieldPort.getText());
			
			try {
				socket = new Socket(host,port);
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				new Thread(()->{
					try {
						int nb = 2;
						System.out.println("J'envoie le nombre "+nb+" au serveur ");
						os.write(nb);
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
	
	public void sendFilepath (String path) throws IOException {
		PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
		pw.println(path);
		
	}

}
