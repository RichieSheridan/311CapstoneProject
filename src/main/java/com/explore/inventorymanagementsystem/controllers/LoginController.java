package com.explore.inventorymanagementsystem.controllers;

import com.explore.inventorymanagementsystem.HelloApplication;
import com.explore.inventorymanagementsystem.models.User;
import com.explore.inventorymanagementsystem.services.UserService;
import com.explore.inventorymanagementsystem.utils.PasswordUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    @FXML
    private Button button_login;

    @FXML
    private ImageView image1;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label status;

    @FXML
    private TextField textField_username;

    @FXML
    private Label title;

    public static Map<String, User> sessions = new HashMap<>();

    private UserService userService = new UserService();

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @FXML
    void Login(ActionEvent event) {
        button_login.setDisable(true);
        String username = textField_username.getText();
        String password = passwordField.getText();

        User user = userService.getUserByUsername(username);

        // Check if the user exists and if the entered password matches the stored password
        if (user != null && passwordMatches(user.getPassword(), password)) {
            // If login is successful, store the user in the session and display a success message
            sessions.put(username, user);
            status.setText("Login successful!");
            status.setStyle("-fx-text-fill: green;");
            button_login.setDisable(false);
            if (user.getRole().equals("ADMIN")) {
                openAdminDashboard();
            } else {
                openUserDashboard();
            }
        } else {
            // If the login credentials are invalid, display an error message
            status.setText("Invalid credentials.");
            status.setStyle("-fx-text-fill: red;");
            button_login.setDisable(false);
        }
    }

    private boolean passwordMatches(String storedPassword, String enteredPassword) {
        return PasswordUtil.verifyPassword(enteredPassword, storedPassword);
    }

    private void openAdminDashboard() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("views/dashboard.fxml"));
                Parent root = loader.load();

                DashboardController dashboardController = loader.getController();
                String username = textField_username.getText();
                User currentUser = sessions.get(username);
                dashboardController.setUsername(currentUser.getUsername());

                Stage stage = (Stage) button_login.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                LOGGER.error("Failed to load the dashboard: ", e);
                status.setText("System error!");
                status.setStyle("-fx-text-fill: red;");
            }
        });
    }

    private void openUserDashboard() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("views/userDashboard.fxml"));
                Parent root = loader.load();

                UserDashboardController dashboardController = loader.getController();
                String username = textField_username.getText();
                User currentUser = sessions.get(username);
                dashboardController.setUsername(currentUser.getUsername());

                Stage stage = (Stage) button_login.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Failed to load the dashboard: ", e);
                status.setText("System error!");
                status.setStyle("-fx-text-fill: red;");
            }
        });
    }

    @FXML
    void onEnter(ActionEvent event) {
        Login(event);
    }
}
