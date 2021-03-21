import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class CommonClient {
	static ArrayList clients;
public static void showusers() throws IOException{
	clients = new ArrayList();
	for(int i =0;i<Server1.clients.size();i++){
		clients.add(Server1.clients.get(i).name);
		}
	for(int i =0;i<Server2.clients.size();i++){
		clients.add(Server2.clients.get(i).name);
		}
	
}

public static void main(String args[]) throws UnknownHostException,IOException {
Scanner scn = new Scanner(System.in);
InetAddress ip = InetAddress.getByName("DESKTOP-28LEJ1I");
Socket s1 = new Socket(ip, 7000);
Socket s2 = new Socket(ip, 7001);
DataInputStream dis1 = new DataInputStream(s1.getInputStream());
DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());
DataInputStream dis2 = new DataInputStream(s2.getInputStream());
DataOutputStream dos2 = new DataOutputStream(s2.getOutputStream());
Thread sendMessage1 = new Thread(new Runnable() {
	@Override
	public void run() {
		while (true) {
			// read the message required to deliver.
			String msg = scn.nextLine();

			try {

				dos1.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
});

Thread readMessage1 = new Thread(new Runnable() {
	@Override
	public void run() {

		while (true) {

			try {
				// reads the message that was sent to this client
				String msg = dis1.readUTF();
				dos2.writeUTF(msg);
				
			} catch (IOException e) {

				break; // break needed to get out of the infinite loop

			}
		}
	}

});
Thread sendMessage2 = new Thread(new Runnable() {
	@Override
	public void run() {
		while (true) {
			// read the message required to deliver.
			String msg = scn.nextLine();

			try {

				dos2.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
});

Thread readMessage2 = new Thread(new Runnable() {
	@Override
	public void run() {

		while (true) {

			try {
				// reads the message that was sent to this client
				String msg = dis2.readUTF();
				dos1.writeUTF(msg);
				
			} catch (IOException e) {

				//System.out.println("Logged out");
				break; // break needed to get out of the infinite loop

			}
		}
	}

});

sendMessage1.start();
readMessage1.start();
sendMessage2.start();
readMessage2.start();
}
}


