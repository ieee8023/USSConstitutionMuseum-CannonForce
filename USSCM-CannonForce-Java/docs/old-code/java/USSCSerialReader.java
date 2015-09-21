package com.ussc.serialreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

public class USSCSerialReader implements Runnable, SerialPortEventListener {
    static CommPortIdentifier portId;
    static Enumeration<?> portList;

    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;
    static SWFLauncher swf_launcher;
    
    @SuppressWarnings("unused")
	public static void main(String[] args) {
    	System.out.println("Program Launched.");
        
    	// Kill Taskbar.
    	TaskbarKiller kill_taskbar = new TaskbarKiller();
    	System.out.println("Killed explorer.exe process.");
    	
    	// Launch the "home screen" process.
    	FullScreenJFrame frame = new FullScreenJFrame("");
        frame.setVisible(true);
        System.out.println("Launched fullscreen background.");
        
        // SWF Launcher thread to fire when it is full from COM inputs.
        swf_launcher = new SWFLauncher();
        swf_launcher.start();
        
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals(args[0])) {
                	 System.out.println("Launched USSCSerialReader: " + args[0].toString());
                	 USSCSerialReader reader = new USSCSerialReader();
                }
            }
        }
    }

    public USSCSerialReader() {
        try {
            serialPort = (SerialPort) portId.open("USSCSerialReader", 2000);
        } catch (PortInUseException e) {
        	System.out.println(e);
        }
        
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {
        	System.out.println(e);
        }
        
        try {
        	serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
        	System.out.println(e);
        }
        
        serialPort.notifyOnDataAvailable(true);
        try {
        	serialPort.setSerialPortParams(19200,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
        	System.out.println(e);
        }
        
        readThread = new Thread(this);
        readThread.start();
    }

    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        	System.out.println(e);
        }
    }

    @SuppressWarnings("unused")
	public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
	        case SerialPortEvent.BI:
	        case SerialPortEvent.OE:
	        case SerialPortEvent.FE:
	        case SerialPortEvent.PE:
	        case SerialPortEvent.CD:
	        case SerialPortEvent.CTS:
	        case SerialPortEvent.DSR:
	        case SerialPortEvent.RI:
	        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
	            break;
	        
	        case SerialPortEvent.DATA_AVAILABLE:
	            byte[] readBuffer = new byte[3];
	
	            try {
	            	
	            	// Collect input into buffer.
	            	while (inputStream.available() > 0) {
	            		int numBytes = inputStream.read(readBuffer);
	                }
	            	
	            	byte[] bytes = readBuffer;
	                swf_launcher.set_input(bytes);

	
	            } catch (IOException e) {
	            	System.out.println(e);
	            }
	            break;
        }
    }
    

    
}
