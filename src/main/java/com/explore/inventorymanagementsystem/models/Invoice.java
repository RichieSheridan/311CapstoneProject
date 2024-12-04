package com.explore.inventorymanagementsystem.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Invoice implements Reportable{
    private Integer id;
    private String itemId;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private LocalDateTime saleDate;
    private String customerInfo;

    public Invoice(String itemId, int quantity, double unitPrice, double totalPrice, LocalDateTime saleDate, String customerInfo) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.saleDate = saleDate;
        this.customerInfo = customerInfo;
    }

    public Invoice(String itemId, int quantity, double unitPrice, double totalPrice, LocalDateTime saleDate) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.saleDate = saleDate;
    }

    public Invoice(Integer id, String itemId, int quantity, double unitPrice, double totalPrice, LocalDateTime saleDate) {
        this.id = id;
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.saleDate = saleDate;
    }


    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return "";
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public String getDate() {
        return "";
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }
}
