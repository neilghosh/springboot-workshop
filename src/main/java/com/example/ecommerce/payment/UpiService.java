package com.example.ecommerce.payment;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UpiService implements PaymentService {

    @Override
    public String processPayment() {
        return "UPI";
    }
}