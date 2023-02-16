package com.senior.api.UserConsumption.model.product_service;

import lombok.Data;

@Data
public class ProductServiceOrderDTO {
    private ProductServiceBaseDTO productService;
    private Integer amount;
    private Double price;
}
