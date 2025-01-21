package com.example.watertall;

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

import java.io.IOException;

public class infoController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;


    @FXML
    public void initialize() {


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

    public void switchToSoftware(ActionEvent event) {
        switchToScene(event, "software.fxml");
    }

    public void switchToPrivacy(ActionEvent event) {
        switchToScene(event, "privacy.fxml");
    }

    public void switchToVragen(ActionEvent event) {
        switchToScene(event, "vragen.fxml");
    }

    public void switchToOver(ActionEvent event) {
        switchToScene(event, "over.fxml");
    }
}
