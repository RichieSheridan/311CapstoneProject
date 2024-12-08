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
        // Get the selected quantity from the dropdown
        int quantity = Integer.parseInt(quantityField.getSelectionModel().getSelectedItem().toString());

        // Open confirmation modal
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Purchase");
        confirmationAlert.setHeaderText("Are you sure you want to purchase " + quantity + " units of " + product.getName() + "?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // If the user clicks "OK", proceed with the purchase
            processPurchase(quantity);
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        stage.close();
    }

    @FXML
    private void processPurchase(int quantity) {
        // This method processes the purchase, updates the inventory, creates the invoice, and logs the sale
        Stage stage = (Stage) quantityField.getScene().getWindow();
        stage.close();

        // Add purchase to database
        Invoice invoice = new Invoice(generateInvoiceNumber(), quantity, product.getPrice(), calculateTotalPrice(product, quantity), LocalDateTime.now());
        Sales sales = new Sales(invoice.getItemId(), user.getId(), user.getUsername(), product.getPrice(), quantity, calculateTotalPrice(product, quantity), LocalDate.now().toString(), product.getId().toString());
        product.setQuantity(product.getQuantity() - quantity);
        productService.updateItem(product);

        salesService.createItem(sales);
        invoiceService.createInvoice(invoice);

    }

    // Retrieves the last invoice number from the sales service and extracts the last 3 digits
    private int getInvoiceCount() {
        String lastInvoiceNumber = salesService.getLastSalesItem();

        if (lastInvoiceNumber != null) {
            Pattern pattern = Pattern.compile("(\\d{3})$"); // Regex to match the last 3 digits
            Matcher matcher = pattern.matcher(lastInvoiceNumber);

            // If the regex matches, extract and return the last 3 digits
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            } else {
                // Log a warning if the invoice number format is invalid
                LOGGER.warn("Invoice number format invalid: " + lastInvoiceNumber);
            }
        }

        // Return 0 if the invoice number cannot be found or matched
        return 0;
    }

    private String generateInvoiceNumber() {
        // Get the last invoice count and generate a new invoice number
        int invoiceCount = getInvoiceCount();
        return String.format("INV-2024-%03d", invoiceCount + 1);
    }

    private double calculateTotalPrice(Product product, int quantity) {
        return product.getPrice() * quantity;
    }

    public void setQuantityOptions() {
        // Create a list to store the available quantity options
        ObservableList<Integer> quantityOptions = FXCollections.observableArrayList();

        // Add numbers from 1 to the available product quantity in the dropdown
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
