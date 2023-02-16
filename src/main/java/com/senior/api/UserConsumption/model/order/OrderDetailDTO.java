package com.senior.api.UserConsumption.model.order;

import com.senior.api.UserConsumption.domain.OrderItem;
import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderDetailDTO {
    private Long id;
    private String orderCode;
    private Double discountPercentage;
    private OrderStatusEnum status;
    private Double finalPrice;

}
