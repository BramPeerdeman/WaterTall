package com.example.watertall;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUser {

    public boolean login(String email, String password) {
        Database database = new Database();
        String query = "SELECT wachtwoord FROM klant WHERE email = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("wachtwoord");
                // Vergelijk het ingevoerde wachtwoord met de hash in de database
                return BCrypt.checkpw(password, hashedPassword);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Geeft een fout melding aan als de inlog gegevens niet klopen
    }
}
