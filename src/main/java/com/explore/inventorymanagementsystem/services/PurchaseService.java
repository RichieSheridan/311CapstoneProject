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
        String sql = "INSERT INTO purchases (item_id, quantity, unit_price, total_price, " +
                "purchase_date, supplier_info, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // pstmt.setObject(1, purchase.getId());
            pstmt.setObject(2, purchase.getItemId());
            pstmt.setInt(3, purchase.getQuantity());
            pstmt.setDouble(4, purchase.getUnitPrice());
            pstmt.setDouble(5, purchase.getTotalPrice());
            pstmt.setTimestamp(6, Timestamp.valueOf(purchase.getPurchaseDate()));
            pstmt.setString(7, purchase.getSupplierInfo());
            pstmt.setString(8, purchase.getStatus().toString());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error creating purchase", e);
            return false;
        }
    }

    public Purchase getPurchasesById(UUID id) {
        String sql = "SELECT * FROM purchases WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Purchase(
                            rs.getString("item_id"),
                            rs.getInt("quantity"),
                            rs.getDouble("unit_price"),
                            rs.getDouble("total_price"),
                            rs.getDate("purchase_date").toLocalDate().atStartOfDay(),
                            rs.getString("supplier_info"),
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

    public ObservableList<Purchase> getAllPurchases() throws SQLException {
        ObservableList<Purchase> purchases = FXCollections.observableArrayList();
        String sql = "SELECT * FROM purchases";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                purchases.add(new Purchase(
                        rs.getInt("id"),
                        rs.getString("item_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("total_price"),
                        rs.getDate("purchase_date").toLocalDate().atStartOfDay(),
                        rs.getString("supplier_info"),
                        Status.valueOf(rs.getString("status"))
                ));
            }
        }
        return purchases;
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
