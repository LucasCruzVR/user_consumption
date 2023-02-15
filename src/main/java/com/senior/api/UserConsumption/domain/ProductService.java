package com.senior.api.UserConsumption.domain;

import com.senior.api.UserConsumption.itemize.ProductServiceEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
public class ProductService implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Integer type;

    public ProductService(Long id, String name, ProductServiceEnum code) {
        this.id = id;
        this.name = name;
        this.type = code.getCode();
    }

    public void setType(ProductServiceEnum code) {
        this.type = code.getCode();
    }

    public ProductServiceEnum getType() {
        return ProductServiceEnum.toEnum(type);
    }
}
