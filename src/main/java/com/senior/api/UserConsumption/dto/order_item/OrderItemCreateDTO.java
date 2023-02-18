package com.senior.api.UserConsumption.dto.order_item;

import com.senior.api.UserConsumption.dto.product_service.ProductServiceBaseDTO;
import lombok.Data;

@Data
public class OrderItemCreateDTO {
    private ProductServiceBaseDTO productService;
    private Integer amount;
}
