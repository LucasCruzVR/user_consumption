package com.senior.api.UserConsumption.itemize;

public enum ProductServiceTypeEnum {
    PRODUCT(0, "Product"),
    SERVICE(1, "Service");

    private int code;
    private String description;

    private ProductServiceTypeEnum(int cod, String description) {
        this.code = cod;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProductServiceTypeEnum toEnum(Integer code) {
        if (code == null) {
            return ProductServiceTypeEnum.PRODUCT;
        }
        for (ProductServiceTypeEnum value : ProductServiceTypeEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + code);
    }
}
