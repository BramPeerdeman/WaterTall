package com.example.watertall;

import java.sql.Connection;
import java.sql.DriverManager;
import io.github.cdimascio.dotenv.Dotenv;

public class Database
{
    public Connection databaseLink;
    Dotenv dotenv = Dotenv.configure().load();

    public Connection getConnection()
    {
        String dbName = dotenv.get("DB_NAME");
        String dbUser = dotenv.get("DB_USER");
        String dbPass = dotenv.get("DB_PASS");
        String dbURL = "jdbc:mysql://localhost:3306/" + dbName;

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(dbURL, dbUser, dbPass);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return databaseLink;
    }
}
