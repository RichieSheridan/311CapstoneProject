package com.explore.inventorymanagementsystem.controllers;

import com.explore.inventorymanagementsystem.HelloApplication;
import com.explore.inventorymanagementsystem.models.*;
import com.explore.inventorymanagementsystem.services.InvoiceService;
import com.explore.inventorymanagementsystem.services.ProductService;
import com.explore.inventorymanagementsystem.services.PurchaseService;
import com.explore.inventorymanagementsystem.services.SalesService;
import com.explore.inventorymanagementsystem.utils.ReportGeneratorUtil;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.controlsfx.control.textfield.TextFields;

public class DashboardController {

    @FXML
    private DatePicker bill_date;

    @FXML
    private TextField bill_item;

    @FXML
    private TextField bill_name;

    @FXML
    private TextField bill_phone;

    @FXML
    private TextField bill_price;

    @FXML
    private ComboBox<Integer> bill_quantity;
    @FXML
    private TextField bill_total_amount;

    @FXML
    private AnchorPane billing_pane;

    @FXML
    private TableView<Invoice> billing_table;

    @FXML
    private TableView<Reportable> product_table;

    @FXML
    private TextField billing_table_search;

    @FXML
    private TableColumn<Invoice, Integer> col_bill_item_num;

    @FXML
    private TableColumn<Invoice, String> col_bill_price;

    @FXML
    private TableColumn<Invoice, Integer> col_bill_quantity;

    @FXML
    private TableColumn<Invoice, Double> col_bill_total_amt;

    @FXML
    private TableColumn<Product, Integer> col_product_item_num;

    @FXML
    private TableColumn<Product, String> col_product_name;

    @FXML
    private TableColumn<Product, Integer> col_product_quantity;

    @FXML
    private TableColumn<Product, Integer> col_product_price;

    @FXML
    private TableColumn<Product, Integer> col_product_total_amt;

    @FXML
    private AnchorPane customer_pane;

    @FXML
    private AnchorPane dasboard_pane;

    @FXML
    private Label final_amount;

    @FXML
    private AnchorPane purchase_pane;

    @FXML
    private AnchorPane sales_pane;

    @FXML
    private Button signout_btn;

    @FXML
    private Label ItemDescription;

    @FXML
    private Label ItemName;

    @FXML
    private Label ItemPrice;

    @FXML
    private Label ItemSupplier;

    @FXML
    private Label ItemTotalAmount;

    @FXML
    private Label ItemQuantity;

    @FXML
    private Label ItemDate;

    @FXML
    private Label user;

