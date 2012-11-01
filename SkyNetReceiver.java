package SkyNet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import lejos.nxt.comm.RS485Connection;

public class SkyNetReceiver extends Thread {
	private int CONNECTION_TIMEOUT = 50000;
	
	private static DataOutputStream oStream;
	
	ArrayList<Integer> messageQueue;
	ArrayList<Integer> receivedMessageQueue;
	
	public SkyNetReceiver(){
		messageQueue = new ArrayList<Integer>();
		receivedMessageQueue = new ArrayList<Integer>();
	}
	
	public void run(){
		//We're controlling the slave brick
		//All it does is wait message from the master brick
		//and execute commands based on these messages
		//NXTConnection connection = Bluetooth.waitForConnection();
		RS485Connection connection = RS485.waitForConnection(CONNECTION_TIMEOUT, NXTConnection.PACKET);
		oStream = connection.openDataOutputStream();
		DataInputStream iStream = connection.openDataInputStream();		

		while(true){
			try {
				int command = iStream.readInt();
				receivedMessageQueue.add(command);
			} catch (IOException e) {
				System.out.println("Error reading from bluetooth!");
			}
		}
	}
	
	public static void waitForConnection(){
		while(oStream == null);
	}
	
	public static void sendMessage(int command){
		System.out.println("Sending message: "+command);
		if(oStream == null)return;
		try{
			oStream.writeInt(command);
			oStream.flush();
		}catch(IOException e){
			System.out.println("Couldn't write to remote!");
		}
	}
}
