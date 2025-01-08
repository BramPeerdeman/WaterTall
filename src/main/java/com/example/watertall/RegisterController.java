package com.example.watertall;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    public void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Controleer of alle velden ingevuld zijn
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Alle velden zijn verplicht!");
            errorLabel.setVisible(true);
        }
        // Controleer of de wachtwoorden overeenkomen
        else if (!password.equals(confirmPassword)) {
            errorLabel.setText("Wachtwoorden komen niet overeen!");
            errorLabel.setVisible(true);
        }
        else {
            // Probeer de gebruiker te registreren
            RegisterUser registerUser = new RegisterUser();
            boolean isRegistered = registerUser.register(username, email, password);

            // Als registratie succesvol is
            if (isRegistered) {
                errorLabel.setText("Registratie succesvol!");
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setVisible(true);

                // Wacht een moment en ga terug naar login
                try {
                    Thread.sleep(2000); // Wacht 2 seconden voor de gebruiker om de boodschap te zien
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    // Laad de login pagina
                    redirectToLogin(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Als registratie mislukt
                errorLabel.setText("Er is iets misgegaan. Probeer opnieuw.");
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setVisible(true);
            }
        }
    }

    // Functie om naar de loginpagina te navigeren
    private void redirectToLogin(ActionEvent event) throws IOException {
        // Laad de login pagina
        Parent loginPage = FXMLLoader.load(getClass().getResource("login.fxml"));

        // Haal het huidige venster (Stage) op
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Stel de nieuwe Scene in
        stage.setScene(new Scene(loginPage, 700, 400));
        stage.show();
    }
}
