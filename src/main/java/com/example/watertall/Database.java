package com.example.watertall;

import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;

public class Database
{
    public Connection databaseLink;
    Dotenv dotenv = Dotenv.configure().load();

    public Connection getConnection()
    {
        String dbName = dotenv.get("DB_NAME");
        String dbUser = dotenv.get("DB_USER");
        String dbPass = dotenv.get("DB_PASS");
        String dbURL = "jdbc:mysql://localhost:3306/" + dbName;

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(dbURL, dbUser, dbPass);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return databaseLink;
    }

    public void setPlantData (Integer id) {
        try (Statement stmt = databaseLink.createStatement()) {
            ResultSet plantRs = stmt.executeQuery("SELECT naam_plant, planttype, min_water, max_water, min_temperatuur, max_temperatuur FROM plant WHERE plant_id = 1");
            plantRs.next();
            new Plant(1,
                    plantRs.getString("naam_plant"),
                    plantRs.getString("planttype"),
                    plantRs.getDouble("min_water"), //water is in bodemvochtigheids%
                    plantRs.getDouble("max_water"),
                    plantRs.getDouble("min_temperatuur"), // in celcius
                    plantRs.getDouble("max_temperatuur"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

class Plant {
    private final Integer id;
    private final String naam;
    private final String plantType;
    private final Double minWater;
    private final Double maxWater;
    private final Double minOptimumTemp;
    private final Double maxOptimumTemp;

    public Plant(Integer id, String naam, String plantType, Double minWater, Double maxWater, Double minOptimumTemp, Double maxOptimumTemp) {
        this.id = id;
        this.naam = naam;
        this.plantType = plantType;
        this.minWater = minWater;
        this.maxWater = maxWater;
        this.minOptimumTemp = minOptimumTemp;
        this.maxOptimumTemp = maxOptimumTemp;
    }

    public Integer getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public String getPlantType() {
        return plantType;
    }

    public Double getMinWater() {
        return minWater;
    }

    public Double getMaxWater() {
        return maxWater;
    }

    public Double getMinOptimumTemp() {
        return minOptimumTemp;
    }

    public Double getMaxOptimumTemp() {
        return maxOptimumTemp;
    }
}
