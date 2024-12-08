package com.explore.inventorymanagementsystem.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Purchase implements Reportable{
    private Integer id;
    private String itemId;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private LocalDateTime purchaseDate;
    private String supplierInfo;
    private Status status;

    public Purchase(Integer id, String itemId, int quantity, double unitPrice, double totalPrice,
                    LocalDateTime purchaseDate, String supplierInfo, Status status) {
        this.id = id;
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
        this.supplierInfo = supplierInfo;
        this.status = status;
    }

    public Purchase(String itemId, int quantity, double unitPrice, double totalPrice,
                    LocalDateTime purchaseDate, String supplierInfo, Status status) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
        this.supplierInfo = supplierInfo;
        this.status = status;
    }
    public Integer getId() {
        return (Integer) id;
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

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getSupplierInfo() {
        return supplierInfo;
    }

    public void setSupplierInfo(String supplierInfo) {
        this.supplierInfo = supplierInfo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
