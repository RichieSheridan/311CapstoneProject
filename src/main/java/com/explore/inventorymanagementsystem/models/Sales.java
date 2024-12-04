package com.explore.inventorymanagementsystem.models;

public class Sales implements Reportable{
    private Integer id;
    private String invoiceNumber;

    private int customerId;

    private String customerName;

    private double price;
    private int quantity;

    private double totalAmount;

    private String date;

    private String itemNum;

    public Sales(Integer id, String invoiceNumber, int customerId, String customerName, double price, int quantity, double totalAmount, String date, String itemNum) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.price = price;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.date = date;
        this.itemNum = itemNum;
    }

    public Sales(String invoiceNumber, int customerId, String customerName, double price, int quantity, double totalAmount, String date, String itemNum) {
        this.invoiceNumber = invoiceNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.price = price;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.date = date;
        this.itemNum = itemNum;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return "";
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }
}