    private final ProductService productService = new ProductService();
    private final PurchaseService purchaseService = new PurchaseService();
    private List<Product> productList = new ArrayList<>();
    private final InvoiceService invoiceService = new InvoiceService();
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);
    private static final Integer[] quantityList = { 1,2, 3, 4, 5, 6, 7, 8, 9, 10};
    private final SalesService salesService = new SalesService();
    private ObservableList<Reportable> originalItems;

    @FXML
    void onExit(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    public void downloadReport (){
        try {
            switch (currentMode){
                case PRODUCT ->
                        ReportGeneratorUtil.generateDatabaseReport("products", "Product Report", "product_report_" + LocalDateTime.now());
                case SALES ->
                        ReportGeneratorUtil.generateDatabaseReport("sales", "Sales Report", "sales_report" + LocalDateTime.now());
                case INVOICE ->
                        ReportGeneratorUtil.generateDatabaseReport("invoices", "Invoice Report", "invoice_report_" + LocalDateTime.now());
                case PURCHASE ->
                        ReportGeneratorUtil.generateDatabaseReport("purchases", "Purchase Report", "purchase_report_" + LocalDateTime.now());
                default -> {
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error generating report: {}", e.getMessage());
        }
    }

    public ObservableList<Product> getItemsList(){
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        try {
            productsList = productService.getAllItems();
        } catch (Exception err){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeight(500);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText(err.getMessage());
            alert.showAndWait();
        }
        return productsList;
    }

    public ObservableList<Sales> getSalesList(){
        ObservableList<Sales> salesList = FXCollections.observableArrayList();
        try {
            salesList = salesService.getAllItems();
        } catch (Exception err){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeight(500);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText(err.getMessage());
            alert.showAndWait();
        }
        return salesList;
    }

    public ObservableList<Invoice> getInvoiceList(){
        ObservableList<Invoice> invoiceList = FXCollections.observableArrayList();
        try {
            invoiceList = invoiceService.getAllInvoices();
        } catch (Exception err){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeight(500);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText(err.getMessage());
            alert.showAndWait();
        }
        return invoiceList;
    }

    public ObservableList<Purchase> getPurchaseList(){
        ObservableList<Purchase> purchaseList = FXCollections.observableArrayList();
        try {
            purchaseList = purchaseService.getAllPurchases();
        } catch (Exception err){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeight(500);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText(err.getMessage());
            alert.showAndWait();
        }
        return purchaseList;
    }

    @FXML
    public void loadProductTable () {
        setMode(Mode.PRODUCT);

        col_product_item_num.setText("ID");
        col_product_name.setText("Product Name");
        col_product_quantity.setText("Quantity");
        col_product_price.setText("Price");
        col_product_total_amt.setText("Reorder Point");

        showProductData();

        productList = getItemsList();
        product_table.getItems().setAll(productList);
        billing_table.setVisible(false);
    }

    @FXML
    private void loadSalesTable() {
        setMode(Mode.SALES);
        col_product_item_num.setText("ID");
        col_product_name.setText("Invoice Number");
        col_product_quantity.setText("Customer Name");
        col_product_price.setText("Total Price");
        col_product_total_amt.setText("Date");

        col_product_item_num.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_product_name.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        col_product_quantity.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        col_product_price.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        col_product_total_amt.setCellValueFactory(new PropertyValueFactory<>("date"));
        ObservableList<Sales> salesList = getSalesList();
        ObservableList<Reportable> reportableList = convertToReportableList(salesList);
        product_table.setItems(reportableList);
        billing_table.setVisible(false);
    }

    @FXML
    private void loadPurchaseTable() {
        setMode(Mode.PURCHASE);

        col_product_name.setText("Supplier");
        col_product_quantity.setText("Status");
        col_bill_price.setText("Total Price");
        col_bill_total_amt.setText("Quantity");

        col_product_item_num.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_product_name.setCellValueFactory(new PropertyValueFactory<>("supplierInfo"));
        col_product_quantity.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_bill_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        col_bill_total_amt.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        ObservableList<Purchase> purchaseList = getPurchaseList();
        ObservableList<Reportable> reportableList = convertToReportableList(purchaseList);
        product_table.setItems(reportableList);
        billing_table.setVisible(false);
    }

    @FXML
    private void loadInvoiceTable() {
        setMode(Mode.INVOICE);
        col_product_item_num.setText("ID");
        col_product_name.setText("Quantity");
        col_product_quantity.setText("Unit Price");
        col_product_price.setText("Total Price");
        col_product_total_amt.setText("Sale Date");

        col_product_item_num.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_product_name.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_product_quantity.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        col_product_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        col_product_total_amt.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
//        ObservableList<Invoice> invoiceList = getInvoiceList();
//        product_table.setItems(invoiceList);
        ObservableList<Invoice> invoiceList = getInvoiceList();
        ObservableList<Reportable> reportableList = convertToReportableList(invoiceList);
        product_table.setItems(reportableList);
        billing_table.setVisible(false);
    }

    private <T extends Reportable> ObservableList<Reportable> convertToReportableList(ObservableList<T> list) {
        return FXCollections.observableArrayList(list);
    }

    private Mode currentMode;

    public void setMode(Mode mode) {
        currentMode = mode;
        switch (mode) {
            case SALES:
                ItemName.setText("Invoice Number");
                ItemDescription.setText("Customer Name");
                ItemPrice.setText("Price");
                ItemTotalAmount.setText("Total Amount");
                ItemSupplier.setText("Invoice Number");

                bill_name.setPromptText("Generated Invoice Number");
                bill_name.setText(generateInvoiceNumber());
                bill_name.setEditable(false);
                bill_phone.setPromptText("Enter Customer Name");

                break;

            case INVOICE:
                ItemName.setText("Product ID");
                ItemDescription.setText("Quantity");
                ItemPrice.setText("Unit Price");
                ItemTotalAmount.setText("Total Price");
                ItemDate.setText("Sale Date");

//                bill_name.setPromptText("Enter Quantity");
//                bill_phone.setPromptText("Enter Sale Date");
                bill_name.setPromptText("Enter Product Id");
                // bill_phone.setPromptText("Enter Status");
                bill_phone.setEditable(false);
                bill_item.setEditable(false);
                bill_phone.setEditable(true);

                break;

            case PURCHASE:
                ItemName.setText("Item ID");
                ItemDescription.setText("Supplier Info");
                ItemPrice.setText("Unit Price");
                ItemTotalAmount.setText("Total Price");
                ItemDate.setText("Purchase Date");

                bill_name.setPromptText("Enter Supplier Info");
                bill_phone.setPromptText("Enter Status");
                bill_phone.setEditable(true);

                break;

            case PRODUCT:
                ItemName.setText("Product Name");
                ItemDescription.setText("Product Description");
                ItemPrice.setText("Unit Price");
                ItemTotalAmount.setText("Total Price");
                ItemSupplier.setText("Supplier");

                bill_name.setPromptText("Enter Product Name");
                bill_phone.setPromptText("Enter Product Description");
                bill_phone.setEditable(true);
                bill_item.setPromptText("Enter Supplier Info");
                bill_price.setPromptText("Enter Product Price");
                bill_total_amount.setPromptText("Enter Total amount");
        }
    }

    public void showProductData() {
        // TODO (Kyle): Implement product data display
        // 1. Get product list from service
        // 2. Set up table column cell value factories
        // 3. Convert to reportable list and update table
        // 4. Set current date in date picker
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @FXML
    private void searchValues() {
        // TODO (Kyle): Implement search functionality for the product table
        // 1. Get search text from billing_table_search TextField
        // 2. Filter items in product_table based on:
        //    - Product name (case-insensitive)
        //    - Date
        // 3. Update table with filtered results
        // 4. Show alert if no matches found
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @FXML
    private void openEditModal(Object item, Mode mode) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("views/EditItemModal.fxml"));
            Parent root = loader.load();
            EditItemModalController controller = loader.getController();
            controller.setItemData(item, mode);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 500, 500));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            showErrorAlert("Error opening Modal");
        }
    }

    @FXML
    private void addBillingData() {
        switch (currentMode) {
            case SALES:
                saveSalesData();
                break;
            case INVOICE:
                saveInvoiceData();
                break;
            case PURCHASE:
                savePurchaseData();
                break;
            case PRODUCT:
                saveProductData();
                break;
        }
    }

    private void saveSalesData() {
        boolean status = salesService.createItem(new Sales(
                generateInvoiceNumber(),
                Integer.parseInt(bill_phone.getText()),
                bill_name.getText(),
                Double.parseDouble(bill_price.getText()),
                Integer.parseInt(bill_quantity.getValue().toString()),
                Double.parseDouble(bill_total_amount.getText()),
                bill_date.getValue().toString(),
                bill_item.getText()
        ));

        if (!status) {
            showErrorAlert("Error Saving new Sales Data");
        } else {
            showSuccessAlert("Saved Sales Data");
            ObservableList<Sales> salesList = getSalesList();
            ObservableList<Reportable> reportableList = convertToReportableList(salesList);
            product_table.setItems(reportableList);
        }
    }

    private void saveInvoiceData() {
        // TODO (Richie): Implement invoice data persistence
        // 1. Create new Invoice object using:
        //    - Generated invoice number
        //    - Quantity from bill_quantity
        //    - Price from bill_price
        //    - Total amount from bill_total_amount
        //    - Current date/time
        // 2. Save to database using invoiceService
        // 3. Handle success/failure scenarios
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void saveProductData() {
        boolean status = productService.createItem(new Product(
                bill_name.getText(),
                bill_phone.getText(),
                Integer.parseInt(bill_quantity.getValue().toString()),
                Double.parseDouble(bill_price.getText()),
                ItemSupplier.getText(),
                bill_date.getValue().toString(),
                10 // reorder Amount
        ));

        if (!status) {
            showErrorAlert("Error Saving new Product Data");
        } else {
            showSuccessAlert("Saved Product Data");
            ObservableList<Product> productList = getItemsList();
            ObservableList<Reportable> reportableList = convertToReportableList(productList);
            product_table.setItems(reportableList);
        }
    }

    private void savePurchaseData() {
        // TODO (Richie): Implement purchase data persistence
        // 1. Create new Purchase object using form data:
        //    - Item ID, quantity, price, total amount
        //    - Current date/time
        //    - Supplier info and PENDING status
        // 2. Save to database using purchaseService
        // 3. Update UI based on success/failure
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private String generateInvoiceNumber() {
        // TODO (Efe): Implement invoice number generation
        // 1. Get current invoice count
        // 2. Format new invoice number as: "INV-2024-XXX" 
        //    where XXX is padded count + 1
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private int getInvoiceCount() {
        // TODO (Richie): Implement invoice counting logic
        // 1. Get last invoice number from salesService
        // 2. Extract and parse the numeric portion
        // 3. Handle null/invalid cases
        // 4. Return current count or 0 if none exist
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void billClearData() {
        bill_item.clear();
        bill_name.clear();
        bill_phone.clear();
        bill_price.clear();
        bill_quantity.getSelectionModel().clearSelection();
        bill_total_amount.clear();
        bill_date.setValue(LocalDate.now());
    }

    public void setUsername(String username){
        user.setText(username);
    }

    public void activateDashboard() {
        // TODO (Sam): Implement dashboard activation
        // 1. Show dashboard pane
        // 2. Hide all other panes (billing, customer, sales, purchase)
        // 3. Update UI state
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ObservableList<Invoice> listBillingData() {
        // TODO (Richie): Implement billing data retrieval
        // 1. Create observable list for invoices
        // 2. Get all invoices from service
        // 3. Handle any database errors
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void calculateFinalAmount() {
        // TODO (Richie): Calculate and display final amount
        // 1. Get final amount from invoice service
        // 2. Update final_amount label
        // 3. Handle any calculation errors
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void showBillingData() {
        // TODO (Kyle): Implement billing data display
        // 1. Get billing list from listBillingData()
        // 2. Set up table column cell value factories
        // 3. Update billing_table with data
        // 4. Set current date in date picker
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void comboBoxQuantity(){
        ObservableList<Integer> comboList= FXCollections.observableArrayList(quantityList);
        bill_quantity.setItems(comboList);
    }

    @FXML
    public void signOut(){
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

    @FXML
    public void initialize() {
        setMode(Mode.PRODUCT);
        loadProductTable();
        showProductData();
        comboBoxQuantity();


        product_table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !product_table.getSelectionModel().isEmpty()) {
                Object selectedItem = product_table.getSelectionModel().getSelectedItem();

                if (selectedItem instanceof Product) {
                    openEditModal(selectedItem, Mode.PRODUCT);
                } else if (selectedItem instanceof Purchase) {
                    openEditModal(selectedItem, Mode.PURCHASE);
                } else if (selectedItem instanceof Invoice) {
                    openEditModal(selectedItem, Mode.INVOICE);
                } else if (selectedItem instanceof Sales) {
                    openEditModal(selectedItem, Mode.SALES);
                }
            }
        });
        originalItems = FXCollections.observableArrayList(product_table.getItems());
        billing_table_search.setOnKeyTyped(event -> searchValues());
    }
}
