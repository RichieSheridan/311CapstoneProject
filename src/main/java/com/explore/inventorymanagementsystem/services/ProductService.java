package com.explore.inventorymanagementsystem.services;

import com.explore.inventorymanagementsystem.models.Product;
import com.explore.inventorymanagementsystem.utils.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    public boolean createItem(Product item) {
        // TODO (Richie): Implement product creation in database
        // 1. Prepare SQL INSERT statement for products table
        // 2. Set parameters from Product object
        // 3. Execute update and handle errors
        // 4. Return success/failure status
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Read
    public Product getItemById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getDouble("price"),
                            rs.getString("category"),
                            rs.getString("supplier"),
                            rs.getInt("reorder_point")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching inventory item", e, Level.SEVERE);
        }
        return null;
    }

    public ObservableList<Product> getAllItems() {

        String sql = "SELECT * FROM products";
        ObservableList<Product> products = FXCollections.observableArrayList();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getString("supplier"),
                        rs.getInt("reorder_point")
                ));
            }
        } catch (SQLException e) {
            LOGGER.error("Error retrieving products from database", e, Level.SEVERE);
        }
        return products;
    }

    // Update
    public boolean updateItem(Product item) {

        String sql = "UPDATE products SET name = ?, description = ?, quantity = ?, price = ?, category = ?, supplier = ?, reorder_point = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setDouble(4, item.getPrice());
            pstmt.setString(5, item.getCategory());
            pstmt.setString(6, item.getSupplier());
            pstmt.setInt(7, item.getReorderPoint());
            pstmt.setInt(8, item.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            LOGGER.error("Error updating product in database", e, Level.SEVERE);
        }
        return false;
    }

    // Delete
    public boolean deleteItem(Integer id) {
        // TODO (Richie): Implement product deletion from database
        // 1. Prepare SQL DELETE statement
        // 2. Execute update and handle errors
        // 3. Return success/failure status
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Additional utility methods
    public List<Product> getLowStockItems() {
        String sql = "SELECT * FROM products WHERE quantity <= reorder_point";
        List<Product> lowStockItems = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lowStockItems.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getString("supplier"),
                        rs.getInt("reorder_point")
                ));
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching low stock items", e, Level.SEVERE);
        }
        return lowStockItems;
    }
}
