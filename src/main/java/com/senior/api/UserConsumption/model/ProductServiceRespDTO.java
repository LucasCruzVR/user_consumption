package com.senior.api.UserConsumption.model;

import com.senior.api.UserConsumption.itemize.ProductServiceEnum;
import lombok.Data;

@Data
public class ProductServiceRespDTO {
    private Long id;
    private String name;
    private Double price;
    private ProductServiceEnum type;
}
