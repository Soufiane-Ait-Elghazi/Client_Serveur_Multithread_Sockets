package server;
import java.io.BufferedInputStream;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class Server extends Thread {
	private boolean isActive = true ;
	private int  nombreClients = 0;
	private String filepath;
	private List<Conversation> clients = new ArrayList<>();
	public static void main(String[] args) {
		new Server().start();
	}
	@Override
	public void run() {
		try
		{
			ServerSocket serverSocket = new ServerSocket(1234);
			while(isActive){
				Socket socket = serverSocket.accept();
				System.out.println("J'attend que le client envoie un message de controle(1=> Chat ,2=> File transfert ,3=> conrole)..");
				++nombreClients;
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				int nb = is.read();
				System.out.println("J'ai recu un nombre "+nb);
				os.write(nombreClients);
				Conversation conversation = new Conversation(socket,nombreClients,nb);
				clients.add(conversation);
		    	conversation.start();
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	class Conversation extends Thread {
		protected Socket socketClient ;
		protected int numero ;
		protected int service ;
		
		public Conversation(Socket socketClient, int numero,int service) { 
			this.socketClient = socketClient;
			this.numero = numero;
			this.service = service ;
		}
		
		public void broadcastMessage(String mssg,int nmbre) throws IOException {
					for(Conversation client: clients) {
						if(client.numero != nmbre) {
							PrintWriter pw = new PrintWriter(client.socketClient.getOutputStream(),true);
							pw.println(mssg);
						}
					}
		}
		
		 @Override
		public void run() {
			 if(this.service == 1) {
					 try 
					 {
					    InputStream is = socketClient.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
				        BufferedReader br = new BufferedReader(isr);
						PrintWriter pw = new PrintWriter(socketClient.getOutputStream(),true);
						String ipClient = socketClient.getRemoteSocketAddress().toString() ;
						pw.println("Bienvenue , vous etes le client numero"+numero);
						System.out.println("Connexion du client numero "+numero+" IP = "+ipClient);
						while(true) {
							String req = br.readLine();
							broadcastMessage(req,numero);
						}
						
					 } catch (IOException e) 
					{
						e.printStackTrace();
					}
			 }else if(this.service == 2) {
				try {
					System.out.println("File transfert !!");
					InputStream is = socketClient.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					DataInputStream dis = new DataInputStream(socketClient.getInputStream());
				    BufferedReader br = new BufferedReader(isr);
				    String req = br.readLine();
				    System.out.println("this is req :  "+req);
				    String str=req;
				    String separator = "\\";
				    String[] arrOfStr=str.replaceAll(Pattern.quote(separator), "\\\\").split("\\\\");
				    for(String a : arrOfStr) {
				    	req = a;
				    }
				    System.out.println("this is req :  "+req);
			            FileOutputStream fout = new FileOutputStream("/home/bendirmohammed/Bureau/"+req);
			            int i;
			            while ( (i = dis.read()) > -1) {
			                fout.write(i);
			            }
			            System.out.println("Fin !!!!");

				} catch (IOException e1) {
					e1.printStackTrace();
				}
		     }else {   
			 System.out.println("Controle !!");
			    while(true) {
		        try {
		        	BufferedReader br = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
					String cmd = br.readLine();
				    System.out.println("commande "+cmd);
			        Process p;String s;
		            p = Runtime.getRuntime().exec(cmd);
		            PrintWriter pw = new PrintWriter(socketClient.getOutputStream(),true);
		            
		            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		            while ((s = br.readLine()) != null) {
		                System.out.println("" + s);
		                pw.println(s);
		            }
		            p.waitFor();
		            System.out.println ("--");
		            pw.write("--");
		            p.destroy();
		        } catch (Exception e) {}
			 }
		     }
		
		 }
		}

}