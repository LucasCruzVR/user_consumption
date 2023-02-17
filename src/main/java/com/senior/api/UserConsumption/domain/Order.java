package com.senior.api.UserConsumption.domain;

import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderCode;
    private Double discountPercentage;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status = OrderStatusEnum.ACTIVE;

    private Double finalPrice;

    @Builder.Default
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<OrderItem> items = new HashSet<>();

}
