package com.senior.api.UserConsumption.model.order;

import com.senior.api.UserConsumption.domain.OrderItem;
import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import com.senior.api.UserConsumption.model.order_item.OrderItemDetail;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class OrderDetailDTO {
    private Long id;
    private String orderCode;
    private Double discountPercentage;
    private OrderStatusEnum status;
    private Double finalPrice;
    private Set<OrderItem> orderItems;
}
