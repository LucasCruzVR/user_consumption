package com.senior.api.UserConsumption.model.order_item;

import com.senior.api.UserConsumption.model.product_service.ProductServiceBaseDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemUpdateDTO {
    private Long id;
    private Integer amount;
    private ProductServiceBaseDTO productService;
}
