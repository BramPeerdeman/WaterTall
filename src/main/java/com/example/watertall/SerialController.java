package com.example.watertall;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;

import java.io.InputStream;
import java.util.Scanner;

public class SerialController {

    private SerialPort microbitPort;
    private DataListener listener;

    public SerialController(String portName, int baudRate)
    {
        microbitPort = SerialPort.getCommPort(portName);
        microbitPort.setBaudRate(baudRate);
    }

    public void setDataListener(DataListener listener)
    {
        this.listener = listener;
    }

    public void start()
    {
        if (microbitPort.openPort())
        {
            System.out.println("Port opened successfully.");
            new Thread(this::readData).start();
        }
        else
        {
            System.err.println("Failed to open port.");
        }
    }

    private String lastData = "";

    private void readData() {
        try (InputStream inputStream = microbitPort.getInputStream()) {
            byte[] buffer = new byte[1024];
            while (true) {
                int bytesAvailable = microbitPort.bytesAvailable();
                if (bytesAvailable > 0) {
                    int bytesRead = inputStream.read(buffer, 0, bytesAvailable);
                    System.out.println("Bytes available: " + bytesAvailable);
                    System.out.println("Bytes read: " + bytesRead);

                    if (bytesRead > 0) {
                        String data = new String(buffer, 0, bytesRead).trim();
                        if (!data.isEmpty() && !data.equals(lastData)) {
                            lastData = data;
                            System.out.println("Received data: " + data);
                            if (listener != null) {
                                Platform.runLater(() -> listener.onDataReceived(data));
                            }
                        } else {
                            System.out.println("Duplicate or empty data ignored");
                        }
                    }
                }

                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface DataListener
    {
        void onDataReceived(String data);
    }
}