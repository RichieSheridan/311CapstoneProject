package com.explore.inventorymanagementsystem.controllers;

import com.explore.inventorymanagementsystem.models.*;
import com.explore.inventorymanagementsystem.services.InvoiceService;
import com.explore.inventorymanagementsystem.services.ProductService;
import com.explore.inventorymanagementsystem.services.PurchaseService;
import com.explore.inventorymanagementsystem.services.SalesService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EditItemModalController {
    @FXML
    private TextField customerNameField;

    @FXML
    private Button iconUpdate;

    @FXML
    private TextField idField;

    @FXML
    private TextField invoiceNumberField;

    @FXML
    private TextField itemIdField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField saleDateField;

    @FXML
    private TextField totalPriceField;

    @FXML
    private TextField unitPriceField;


    private Object selectedItem;

    private Mode currentMode;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ProductService productService = new ProductService();
    private final PurchaseService purchaseService = new PurchaseService();
    private final InvoiceService invoiceService = new InvoiceService();
    private final SalesService salesService = new SalesService();

    Stage modalStage;

    public void setItemData(Object item, Mode mode) {
        // TODO (Richie): Load item data into modal fields
        // 1. Check mode and cast item to appropriate type (e.g., Invoice)
        // 2. Populate fields with item data (e.g., id, quantity, price)
        // 3. Handle any data conversion or formatting
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @FXML
    private void enableEditing() {
        invoiceNumberField.setEditable(true);
        customerNameField.setEditable(true);
        itemIdField.setEditable(true);
        quantityField.setEditable(true);
        unitPriceField.setEditable(true);
        totalPriceField.setEditable(true);
        saleDateField.setEditable(true);
    }

    @FXML
    private void saveChanges() {
        // TODO (Efe): Implement save changes functionality
        // 1. Validate input data from modal fields
        // 2. Update item in database using appropriate service
        // 3. Close modal and refresh parent view
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @FXML
    public void onDelete() {
        // TODO (Efe): Implement item deletion
        // 1. Get selected item details
        // 2. Show confirmation dialog
        // 3. Delete from appropriate service
        // 4. Update UI and close modal
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @FXML
    public void onSave() {
        // TODO (Efe): Implement save functionality
        // 1. Validate form fields
        // 2. Update item using appropriate service
        // 3. Show success/error message
        // 4. Close modal on success
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Error Updating Item, please try again.");
        alert.showAndWait();
    }

    @FXML
    public void initialize(){
        Platform.runLater(() -> {
                modalStage = (Stage) idField.getScene().getWindow();
        });
    }

    @FXML
    private void cancelEdit() {
        // TODO (Sam): Implement cancel edit functionality
        // 1. Close modal without saving changes
        // 2. Optionally confirm cancellation with user
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
