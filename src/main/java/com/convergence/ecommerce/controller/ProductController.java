package com.convergence.ecommerce.controller;

import com.convergence.ecommerce.dto.ProductRequestDTO;
import com.convergence.ecommerce.dto.ProductResponseDTO;
import com.convergence.ecommerce.dto.ProductSummaryDTO;
import com.convergence.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Create, query, update, delete, and enrich products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "List all products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/{id}/summary")
    @Operation(summary = "Get a product enriched with external pricing")
    public ResponseEntity<ProductSummaryDTO> getProductSummary(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductSummary(id));
    }

    @PostMapping
    @Operation(summary = "Create a product")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO created = productService.createProduct(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO updated = productService.updateProduct(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
