//
// From https://github.com/rlunding/Lejos.git
//

package com.darcimaher.smartcarrelay;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class NXTRelay {

	private final USBCommunicator relayToRemoteUSBCommunicator;
	private boolean keepLooping;

	public NXTRelay() {

		keepLooping = true;

		Button.ESCAPE.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
			}

			@Override
			public void buttonPressed(Button b) {
				keepLooping = false;
				closeOurConnections();
			}
		});

		this.relayToRemoteUSBCommunicator = USBCommunicator.getInstance();
		BluetoothCommunicator.connect();

		while (keepLooping && relayToRemoteUSBCommunicator.isConnected()
				&& BluetoothCommunicator.getIsConnected()) {

			// This will WAIT for a command.
			int commandReceivedFromTheRemote = relayToRemoteUSBCommunicator
					.receiveInt();
			if (keepLooping) {
				System.out.println("Sending: " + commandReceivedFromTheRemote);
				// Send to the car.
				BluetoothCommunicator.sendInt(commandReceivedFromTheRemote);
			}
		}

	}

	private void closeOurConnections() {

		if (this.relayToRemoteUSBCommunicator != null) {
			this.relayToRemoteUSBCommunicator.stopConnection();
		}

		BluetoothCommunicator.stopConnection();

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

		System.out.println("Press any key to start.");
		Button.waitForAnyPress();

		new NXTRelay();
	}
}
