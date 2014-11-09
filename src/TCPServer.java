import java.io.*;
import java.net.*;

import GUI.openAdmin;
import JsonClasses.CalendarInfo;

import com.google.gson.stream.JsonReader;

class TCPServer{    
	
	public static void main(String argv[]) throws Exception       {

		openAdmin admin = new openAdmin();
		Thread adminthread = new Thread(admin, "admin");
		adminthread.run();;
		//Creates a socket to send and recieve messages in port 8888
		int port = 8888;
		ServerSocket welcomeSocket = new ServerSocket(port);
		System.out.println("Server is up and running");
		System.out.println("Listening on port: " + port);
		//While something is true
		while(true){
			//Creates a socket and a buffered reader which recieves some sort of input from somewhere around the internet!
			
			Socket connectionSocket = welcomeSocket.accept();
			ClientWorker client= new ClientWorker(connectionSocket);
			Thread thread = new Thread(client, "client");
			thread.start();
		}
	}
}