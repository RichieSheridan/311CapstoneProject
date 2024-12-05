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
    private void handleLogin(ActionEvent event) {
        // TODO (Efe): Implement login functionality
        // 1. Validate username and password fields
        // 2. Authenticate user using UserService
        // 3. Redirect to dashboard on success
        // 4. Display error message on failure
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @FXML
    public void initialize() {
        // TODO (Kyle): Initialize login UI components
        // 1. Set up event listeners for login button and enter key
        // 2. Style UI elements using styles.css
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @FXML
    void onEnter(ActionEvent event) {
        Login(event);
    }
}
