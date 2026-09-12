package com.convergence.ecommerce.service;

import com.convergence.ecommerce.dto.ProductRequestDTO;
import com.convergence.ecommerce.dto.ProductResponseDTO;
import com.convergence.ecommerce.model.ProductEntity;
import com.convergence.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    // Persistence work belongs inside an explicit service transaction boundary.
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        ProductEntity entity = new ProductEntity();
        copy(request, entity);
        return toResponse(repository.save(entity));
    }

    private void copy(ProductRequestDTO request, ProductEntity entity) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setStockQuantity(request.getStockQuantity());
        entity.setCategory(request.getCategory());
    }

    private ProductResponseDTO toResponse(ProductEntity entity) {
        ProductResponseDTO response = new ProductResponseDTO();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setPrice(entity.getPrice());
        response.setStockQuantity(entity.getStockQuantity());
        response.setCategory(entity.getCategory());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }
}
