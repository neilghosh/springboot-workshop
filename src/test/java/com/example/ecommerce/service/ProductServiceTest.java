package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductRequestDTO;
import com.example.ecommerce.dto.ProductResponseDTO;
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
}
