package com.explore.inventorymanagementsystem.services;

import com.explore.inventorymanagementsystem.models.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {
    private ProductService productService;
    private Connection connection;

    @BeforeEach
    public void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        productService = new ProductService();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE products (" +
                    "id INT PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "description VARCHAR(255), " +
                    "quantity INT, " +
                    "price DOUBLE, " +
                    "category VARCHAR(255), " +
                    "supplier VARCHAR(255), " +
                    "reorder_point INT)");
        }
    }

    @AfterEach
    public void teardown() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE products");
        }
        connection.close();
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product product = new Product(
                1,
                "Laptop",
                "High-end gaming laptop",
                10,
                1500.0,
                "Electronics",
                "TechSupplier",
                5
        );

        boolean result = productService.createItem(product);

        assertTrue(result, "Product creation should succeed.");
    }

    @Test
    public void testRetrieveProductById() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO products VALUES (1, 'Laptop', 'High-end gaming laptop', 10, 1500.0, 'Electronics', 'TechSupplier', 5)");
        }

        Product product = productService.getItemById(1);

        assertNotNull(product, "Product should be retrieved successfully.");
        assertEquals("Laptop", product.getName(), "Product name should match.");
        assertEquals(10, product.getQuantity(), "Product quantity should match.");
        assertEquals(1500.0, product.getPrice(), "Product price should match.");
    }

    @Test
    public void testUpdateProduct() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO products VALUES (1, 'Laptop', 'High-end gaming laptop', 10, 1500.0, 'Electronics', 'TechSupplier', 5)");
        }

        Product updatedProduct = new Product(
                1,
                "Laptop",
                "Updated description",
                20,
                1400.0,
                "Electronics",
                "TechSupplier",
                5
        );

        boolean result = productService.updateItem(updatedProduct);

        assertTrue(result, "Product update should succeed.");

        Product retrievedProduct = productService.getItemById(1);
        assertNotNull(retrievedProduct, "Updated product should be retrieved successfully.");
        assertEquals("Updated description", retrievedProduct.getDescription(), "Product description should match the updated value.");
        assertEquals(20, retrievedProduct.getQuantity(), "Product quantity should match the updated value.");
        assertEquals(1400.0, retrievedProduct.getPrice(), "Product price should match the updated value.");
    }

    @Test
    public void testDeleteProduct() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO products VALUES (1, 'Laptop', 'High-end gaming laptop', 10, 1500.0, 'Electronics', 'TechSupplier', 5)");
        }

        boolean result = productService.deleteItem(1);

        assertTrue(result, "Product deletion should succeed.");

        Product deletedProduct = productService.getItemById(1);
        assertNull(deletedProduct, "Deleted product should no longer exist.");
    }

    @Test
    public void testErrorHandlingForNonExistentProduct() throws Exception {
        Product product = productService.getItemById(999);
        assertNull(product, "Retrieving a non-existent product should return null.");

        boolean result = productService.deleteItem(999);
        assertFalse(result, "Deleting a non-existent product should fail.");
    }
}
