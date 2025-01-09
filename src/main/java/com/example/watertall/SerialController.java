package com.example.watertall;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import java.io.InputStream;

public class SerialController {

    private SerialPort microbitPort;
    private DataListener listener;

    public SerialController(String portName, int baudRate) {
        // Haal de gewenste poort op
        microbitPort = SerialPort.getCommPort(portName);

        // Stel de baudrate in
        microbitPort.setBaudRate(baudRate);

        // Extra logging om te zien welke poort we proberen te openen
        System.out.println("Attempting to open port: " + portName);
    }

    public void setDataListener(DataListener listener) {
        this.listener = listener;
    }

    public void start() {
        // Controleer of de poort kan worden geopend
        if (microbitPort.openPort()) {
            System.out.println("Port opened successfully: " + microbitPort.getSystemPortName());
            new Thread(this::readData).start();
        } else {
            System.err.println("Failed to open port: " + microbitPort.getSystemPortName());
            System.err.println("Error code: " + microbitPort.getLastErrorCode());
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

                // Voeg een kleine vertraging toe zodat we niet teveel resources gebruiken
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface DataListener {
        void onDataReceived(String data);
    }
}
