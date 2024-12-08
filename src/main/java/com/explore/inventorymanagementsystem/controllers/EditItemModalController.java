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
        this.selectedItem = item;
        this.currentMode = mode;

        if (mode == Mode.INVOICE && item instanceof Invoice invoice) {
            invoiceNumberField.setVisible(false);
            idField.setText(String.valueOf(invoice.getId()));
            itemIdField.setText(invoice.getItemId());
            quantityField.setText(String.valueOf(invoice.getQuantity()));
            unitPriceField.setText(String.valueOf(invoice.getUnitPrice()));
            totalPriceField.setText(String.valueOf(invoice.getTotalPrice()));
            saleDateField.setText(invoice.getSaleDate().format(formatter));
            // Populate additional Invoice fields here

        } else if (mode == Mode.PRODUCT && item instanceof Product product) {
            idField.setText(String.valueOf(product.getId()));
            invoiceNumberField.setVisible(true);
            invoiceNumberField.setText(product.getName());
            customerNameField.setText(product.getDescription());
            quantityField.setText(String.valueOf(product.getQuantity()));
            unitPriceField.setText(String.valueOf(product.getPrice()));
            itemIdField.setText(product.getCategory());
            saleDateField.setText(product.getSupplier());
            totalPriceField.setText(String.valueOf(product.getReorderPoint()));

        } else if (mode == Mode.PURCHASE && item instanceof Purchase purchase) {
            idField.setText(String.valueOf(purchase.getItemId()));
            invoiceNumberField.setVisible(false);
            quantityField.setText(String.valueOf(purchase.getQuantity()));
            unitPriceField.setText(String.valueOf(purchase.getUnitPrice()));
            totalPriceField.setText(String.valueOf(purchase.getTotalPrice()));
            saleDateField.setText(purchase.getPurchaseDate().format(formatter));
            itemIdField.setText(purchase.getSupplierInfo());
            customerNameField.setText(purchase.getStatus().toString());

        } else if (mode == Mode.SALES && item instanceof Sales sale) {
            invoiceNumberField.setVisible(true);
            invoiceNumberField.setText(sale.getInvoiceNumber());
            idField.setText(String.valueOf(sale.getCustomerId()));
            customerNameField.setText(sale.getCustomerName());
            unitPriceField.setText(String.valueOf(sale.getPrice()));
            quantityField.setText(String.valueOf(sale.getQuantity()));
            totalPriceField.setText(String.valueOf(sale.getTotalAmount()));
            saleDateField.setText(sale.getDate());
            itemIdField.setText(sale.getItemNum());
        }
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
        if (currentMode == Mode.INVOICE && selectedItem instanceof Invoice invoice) {
            invoice.setItemId(itemIdField.getText());
            invoice.setQuantity(Integer.parseInt(quantityField.getText()));
            invoice.setUnitPrice(Double.parseDouble(unitPriceField.getText()));
            invoice.setTotalPrice(Double.parseDouble(totalPriceField.getText()));
            // Update additional fields and save to the database
            try {
                invoiceService.updateInvoice(invoice.getId(), invoice.getQuantity(), invoice.getUnitPrice());
            } catch (Exception e){
                LoggerFactory.getLogger(EditItemModalController.class).error(e.getMessage());
            }
            modalStage.close();
        } else if (currentMode == Mode.PRODUCT && selectedItem instanceof Product product) {
            product.setName(invoiceNumberField.getText());
            product.setDescription(customerNameField.getText());
            product.setQuantity(Integer.parseInt(quantityField.getText()));
            product.setPrice(Double.parseDouble(unitPriceField.getText()));
            product.setCategory(itemIdField.getText());
            product.setSupplier(saleDateField.getText());
            product.setReorderPoint(Integer.parseInt(totalPriceField.getText()));

            try {
                productService.updateItem(product);

            } catch (Exception e){
                LoggerFactory.getLogger(EditItemModalController.class).error(e.getMessage());
            }

        } else if (currentMode == Mode.PURCHASE && selectedItem instanceof Purchase purchase) {
            purchase.setQuantity(Integer.parseInt(quantityField.getText()));
            purchase.setUnitPrice(Double.parseDouble(unitPriceField.getText()));
            purchase.setTotalPrice(Double.parseDouble(totalPriceField.getText()));
            purchase.setSupplierInfo(itemIdField.getText());
            // Update status if necessary

            try {
                purchaseService.updatePurchase(purchase);

            } catch (Exception e){
                LoggerFactory.getLogger(EditItemModalController.class).error(e.getMessage());
            }
            modalStage.close();

        } else if (currentMode == Mode.SALES && selectedItem instanceof Sales sale) {
            sale.setInvoiceNumber(invoiceNumberField.getText());
            sale.setCustomerId(Integer.parseInt(idField.getText()));
            sale.setCustomerName(customerNameField.getText());
            sale.setPrice(Double.parseDouble(unitPriceField.getText()));
            sale.setQuantity(Integer.parseInt(quantityField.getText()));
            sale.setTotalAmount(Double.parseDouble(totalPriceField.getText()));

            try {
                salesService.updateSales(sale);

            } catch (Exception e){
                LoggerFactory.getLogger(EditItemModalController.class).error(e.getMessage());
            }
            modalStage.close();
        }
    }

    @FXML
    private void onDelete(){
        if (currentMode == Mode.INVOICE && selectedItem instanceof Invoice invoice) {
            try {
                invoiceService.deleteInvoice(invoice.getId());
            } catch (Exception e){
                LoggerFactory.getLogger(EditItemModalController.class).error(e.getMessage());
            }
            modalStage.close();
        } else if (currentMode == Mode.PRODUCT && selectedItem instanceof Product product) {
            try {
                productService.deleteItem(product.getId());

            } catch (Exception e){
                LoggerFactory.getLogger(EditItemModalController.class).error(e.getMessage());
            }
            modalStage.close();
        } else if (currentMode == Mode.PURCHASE && selectedItem instanceof Purchase purchase) {
            try {
                purchaseService.deletePurchase(purchase.getId());

            } catch (Exception e){
                LoggerFactory.getLogger(EditItemModalController.class).error(e.getMessage());
            }
            modalStage.close();

        } else if (currentMode == Mode.SALES && selectedItem instanceof Sales sale) {
            try {
                salesService.deleteSales(sale.getId());

            } catch (Exception e){
                LoggerFactory.getLogger(EditItemModalController.class).error(e.getMessage());
            }
            modalStage.close();
        }
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
}
