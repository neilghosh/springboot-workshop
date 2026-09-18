package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderRequestDTO;
import com.example.ecommerce.model.OrderEntity;
import com.example.ecommerce.model.ProductEntity;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("default")
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void cleanDatabase() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void createOrderAndGetSummary() throws Exception {
        ProductEntity product = new ProductEntity();
        product.setName("Mechanical Keyboard");
        product.setDescription("Compact keyboard");
        product.setPrice(49.99);
        product.setStockQuantity(10);
        product.setCategory("Electronics");
        ProductEntity savedProduct = productRepository.save(product);

        OrderRequestDTO request = new OrderRequestDTO(savedProduct.getId(), 2);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.productId").value(savedProduct.getId()))
                .andExpect(jsonPath("$.quantity").value(2));

        OrderEntity savedOrder = orderRepository.findAll().get(0);
        assertThat(savedOrder.getProduct().getId()).isEqualTo(savedProduct.getId());

        mockMvc.perform(get("/api/orders/{id}/summary", savedOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(savedOrder.getId()))
                .andExpect(jsonPath("$.productDescription").value("Compact keyboard"))
                .andExpect(jsonPath("$.productId").doesNotExist())
                .andExpect(jsonPath("$.productName").value("Mechanical Keyboard"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.unitPrice").value(49.99))
                .andExpect(jsonPath("$.totalPrice").value(99.98));

            mockMvc.perform(post("/api/orders/{id}/process", savedOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentMethod").value("UPI"));
    }

    @Test
    void rejectInvalidOrder() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO(1L, 0);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.messages.quantity").value("Quantity must be greater than zero"));
    }

    @Test
    void returnNotFoundForUnknownProductAndOrder() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO(999L, 1);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found with id: 999"));

        mockMvc.perform(get("/api/orders/{id}/summary", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found with id: 999"));

        mockMvc.perform(post("/api/orders/{id}/process", 999L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Order not found with id: 999"));
    }
}