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

    public boolean createInvoice(Invoice invoice) {
        // TODO (Richie): Implement invoice creation in database
        // 1. Prepare SQL INSERT statement for invoices table
        // 2. Set parameters from Invoice object
        // 3. Execute update and handle errors
        throw new UnsupportedOperationException("Not implemented yet");
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
    public ObservableList<Invoice> getAllInvoices() {
        // TODO (Richie): Implement invoice retrieval
        // 1. Query all records from invoices table
        // 2. Convert ResultSet to Invoice objects
        // 3. Return as ObservableList
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Update an invoice's status and amount
    public void updateInvoice(Integer id, int quantity, double unitPrice) {
        // TODO (Richie): Implement invoice update in database
        // 1. Prepare SQL UPDATE statement
        // 2. Calculate new total price
        // 3. Execute update and handle errors
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Delete an invoice by ID
    public void deleteInvoice(int id) {
        // TODO (Steven): Implement invoice deletion
        // 1. Verify database state before/after deletion
        // 2. Test error handling scenarios
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public String calculateFinalAmount() {
        // TODO (Steven): Implement final amount calculation with testing
        // 1. Verify calculation accuracy
        // 2. Test edge cases (null values, zero amounts)
        throw new UnsupportedOperationException("Not implemented yet");
    }
}



