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
@NoArgsConstructor
public class ProductService implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Integer type;
    private Integer status;

    @JsonIgnore
    @OneToMany(mappedBy = "productService")
    private Set<OrderItem> orderItems = new HashSet<>();

    public ProductService(Long id, String name, Double price, ProductServiceTypeEnum codeType, ProductServiceStatusEnum codeStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = codeType.getCode();
        this.status = codeStatus.getCode();
    }

    public void setType(ProductServiceTypeEnum code) {
        this.type = code.getCode();
    }

    public ProductServiceTypeEnum getType() {
        return ProductServiceTypeEnum.toEnum(type);
    }

    public void setStatus(ProductServiceStatusEnum code) {
        this.status = code.getCode();
    }

    public ProductServiceStatusEnum getStatus() {
        return ProductServiceStatusEnum.toEnum(status);
    }
}
