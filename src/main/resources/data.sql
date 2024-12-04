-- Drop tables if they exist
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS invoices;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS purchases;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS customers;


-- Users table with automatic timestamps
CREATE TABLE users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        email VARCHAR(100) NOT NULL UNIQUE,
        role VARCHAR(20) NOT NULL,
        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Products table with automatic timestamps
CREATE TABLE products (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description TEXT,
                          quantity INT NOT NULL,
                          price DECIMAL(10,2) NOT NULL,
                          category VARCHAR(50),
                          supplier VARCHAR(100),
                          reorder_point INT NOT NULL,
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create customers table
CREATE TABLE customers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           phone_number VARCHAR(20) NOT NULL,
                           created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create invoices table
CREATE TABLE invoices (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          item_id VARCHAR(36) NOT NULL,
                          quantity INT NOT NULL,
                          unit_price DECIMAL(10,2) NOT NULL,
                          total_price DECIMAL(10,2) NOT NULL,
                          sale_date DATETIME NOT NULL,
                          customer_info VARCHAR(255) NOT NULL,
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create sales table
CREATE TABLE sales (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       invoice_number VARCHAR(50) NOT NULL,
                       customer_id INT NOT NULL,
                       customer_name VARCHAR(100) NOT NULL,
                       price DECIMAL(10,2) NOT NULL,
                       quantity INT NOT NULL,
                       total_amount DECIMAL(10,2) NOT NULL,
                       date DATE NOT NULL,
                       item_num VARCHAR(50) NOT NULL,
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Create purchase table
CREATE TABLE purchases (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           item_id VARCHAR(36) NOT NULL,
                           quantity INT NOT NULL,
                           unit_price DECIMAL(10,2) NOT NULL,
                           total_price DECIMAL(10,2) NOT NULL,
                           purchase_date DATETIME NOT NULL,
                           supplier_info VARCHAR(255) NOT NULL,
                           status VARCHAR(20) NOT NULL,
                           created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert test data into users table
INSERT INTO users (username, password, email, role) VALUES
                                                        ('adminUser', 'x/4JfhRDtWWFnr0ES76+uA==:jF4EmsIBwRet7djHGMkazA==', 'admin@example.com', 'ADMIN'),
                                                        ('normalUser1', 'x/4JfhRDtWWFnr0ES76+uA==:jF4EmsIBwRet7djHGMkazA==', 'user1@example.com', 'USER'),
                                                        ('normalUser2', 'x/4JfhRDtWWFnr0ES76+uA==:jF4EmsIBwRet7djHGMkazA==', 'user2@example.com', 'USER');

-- Insert test data into products table
INSERT INTO products (name, description, quantity, price, category, supplier, reorder_point) VALUES
                                                                                                 ('Laptop', '14-inch business laptop', 10, 899.99, 'Electronics', 'Tech Supplier', 5),
                                                                                                 ('Smartphone', 'Latest smartphone with 5G', 25, 499.99, 'Electronics', 'Mobile Supplier', 10),
                                                                                                 ('Headphones', 'Noise-cancelling over-ear headphones', 50, 199.99, 'Accessories', 'Audio Supplier', 15),
                                                                                                 ('Keyboard', 'Mechanical keyboard with RGB', 30, 69.99, 'Peripherals', 'Keyboard Supplier', 10),
                                                                                                 ('Monitor', '27-inch 4K UHD monitor', 15, 299.99, 'Electronics', 'Display Supplier', 5);


-- Insert test data for customers
INSERT INTO customers (name, phone_number) VALUES
                                               ('John Doe', '+1234567890'),
                                               ('Jane Smith', '+1987654321'),
                                               ('Bob Johnson', '+1122334455'),
                                               ('Alice Brown', '+1555666777'),
                                               ('Charlie Wilson', '+1999888777');

-- Insert test data for invoices
INSERT INTO invoices (item_id, quantity, unit_price, total_price, sale_date, customer_info) VALUES
                                                                                                    ('INV-2024-001', 2, 899.99, 1799.98, '2024-03-01 10:00:00', 'John Doe, +1234567890'),
                                                                                                    ( 'INV-2024-002', 1, 499.99, 499.99, '2024-03-02 11:30:00', 'Jane Smith, +1987654321'),
                                                                                                    ('INV-2024-003', 3, 199.99, 599.97, '2024-03-03 14:15:00', 'Bob Johnson, +1122334455'),
                                                                                                    ( 'INV-2024-003', 2, 299.99, 599.98, '2024-03-04 16:45:00', 'Alice Brown, +1555666777'),
                                                                                                    ( 'INV-2024-005', 1, 69.99, 69.99, '2024-03-05 09:20:00', 'Charlie Wilson, +1999888777');

-- Insert test data for sales
INSERT INTO sales (invoice_number, customer_id, customer_name, price, quantity, total_amount, date, item_num) VALUES
                                                                                                                  ('INV-2024-001', 1, 'John Doe', 899.99, 2, 1799.98, '2024-03-01', 'PROD-001'),
                                                                                                                  ('INV-2024-002', 2, 'Jane Smith', 499.99, 1, 499.99, '2024-03-02', 'PROD-002'),
                                                                                                                  ('INV-2024-003', 3, 'Bob Johnson', 199.99, 3, 599.97, '2024-03-03', 'PROD-003'),
                                                                                                                  ('INV-2024-004', 4, 'Alice Brown', 299.99, 2, 599.98, '2024-03-04', 'PROD-004'),
                                                                                                                  ('INV-2024-005', 5, 'Charlie Wilson', 69.99, 1, 69.99, '2024-03-05', 'PROD-005');



-- Insert test data into purchases table
INSERT INTO purchases (item_id, quantity, unit_price, total_price, purchase_date, supplier_info, status) VALUES
                                                                                                                 ( UUID(), 100, 799.99, 79999.00, '2024-03-01 10:00:00', 'Tech Supplier (Bulk Electronics)', 'COMPLETED'),
                                                                                                                 ( UUID(), 50, 459.99, 22999.50, '2024-03-02 11:30:00', 'Mobile Supplier (Smartphone Wholesale)', 'PENDING'),
                                                                                                                 ( UUID(), 200, 179.99, 35998.00, '2024-03-03 14:15:00', 'Audio Supplier (Premium Headphones)', 'PROCESSING'),
                                                                                                                 (UUID(), 150, 59.99, 8998.50, '2024-03-04 16:45:00', 'Keyboard Supplier (Mechanical KB Dist)', 'COMPLETED'),
                                                                                                                 (UUID(), 75, 279.99, 20999.25, '2024-03-05 09:20:00', 'Display Supplier (Monitor Wholesale)', 'COMPLETED'),
                                                                                                                 (UUID(), 25, 899.99, 22499.75, '2024-03-06 13:10:00', 'Tech Supplier (Premium Laptops)', 'PENDING'),
                                                                                                                 ( UUID(), 80, 299.99, 23999.20, '2024-03-07 15:30:00', 'Accessories Supplier (Bulk Purchase)', 'PROCESSING'),
                                                                                                                 (UUID(), 40, 599.99, 23999.60, '2024-03-08 11:45:00', 'Global Electronics Ltd', 'CANCELLED'),
                                                                                                                 (UUID(), 60, 159.99, 9599.40, '2024-03-09 14:20:00', 'FastTrack Supply Chain', 'COMPLETED'),
                                                                                                                 (UUID(), 90, 399.99, 35999.10, '2024-03-10 16:00:00', 'Direct Manufacturers Co', 'PROCESSING');