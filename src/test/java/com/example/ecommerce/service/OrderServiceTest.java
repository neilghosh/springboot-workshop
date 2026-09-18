package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProcessOrderResponseDTO;
import com.example.ecommerce.payment.PaymentService;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processOrderUsesInjectedPaymentService() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        when(paymentService.processPayment()).thenReturn("TEST PAYMENT");

        ProcessOrderResponseDTO response = orderService.processOrder(1L);

        assertEquals("TEST PAYMENT", response.getPaymentMethod());
        verify(paymentService).processPayment();
    }
}