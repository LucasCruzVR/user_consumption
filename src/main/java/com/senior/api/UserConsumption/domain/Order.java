package com.senior.api.UserConsumption.domain;

import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "orders")
@NoArgsConstructor
@Data
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderCode;
    private Double discountPercentage;
    private Integer status;

    public Order(Long id, String orderCode, Double discountPercentage, OrderStatusEnum code) {
        this.id = id;
        this.orderCode = orderCode;
        this.discountPercentage = discountPercentage;
        this.status = code.getCode();
    }

    public void setStatus(OrderStatusEnum code) {
        this.status = code.getCode();
    }

    public OrderStatusEnum getStatus() {
        return OrderStatusEnum.toEnum(status);
    }
}
