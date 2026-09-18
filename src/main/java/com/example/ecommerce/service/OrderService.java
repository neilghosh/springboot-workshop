package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderRequestDTO;
import com.example.ecommerce.dto.OrderResponseDTO;
import com.example.ecommerce.dto.OrderSummaryDTO;
import com.example.ecommerce.dto.ProcessOrderResponseDTO;
import com.example.ecommerce.model.OrderEntity;
import com.example.ecommerce.model.ProductEntity;
import com.example.ecommerce.payment.PaymentService;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentService = paymentService;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        ProductEntity product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + requestDTO.getProductId()));

        OrderEntity order = new OrderEntity();
        order.setProduct(product);
        order.setQuantity(requestDTO.getQuantity());

        return mapToResponseDTO(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public OrderSummaryDTO getOrderSummary(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        ProductEntity product = order.getProduct();
        Double totalPrice = BigDecimal.valueOf(product.getPrice())
                .multiply(BigDecimal.valueOf(order.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        return new OrderSummaryDTO(
                order.getId(),
                product.getDescription(),
                product.getName(),
                order.getQuantity(),
                product.getPrice(),
                totalPrice);
    }

    @Transactional(readOnly = true)
    public ProcessOrderResponseDTO processOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }

        return new ProcessOrderResponseDTO(paymentService.processPayment());
    }

    private OrderResponseDTO mapToResponseDTO(OrderEntity order) {
        return new OrderResponseDTO(order.getId(), order.getProduct().getId(), order.getQuantity());
    }
}