package com.example.watertall;

import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;

public class Database
{
    private Plant plant;

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

    public void setPlantData(Integer plantId) {
        String query = "SELECT naam_plant, planttype, min_water, max_water, min_optimumtemperatuur, max_optimumtemperatuur " +
                "FROM watertall.profiel_plant WHERE plant_id = ?";
        try (Connection connection = getConnection(); // Ensure you're getting a new connection here
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, plantId);  // Use plant_id to fetch plant data

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    this.plant = new Plant(
                            plantId,
                            resultSet.getString("naam_plant"),
                            resultSet.getString("planttype"),
                            resultSet.getDouble("min_water"),
                            resultSet.getDouble("max_water"),
                            resultSet.getDouble("min_optimumtemperatuur"),
                            resultSet.getDouble("max_optimumtemperatuur")
                    );
                } else {
                    System.out.println("No plant found for ID: " + plantId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public Plant getPlant() {
        return plant;
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
