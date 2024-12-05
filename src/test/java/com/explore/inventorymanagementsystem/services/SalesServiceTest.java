package com.explore.inventorymanagementsystem.services;

import com.explore.inventorymanagementsystem.models.Sales;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class SalesServiceTest {
    private SalesService salesService;
    private Connection connection;

    @BeforeEach
    public void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        salesService = new SalesService();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE sales (" +
                    "id INT PRIMARY KEY, " +
                    "invoice_number VARCHAR(255), " +
                    "customer_id INT, " +
                    "customer_name VARCHAR(255), " +
                    "price DOUBLE, " +
                    "quantity INT, " +
                    "total_amount DOUBLE, " +
                    "date VARCHAR(255), " +
                    "item_num VARCHAR(255))");
        }
    }

    @AfterEach
    public void teardown() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE sales");
        }
        connection.close();
    }

    @Test
    public void testCreateItem() {
        Sales sales = new Sales(
                1,
                "INV001",
                101,
                "John Doe",
                100.0,
                5,
                500.0,
                "2024-12-01",
                "ITEM001"
        );

        boolean result = salesService.createItem(sales);

        assertTrue(result, "Sales record should be created successfully");
    }

    @Test
    public void testGetTotalSales() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO sales VALUES (1, 'INV001', 101, 'John Doe', 100.0, 5, 500.0, '2024-12-01', 'ITEM001')");
            stmt.execute("INSERT INTO sales VALUES (2, 'INV002', 102, 'Jane Smith', 200.0, 10, 2000.0, '2024-12-02', 'ITEM002')");
        } catch (Exception e) {
            fail("Failed to insert test data into the database");
        }

        String totalSales = salesService.getTotalSales();

        assertEquals("15", totalSales, "Total sales should be 15");
    }

    @Test
    public void testGetItemSoldThisMonth() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO sales VALUES (1, 'INV001', 101, 'John Doe', 100.0, 5, 500.0, '2024-12-01', 'ITEM001')");
            stmt.execute("INSERT INTO sales VALUES (2, 'INV002', 102, 'Jane Smith', 200.0, 10, 2000.0, '2024-12-02', 'ITEM002')");
        } catch (Exception e) {
            fail("Failed to insert test data into the database");
        }

        String itemsSold = salesService.getItemSoldThisMonth("December");

        assertEquals("15", itemsSold, "Total items sold in December should be 15");
    }
}
