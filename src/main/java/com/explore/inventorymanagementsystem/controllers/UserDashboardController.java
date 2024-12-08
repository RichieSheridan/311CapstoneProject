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

                // Remove the current user's session from the active sessions list
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
    public void loadProductTable () {
        // setMode(Mode.PRODUCT);

        col_product_item_num.setText("ID");
        col_product_name.setText("Product Name");
        col_product_quantity.setText("Quantity");
        col_product_price.setText("Price");
        col_product_total_amt.setText("Total Amount");

        showProductData();

        productList = displayProducts();
        product_table.getItems().setAll(productList);
    }

    private <T extends Reportable> ObservableList<Reportable> convertToReportableList(ObservableList<T> list) {
        return FXCollections.observableArrayList(list);
    }

    public void showProductData(){
        ObservableList<Product> productsList = displayProducts();
        col_product_item_num.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_product_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_product_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_product_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_product_total_amt.setCellValueFactory(new PropertyValueFactory<>("reorderPoint"));

        // Convert product list to Reportable objects and display in the table
        ObservableList<Reportable> reportableList = convertToReportableList(productsList);
        product_table.setItems(reportableList);
    }

    // Fetches the products from the database and handles errors if they occur
    public ObservableList<Product> displayProducts(){
        try {
            productList = productService.getAllItems();
            return productList;
        } catch (Exception err){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeight(500);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText(err.getMessage());
            alert.showAndWait();
        }
        return null;
    }

    public void setUsername(String username){
        user.setText(username);
    }

    @FXML
    public void handleProductClick(MouseEvent event) {
        // Handles the event when a product is double-clicked in the product table
        if (event.getClickCount() == 2) { // Double-click to open modal
            Product selectedProduct = (Product) product_table.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                openPurchaseModal(selectedProduct);
            }
        }
    }

    private void openPurchaseModal(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("views/purchaseModal.fxml"));
            Parent root = loader.load();

            PurchaseModalController controller = loader.getController();
            controller.setProduct(product);
            controller.setUser(LoginController.sessions.get(user.getText()));

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        loadProductTable();
        showProductData();
        // SetMode
        product_table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !product_table.getSelectionModel().isEmpty()) {
                Product selectedItem = (Product) product_table.getSelectionModel().getSelectedItem();
                openPurchaseModal(selectedItem);
            }
        });

    }

}
