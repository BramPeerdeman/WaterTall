package com.example.watertall;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

public class weatherAPI {

    private static final String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=52.0767&longitude=4.2986&hourly=temperature_2m,relative_humidity_2m&daily=precipitation_sum&forecast_days=3";

    //dit zorgt ervoor dat de weerAPI elk uur wordt geactiveerd
    public static void main(String[] args) {
        weerUpdate();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // Calculate the delay until the next hour
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        long initialDelay = calendar.getTimeInMillis() - System.currentTimeMillis();
        long period = 60 * 30 * 1000;
        scheduler.scheduleAtFixedRate(weatherAPI::weerUpdate, initialDelay, period, TimeUnit.MILLISECONDS); //Vegeet niet weer naar HOURS te veranderen
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

                // Parsed JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray timesHour = jsonResponse.getJSONObject("hourly").getJSONArray("time");
                JSONArray timesDay = jsonResponse.getJSONObject("daily").getJSONArray("time");
                JSONArray temperatures = jsonResponse.getJSONObject("hourly").getJSONArray("temperature_2m");
                JSONArray humidity = jsonResponse.getJSONObject("hourly").getJSONArray("relative_humidity_2m");
                JSONArray precipitation = jsonResponse.getJSONObject("daily").getJSONArray("precipitation_sum");

                // Krijgt de huidige tijd
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:00");
                DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter formatterNow = DateTimeFormatter.ofPattern("HH-mm-ss");
                String currentHour = now.format(formatterHour);
                String currentDay = now.format(formatterDay);
                String currentTime = now.format(formatterNow);

                System.out.println(currentTime);
                System.out.println(currentHour);
                System.out.println(currentDay);

                // Vindt de temperatuur en relatieve luchtvochtigheid van de huidige uur
                for (int i = 0; i < timesHour.length(); i++) {
                    if (timesHour.getString(i).equals(currentHour)) {
                        new Temperature(temperatures.getDouble(i));
                        new Humidity(humidity.getInt(i));
                        break;
                    }
                }
                // Vindt de totale verwachte neerslag van de huidige dag
                for (int i = 0; i < timesDay.length(); i++) {
                    if (timesDay.getString(i).equals(currentDay)) {
                        new Precipitation(precipitation.getDouble(i));
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
        System.out.println(temperature + "°C");
    }

    public double getTemperature() {
        return temperature;
    }

}

class Humidity {
    private final int humidity;

    public Humidity(int humidity) {
        this.humidity = humidity;
        System.out.println(humidity + "%");
    }

    public int getHumidity() {
        return humidity;
    }

}

class Precipitation {
    private final double precipitation;

    public Precipitation(double precipitation) {
        this.precipitation = precipitation;
        System.out.println(precipitation + "mm");
    }

    public double getPrecipitation() {
        return precipitation;
    }

}

