package com.senior.api.UserConsumption.model;

import com.senior.api.UserConsumption.itemize.ProductServiceStatusEnum;
import com.senior.api.UserConsumption.itemize.ProductServiceTypeEnum;
import lombok.Data;

@Data
public class ProductServiceRespDTO {
    private Long id;
    private String name;
    private Double price;
    private ProductServiceTypeEnum type;
    private ProductServiceStatusEnum status;
}
