//
// From https://github.com/rlunding/Lejos.git
//

package com.darcimaher.smartcarrelay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BluetoothCommunicator {

	private static DataInputStream dis = null;
	private static DataOutputStream dos = null;
	private static BTConnection btc;
	private static boolean isConnected;

	private BluetoothCommunicator() {
		isConnected = false;
	}

	public static boolean getIsConnected() {
		return isConnected;
	}

	public static void sendData(String output) {
		try {
			dos.writeUTF(output);
			dos.flush();
		} catch (IOException ioe) {
			isConnected = false;
			stopConnection();
			connect();
			System.out.println("Bluetooth failed to send");
		}
	}

	public static void sendInt(int output) {
		try {
			dos.writeInt(output);
			dos.flush();
		} catch (IOException ioe) {
			isConnected = false;
			stopConnection();
			connect();
			System.out.println("Bluetooth failed to send");
		}
	}

	public static String receiveData() {
		String s = " ";
		try {
			s = dis.readUTF();
			return s;
		} catch (IOException e) {
			System.out.println("Bluetooth failed to receive");
			isConnected = false;
			stopConnection();
			connect();
			return "0";
		}
	}

	public static void stopConnection() {
		try {
			dos.close();
			dis.close();
			btc.close();
		} catch (IOException e) {
			System.out.println("Bluetooth failed to close");
		}
	}

	public static void connect(String name) {
		System.out.println("Bluetooth Connecting...");
		RemoteDevice btrd = Bluetooth.getKnownDevice(name);
		if (btrd == null) {
			
			System.out.println("Bluetooth No such device");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		btc = Bluetooth.connect(btrd);
		if (btc == null) {
			
			System.out.println("Bluetooth Connect fail");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else {
			
			System.out.println("Bluetooth Connected");
			isConnected = true;
			dis = btc.openDataInputStream();
			dos = btc.openDataOutputStream();
		}
	}

	public static void connect() {
		System.out.println("Bluetooth Waiting for Bluetooth...");
		btc = Bluetooth.waitForConnection();
		System.out.println("Bluetooth Connected  ");
		isConnected = true;
		dis = btc.openDataInputStream();
		dos = btc.openDataOutputStream();
	}
}
