package com.senior.api.UserConsumption.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductServiceReqDTO {
    private String name;
    private Double price;
    private Integer type;
}
