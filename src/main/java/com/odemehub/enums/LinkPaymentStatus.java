package com.odemehub.enums;

/**
 * Where a payment at a link stands: open while the payer is paying, and
 * while their bank turns them away, then paid once a payment goes through.
 */
public enum LinkPaymentStatus {

    OPEN("open"),
    PAID("paid");

    private final String value;

    LinkPaymentStatus(String value) {
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
    public static LinkPaymentStatus from(String value) {
        for (LinkPaymentStatus member : values()) {
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
