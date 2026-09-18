package com.example.ecommerce.payment;

import org.springframework.stereotype.Service;

@Service
public class CardService implements PaymentService {

    @Override
    public String processPayment() {
        return "CARD";
    }
}