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
import java.util.Arrays;
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

    public void checkForPriceAndQuantity(){
        if(!bill_price.getText().isBlank()&& !bill_quantity.getSelectionModel().isEmpty()){
            bill_total_amount.setText(String.valueOf(Integer.parseInt(bill_price.getText())*Integer.parseInt(bill_quantity.getValue().toString())));
        }else{
            bill_total_amount.setText("0");
        }
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
        col_product_price.setText("Total Price");
        col_product_total_amt.setText("Quantity");

        col_product_item_num.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_product_name.setCellValueFactory(new PropertyValueFactory<>("supplierInfo"));
        col_product_quantity.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_product_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        col_product_total_amt.setCellValueFactory(new PropertyValueFactory<>("quantity"));

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
                bill_name.clear();
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

                bill_name.clear();
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
                bill_name.clear();
        }
    }

    public void showProductData(){
        ObservableList<Product> productsList = getItemsList();
        col_product_item_num.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_product_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_product_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_product_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_product_total_amt.setCellValueFactory(new PropertyValueFactory<>("reorderPoint"));

        ObservableList<Reportable> reportableList = convertToReportableList(productsList);
        product_table.setItems(reportableList);

        // product_table.setItems(productsList);
        LocalDate date = LocalDate.now();
        bill_date.setValue(date);
    }

    @FXML
    private void searchValues() {
        String searchText = billing_table_search.getText().trim().toLowerCase();
        var allItems = product_table.getItems();

        if (searchText.isEmpty() ) {
            product_table.setItems(originalItems);
            return;
        }

        ObservableList<Reportable> filteredItems = allItems.filtered(item ->
                item.getName().toLowerCase().contains(searchText) ||
                        item.getDate().contains(searchText) ||
                        item.getName().toLowerCase().contains(searchText)
        );

        if (filteredItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Result");
            alert.setHeaderText(null);
            alert.setContentText("No matching items found.");
            alert.showAndWait();
        }

        product_table.setItems(filteredItems);
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
        boolean status = invoiceService.createInvoice(new Invoice(
                generateInvoiceNumber(),
                Integer.parseInt(bill_quantity.getValue().toString()),
                Double.parseDouble(bill_price.getText()),
                Double.parseDouble(bill_total_amount.getText()),
                bill_date.getValue().atStartOfDay()
        ));

        if (!status) {
            showErrorAlert("Error Saving new Invoice Data");
        } else {
            showSuccessAlert("Saved Invoice Data");
            ObservableList<Invoice> invoiceList = getInvoiceList();
            ObservableList<Reportable> reportableList = convertToReportableList(invoiceList);
            product_table.setItems(reportableList);
        }
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
        boolean status = purchaseService.createPurchase(new Purchase(
                bill_item.getText(),
                Integer.parseInt(bill_quantity.getValue().toString()),
                Double.parseDouble(bill_price.getText()),
                Double.parseDouble(bill_total_amount.getText()),
                bill_date.getValue().atStartOfDay(),
                ItemSupplier.getText(),
                Status.PENDING
        ));

        if (!status) {
            showErrorAlert("Error Saving new Purchase Data");
        } else {
            showSuccessAlert("Saved Purchase Data");
            ObservableList<Purchase> purchaseList = getPurchaseList();
            ObservableList<Reportable> reportableList = convertToReportableList(purchaseList);
            product_table.setItems(reportableList);
        }
    }

    private String generateInvoiceNumber() {
        int invoiceCount = getInvoiceCount();
        return String.format("INV-2024-%03d", invoiceCount + 1);
    }

    private int getInvoiceCount() {
        String lastInvoiceNumber = salesService.getLastSalesItem();

        if (lastInvoiceNumber != null) {
            Pattern pattern = Pattern.compile("(\\d{3})$");
            Matcher matcher = pattern.matcher(lastInvoiceNumber);

            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            } else {
                LOGGER.warn("Invoice number format invalid: " + lastInvoiceNumber);
            }
        }

        return 0;
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

    public void activateDashboard(){
        dasboard_pane.setVisible(true);
        billing_pane.setVisible(false);
        customer_pane.setVisible(false);
        sales_pane.setVisible(false);
        purchase_pane.setVisible(false);
    }

    public ObservableList<Invoice> listBillingData(){
        ObservableList<Invoice> billingList = FXCollections.observableArrayList();
        try {
            billingList = invoiceService.getAllInvoices();
            return billingList;
        } catch (Exception e){
            LOGGER.error("Error executing invoice data sql statement {}", e.getMessage());
        }
        return billingList;
    }

    public void calculateFinalAmount(){
        try{
            final_amount.setText(invoiceService.calculateFinalAmount());
        }catch (Exception err){
            LOGGER.error("Error executing final amount sql statement {}", err.getMessage());
        }

    }

    public void showBillingData(){
        ObservableList<Invoice> billingList = listBillingData();
        col_bill_item_num.setCellValueFactory(new PropertyValueFactory<>("item_number"));
        col_bill_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_bill_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_bill_total_amt.setCellValueFactory(new PropertyValueFactory<>("total_amount"));

        billing_table.setItems(billingList);
        LocalDate date = LocalDate.now();
        bill_date.setValue(date);
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
