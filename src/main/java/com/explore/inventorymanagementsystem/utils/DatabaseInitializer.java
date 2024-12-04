package com.explore.inventorymanagementsystem.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void executeSqlScript(Connection connection, String filePath) throws SQLException, IOException {
        StringBuilder sqlStatements = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    sqlStatements.append(line);
                    sqlStatements.append(" ");
                }
            }
        }

        String[] statements = sqlStatements.toString().split(";");

        try (Statement statement = connection.createStatement()) {
            for (String stmt : statements) {
                if (!stmt.trim().isEmpty()) {
                    System.out.println("Executing: " + stmt.trim() + "\n");
                    statement.execute(stmt.trim());
                }
            }
        }
    }

    public static void main(String[] args) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            executeSqlScript(connection, "src/main/resources/data.sql");
            System.out.println("Data loaded successfully!");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}