//
// From https://github.com/rlunding/Lejos.git
//

package com.darcimaher.smartcarrelay;

import lejos.nxt.LCD;
import lejos.util.Delay;

public class NXTRelay {
	
	private USBCom usb;
	private volatile int toBrick;
	private volatile String toMac;
	private volatile boolean keepGoing = true;
	
	public NXTRelay(){
		toMac = "";
		toBrick = 0;
		talkToTheMac();
		talkToTheBrick();
		while(keepGoing){
			Delay.msDelay(1000);
		}
	}
	
	private void talkToTheMac(){
		Runnable r = new Runnable(){
			public void run() {
				usb = USBCom.getInstance();
				while(keepGoing){
					while(usb.isConnected()){
						usb.sendString(toMac);
						toBrick = usb.receiveInt();
						LCD.drawString("Got from Mac", 0, 4);
						LCD.drawInt(toBrick, 0, 5);
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	private void talkToTheBrick(){
		Runnable r = new Runnable(){
			public void run() {
				BTCom.connect();
				while(keepGoing){
					while(BTCom.getIsConnected()){
						toMac = BTCom.receiveData();
						LCD.drawString("Got from Brick", 0, 2);
						LCD.drawString(toMac, 0, 3);
						BTCom.sendInt(toBrick);
					}
				}				
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public static void main(String[] args) {
		new NXTRelay();
	}
}
