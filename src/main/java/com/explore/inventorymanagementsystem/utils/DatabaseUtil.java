package com.explore.inventorymanagementsystem.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DatabaseUtil {
    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());
    // private static SQLServerDataSource dataSource = null;
    private static String url;
    private static Properties props;

    static {
        initializeDataSource();
    }

    private static void initializeDataSource() {
        try {
            props = loadProperties();
            url = props.getProperty("db.server") + ":" + props.getProperty("db.port") + "/" + props.getProperty("db.name")
                    + "?serverTimezone=UTC&useSSL=true&requireSSL=false";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize database connection", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/database.properties")) {
            props.load(fis);
        }
        return props;
    }

    public static Connection getConnection() throws SQLException {
        // TODO (Richie): Implement database connection
        // 1. Load database properties
        // 2. Establish connection using JDBC
        // 3. Handle connection errors
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing connection", e);
            }
        }
    }

    // Example method to test connection
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            LOGGER.log(Level.INFO, "connection established");
            if (conn != null) {
                conn.isClosed();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection test failed", e);
        }
    }

    public static void initializeDatabase() {
        // TODO (Richie): Implement database initialization
        // 1. Create tables if they don't exist
        // 2. Set up initial data if needed
        // 3. Verify database schema
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static void main(String[] args){
        testConnection();
    }
}