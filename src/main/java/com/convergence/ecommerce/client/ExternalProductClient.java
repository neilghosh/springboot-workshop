package com.convergence.ecommerce.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalProductClient {

    private static final Logger logger = LoggerFactory.getLogger(ExternalProductClient.class);

    private final RestTemplate restTemplate;
    private final String externalProductUrl;

    public ExternalProductClient(
            RestTemplate restTemplate,
            @Value("${external.product-url}") String externalProductUrl) {
        this.restTemplate = restTemplate;
        this.externalProductUrl = externalProductUrl;
    }

    public ExternalProductResponse getProduct() {
        logger.info("Calling external product API: {}", externalProductUrl);
        return restTemplate.getForObject(externalProductUrl, ExternalProductResponse.class);
    }

    public String getExternalProductUrl() {
        return externalProductUrl;
    }

    public record ExternalProductResponse(
            Long id,
            String title,
            String description,
            Double price,
            String category,
            Integer stock) {
    }
}
