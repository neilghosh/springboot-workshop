package com.convergence.ecommerce.service;

import com.convergence.ecommerce.dto.ProductRequestDTO;
import com.convergence.ecommerce.dto.ProductResponseDTO;
import com.convergence.ecommerce.model.ProductEntity;
import com.convergence.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Constructor Injection (Spring DI)
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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

    // The lookup and write form one read-modify-write business operation.
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

    // The existence check and delete must share the same transaction boundary.
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
}
