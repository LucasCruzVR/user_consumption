package com.senior.api.UserConsumption.model.order;

import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import com.senior.api.UserConsumption.model.order_item.OrderItemCreateDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class OrderCreateDTO {
    @NotNull(message = "Code of order can't be null")
    @NotEmpty(message = "Code of order can't be empty")
    private String orderCode;
    private Double discountPercentage;
    private OrderStatusEnum status;
    private Set<OrderItemCreateDTO> orderItem;
}

