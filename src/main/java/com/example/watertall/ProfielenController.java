package com.example.watertall;

import java.io.IOException;
import java.sql.*;

import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ProfielenController
{
    private Stage stage;
    private Scene scene;
    private Parent root;
    private final Database database = new Database();

    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;

    @FXML
    private ComboBox<String> plantTypeComboBox; // ComboBox for plant types

    @FXML
    private ComboBox<String> plantNameComboBox; // ComboBox for plant names

    @FXML
    private Label naamPlantLabel;

    @FXML
    private Label planttypeLabel;

    @FXML
    private Label minWaterLabel;

    @FXML
    private Label maxWaterLabel;

    @FXML
    private Label minTempLabel;

    @FXML
    private Label maxTempLabel;

    @FXML
    private Label maxSpeedLabel;

    @FXML
    private Label minMoistLabel;

    private SerialController serialController;

    @FXML
    public void initialize() {
        loadPlantTypes();

        serialController = new SerialController("COM6", 115200);
        serialController.start();

        plantNameComboBox.setOnAction(event -> {
            String selectedPlantName = plantNameComboBox.getValue();
            if (selectedPlantName != null) {
                loadPlantDetails(selectedPlantName);
            }
        });

        Exit.setOnMouseClicked(event ->
        {
            System.exit(0);
        });
        slider.setTranslateX(-176);

        Menu.setOnMouseClicked(event ->
        {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e)->
            {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });

        MenuClose.setOnMouseClicked(event ->
        {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)->
            {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });
    }

    @FXML
    private void handleSaveProfile(ActionEvent event) {
        try {
            String selectedPlantName = plantNameComboBox.getValue();

            if (selectedPlantName != null) {
                Integer plantId = getPlantIdByName(selectedPlantName);

                if (plantId != null) {
                    database.setPlantData(plantId);
                    Plant selectedPlant = database.getPlant();

                    if (selectedPlant == null) {
                        System.err.println("Selected plant is null. Check database query.");
                        return;
                    }

                    weatherAPI weatherAPI = new weatherAPI();
                    weatherAPI.weerUpdate();

                    // Debug weather data
                    System.out.println("Weather API Temperature: " + weatherAPI.getTemperature().getTemperature());
                    System.out.println("Weather API Humidity: " + weatherAPI.getHumidity().getHumidity());

                    // Calculate bodemvochtigheid
                    double bodemvochtigheid = SproeiAlgorithme.Algorithme(weatherAPI, database, selectedPlant);

                    // Check bodemvochtigheid value
                    if (Double.isNaN(bodemvochtigheid) || Double.isInfinite(bodemvochtigheid)) {
                        System.err.println("Invalid bodemvochtigheid value: " + bodemvochtigheid);
                        return;
                    }

                    System.out.println("Bodemvochtigheid: " + bodemvochtigheid);

                    // Send the value to the micro:bit
                    if (serialController != null) {
                        System.out.println("Test: " + bodemvochtigheid);
                        serialController.sendData(String.valueOf(bodemvochtigheid));
                    } else {
                        System.err.println("SerialController is not initialized!");
                    }
                } else {
                    System.err.println("Plant ID not found!");
                }
            } else {
                System.err.println("No plant selected!");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log unexpected errors
        }
    }





    private Integer getPlantIdByName(String plantName) {
        String query = "SELECT plant_id FROM watertall.profiel_plant WHERE naam_plant = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, plantName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("plant_id");  // Return the plant_id
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no plant_id is found
    }

    private void loadPlantDetails(String plantName) {
        String query = "SELECT naam_plant, planttype, min_water, max_water, min_optimumtemperatuur, max_optimumtemperatuur, max_speed, min_moisture " +
                "FROM watertall.profiel_plant WHERE naam_plant = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, plantName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String naamPlant = resultSet.getString("naam_plant");
                    String plantType = resultSet.getString("planttype");
                    String minWater = resultSet.getString("min_water");
                    String maxWater = resultSet.getString("max_water");
                    String minTemp = resultSet.getString("min_optimumtemperatuur");
                    String maxTemp = resultSet.getString("max_optimumtemperatuur");
                    Integer maxSpeed = resultSet.getInt("max_speed");
                    Integer minMoisture = resultSet.getInt("min_moisture");

                    System.out.println("Plant Name: " + naamPlant);

                    // Update the labels with the fetched data
                    naamPlantLabel.setText(naamPlant);
                    planttypeLabel.setText(plantType);
                    minWaterLabel.setText(minWater);
                    maxWaterLabel.setText(maxWater);
                    minTempLabel.setText(minTemp);
                    maxTempLabel.setText(maxTemp);
                    maxSpeedLabel.setText(String.valueOf(maxSpeed));
                    minMoistLabel.setText(String.valueOf(minMoisture));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void switchToScene(ActionEvent event, String fxmlFile) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFile));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, display an error message to the user
        }
    }

    public void switchToHome(ActionEvent event) {
        switchToScene(event, "homepage.fxml");
    }

    public void switchToInstellingen(ActionEvent event) {
        switchToScene(event, "instellingen.fxml");
    }

    public void switchToProfielen(ActionEvent event) {
        switchToScene(event, "profiel.fxml");
    }


    // Method to load plant types into ComboBox
    @FXML
    private void loadPlantTypes() {
        ObservableList<String> plantTypes = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT planttype FROM watertall.profiel_plant";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                plantTypes.add(resultSet.getString("planttype"));
            }
            plantTypeComboBox.setItems(plantTypes);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add a listener to handle plant type selection
        plantTypeComboBox.setOnAction(event -> {
            String selectedPlantType = plantTypeComboBox.getValue();
            if (selectedPlantType != null) {
                loadPlantNames(selectedPlantType);
            }
        });
    }

    // Method to load plant names based on selected plant type
    private void loadPlantNames(String plantType) {
        ObservableList<String> plantNames = FXCollections.observableArrayList();
        String query = "SELECT naam_plant FROM watertall.profiel_plant WHERE planttype = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, plantType);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    plantNames.add(resultSet.getString("naam_plant"));
                }
            }
            plantNameComboBox.setItems(plantNames);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
