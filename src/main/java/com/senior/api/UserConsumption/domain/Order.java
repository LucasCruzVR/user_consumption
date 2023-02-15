package com.senior.api.UserConsumption.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderCode;
    private Double discountPercentage;
    private Integer status;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>();

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
