package com.example.watertall;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SproeiAlgorithme {
    private weatherAPI weatherAPI;
    private Database database;
    private Plant plant;

    public SproeiAlgorithme (weatherAPI weatherAPI, Database database, Plant plant) {
        this.weatherAPI = weatherAPI;
        this.database = database;
        this.plant = plant;
    }

    public void test () {
        System.out.println(weatherAPI.getHumidity().getHumidity());
        System.out.println(database.getPlant().getMaxOptimumTemp());
    }

    public double waterBenodigdheid() {
        double waterBenodigdheid;
        double temperatuur = weatherAPI.getTemperature().getTemperature();
        double minTemp = plant.getMinOptimumTemp();
        double maxTemp = plant.getMaxOptimumTemp();
        double minWater = plant.getMinWater();
        double maxWater = plant.getMaxWater();
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
        return waterBenodigdheid;
    }

    public static double Algorithme(weatherAPI weatherAPI, Database database, Plant plant) {
        SproeiAlgorithme sproeiAlgorithme = new SproeiAlgorithme(weatherAPI, database, plant);
        double waterBenodigdheid = sproeiAlgorithme.waterBenodigdheid() * 10.23;
        System.out.println(waterBenodigdheid + " bodemvochtigheid");
        return waterBenodigdheid; // Return the calculated value
    }

//    public static void main(String[] args) {
//        //GEBRUIK DEZE VOOR DE PROJECT
//        SproeiAlgorithme.Algorithme();
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.add(Calendar.HOUR_OF_DAY, 1);
//        long initialDelay = calendar.getTimeInMillis() - System.currentTimeMillis();
//        long period = 60 * 30 * 1000;
//        scheduler.scheduleAtFixedRate(SproeiAlgorithme::Algorithme, initialDelay, period, TimeUnit.MILLISECONDS);
//    }
}


