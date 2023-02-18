package com.senior.api.UserConsumption.dto.product_service;

import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductServiceCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "Product name can't be null")
    @NotEmpty(message = "Product name can't be empty")
    private String name;
    @NotNull(message = "Product price can't be null")
    private Double price;
    private ProductServiceTypeEnum type;
    private ProductServiceStatusEnum status;
}
