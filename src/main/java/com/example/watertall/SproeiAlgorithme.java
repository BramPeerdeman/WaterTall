package com.example.watertall;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SproeiAlgorithme {
    private weatherAPI weatherAPI;
    private Database database;

    public SproeiAlgorithme (weatherAPI weatherAPI, Database database) {
        this.weatherAPI = weatherAPI;
        this.database = database;

    }

    public void test () {
        System.out.println(weatherAPI.getHumidity().getHumidity());
        System.out.println(database.getPlant().getMaxOptimumTemp());
    }

    public double waterBenodigdheid () {
        double waterBenodigdheid;
        double temperatuur = weatherAPI.getTemperature().getTemperature();
        double minTemp = database.getPlant().getMinOptimumTemp();
        double maxTemp = database.getPlant().getMaxOptimumTemp();
        double minWater = database.getPlant().getMinWater();
        double maxWater = database.getPlant().getMaxWater();
        waterBenodigdheid = (minWater + maxWater) / 2;
        if (temperatuur < minTemp) {
            waterBenodigdheid = minWater;
        }
        if (temperatuur > maxTemp) {
            waterBenodigdheid = maxWater;
        }
        double luchtVochtigheid = weatherAPI.getHumidity().getHumidity();
        if (luchtVochtigheid < 83) {
            waterBenodigdheid = waterBenodigdheid + ((waterBenodigdheid - minWater) * 0.2);
        }
        if (luchtVochtigheid < 50) {
            waterBenodigdheid = waterBenodigdheid + ((waterBenodigdheid - minWater) * 0.5);
        }
        if (luchtVochtigheid > 87) {
            waterBenodigdheid = waterBenodigdheid - ((waterBenodigdheid - minWater) * 0.9);
        }
        return waterBenodigdheid - weatherAPI.getPrecipitation().getPrecipitation();
    }

    public static void Algorithme() {
        weatherAPI weatherAPI = new weatherAPI();
        weatherAPI.weerUpdate();
        Database database = new Database();
        database.getConnection();
        database.setPlantData(11);
        SproeiAlgorithme sproeiAlgorithme = new SproeiAlgorithme(weatherAPI, database);
        double waterBenodigdheid = sproeiAlgorithme.waterBenodigdheid() * 10.23;
        System.out.println(waterBenodigdheid + "bodemvochtigheid");
    }

    public static void main(String[] args) {
        //GEBRUIK DEZE VOOR DE PROJECT
        SproeiAlgorithme.Algorithme();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        long initialDelay = calendar.getTimeInMillis() - System.currentTimeMillis();
        long period = 60 * 30 * 1000;
        scheduler.scheduleAtFixedRate(SproeiAlgorithme::Algorithme, initialDelay, period, TimeUnit.MILLISECONDS);
    }
}


