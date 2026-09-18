package com.example.ecommerce.dto;

public class OrderSummaryDTO {

    private Long orderId;
    private String productDescription;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;

    public OrderSummaryDTO() {
    }

    public OrderSummaryDTO(Long orderId, String productDescription, String productName, Integer quantity, Double unitPrice, Double totalPrice) {
        this.orderId = orderId;
        this.productDescription = productDescription;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}