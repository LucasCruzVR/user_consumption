package com.senior.api.UserConsumption.itemize;

public enum ProductServiceEnum {
    PRODUCT(0, "Product"),
    SERVICE(1, "Service");

    private int code;
    private String description;

    private ProductServiceEnum(int cod, String description) {
        this.code = cod;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProductServiceEnum toEnum(Integer code) {
        if (code == null) {
            return ProductServiceEnum.PRODUCT;
        }
        for (ProductServiceEnum value : ProductServiceEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + code);
    }
}
