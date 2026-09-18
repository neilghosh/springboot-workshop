package com.example.ecommerce.dto;

public class ProcessOrderResponseDTO {

    private String paymentMethod;

    public ProcessOrderResponseDTO() {
    }

    public ProcessOrderResponseDTO(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}