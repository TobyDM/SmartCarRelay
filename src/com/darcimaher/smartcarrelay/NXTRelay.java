//
// From https://github.com/rlunding/Lejos.git
//

package com.darcimaher.smartcarrelay;

import lejos.nxt.LCD;

public class NXTRelay {

	private USBCommunicator relayToRemoteUSBCommunicator;

	public NXTRelay() {

		relayToRemoteUSBCommunicator = USBCommunicator.getInstance();
		BluetoothCommunicator.connect();
		
		while (relayToRemoteUSBCommunicator.isConnected()
				&& BluetoothCommunicator.getIsConnected()) {
			// This will WAIT for a command.
			int commandReceivedFromTheRemote = relayToRemoteUSBCommunicator.receiveInt();
			LCD.drawString("Sending to car: ", 0, 4);
			LCD.drawInt(commandReceivedFromTheRemote, 1, 5);
			// Send to the car.
			BluetoothCommunicator.sendInt(commandReceivedFromTheRemote);
		}

	}

	// private void talkToTheRemote(){
	// // Create something that will listen for a command from the remote.
	// Runnable remoteListener = new Runnable(){
	// public void run() {
	// relayToRemoteUSBCommunicator = USBCommunicator.getInstance();
	// while(true){
	// while(relayToRemoteUSBCommunicator.isConnected()){
	// // usb.sendString(textReceivedFromTheCar);
	// // This will WAIT for a command.
	// commandReceivedFromTheRemote = relayToRemoteUSBCommunicator.receiveInt();
	// LCD.drawString("Got from Mac", 0, 4);
	// LCD.drawInt(commandReceivedFromTheRemote, 0, 5);
	// }
	// }
	// }
	// };
	//
	// // Run our listener
	// Thread remoteListenerThread = new Thread(remoteListener);
	// remoteListenerThread.start();
	// }
	//
	// private void talkToTheCar(){
	//
	// // Create something that will talk to the car
	// Runnable carCommandSender = new Runnable(){
	// public void run() {
	// BluetoothCommunicator.connect();
	// while(true){
	// while(BluetoothCommunicator.getIsConnected()){
	// // textReceivedFromTheCar = BTCom.receiveData();
	// // LCD.drawString("Got from Car", 0, 2);
	// // LCD.drawString(textReceivedFromTheCar, 0, 3);
	// BluetoothCommunicator.sendInt(commandReceivedFromTheRemote);
	// }
	// }
	// }
	// };
	//
	// // Run our command sender
	// Thread carCommandSenderThread = new Thread(carCommandSender);
	// carCommandSenderThread.start();
	// }

	public static void main(String[] args) {
		new NXTRelay();
	}
}
