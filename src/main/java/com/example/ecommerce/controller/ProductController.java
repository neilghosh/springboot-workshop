package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductRequestDTO;
import com.example.ecommerce.dto.ProductResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final List<ProductResponseDTO> products = new ArrayList<>();
    private long nextId = 1;

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return products;
    }

    // @Valid enforces the DTO constraints before invalid input reaches storage.
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        ProductResponseDTO product = new ProductResponseDTO();
        product.setId(nextId++);
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setCreatedAt(LocalDateTime.now());
        products.add(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
}
