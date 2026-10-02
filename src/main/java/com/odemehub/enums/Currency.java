package com.odemehub.enums;

/**
 * The money a payment, an order, a subscription or a payment link is taken in.
 */
public enum Currency {

    TRY("TRY"),
    USD("USD"),
    EUR("EUR"),
    GBP("GBP");

    private final String value;

    Currency(String value) {
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
    public static Currency from(String value) {
        for (Currency member : values()) {
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
