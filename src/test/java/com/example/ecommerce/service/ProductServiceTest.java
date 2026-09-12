package com.example.ecommerce.service;

import com.example.ecommerce.client.ExternalProductClient;
import com.example.ecommerce.dto.ProductRequestDTO;
import com.example.ecommerce.dto.ProductResponseDTO;
import com.example.ecommerce.dto.ProductSummaryDTO;
import com.example.ecommerce.model.ProductEntity;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ExternalProductClient externalProductClient;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Test Product");
        request.setDescription("Test Description");
        request.setPrice(99.99);
        request.setStockQuantity(10);
        request.setCategory("Test Category");

        ProductEntity savedEntity = new ProductEntity();
        savedEntity.setId(1L);
        savedEntity.setName(request.getName());
        savedEntity.setDescription(request.getDescription());
        savedEntity.setPrice(request.getPrice());
        savedEntity.setStockQuantity(request.getStockQuantity());
        savedEntity.setCategory(request.getCategory());
        savedEntity.setCreatedAt(LocalDateTime.now());

        when(productRepository.save(any(ProductEntity.class))).thenReturn(savedEntity);

        ProductResponseDTO response = productService.createProduct(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Product", response.getName());
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            productService.getProductById(99L);
        });
    }

    @Test
    void testGetProductSummaryUsesLivePrice() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setName("Mechanical Keyboard");
        entity.setDescription("RGB Wireless");
        entity.setPrice(49.99);
        entity.setStockQuantity(50);
        entity.setCategory("Electronics");
        entity.setCreatedAt(LocalDateTime.now());

        ExternalProductClient.ExternalProductResponse external =
                new ExternalProductClient.ExternalProductResponse(
                        1L,
                        "Mechanical Keyboard",
                        "RGB wireless mechanical keyboard",
                        79.99,
                        "electronics",
                        50);

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(externalProductClient.getProduct()).thenReturn(external);
        when(externalProductClient.getExternalProductUrl())
                .thenReturn("http://localhost:8080/external-product.json");

        ProductSummaryDTO summary = productService.getProductSummary(1L);

        assertEquals(49.99, summary.getProduct().getPrice());
        assertEquals(79.99, summary.getLivePrice());
        assertEquals(-30.0, summary.getPriceDifference(), 0.001);
        assertEquals(
                "http://localhost:8080/external-product.json",
                summary.getExternalUrl());
        verify(externalProductClient).getProduct();
    }
}
