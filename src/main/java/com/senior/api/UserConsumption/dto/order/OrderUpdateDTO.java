package com.senior.api.UserConsumption.dto.order;

import com.senior.api.UserConsumption.dto.order_item.OrderItemCreateDTO;
import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Set;

@Data
public class OrderUpdateDTO {
    @Min(value = 0, message = "Discount needs to be between 0 and 100")
    @Max(value = 100, message = "Discount needs to be between 0 and 100")
    private Double discountPercentage;
    private OrderStatusEnum status;
    private Set<OrderItemCreateDTO> orderItem;
}
