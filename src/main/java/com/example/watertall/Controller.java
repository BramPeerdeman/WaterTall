package com.example.watertall;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller implements Initializable {

    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private ImageView weatherIcon;

    @FXML
    private Label moistureLabel;

    @FXML
    private BarChart<String, Number> precipitationChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private weatherAPI weatherApi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Exit button functionality
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        slider.setTranslateX(-176);

        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });

        MenuClose.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });

        // Initialize weatherAPI and update weather data
        weatherApi = new weatherAPI();
        updateWeatherData();

        // Schedule periodic updates every hour
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateWeatherData, 0, 1, TimeUnit.HOURS);
    }

    private void updateWeatherData() {
        // Run in a separate thread to avoid blocking the UI
        new Thread(() -> {
            // Fetch weather data from the API
            weatherApi.weerUpdate();

            // Get temperature, humidity and precipitation data
            Temperature temperature = weatherApi.getTemperature();  // Haal de temperatuur op
            Humidity humidity = weatherApi.getHumidity();
            Precipitation precipitation = weatherApi.getPrecipitation();
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
            String formattedDate = now.format(formatterDay);

            // Get precipitation data from the API
            JSONArray precipitationArray = weatherApi.getPrecipitationArray();
            JSONArray dayArray = weatherApi.getDayArray();

            Platform.runLater(() -> {
                // Update the labels on the UI
                dateLabel.setText(formattedDate);

                // Update the temperature label
                if (temperature != null) {
                    temperatureLabel.setText(temperature.getTemperature() + "°C");
                } else {
                    temperatureLabel.setText("Geen temperatuur data");
                }

                // Display the air humidity and ground moisture (precipitation)
                if (humidity != null && precipitation != null) {
                    String moistureText = "Luchtvochtigheid: " + humidity.getHumidity() + "%\nGrondvochtigheid: "
                            + precipitation.getPrecipitation() + "mm";
                    moistureLabel.setText(moistureText);
                } else {
                    moistureLabel.setText("Geen data beschikbaar");
                }

                // Handle precipitation chart if necessary
                if (precipitationArray != null && dayArray != null) {
                    xAxis.setLabel("Datum");
                    yAxis.setLabel("Neerslag (mm)");

                    precipitationChart.getData().clear();

                    XYChart.Series<String, Number> precipitationSeries = new XYChart.Series<>();
                    precipitationSeries.setName("Neerslag");

                    for (int i = 0; i < precipitationArray.length(); i++) {
                        String date = dayArray.getString(i);
                        double precipitationValue = precipitationArray.getDouble(i);
                        precipitationSeries.getData().add(new XYChart.Data<>(date, precipitationValue));
                    }

                    precipitationChart.getData().add(precipitationSeries);

                    ObservableList<String> categories = FXCollections.observableArrayList();
                    for (int i = 0; i < dayArray.length(); i++) {
                        categories.add(dayArray.getString(i));
                    }
                    xAxis.setCategories(categories);
                    xAxis.setTickLabelRotation(45);
                }
            });
        }).start();
    }



    public void homeRedirect(ActionEvent event) throws IOException {
        Parent registerPage = FXMLLoader.load(getClass().getResource("homepage.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(registerPage, 1000, 700));
            stage.show();
    }

    public void profielRedirect(ActionEvent event) throws IOException {
        Parent registerPage = FXMLLoader.load(getClass().getResource("profiel.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(registerPage, 1000, 700));
        stage.show();
    }

    public void settingRedirect(ActionEvent event) throws IOException {
        Parent registerPage = FXMLLoader.load(getClass().getResource("instellingen.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(registerPage, 1000, 700));
        stage.show();
    }

    public void handleLoginClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRegisterClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registreren.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
