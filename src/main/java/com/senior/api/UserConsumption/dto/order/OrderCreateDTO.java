package com.senior.api.UserConsumption.dto.order;

import com.senior.api.UserConsumption.dto.order_item.OrderItemCreateDTO;
import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDTO implements Serializable {
    @NotNull(message = "Code of order can't be null")
    @NotEmpty(message = "Code of order can't be empty")
    private String orderCode;

    @Min(value = 0, message = "Discount needs to be between 0 and 100")
    @Max(value = 100, message = "Discount needs to be between 0 and 100")
    private Double discountPercentage;
    private OrderStatusEnum status;
    private Set<OrderItemCreateDTO> orderItem;
}

