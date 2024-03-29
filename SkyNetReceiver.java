package SkyNet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class SkyNetReceiver extends Thread {	
	private static DataOutputStream oStream;
	
	ArrayList<String> messageQueue;
	ArrayList<String> receivedMessageQueue;
	
	public SkyNetReceiver(){
		messageQueue = new ArrayList<String>();
		receivedMessageQueue = new ArrayList<String>();
		
		this.start();
	}
	
	public void run(){
		NXTConnection connection = Bluetooth.waitForConnection();
		oStream = connection.openDataOutputStream();
		DataInputStream iStream = connection.openDataInputStream();		

		while(true){
			try {
				String command = iStream.readUTF();
				receivedMessageQueue.add(command);
			} catch (IOException e) {
				System.out.println("Error reading from bluetooth!");
			}
		}
	}
	
	public static void waitForConnection(){
		while(oStream == null);
	}
	
	public static void sendMessage(String command){
		System.out.println("Sending message: "+command);
		if(oStream == null)return;
		try{
			oStream.writeUTF(command);
			oStream.flush();
		}catch(IOException e){
			System.out.println("Couldn't write to remote!");
		}
	}
}
