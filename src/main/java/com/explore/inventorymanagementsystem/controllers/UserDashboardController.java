package com.explore.inventorymanagementsystem.controllers;

import com.explore.inventorymanagementsystem.HelloApplication;
import com.explore.inventorymanagementsystem.models.*;
import com.explore.inventorymanagementsystem.services.ProductService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;

public class UserDashboardController {

    @FXML
    private AnchorPane billing_pane;

    @FXML
    private TableView<Reportable> product_table;

    @FXML
    private TableColumn<?, ?> col_product_item_num;

    @FXML
    private TableColumn<?, ?> col_bill_price;

    @FXML
    private TableColumn<?, ?> col_product_quantity;

    @FXML
    private TableColumn<?, ?> col_product_total_amt;

    @FXML
    private TableColumn<?, ?> col_product_name;

    @FXML
    private TableColumn<?, ?> col_product_price;

    @FXML
    private Button signout_btn;

    @FXML
    private Label user;

    @FXML
    void activateAnchorPane(MouseEvent event) {

    }

    @FXML
    void loadProductTable(ActionEvent event) {

    }

    @FXML
    void loadPurchaseTable(ActionEvent event) {

    }

    @FXML
    void onExit(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void signOut(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("views/Login.fxml"));
                Parent root = loader.load();

                String username = user.getText();
                LoginController.sessions.remove(username);

                Stage stage = (Stage) signout_btn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                LOGGER.error("Failed to log out: ", e);
            }
        });
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDashboardController.class);
    ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ProductService productService = new ProductService();

    @FXML
    public void initialize() {
        // TODO (Kyle): Initialize UI components
        // 1. Set up product_table columns with PropertyValueFactory
        // 2. Load initial product data into table
        // 3. Set up event listeners for table row selection
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        // TODO (Efe): Implement sign-out functionality
        // 1. Close current stage
        // 2. Redirect to login screen
        // 3. Clear any session data
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void loadProductData() {
        // TODO (Richie): Load product data from database
        // 1. Fetch product list using productService
        // 2. Populate product_table with data
        // 3. Handle any data retrieval errors
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void updateProductTable() {
        // TODO (Kyle): Update product table with new data
        // 1. Refresh table view with updated product list
        // 2. Ensure UI reflects any changes in data
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
