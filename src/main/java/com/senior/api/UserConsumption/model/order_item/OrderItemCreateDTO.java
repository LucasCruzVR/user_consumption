package com.senior.api.UserConsumption.model.order_item;

import com.senior.api.UserConsumption.model.product_service.ProductServiceBaseDTO;
import lombok.Data;

@Data
public class OrderItemCreateDTO {
    private ProductServiceBaseDTO productService;
    private Integer amount;
    private Double price;
}
