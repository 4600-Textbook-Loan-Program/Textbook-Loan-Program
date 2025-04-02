package com.example.textbook_loan_program.config;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    public static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("⚠ Could not find config.properties");
                return null;
            }
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }
}
