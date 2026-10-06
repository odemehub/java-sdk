package com.odemehub.enums;

/**
 * What a payment link lets the payer pay: the lines the merchant wrote, any
 * amount they write themselves, one of the amounts offered, or one of those
 * or an amount of their own.
 */
public enum AmountType {

    FIXED("fixed"),
    CUSTOM("custom"),
    PREDEFINED("predefined"),
    PREDEFINED_AND_CUSTOM("predefined_and_custom");

    private final String value;

    AmountType(String value) {
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
    public static AmountType from(String value) {
        for (AmountType member : values()) {
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
