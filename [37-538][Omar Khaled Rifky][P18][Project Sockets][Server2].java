
import java.awt.Color;
import java.io.*;
import java.util.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Server2 {
	    
	public static  ArrayList <ClientHandler>clients = new ArrayList();


	public static boolean  exists(String x){
		for(int i =0;i<clients.size();i++){
			if(clients.get(i).name.compareTo(x)==0){
				return true;
			}
		}
		return false;
	}
	public static void showusers()
	 {
		for(int i =0;i<clients.size();i++){
			System.out.println(clients.get(i).name);
			}
		}
		
	 
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(7001);
		Socket clientsocket;
		while (true) {
			if(exists("ClientHandler")==true){
			clientsocket = ss.accept();
			//View v = new View();
			String name=Client.getname();
//			Scanner scanner = new Scanner(System.in); 
//			System.out.println("Please enter your name : ");
//			String name = scanner.nextLine();
			System.out.println("Welcome " + name + " to our chatting application");
			DataInputStream dis = new DataInputStream(clientsocket.getInputStream());
			DataOutputStream dos = new DataOutputStream(clientsocket.getOutputStream());
			ClientHandler client = new ClientHandler(clientsocket, name, dis, dos );
			Thread t = new Thread(client);
		    clients.add(client);
		    System.out.println("Welcome to Server1");
			t.start();

		}
			if(exists("ClientHandler")==false){
				clientsocket = ss.accept();
				DataInputStream dis = new DataInputStream(clientsocket.getInputStream());
				DataOutputStream dos = new DataOutputStream(clientsocket.getOutputStream());
				ClientHandler client = new ClientHandler(clientsocket, "ClientHandler", dis, dos );
				Thread t = new Thread(client);
			    clients.add(client);
				t.start();
			}
		}
		
	}
}