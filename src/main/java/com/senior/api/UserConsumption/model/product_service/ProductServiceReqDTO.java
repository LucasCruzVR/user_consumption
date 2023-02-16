package com.senior.api.UserConsumption.model.product_service;

import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductServiceReqDTO {
    private String name;
    private Double price;
    private ProductServiceTypeEnum type;
    private ProductServiceStatusEnum status;
}
