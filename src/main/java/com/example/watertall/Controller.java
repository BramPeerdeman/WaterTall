package com.example.watertall;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Controller implements Initializable
{

    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;

    @FXML
    private Label locationLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

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
    public void getLocation()
    {
        new Thread(() ->
        {
            try
            {
                URL url = new URL("http://ip-api.com/json/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();
                String location = parseLocation(jsonResponse);

                Platform.runLater(() ->
                {
                    if (location != null)
                    {
                        locationLabel.setText("Location: " + location);
                    } else
                    {
                        showError("Unable to determine location.");
                    }
                });
            } catch (Exception e)
            {
                Platform.runLater(() -> showError("An error occurred while fetching location."));
            }
        }).start();
    }

    private String parseLocation(String json)
    {
        try
        {
            if (json.contains("\"city\":\"") && json.contains("\"regionName\":\""))
            {
                String city = json.split("\"city\":\"")[1].split("\"")[0];
                String region = json.split("\"regionName\":\"")[1].split("\"")[0];
                return city + ", " + region;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private void showError(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    // Methode om de registratie pagina te openen
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