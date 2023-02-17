package com.senior.api.UserConsumption.model.order_item;

import com.senior.api.UserConsumption.model.product_service.ProductServiceBaseDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class OrderItemCreateDTO {
    private ProductServiceBaseDTO productService;
    private Integer amount;
    private Double price;
}
