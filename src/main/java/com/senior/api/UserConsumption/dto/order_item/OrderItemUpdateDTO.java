package com.senior.api.UserConsumption.dto.order_item;

import com.senior.api.UserConsumption.dto.product_service.ProductServiceBaseDTO;
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
