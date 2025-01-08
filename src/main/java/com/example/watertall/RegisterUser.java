package com.example.watertall;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterUser {

    public boolean register(String naam, String email, String wachtwoord) {
        Database database = new Database();
        Connection conn = database.getConnection();

        // Hash het wachtwoord
        String hashedPassword = hashPassword(wachtwoord);

        String query = "INSERT INTO klant (naam, email, wachtwoord) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, naam);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, hashedPassword);  // Opslaan van de gehashte versie

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String hashPassword(String password) {
        // maakt een Hash in de database aan
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
