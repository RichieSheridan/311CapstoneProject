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
        String sql = "INSERT INTO sales (invoice_number, customer_id, customer_name, price, quantity, total_amount, date, item_num) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Assuming Product has these getter methods
            pstmt.setString(1, item.getInvoiceNumber()); // Invoice number
            pstmt.setInt(2, item.getCustomerId()); // Customer ID
            pstmt.setString(3, item.getCustomerName()); // Customer name
            pstmt.setDouble(4, item.getPrice()); // Price
            pstmt.setInt(5, item.getQuantity()); // Quantity
            pstmt.setDouble(6, item.getTotalAmount()); // Total amount
            pstmt.setDate(7, Date.valueOf(item.getDate())); // Date, assuming LocalDate in Product
            pstmt.setString(8, item.getItemNum()); // Item number

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error creating inventory item {}", e);
            return false;
        }
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
        String sql = "SELECT SUM(total_amount) AS total_sales_this_month FROM SALES WHERE MONTHNAME(DATE) = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, monthName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("total_sales_this_month") == null ? "0.00" : resultSet.getString("total_sales_this_month");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching sales details for this month", e);
        }
        return "0.00";
    }

    public ObservableList<Sales> getAllItems() {
        String sql = "SELECT * FROM sales";
        ObservableList<Sales> sales = FXCollections.observableArrayList();;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                sales.add(new Sales(
                        rs.getInt("id"),
                        rs.getString("invoice_number"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getDouble("total_amount"),
                        rs.getString("date"),
                        rs.getString("item_num")
                ));
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching all sales items", e);
        }
        return sales;
    }

    // Method to get the items sold for the current month
    public String getItemSoldThisMonth(String monthName) {
        String sql = "SELECT SUM(quantity) AS total_items_sold_this_month FROM SALES WHERE MONTHNAME(DATE) = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, monthName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("total_items_sold_this_month") == null ? "0" : resultSet.getString("total_items_sold_this_month");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching items sold for this month", e);
        }
        return "0";
    }

    public String getLastSalesItem() {
        String sql = "SELECT invoice_number FROM sales ORDER BY date DESC LIMIT 1"; // Assuming `date` is a timestamp column to order by most recent

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("invoice_number");
            } else {
                LOGGER.warn("No sales data found in the database.");
                return null;
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching last sales item", e);
            return null;
        }
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
