package com.senior.api.UserConsumption.itemize;

public enum ProductServiceStatusEnum {
    ACTIVE(0, "Active"),
    DISABLE(1, "Disable");

    private int code;
    private String description;

    private ProductServiceStatusEnum(int cod, String description) {
        this.code = cod;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProductServiceStatusEnum toEnum(Integer code) {
        if (code == null) {
            return ProductServiceStatusEnum.ACTIVE;
        }
        for (ProductServiceStatusEnum value : ProductServiceStatusEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + code);
    }
}
