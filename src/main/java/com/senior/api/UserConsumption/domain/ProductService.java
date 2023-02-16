package com.senior.api.UserConsumption.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductService implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;

    @Enumerated(EnumType.STRING)
    private ProductServiceTypeEnum type = ProductServiceTypeEnum.PRODUCT;
    @Enumerated(EnumType.STRING)
    private ProductServiceStatusEnum status = ProductServiceStatusEnum.ACTIVE;

    @OneToMany(mappedBy = "productService")
    private Set<OrderItem> orderItems = new HashSet<>();
}
