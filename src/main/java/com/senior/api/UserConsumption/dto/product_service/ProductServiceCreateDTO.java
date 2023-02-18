package com.senior.api.UserConsumption.dto.product_service;

import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductServiceCreateDTO {
    @NotNull(message = "Name of order can't be null")
    @NotEmpty(message = "Name of order can't be empty")
    private String name;
    private Double price;
    private ProductServiceTypeEnum type;
    private ProductServiceStatusEnum status;
}
