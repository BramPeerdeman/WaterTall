package com.example.watertall;

import com.fazecast.jSerialComm.SerialPort;

public class SerialController {

    private SerialPort microbitPort;

    public SerialController(String portName, int baudRate) {
        microbitPort = SerialPort.getCommPort(portName);
        microbitPort.setBaudRate(baudRate);
    }

    public void start() {
        if (microbitPort.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.err.println("Failed to open port.");
        }
    }

    public void stop() {
        if (microbitPort.closePort()) {
            System.out.println("Port closed successfully.");
        } else {
            System.err.println("Failed to close port.");
        }
    }

    public void sendData(String data) {
        if (microbitPort.isOpen()) {
            try {
                byte[] dataBytes = (data + "\n").getBytes(); // Append newline
                microbitPort.writeBytes(dataBytes, dataBytes.length);
                System.out.println("Sent data: " + data);
            } catch (Exception e) {
                System.err.println("Failed to send data: " + e.getMessage());
            }
        } else {
            System.err.println("Port is not open. Cannot send data.");
        }
    }

}
