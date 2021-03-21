import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Client extends JFrame implements ActionListener {
	    static JTextField messagetosend = new JTextField();
		JTextField messagetorecieve = new JTextField();
		JPanel panel = new JPanel();
		JButton send = new JButton();
		static JTextArea chattingfield = new JTextArea();
		static String message = "";
		static boolean f = false;
		static String name;
		public Client(){
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setSize(900, 600);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			panel.setPreferredSize(new Dimension(600, 600));
			panel.setBackground(Color.BLUE);
			messagetosend.setPreferredSize(new Dimension(200, 200));
			panel.add(messagetosend,BorderLayout.NORTH);
			panel.add(chattingfield,BorderLayout.EAST);
			chattingfield.setPreferredSize(new Dimension(300, 600));
			chattingfield.setText("Recived Messages :");
			send.setText("Send Message");
			send.setFont(new Font("Chiller", Font.BOLD, 25));
			send.addActionListener(this);
			panel.add(send,BorderLayout.SOUTH);
			this.add(panel);
			this.setVisible(true);
			chattingfield.setEditable(false);

		}
		public static String getname(){
			 name =  JOptionPane.showInputDialog("Please enter desired name: ");
			return name;
		}
		
		
		public static void refresh(String message){
			String s = chattingfield.getText();
			s = s+ "\n" + message;
			chattingfield.setText(s);
			
		}


public static void main(String args[]) throws UnknownHostException,
			IOException, InterruptedException {
	Client c = new Client();
	Scanner scn = new Scanner(System.in);
		InetAddress ip = InetAddress.getByName("DESKTOP-28LEJ1I");
		int serverport = 0;

        String server= JOptionPane.showInputDialog("Please enter desired Server: ");
		if(server.compareTo("Server1")==0){
			serverport = 7000;
			JOptionPane.showMessageDialog(null, " Welcome to Server1");
			
		}
		if(server.compareTo("Server2")==0){
			serverport = 7001;
			JOptionPane.showMessageDialog(null, " Welcome to Server2");
			
		}

		Socket s = new Socket(ip, serverport);

		DataInputStream din = new DataInputStream(s.getInputStream());
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());
		Thread sendMessage = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (f==true) {
						String s = messagetosend.getText() ;
						try {
							dout.writeUTF(s);
						} catch (Exception e) {
						}
						f = false;
						messagetosend.setText("");


					}
				}
			}
		});

		Thread readMessage = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String msg = din.readUTF();
						if (!msg.equals("")) {
							refresh(msg);
						}
					} catch (Exception e) {

					}
				}
			}

		});
		sendMessage.start();
		readMessage.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		f = true;

	}
}


 class ClientHandler implements Runnable {
	Scanner scn = new Scanner(System.in);
	String name;
	DataInputStream dis;
	DataOutputStream dos;
	Socket s;
	public boolean isloggedin;
   static int ttl;
	public boolean exists(String s){
	for(int i =0;i<s.length();i++){
		if(s.charAt(i)=='/')
			return true;
	}
	return false;
	}
	static ArrayList clients = new ArrayList();
	public ClientHandler(Socket s, String name, DataInputStream dis,
			DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
		this.name = name;
		this.s = s;
		this.isloggedin = true;
		clients.add(name);

	}
//	public static void viewusers(String x){
//		
//		JFrame window = new JFrame();
//		window.setVisible(true);
//		JPanel p = new JPanel();
//		p.setVisible(true);
//		JTextArea clients = new JTextArea();
//		clients.setVisible(true);
//		clients.setText();
//		clients.setEditable(false);
//		p.add(clients);
//		window.add(p);
//	}

	public ArrayList splitmessage(String s) {
		
		ArrayList x = new ArrayList();
		StringTokenizer st = new StringTokenizer(s, "-");
		x.add(st.nextToken());
		x.add(st.nextToken());
		x.add(st.nextToken());
		return x;
		}
		
	
public ArrayList splitmessageclienthandler(String s) {
		
		ArrayList x = new ArrayList();
		StringTokenizer st = new StringTokenizer(s, "|");
		x.add(st.nextToken());
		x.add(st.nextToken());
		return x;
		}
	@Override
	public void run() {
		try {

			while (true) {

				String message = dis.readUTF();
				//System.out.println(message);
				boolean f = true;
				if (message.compareTo("logout") == 0) {

					System.out.println(this.name + " logged out");
					this.isloggedin = false;
					for(int i =0;i<Server1.clients.size();i++){
						if(this.name.compareTo(Server1.clients.get(i).name)==0)
							Server1.clients.remove(i);
					}
					for(int i =0;i<Server2.clients.size();i++){
						if(this.name.compareTo(Server2.clients.get(i).name)==0)
							Server2.clients.remove(i);
					}
					
					this.s.close();
					break; // break needed to get out of the infinite loop and
							// if not written the last two
							// statments won't be reachable
				}
				else if (message.compareTo("show online users") == 0) {
					
					ArrayList x = new ArrayList();
					
					for(int i = 0;i<Server1.clients.size();i++){
						if(Server1.clients.get(i).name.compareTo("ClientHandler")!=0)
						x.add(Server1.clients.get(i).name);
					}
					for(int i = 0;i<Server2.clients.size();i++){
						if(Server2.clients.get(i).name.compareTo("ClientHandler")!=0)
						x.add(Server2.clients.get(i).name);
					}
					String s = "";
					for(int i = 0;i<x.size();i++){
						s = s+ "\n" + x.get(i);
				}
					JFrame window = new JFrame();
				
					window.setVisible(true);
					JPanel p = new JPanel();
					p.setVisible(true);
					JTextArea clients = new JTextArea();
					clients.setVisible(true);
					clients.setText(s);
					clients.setEditable(false);
					p.add(clients);
					window.add(p);
				}
				
				else {
					if(this.name.compareTo("ClientHandler")==0){
					ArrayList y = splitmessageclienthandler(message);
					message = (String) y.get(0);
					String sender = (String) y.get(1);
					ArrayList x = splitmessage(message);
					String messagecontent = (String) x.get(0);
					String messagerecipient = (String) x.get(1);
					String timetolive = (String) x.get(2);
                    ttl = Integer.parseInt(timetolive);
//					System.out.println(sender);
//					System.out.println(messagecontent);
//					System.out.println(messagerecipient);
                 	System.out.println(timetolive);
                    if(ttl>0){
                    	//System.out.println(ttl);
                    	ttl--;
                    	//System.out.println(ttl);
                    	if(ttl>0){
					for (int i = 0;i<Server1.clients.size();i++){
						ClientHandler client = Server1.clients.get(i);
						if (client.name.equals(messagerecipient)
								&& client.isloggedin == true) {
							client.dos.writeUTF("From " + sender + " : "+ messagecontent);
							f = false;
							break;
						}
					}
	                
					
					for (int i = 0;i<Server2.clients.size();i++) {
						ClientHandler client = Server2.clients.get(i);
						if (client.name.equals(messagerecipient)
								&& client.isloggedin == true) {
							client.dos.writeUTF("From " + sender + " : "+ messagecontent);
							f = false;
							break;
						}
					}
					}
                    }
					}
					else{
						ArrayList x = splitmessage(message);
						String messagecontent = (String) x.get(0);
						String messagerecipient = (String) x.get(1);
						String timetolive = (String) x.get(2);
						//System.out.println(timetolive);
	                    int ttl = Integer.parseInt(timetolive);
	                     if(ttl>0){
	                    	 //System.out.println(ttl);
	                     	ttl--;
	                     	if(ttl>0){
	                     	//System.out.println(ttl);
						for (int i = 0;i<Server1.clients.size();i++) {
							ClientHandler client = Server1.clients.get(i);
							if (client.name.equals(messagerecipient)
									&& client.isloggedin == true) {
								client.dos.writeUTF("From " + this.name + " : "
										+ messagecontent);
								f = false;
								break;
							}
						}

						
						for (int i = 0;i<Server2.clients.size();i++) {
							ClientHandler client = Server2.clients.get(i);
							if (client.name.equals(messagerecipient)
									&& client.isloggedin == true) {
								client.dos.writeUTF("From " + this.name + " : "+ messagecontent);
								f = false;
								break;
								
							}
						}
	                     	}
						if (f == true) {
							ttl--;
							//System.out.println(ttl);
							if(ttl>0){
							for (int i = 0;i<Server1.clients.size();i++) {
								ClientHandler client = Server1.clients.get(i);
								if (client.name.equals("ClientHandler")
										&& client.isloggedin == true) {
									
									System.out.println(ttl);
									client.dos.writeUTF(message+"-"+ttl+"|"+this.name);
									f = false;
									break;
								}
							}
						
							for (int i = 0;i<Server2.clients.size();i++  ) {
								ClientHandler client = Server2.clients.get(i);
								if (client.name.equals("ClientHandler")
										&& client.isloggedin == true) {
								
									client.dos.writeUTF(message+"-"+ttl+"|"+this.name);
									f = false;
									break;
								
							}
						}

						}
						}
						}
					}
					}
					
				}

			

			this.dis.close();
			this.dos.close();

		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}
}
