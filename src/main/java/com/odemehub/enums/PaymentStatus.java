package com.odemehub.enums;

/**
 * What became of the money of a payment, which can move on to refunded long
 * after the attempt is over.
 */
public enum PaymentStatus {

    UNPAID("unpaid"),
    PAID("paid"),
    CANCELLED("cancelled"),
    REFUNDED("refunded"),
    PARTIALLY_REFUNDED("partially_refunded");

    private final String value;

    PaymentStatus(String value) {
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
    public static PaymentStatus from(String value) {
        for (PaymentStatus member : values()) {
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
