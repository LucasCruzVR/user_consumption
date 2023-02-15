package com.senior.api.UserConsumption.itemize;

public enum OrderStatusEnum {
    ACTIVE(0, "Active"),
    DISABLE(1, "Disable");

    private int code;
    private String description;

    private OrderStatusEnum(int cod, String description) {
        this.code = cod;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatusEnum toEnum(Integer code) {
        if (code == null) {
            return OrderStatusEnum.ACTIVE;
        }
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + code);
    }
}
