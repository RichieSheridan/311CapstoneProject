package com.explore.inventorymanagementsystem.controllers;

import com.explore.inventorymanagementsystem.models.*;
import com.explore.inventorymanagementsystem.services.InvoiceService;
import com.explore.inventorymanagementsystem.services.ProductService;
import com.explore.inventorymanagementsystem.services.SalesService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PurchaseModalController {
    @FXML
    private ComboBox<Integer> quantityField;
    @FXML
    private Label productLabel;

    private Product product;
    private User user;
    private Stage stage;

    private final ProductService productService = new ProductService();
    private final InvoiceService invoiceService = new InvoiceService();
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseModalController.class);
    private final SalesService salesService = new SalesService();
    
    public void setProduct(Product product) {
        this.product = product;

        if (product != null) {
            productLabel.setText(product.getName());
        }
    }
    
    public void setUser(User user){
        this.user = user;
    }

    @FXML
    private void handlePurchaseButtonAction() {
        int quantity = Integer.parseInt(quantityField.getSelectionModel().getSelectedItem().toString());

        // Open confirmation modal
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Purchase");
        confirmationAlert.setHeaderText("Are you sure you want to purchase " + quantity + " units of " + product.getName() + "?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            processPurchase(quantity);
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        stage.close();
    }

    @FXML
    private void processPurchase(int quantity) {
        Stage stage = (Stage) quantityField.getScene().getWindow();
        stage.close();

        Invoice invoice = new Invoice(generateInvoiceNumber(), quantity, product.getPrice(), calculateTotalPrice(product, quantity), LocalDateTime.now());
        Sales sales = new Sales(invoice.getItemId(), user.getId(), user.getUsername(), product.getPrice(), quantity, calculateTotalPrice(product, quantity), LocalDate.now().toString(), product.getId().toString());
        product.setQuantity(product.getQuantity() - quantity);
        productService.updateItem(product);

        salesService.createItem(sales);
        invoiceService.createInvoice(invoice);
    }

    private String generateInvoiceNumber() {
        // TODO (Efe): Implement invoice number generation
        // 1. Generate unique invoice number based on current date/time
        // 2. Ensure no duplicates in database
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private double calculateTotalPrice(Product product, int quantity) {
        return product.getPrice() * quantity;
    }

    public void setQuantityOptions() {
        ObservableList<Integer> quantityOptions = FXCollections.observableArrayList();
        for (int i = 1; i <= product.getQuantity(); i++) {
            quantityOptions.add(i);
        }

        quantityField.setItems(quantityOptions);
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            stage = (Stage) quantityField.getScene().getWindow();
            setQuantityOptions();
        });
    }
}
