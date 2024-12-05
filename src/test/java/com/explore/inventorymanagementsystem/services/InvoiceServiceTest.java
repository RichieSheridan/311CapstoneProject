package com.explore.inventorymanagementsystem.services;

import com.explore.inventorymanagementsystem.models.Invoice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceServiceTest {
    private InvoiceService invoiceService;
    private Connection connection;

    @BeforeEach
    public void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        invoiceService = new InvoiceService();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE invoices (" +
                    "id INT PRIMARY KEY, " +
                    "item_id VARCHAR(255), " +
                    "quantity INT, " +
                    "unit_price DOUBLE, " +
                    "total_price DOUBLE, " +
                    "sale_date TIMESTAMP, " +
                    "customer_info VARCHAR(255))");
        }
    }

    @AfterEach
    public void teardown() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE invoices");
        }
        connection.close();
    }

    @Test
    public void testCreateInvoice() throws Exception {
        Invoice invoice = new Invoice(
                "ITEM001",
                10,
                50.0,
                500.0,
                LocalDateTime.of(2024, 12, 1, 0, 0),
                "John Doe"
        );

        boolean result = invoiceService.createInvoice(invoice);

        assertTrue(result);
    }

    @Test
    public void testRetrieveInvoiceById() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO invoices VALUES (1, 'ITEM001', 10, 50.0, 500.0, '2024-12-01 00:00:00', 'John Doe')");
        }

        Invoice invoice = invoiceService.getInvoiceById(1);

        assertNotNull(invoice);
        assertEquals("ITEM001", invoice.getItemId());
        assertEquals(10, invoice.getQuantity());
        assertEquals(500.0, invoice.getTotalPrice());
        assertEquals(LocalDateTime.of(2024, 12, 1, 0, 0), invoice.getSaleDate());
    }

    @Test
    public void testUpdateInvoice() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO invoices VALUES (1, 'ITEM001', 10, 50.0, 500.0, '2024-12-01 00:00:00', 'John Doe')");
        }

        invoiceService.updateInvoice(1, 20, 60.0);

        Invoice updatedInvoice = invoiceService.getInvoiceById(1);
        assertNotNull(updatedInvoice);
        assertEquals(20, updatedInvoice.getQuantity());
        assertEquals(1200.0, updatedInvoice.getTotalPrice());
    }

    @Test
    public void testDataValidation() throws Exception {
        Invoice invalidInvoice = new Invoice(
                "ITEM001",
                -5,
                50.0,
                500.0,
                LocalDateTime.of(2024, 12, 1, 0, 0),
                "John Doe"
        );

        boolean result = invoiceService.createInvoice(invalidInvoice);
        assertFalse(result);
    }

    @Test
    public void testErrorScenario() throws Exception {
        Invoice invoice = invoiceService.getInvoiceById(999);

        assertNull(invoice);
    }
}
