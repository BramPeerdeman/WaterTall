package com.example.watertall;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mysql.cj.xdevapi.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;

public class weatherAPI {

    private static final String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=52.374&longitude=4.8897&hourly=temperature_80m,soil_moisture_0_to_1cm&forecast_days=1";

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(weatherAPI::weerUpdate, 0, 1, TimeUnit.HOURS);
    }

    public static void weerUpdate () {
        try {

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer YOUR_API_KEY");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray times = jsonResponse.getJSONObject("hourly").getJSONArray("time");
                JSONArray temperatures = jsonResponse.getJSONObject("hourly").getJSONArray("temperature_80m");

                // Get current hour
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:00");
                String currentHour = now.format(formatter);

                // Find the temperature for the current hour
                for (int i = 0; i < times.length(); i++) {
                    if (times.getString(i).equals(currentHour)) {
                        new Temperature(temperatures.getDouble(i));
                        break;
                    }
                }
            } else {
                System.out.println("GET request failed: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Temperature {
    private final double temperature;

    public Temperature (double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }


}
