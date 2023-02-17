package com.senior.api.UserConsumption.domain;

import com.senior.api.UserConsumption.itemize.OrderStatusEnum;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotNull(message = "Order code can't be null")
    @NotEmpty(message = "Order code can't be empty")
    private String orderCode;

    private Double discountPercentage;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status = OrderStatusEnum.ACTIVE;

    private Double finalPrice;

    @Builder.Default
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItem = new HashSet<>();

}
