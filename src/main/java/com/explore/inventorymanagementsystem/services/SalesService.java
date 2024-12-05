package com.explore.inventorymanagementsystem.services;

import com.explore.inventorymanagementsystem.models.Product;
import com.explore.inventorymanagementsystem.models.Sales;
import com.explore.inventorymanagementsystem.utils.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.logging.Level;

public class SalesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesService.class);

    public boolean createItem(Sales item) {
        // TODO (Richie): Implement sales record creation
        // 1. Prepare SQL INSERT statement for sales table
        // 2. Set parameters from Sales object (invoice number, customer details, amounts)
        // 3. Execute update and handle errors
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String getLastSalesItem() {
        // TODO (Richie): Retrieve most recent sales record
        // 1. Query sales table for most recent entry
        // 2. Extract and return invoice number
        // 3. Handle case when no sales exist
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ObservableList<Sales> getAllSales() {
        // TODO (Richie): Implement sales record retrieval
        // 1. Execute SELECT query on sales table
        // 2. Convert ResultSet to Sales objects
        // 3. Return ObservableList of sales
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Method to get the total quantity sold
    public String getTotalSales() {
        String sql = "SELECT SUM(quantity) AS total_sale FROM sales";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getString("total_sale") == null ? "0" : resultSet.getString("total_sale");
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching total sales", e);
        }
        return "0";
    }

    // Method to get the sales details of the current month
    public String getSalesDetailsOfThisMonth(String monthName) {
        // TODO (Richie): Implement monthly sales calculation
        // 1. Query sales for specified month
        // 2. Calculate total amount
        // 3. Handle null results and format output
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public ObservableList<Sales> getAllItems() {
        // TODO (Efe): Implement sales record retrieval and management
        // 1. Design data retrieval strategy
        // 2. Implement error handling protocol
        // 3. Coordinate with UI team for data display
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Method to get the items sold for the current month
    public String getItemSoldThisMonth(String monthName) {

        String sql = "SELECT SUM(quantity) AS total_sold FROM sales WHERE MONTHNAME(date) = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, monthName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int totalSold = rs.getInt("total_sold");
                    return totalSold == 0 ? "No sales this month" : String.valueOf(totalSold);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching items sold this month for {}", monthName, e);
        }
        return "Error retrieving sales data";
    }

    public boolean updateSales(Sales item) {
        String sql = "UPDATE sales SET invoice_number = ?, customer_id = ?, customer_name = ?, " +
                "price = ?, quantity = ?, total_amount = ?, date = ?, item_num = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getInvoiceNumber());
            pstmt.setInt(2, item.getCustomerId());
            pstmt.setString(3, item.getCustomerName());
            pstmt.setDouble(4, item.getPrice());
            pstmt.setInt(5, item.getQuantity());
            pstmt.setDouble(6, item.getTotalAmount());
            pstmt.setDate(7, Date.valueOf(item.getDate()));
            pstmt.setString(8, item.getItemNum());
            pstmt.setInt(9, item.getId()); // Assuming Sales has an ID field to identify the record

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error updating sales item {}", e);
            return false;
        }
    }

    public boolean deleteSales(Integer id) {
        String sql = "DELETE FROM sales WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error deleting inventory item", e, Level.SEVERE);
            return false;
        }
    }
}
