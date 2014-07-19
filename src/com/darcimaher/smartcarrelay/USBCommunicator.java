//
// From https://github.com/rlunding/Lejos.git
//

package com.darcimaher.smartcarrelay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;


public class USBCommunicator {
	
	private static USBCommunicator instance;
	private USBConnection con;
	private DataOutputStream dos;
	private DataInputStream dis;
	private boolean isConnected;
	
	private USBCommunicator(){
		connect();
	}
	
	public static USBCommunicator getInstance(){
		if(instance == null) instance = new USBCommunicator();
		return instance;
	}
	
	public boolean isConnected(){
		return isConnected;
	}
	
	public synchronized void sendString(String output){
		try {
			dos.writeUTF(output);
			dos.flush();
		} catch (IOException e) {
			isConnected = false;
			
			System.out.println("USB Failed to send");
			stopConnection();
//			connect();
		}
	}
	
	public synchronized String receiveString(){
		String s = " ";
		try {
			s = dis.readUTF();
			return s;
		} catch (IOException e) {
			isConnected = false;
			
			System.out.println("USB Failed to receive");
			stopConnection();
//			connect();
			return "FAIL";
		}
	}
	
	public synchronized void sendInt(int output){
		try {
			dos.writeInt(output);
			dos.flush();
		} catch (IOException e) {
			isConnected = false;
			
			System.out.println("USB Failed to send");
			stopConnection();
//			connect();
		}
	}
	
	public synchronized int receiveInt(){
		int i = -1;
		try {
			i = dis.readInt();
			return i;
		} catch (IOException e) {
			isConnected = false;
			
			System.out.println("USB Failed to receive");
			stopConnection();
//			connect();
			return -1;
		}
	}
	
	private synchronized void connect(){
		
		System.out.println("USB Waiting for USB...");
		con = USB.waitForConnection();
		dos = con.openDataOutputStream();
		dis = con.openDataInputStream();
		
		System.out.println("USB Connected");
		isConnected = true;
	}
	
	public synchronized void stopConnection(){
		try {
			dos.close();
			dis.close();
			con.close();
		} catch (IOException e) {
			System.out.println("USB Failed to close");
		}
	}
}
