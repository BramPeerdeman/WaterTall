package com.example.watertall;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField; // Aangepast naar 'emailField'

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel; // Label voor foutmeldingen

    public void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Maak een instance van LoginUser om de login te verwerken
        LoginUser loginUser = new LoginUser();

        // Controleer inloggegevens
        if (loginUser.login(email, password)) {
            System.out.println("Login succesvol!");
            errorLabel.setText(""); // Wis foutmeldingen bij een succesvolle login

            // Laad de nieuwe pagina (homepage.fxml)
            try {
                redirectToSettings(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Onjuiste gebruikersnaam of wachtwoord.");
            errorLabel.setText("Onjuiste e-mailadres of wachtwoord.");
        }
    }

    private void redirectToSettings(ActionEvent event) throws IOException {
        // Laad de homepage.fxml
        Parent settingsPage = FXMLLoader.load(getClass().getResource("homepage.fxml"));

        // Haal het huidige venster (Stage) op
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Stel de nieuwe Scene in
        stage.setScene(new Scene(settingsPage, 1000, 700));
        stage.show();
    }

    // Methode voor navigeren naar de registratiepagina
    public void handleRegisterRedirect(ActionEvent event) {
        try {
            // Laad de registreren.fxml
            Parent registerPage = FXMLLoader.load(getClass().getResource("registreren.fxml"));

            // Haal het huidige venster (Stage) op
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Stel de nieuwe Scene in
            stage.setScene(new Scene(registerPage, 1000, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
