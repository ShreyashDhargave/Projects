package com.bank.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Database Configuration Manager
 * Handles database connection properties securely
 */
public class DatabaseConfig {
    private static final String CONFIG_FILE = "database.properties";
    private static Properties properties;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                // Use default values if config file not found
                setDefaultProperties();
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Error loading database properties: " + e.getMessage());
            setDefaultProperties();
        }
    }
    
    private static void setDefaultProperties() {
        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/bank_management");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "root");
        properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
    }
    
    public static String getUrl() {
        return properties.getProperty("db.url");
    }
    
    public static String getUsername() {
        return properties.getProperty("db.username");
    }
    
    public static String getPassword() {
        return properties.getProperty("db.password");
    }
    
    public static String getDriver() {
        return properties.getProperty("db.driver");
    }
}
