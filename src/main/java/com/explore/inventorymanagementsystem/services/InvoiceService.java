package com.explore.inventorymanagementsystem.services;

import com.explore.inventorymanagementsystem.models.Invoice;
import com.explore.inventorymanagementsystem.utils.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvoiceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);

    public boolean createInvoice(Invoice sale) {
        String sql = "INSERT INTO sales (item_id, quantity, unit_price, total_price, sale_date, customer_info) " +
                "VALUES ( ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // pstmt.setObject(1, sale.getId());
            pstmt.setObject(2, sale.getItemId());
            pstmt.setInt(3, sale.getQuantity());
            pstmt.setDouble(4, sale.getUnitPrice());
            pstmt.setDouble(5, sale.getTotalPrice());
            pstmt.setTimestamp(6, Timestamp.valueOf(sale.getSaleDate()));
            pstmt.setString(7, sale.getCustomerInfo());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error creating invoice {}", String.valueOf(e));
            return false;
        }
    }

    // Retrieve a single invoice by ID
    public Invoice getInvoiceById(int id) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Invoice(
                        rs.getString("item_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("total_price"),
                        rs.getTimestamp("sale_date").toLocalDateTime(),
                        rs.getString("customer_info")
                );
            }
        }
        return null;
    }

    // Retrieve all invoices
    public ObservableList<Invoice> getAllInvoices() throws SQLException {
        ObservableList<Invoice> invoices = FXCollections.observableArrayList();
        String sql = "SELECT * FROM invoices";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                invoices.add(new Invoice(
                        rs.getInt("id"),
                        rs.getString("item_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("total_price"),
                        rs.getTimestamp("sale_date").toLocalDateTime()
                ));
                // rs.getString("customer_info")
            }
        }
        return invoices;
    }

    // Update an invoice's status and amount
    public void updateInvoice(Integer id, int quantity, double unitPrice) throws SQLException {
        String sql = "UPDATE invoices SET quantity = ?, unit_price = ?, total_price = ?, /*customer_info = ?,*/ sale_date = NOW() WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setDouble(2, unitPrice);
            pstmt.setDouble(3, quantity * unitPrice);  // Update total price based on quantity and unit price
            // pstmt.setString(4, customerInfo);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
    }

    // Delete an invoice by ID
    public void deleteInvoice(int id) throws SQLException {
        String sql = "DELETE FROM invoices WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
        }
    }

    public String calculateFinalAmount(){
        String sql="SELECT SUM(total_price) AS final_amount FROM invoices";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery(sql);
            return String.valueOf(rs.toString());
        }catch (Exception err){
            LOGGER.error("Error calculating final amount", err);
        }
        return null;
    }
}



