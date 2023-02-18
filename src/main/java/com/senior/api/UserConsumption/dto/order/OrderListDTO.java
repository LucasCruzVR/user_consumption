package com.senior.api.UserConsumption.dto.order;

import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import lombok.Data;

@Data
public class OrderListDTO {
    private Long id;
    private String orderCode;
    private Double discountPercentage;
    private OrderStatusEnum status;
}
