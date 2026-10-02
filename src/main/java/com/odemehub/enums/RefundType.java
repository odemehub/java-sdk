package com.odemehub.enums;

/**
 * How money went back out of a payment: the whole of it before settlement,
 * or a refund after it.
 */
public enum RefundType {

    CANCEL("cancel"),
    REFUND("refund");

    private final String value;

    RefundType(String value) {
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
    public static RefundType from(String value) {
        for (RefundType member : values()) {
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
