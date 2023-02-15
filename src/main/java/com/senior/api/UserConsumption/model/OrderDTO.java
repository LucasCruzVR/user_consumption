package com.senior.api.UserConsumption.model;

import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private String orderCode;
    private Double discountPercentage;
    private OrderStatusEnum status;
}
