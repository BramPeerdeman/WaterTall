package com.example.watertall;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class Main extends Application {

    private double x, y = 0;

    public Main() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Laad de login.fxml als de startpagina
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

        // Zorg ervoor dat het venster niet standaard decoraties heeft
        primaryStage.initStyle(StageStyle.UNDECORATED);

        // Maak het venster verplaatsbaar door het vast te pakken
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

        // Stel de scène in met de afmetingen 700x400
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
