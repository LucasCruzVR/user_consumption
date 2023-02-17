package com.senior.api.UserConsumption.model.order;

import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import com.senior.api.UserConsumption.model.order_item.OrderItemCreateDTO;
import lombok.Data;

import java.util.Set;

@Data
public class OrderUpdateDTO {
    private String orderCode;
    private Double discountPercentage;
    private OrderStatusEnum status;
    private Set<OrderItemCreateDTO> orderItem;
}
