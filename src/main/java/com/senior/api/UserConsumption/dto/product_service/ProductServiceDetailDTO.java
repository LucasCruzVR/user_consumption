package com.senior.api.UserConsumption.dto.product_service;

import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductServiceDetailDTO {
    private Long id;
    private String name;
    private Double price;
    private ProductServiceTypeEnum type;
    private ProductServiceStatusEnum status;
}
