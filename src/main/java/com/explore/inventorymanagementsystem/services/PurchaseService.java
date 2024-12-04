package com.explore.inventorymanagementsystem.services;

import com.explore.inventorymanagementsystem.models.Invoice;
import com.explore.inventorymanagementsystem.models.Purchase;
import com.explore.inventorymanagementsystem.models.Status;
import com.explore.inventorymanagementsystem.utils.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

public class PurchaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseService.class);

    // Create
    public boolean createPurchase(Purchase purchase) {
        // TODO (Richie): Implement purchase record creation
        // 1. Prepare SQL INSERT statement for purchases table
        // 2. Set parameters from Purchase object
        // 3. Execute update and handle errors
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Purchase getPurchasesById(UUID id) {
        String sql = "SELECT * FROM purchases WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Purchase(
                            rs.getInt("itemId"),
                            rs.getInt("quantity"),
                            rs.getDouble("unitPrice"),
                            rs.getDouble("totalPrice"),
                            rs.getDate("purchaseDate").toLocalDate().atStartOfDay(),
                            rs.getString("supplierInfo"),
                            Status.valueOf(rs.getString("status"))
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching inventory item", e);
        }
        return null;
    }

    public String getTotalPurchase() {
        String sql = "SELECT SUM(total_price) AS total_purchase FROM purchases";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getString("total_purchase") == null ? "0" : resultSet.getString("total_purchase");
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching total purchase", e);
        }
        return "0";
    }

    public ObservableList<Purchase> getAllPurchases() {
        // TODO (Richie): Implement purchase record retrieval
        // 1. Execute SELECT query on purchases table
        // 2. Convert ResultSet to Purchase objects
        // 3. Return ObservableList of purchases
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean updatePurchase(Purchase purchase) {
        String sql = "UPDATE purchases SET item_id = ?, quantity = ?, unit_price = ?, total_price = ?, " +
                "purchase_date = ?, supplier_info = ?, status = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, purchase.getItemId());
            pstmt.setInt(2, purchase.getQuantity());
            pstmt.setDouble(3, purchase.getUnitPrice());
            pstmt.setDouble(4, purchase.getTotalPrice());
            pstmt.setTimestamp(5, Timestamp.valueOf(purchase.getPurchaseDate()));
            pstmt.setString(6, purchase.getSupplierInfo());
            pstmt.setString(7, purchase.getStatus().toString());
            pstmt.setInt(8, purchase.getId()); // Assuming Purchase has an ID field to identify the record

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error updating purchase {}", e);
            return false;
        }
    }

    public boolean deletePurchase(Integer id) {
        String sql = "DELETE FROM purchases WHERE id = ?";

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
