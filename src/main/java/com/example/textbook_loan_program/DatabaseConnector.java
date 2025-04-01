package com.example.textbook_loan_program;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

    public static Connection getConnection() throws SQLException {
        Properties props = DatabaseConfig.loadProperties();
        if (props == null) throw new SQLException("⚠ Failed to load DB properties!");

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.username");
        String pass = props.getProperty("db.password");

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("⚠ SQL Server driver not found!", e);
        }

        return DriverManager.getConnection(url, user, pass);
    }

}
