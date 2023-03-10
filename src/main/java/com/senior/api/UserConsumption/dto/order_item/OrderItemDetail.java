package com.senior.api.UserConsumption.dto.order_item;

import com.senior.api.UserConsumption.dto.product_service.ProductServiceDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDetail {
    private Long id;
    private Integer amount;
    private Double price;
    private ProductServiceDetailDTO productService;
}
