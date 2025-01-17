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

import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Controller implements Initializable
{

    @FXML
    private ImageView Exit;

//    @FXML
//    private ImageView Maximize;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;

    @FXML
    private Label locationLabel;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label moistureLabel;

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

//        Maximize.setOnMouseClicked(event ->
//        {
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setMaximized(!stage.isMaximized());
//        });
    }

    public void homeRedirect(ActionEvent event) throws IOException
    {
            Parent registerPage = FXMLLoader.load(getClass().getResource("homepage.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(registerPage, 700, 400));
            stage.show();
    }

    public void profielRedirect(ActionEvent event) throws IOException
    {
        Parent registerPage = FXMLLoader.load(getClass().getResource("profiel.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(registerPage, 700, 400));
        stage.show();
    }

    public void settingRedirect(ActionEvent event) throws IOException
    {
        Parent registerPage = FXMLLoader.load(getClass().getResource("homepage.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(registerPage, 700, 400));
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