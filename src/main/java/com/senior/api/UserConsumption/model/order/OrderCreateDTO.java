package com.senior.api.UserConsumption.model.order;

import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import com.senior.api.UserConsumption.model.product_service.ProductServiceOrderDTO;
import lombok.Data;

import java.util.Set;

@Data
public class OrderCreateDTO {
    private Long id;
    private String orderCode;
    private Double discountPercentage;
    private OrderStatusEnum status;
    private Double finalPrice;
    private Set<ProductServiceOrderDTO> productServiceList;
}
