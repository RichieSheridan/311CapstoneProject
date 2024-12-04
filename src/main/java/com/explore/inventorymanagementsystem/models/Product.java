package com.explore.inventorymanagementsystem.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Product implements Reportable{
    private Integer id;
    private String name;
    private String description;
    private int quantity;
    private double price;
    private String category;
    private String supplier;
    private LocalDateTime lastUpdated;
    private int reorderPoint;

    public Product(Integer id, String name, String description, int quantity, double price, String category, String supplier, int reorderPoint) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.supplier = supplier;
        this.reorderPoint = reorderPoint;
    }

    public Product(String name, String description, int quantity, double price, String category, String supplier, int reorderPoint) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.supplier = supplier;
        this.reorderPoint = reorderPoint;
    }
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDate() {
        return "";
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
    }
}
