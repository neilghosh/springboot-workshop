package com.convergence.ecommerce.dto;

public class ProductSummaryDTO {

    private ProductResponseDTO product;
    private Double livePrice;
    private Double priceDifference;
    private String externalSource;
    private String externalUrl;

    public ProductResponseDTO getProduct() {
        return product;
    }

    public void setProduct(ProductResponseDTO product) {
        this.product = product;
    }

    public Double getLivePrice() {
        return livePrice;
    }

    public void setLivePrice(Double livePrice) {
        this.livePrice = livePrice;
    }

    public Double getPriceDifference() {
        return priceDifference;
    }

    public void setPriceDifference(Double priceDifference) {
        this.priceDifference = priceDifference;
    }

    public String getExternalSource() {
        return externalSource;
    }

    public void setExternalSource(String externalSource) {
        this.externalSource = externalSource;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }
}
