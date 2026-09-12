package com.convergence.ecommerce.service;

import com.convergence.ecommerce.dto.ProductRequestDTO;
import com.convergence.ecommerce.dto.ProductResponseDTO;
import com.convergence.ecommerce.dto.ProductSummaryDTO;
import com.convergence.ecommerce.client.ExternalProductClient;
import com.convergence.ecommerce.model.ProductEntity;
import com.convergence.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ExternalProductClient externalProductClient;

    public ProductService(ProductRepository productRepository, ExternalProductClient externalProductClient) {
        this.productRepository = productRepository;
        this.externalProductClient = externalProductClient;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public ProductSummaryDTO getProductSummary(Long id) {
        ProductResponseDTO product = getProductById(id);
        ExternalProductClient.ExternalProductResponse external = externalProductClient.getProduct();

        ProductSummaryDTO summary = new ProductSummaryDTO();
        summary.setProduct(product);
        summary.setLivePrice(external.price());
        summary.setPriceDifference(calculatePriceDifference(product.getPrice(), external.price()));
        summary.setExternalSource("local external-product.json fixture");
        summary.setExternalUrl(externalProductClient.getExternalProductUrl());
        return summary;
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        ProductEntity entity = new ProductEntity();
        entity.setName(requestDTO.getName());
        entity.setDescription(requestDTO.getDescription());
        entity.setPrice(requestDTO.getPrice());
        entity.setStockQuantity(requestDTO.getStockQuantity());
        entity.setCategory(requestDTO.getCategory());

        ProductEntity savedEntity = productRepository.save(entity);
        return mapToResponseDTO(savedEntity);
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        ProductEntity existingEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existingEntity.setName(requestDTO.getName());
        existingEntity.setDescription(requestDTO.getDescription());
        existingEntity.setPrice(requestDTO.getPrice());
        existingEntity.setStockQuantity(requestDTO.getStockQuantity());
        existingEntity.setCategory(requestDTO.getCategory());

        ProductEntity updatedEntity = productRepository.save(existingEntity);
        return mapToResponseDTO(updatedEntity);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponseDTO mapToResponseDTO(ProductEntity entity) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setStockQuantity(entity.getStockQuantity());
        dto.setCategory(entity.getCategory());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    private Double calculatePriceDifference(Double productPrice, Double livePrice) {
        return BigDecimal.valueOf(productPrice)
                .subtract(BigDecimal.valueOf(livePrice))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
