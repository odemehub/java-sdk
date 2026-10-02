package com.odemehub.enums;

/**
 * Where an order stands: open until it is paid, then paid.
 */
public enum OrderStatus {

    OPEN("open"),
    PAID("paid");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    /** The value as the gateway writes it. */
    public String getValue() {
        return value;
    }

    /**
     * The member for a value the gateway sent, or null for one this version
     * of the SDK does not know yet, so a new value never breaks reading.
     */
    public static OrderStatus from(String value) {
        for (OrderStatus member : values()) {
            if (member.value.equals(value)) {
                return member;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
